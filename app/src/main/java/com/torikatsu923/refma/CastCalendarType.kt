package com.torikatsu923.refma

import java.text.SimpleDateFormat
import java.util.*

class CastCalendarType {
    private var sdf: SimpleDateFormat = SimpleDateFormat("yyyy/MM/dd")

    //Date　→　"yyyy/MM/dd"
    fun dateToFormat(d: Date) : String {
        return sdf.format(d)
    }

    //Calendar → "yyyy/MM/dd"
    fun calToFormat (c: Calendar) : String {
        return sdf.format(c.time)
    }

    //String　→　"yyyy/MM/dd"
    fun strToFormat(str: String) : String {
        return dateToFormat(Date(str.toLong()))
    }

    //Long　→　"yyyy/MM/dd"
    fun longToFormat (long: Long) : String {
        return dateToFormat( Date(long))
    }

    //now →Format
    fun getNowFormat () : String {
        return longToFormat(System.currentTimeMillis())
    }

    //now →　before30DaysMillis
    fun getPrimaryKey() : Long {
        return  System.currentTimeMillis() - (30L * 24L * 60L * 60L * 1000L)
    }

    //"yyyy/MM/dd"　→　Long
    fun formatToLong (fmt: String) : Long {
        return sdf.parse(fmt).time
    }

    //now RoundLong
    fun getNowLongRound () : Long {
        return formatToLong(longToFormat(System.currentTimeMillis()))
    }

    
    //3 Days after RoundLong
    fun getAft3DaysLonRound () : Long {
        return formatToLong(longToFormat(System.currentTimeMillis())) + (2 * 24 * 60 * 60 * 1000)
    }







}