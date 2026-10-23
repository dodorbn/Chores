package fr.eseo.ld.e5e.android.robinedo.chores.roomdb.repositories

import fr.eseo.ld.e5e.android.robinedo.chores.domain.model.Chore
import fr.eseo.ld.e5e.android.robinedo.chores.domain.model.ChoreStatus
import fr.eseo.ld.e5e.android.robinedo.chores.domain.model.TimeLog
import fr.eseo.ld.e5e.android.robinedo.chores.roomdb.daos.ChoreDao
import fr.eseo.ld.e5e.android.robinedo.chores.roomdb.entities.toChoreRoomEntity
import fr.eseo.ld.e5e.android.robinedo.chores.roomdb.entities.toTimeLogRoomEntity
import fr.eseo.ld.e5e.android.robinedo.chores.roomdb.relations.toChore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.UUID
import javax.inject.Inject

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ChoreRoomRepository @Inject constructor(private val choreDao : ChoreDao) {
    val allChores : Flow<List<Chore>>
        get() = choreDao.getAllChoresWithTimeLogs()
            .map{ choreWithTimeLog ->
                choreWithTimeLog.map{ it.toChore() }
            }

    suspend fun getChoreById(id : UUID) : Chore? = withContext(Dispatchers.IO) {
        return@withContext choreDao.getChoresWithTimeLogsById(id.toString())?.toChore()
    }

    suspend fun insertOrUpdate(chore : Chore) = withContext(Dispatchers.IO) {
        val choreRoomEntity = chore.toChoreRoomEntity()
        val id = choreDao.insertChore(choreRoomEntity)
        if(id == -1L) {
            choreDao.updateChore(choreRoomEntity)
        }
        chore.timeLogs.forEach { timeLog ->
            choreDao.insertTimeLog(timeLog.toTimeLogRoomEntity())
        }
    }

    suspend fun deleteChore(chore : Chore) = withContext(Dispatchers.IO) {
        choreDao.deleteChore(chore.toChoreRoomEntity())
    }

    suspend fun addTimeLog(timeLog : TimeLog) = withContext(Dispatchers.IO) {
        choreDao.insertTimeLog(timeLog.toTimeLogRoomEntity())
    }

    suspend fun addTimeLogAndUpdateChore(choreId : UUID, status : ChoreStatus, timeLog : TimeLog) = withContext(Dispatchers.IO) {
        choreDao.updateChoreStatus(choreId = choreId.toString(), status = status.name)
        choreDao.insertTimeLog(timeLog.toTimeLogRoomEntity())
    }
}
