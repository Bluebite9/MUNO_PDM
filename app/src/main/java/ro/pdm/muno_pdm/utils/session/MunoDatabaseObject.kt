package ro.pdm.muno_pdm.utils.session

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "session")
class MunoDatabaseObject : Serializable{
    @PrimaryKey
    lateinit var key: String
    var value: String? = null
}