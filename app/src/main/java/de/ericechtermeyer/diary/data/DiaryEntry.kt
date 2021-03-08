package de.ericechtermeyer.diary.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "diary_entry_table")
data class DiaryEntry(

    @PrimaryKey(autoGenerate = true)
    var entryId: Long = 0L,

    @ColumnInfo(name = "timestamp")
    var date: String = "12.12.2012",

    @ColumnInfo(name = "entry")
    var entry: String = "Hello!"
)
