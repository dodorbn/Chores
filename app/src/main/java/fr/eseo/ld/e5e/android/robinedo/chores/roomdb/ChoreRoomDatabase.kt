package fr.eseo.ld.e5e.android.robinedo.chores.roomdb

import androidx.room.Database
import androidx.room.RoomDatabase
import fr.eseo.ld.e5e.android.robinedo.chores.roomdb.daos.ChoreDao
import fr.eseo.ld.e5e.android.robinedo.chores.roomdb.entities.ChoreRoomEntity
import fr.eseo.ld.e5e.android.robinedo.chores.roomdb.entities.TimeLogRoomEntity

@Database(
    entities = [
        ChoreRoomEntity::class,
        TimeLogRoomEntity::class
    ],
    version = 1,
    exportSchema = true
)
abstract class ChoreRoomDatabase : RoomDatabase() {
    abstract fun choreDao() : ChoreDao
}