package de.ericechtermeyer.diary.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface DiaryDatabaseDao {

    @Insert
    fun insertRow(entry: DiaryEntry)

    @Query("SELECT * FROM DIARY_ENTRY_TABLE ORDER BY entryId DESC")
    fun readAllData(): LiveData<List<DiaryEntry>>

}