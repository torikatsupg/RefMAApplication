package com.torikatsu923.refma


import android.content.Intent
import android.database.sqlite.SQLiteOpenHelper
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.lang.Exception

class ItemListFragment : Fragment() {

    private val threeDaysItemList = 0
    private val expiredItemList = 1
    private val itemList = 2
    private var _drawListMode = 0
    private var _entriesData: MutableList<MutableMap<String, Any>> =  mutableListOf()
    private lateinit var _helper: SQLiteOpenHelper
    private lateinit var _caster: CastCalendarType
    private  lateinit var _csGetter : GetColumnItem
    private lateinit var rvItemList: RecyclerView
    private val _iconResourceTable = arrayOf(
        R.drawable.ic_0, R.drawable.ic_1, R.drawable.ic_2, R.drawable.ic_3, R.drawable.ic_4, R.drawable.ic_5,
        R.drawable.ic_6, R.drawable.ic_7, R.drawable.ic_8, R.drawable.ic_9, R.drawable.ic_10,
        R.drawable.ic_11, R.drawable.ic_12, R.drawable.ic_13, R.drawable.ic_14, R.drawable.ic_15,
        R.drawable.ic_16, R.drawable.ic_17, R.drawable.ic_18, R.drawable.ic_19, R.drawable.ic_20,
        R.drawable.ic_21, R.drawable.ic_22, R.drawable.ic_23, R.drawable.ic_24, R.drawable.ic_25,
        R.drawable.ic_26, R.drawable.ic_27, R.drawable.ic_28, R.drawable.ic_29, R.drawable.ic_30
    )

    companion object {
        fun newInstance(id: Int) : Fragment {
            val fragment = ItemListFragment()
            fragment._drawListMode = id
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_item_list, container, false)

        _helper = activity?.let { DatabaseHelper(it) }!!
        _caster = CastCalendarType()
        _csGetter = GetColumnItem()
        _entriesData = initData(_entriesData)
        rvItemList = view.findViewById(R.id.rvItemList)
        val manager = LinearLayoutManager(context)
        rvItemList.layoutManager = manager
        val adapter = RecyclerListAdapter(_entriesData)
        rvItemList.adapter = adapter
        return view
    }

    private fun initData(list: MutableList<MutableMap<String, Any>>) : MutableList<MutableMap<String, Any>>{
        list.clear()
        val db = _helper.writableDatabase
        val now = _caster.getNowLongRound()
        val threeAft = _caster.getAft3DaysLonRound()
        var sql = ""
        when(_drawListMode) {
            threeDaysItemList -> sql = "SELECT * FROM refrigerator WHERE deadline BETWEEN $now AND $threeAft"
            expiredItemList -> sql = "SELECT * FROM refrigerator WHERE deadline < $now"
            itemList -> sql = "SELECT * FROM refrigerator WHERE $threeAft < deadline"
        }
        val cursor = db.rawQuery(sql, null)
        cursor.moveToFirst()
        try {
            do {
                val primaryKey = _csGetter.getLon("_id", cursor)
                val name = _csGetter.getStr("name", cursor)
                val deadline = _csGetter.getLonByStr("deadline", cursor)
                val iconId = _csGetter.getLon("iconImage", cursor)
                list.add(mutableMapOf("primaryKey" to primaryKey, "name" to name, "deadline" to deadline, "iconId" to iconId))
            } while (cursor.moveToNext())
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return list
    }

    override fun onDestroy() {
        _helper.close()
        super.onDestroy()
    }

    private inner class RecyclerListViewHolder(itemVew: View) : RecyclerView.ViewHolder(itemVew) {
        var tvName: TextView = itemVew.findViewById(R.id.tvItemName)
        var tvDeadline: TextView = itemVew.findViewById(R.id.tvItemDeadLine)
        var tvPosition: TextView = itemView.findViewById(R.id.tvPosition)
        var ivIcon: ImageView = itemVew.findViewById(R.id.ivIcon)

    }

    private inner class RecyclerListAdapter(private  val _entriesData: MutableList<MutableMap<String, Any>>) : RecyclerView.Adapter<RecyclerListViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerListViewHolder {
            val inflater = LayoutInflater.from(context)
            val view = inflater.inflate(R.layout.row, parent, false)
            view.setOnClickListener(ItemClickListener())
            return RecyclerListViewHolder(view)
        }

        override fun onBindViewHolder(holder: RecyclerListViewHolder, position: Int) {
            val item = _entriesData[position]
            val name = item["name"] as String
            val deadline = _caster.strToFormat(item["deadline"] as String)
            val icon = (item["iconId"] as Long).toInt()
            holder.tvPosition.text = position.toString()
            holder.tvName.text = name
            holder.tvDeadline.text = deadline
            holder.ivIcon.setImageResource(_iconResourceTable[icon])
        }

        override fun getItemCount(): Int {
            return _entriesData.size
        }
    }

    private inner class ItemClickListener : View.OnClickListener {
        override fun onClick(v: View) {
            val position = v.findViewById<TextView>(R.id.tvPosition).text.toString().toInt()
            val intent = Intent(context, EditDataActivity::class.java)
            val primaryKey = _entriesData[position]["primaryKey"] as Long
            intent.putExtra("primaryKey", primaryKey)
            startActivity(intent)
        }
    }
}
