package de.ericechtermeyer.diary.data

import androidx.lifecycle.LiveData

class DiaryRepository(private val diaryDao: DiaryDatabaseDao) {

    val readAllData: LiveData<List<DiaryEntry>> = diaryDao.readAllData()

    suspend fun addEntry(entry:DiaryEntry){
        diaryDao.insertRow(entry)
    }
}