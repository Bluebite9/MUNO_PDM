package ro.pdm.muno_pdm.utils.session

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface SessionDao {

    @Transaction
    @Query("SELECT * FROM session")
    fun getAllObjects() : LiveData<List<MunoDatabaseObject>>

    @Transaction
    @Query("SELECT * FROM session")
    fun getAllObjects2() : List<MunoDatabaseObject>

    @Transaction
    @Query("SELECT * FROM session WHERE `key` = :key")
    fun getObject(key: String) : MunoDatabaseObject

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(munoDatabaseObject: MunoDatabaseObject)

    @Delete
    suspend fun delete(munoDatabaseObject: MunoDatabaseObject) : Int

}