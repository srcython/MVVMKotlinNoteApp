package com.sercancelik.noteappmvvm.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.room.Query
import com.sercancelik.noteappmvvm.data.local.db.Note
import com.sercancelik.noteappmvvm.data.local.db.NoteDao
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NoteRepository @Inject constructor(private val noteDao: NoteDao) {

    val allNotes: LiveData<List<Note>> = noteDao.getAllNotes()

    suspend fun insert(note: Note) {
        noteDao.insert(note)
    }

    suspend fun update(note: Note): Boolean {
        return noteDao.update(note) > 0
    }

    suspend fun delete(id: Long) {
        noteDao.deleteById(id)
    }

    suspend fun getNoteById(id: Long): Note {
        return noteDao.getNoteById(id)
    }

    fun searchNotes(query: String): LiveData<List<Note>> {
        return noteDao.searchNotes(query)
    }
}
