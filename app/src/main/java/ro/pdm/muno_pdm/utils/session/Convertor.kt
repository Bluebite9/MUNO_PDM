package ro.pdm.muno_pdm.utils.session

import androidx.room.TypeConverter
import java.util.*

class Convertor {

    @TypeConverter
    fun dateToLong(date : Date) : Long {
        return date.time
    }

    @TypeConverter
    fun longToDate(date : Long) : Date {
        return Date(date)
    }

}