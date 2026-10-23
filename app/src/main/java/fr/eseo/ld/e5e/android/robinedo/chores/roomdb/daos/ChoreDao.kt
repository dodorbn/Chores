package fr.eseo.ld.e5e.android.robinedo.chores.roomdb.daos


import fr.eseo.ld.e5e.android.robinedo.chores.roomdb.entities.ChoreRoomEntity
import fr.eseo.ld.e5e.android.robinedo.chores.roomdb.entities.TimeLogRoomEntity

import fr.eseo.ld.e5e.android.robinedo.chores.roomdb.relations.ChoreWithTimeLogsRoomRelation
import kotlinx.coroutines.flow.Flow
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update

@Dao
interface ChoreDao {
    @Transaction
    @Query("SELECT * FROM chores ORDER BY id DESC")
    fun getAllChoresWithTimeLogs() : Flow<List<ChoreWithTimeLogsRoomRelation>>

    @Transaction
    @Query("SELECT * FROM chores WHERE id = :choreId")
    fun getChoresWithTimeLogsById(choreId : String) : ChoreWithTimeLogsRoomRelation?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertChore(choreRoomEntity : ChoreRoomEntity) : Long

    @Update
    fun updateChore(chore : ChoreRoomEntity)

    @Delete
    fun deleteChore(chore : ChoreRoomEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTimeLog(timeLog : TimeLogRoomEntity) : Long

    @Query("UPDATE chores SET status = :status WHERE id = :choreId")
    fun updateChoreStatus(choreId : String, status : String)
}


