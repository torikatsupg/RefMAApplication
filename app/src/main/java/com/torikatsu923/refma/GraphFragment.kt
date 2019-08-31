package com.torikatsu923.refma


import android.database.sqlite.SQLiteOpenHelper
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import java.lang.Exception


class GraphFragment : Fragment() {
    private lateinit var _helper: SQLiteOpenHelper
    private lateinit var _caster: CastCalendarType
    private lateinit var _csGetter: GetColumnItem
    private lateinit var chDump: LineChart
    private lateinit var chBought: LineChart
    private lateinit var tvRate: TextView
    private var _boughtList: MutableList<MutableMap<String, Long>> = mutableListOf()
    private var _dumpList: MutableList<MutableMap<String, Long>> = mutableListOf()

    companion object {
        fun newInstance() : GraphFragment {
            val fragment = GraphFragment()
            fragment._boughtList.add(mutableMapOf("date" to 0L, "volume" to 0L))
            fragment._dumpList.add(mutableMapOf("date" to 0L, "volume" to 0L))
            return fragment

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_graph, container, false)
        _helper = context?.let { DatabaseHelper(it) }!!
        _caster = CastCalendarType()
        _csGetter = GetColumnItem()
        assignView(view)
        chDump.data = createLineData("dump")
        chBought.data = createLineData("bought")
        initChart(chDump)
        initChart(chBought)
        tvRate.text = outputRate().toInt().toString()
        return view
    }

    private fun assignView(v: View) {
        chDump = v.findViewById(R.id.chDump)
        chBought = v.findViewById(R.id.chBought)
        tvRate = v.findViewById(R.id.tvRate)
    }

    private fun initChart (v: LineChart) {
        v.apply {
            this.description.isEnabled = false
            this.setTouchEnabled(false)
            this.isScaleXEnabled = true
            this.isDragEnabled = false
            this.setPinchZoom(false)
            this.setDrawGridBackground(false)
            this.axisLeft.textColor = Color.BLACK
            this.axisRight.isEnabled = false
            this.axisLeft.axisMinimum = 0f
            this.axisLeft.axisMaximum = 20000f
            this.axisLeft.labelCount = 5
            this.xAxis.labelCount = 1
            this.xAxis.valueFormatter = IAxisValueFormatter { _, _ -> "" }
        }
    }

    override fun onDestroy() {
        _helper.close()
        super.onDestroy()
    }

    private fun createLineData(databaseName: String) : LineData {
        val key: String
        val chartLabel: String
        val data: MutableList<MutableMap<String, Long>>
        when (databaseName) {
            "dump" -> {
                data = _dumpList
                key = "dump"
                chartLabel = "廃棄量"
            }
            else -> {
                data = _boughtList
                key = "bought"
                chartLabel = "購入量"
            }
        }
        data.clear()
        data.add(mutableMapOf("date" to 0L, "volume" to 0L))
        val db = _helper.writableDatabase
        val sql = "SELECT * FROM $databaseName"
        val cursor = db.rawQuery(sql, null)
        cursor.moveToFirst()
        try {
            do {
                var isDataExist = false
                for (item in data) {
                    if (item["date"] == _csGetter.getLon("${key}Day", cursor)) {
                        item["volume"]?.plus(_csGetter.getLon("volume", cursor))?.let { item.put("volume", it) }
                        isDataExist = true
                        break
                    }
                }
                if (!isDataExist) data.add(mutableMapOf("date" to _csGetter.getLon("${key}Day", cursor), "volume" to _csGetter.getLon("volume", cursor)))
            } while (cursor.moveToNext())
        } catch (e: Exception) {
            e.printStackTrace()
        }
        val entries = mutableListOf<Entry>()
        for (item in data) {
            entries.add(Entry(item["date"]!!.toFloat(), item["volume"]!!.toFloat()))
        }
        return LineData(LineDataSet(entries,"$chartLabel"))
    }

   private fun outputRate() : Double{
       var totalDump = 0L
       for(item in _dumpList) {
           totalDump += item["volume"] as Long
       }
       var totalUse = 0L
       for(item in _boughtList) {
           totalUse += item["volume"] as Long
       }
       var result = totalDump.toDouble() / totalUse.toDouble() * 100.0
       if(result > 100.0) result = 100.0
       if(totalDump == 0L || totalUse == 0L) result = 0.0
       return result
   }

}
