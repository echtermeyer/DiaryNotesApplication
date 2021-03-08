package de.ericechtermeyer.diary.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.internal.synchronized

@Database(entities = [DiaryEntry::class], version = 1, exportSchema = true)
abstract class DiaryDatabase : RoomDatabase() {

    abstract fun diaryDatabaseDao(): DiaryDatabaseDao

    companion object {

        @Volatile
        private var INSTANCE: DiaryDatabase? = null

        @InternalCoroutinesApi
        fun getInstance(context: Context): DiaryDatabase {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        DiaryDatabase::class.java,
                        "diary_history_database"
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}