package fr.eseo.ld.e5e.android.robinedo.chores.hilt

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import fr.eseo.ld.e5e.android.robinedo.chores.roomdb.ChoreRoomDatabase
import fr.eseo.ld.e5e.android.robinedo.chores.roomdb.daos.ChoreDao
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object RoomHiltModule {
    @Provides
    @Singleton
    fun provideChoreLocalDatabase(
        @ApplicationContext context : Context
    ) : ChoreRoomDatabase {
        return Room.databaseBuilder(
            context = context,
            klass = ChoreRoomDatabase::class.java,
            name = "chore_manager_database"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideChoreDao(databse : ChoreRoomDatabase) : ChoreDao {
        return databse.choreDao()
    }

}
