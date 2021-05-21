package ro.pdm.muno_pdm.utils.session

import android.app.Application
import androidx.lifecycle.LiveData
import kotlinx.coroutines.*

class SessionService(application: Application) {
    private val dbTrasee: MunoDatabase = MunoDatabase.getInstanta(application)!!
    private val sessionDao: SessionDao = dbTrasee.sessionDao

    fun getAllObjects(): LiveData<List<MunoDatabaseObject>> {
        return sessionDao.getAllObjects()
    }

    fun getAllObjects2(): List<MunoDatabaseObject> {
        return sessionDao.getAllObjects2()
    }

    fun insert(munoDatabaseObject: MunoDatabaseObject) {
        CoroutineScope(Dispatchers.IO).launch {
            sessionDao.insert(munoDatabaseObject)
        }
    }

    fun get(key: String): Deferred<MunoDatabaseObject> {
        return GlobalScope.async {
           return@async sessionDao.getObject(key)
        }
    }

}