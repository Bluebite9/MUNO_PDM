package ro.pdm.muno_pdm.utils.session

import android.content.Context
import androidx.room.*

@TypeConverters(Convertor::class)
@Database(entities = [MunoDatabaseObject::class], version = 1, exportSchema = false)
abstract class MunoDatabase : RoomDatabase() {
    abstract val sessionDao: SessionDao

    companion object {
        const val DB_NAME = "eLeguma_room.db"
        @Volatile
        private var instance: MunoDatabase? = null

        @Synchronized
        fun getInstanta(context: Context?): MunoDatabase? {
            if (instance == null) {

                instance = Room.databaseBuilder(context!!, MunoDatabase::class.java, DB_NAME)
                    //.allowMainThreadQueries() //de eliminat!!! apelurile la metodele din @Dao se vor realiza asincron!
                    .fallbackToDestructiveMigration()
                    .build()
            }
            return instance
        }
    }
}