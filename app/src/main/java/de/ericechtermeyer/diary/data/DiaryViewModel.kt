package de.ericechtermeyer.diary.data

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.launch

@InternalCoroutinesApi
class DiaryViewModel(application: Application) : AndroidViewModel(application) {

    val readAllData: LiveData<List<DiaryEntry>>
    private val repository: DiaryRepository

    init {
        val diaryDatabaseDao = DiaryDatabase.getInstance(application).diaryDatabaseDao()
        repository = DiaryRepository(diaryDatabaseDao)
        readAllData = repository.readAllData
    }

    fun addEntry(entry: DiaryEntry) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addEntry(entry)
        }
    }

}