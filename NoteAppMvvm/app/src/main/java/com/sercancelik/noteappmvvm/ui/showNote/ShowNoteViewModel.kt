package com.sercancelik.noteappmvvm.ui.showNote

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sercancelik.noteappmvvm.data.local.db.Note
import com.sercancelik.noteappmvvm.repository.NoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShowNoteViewModel @Inject constructor(
    private val repository: NoteRepository
) : ViewModel() {

    private val _note = MutableLiveData<Note>()
    val note: LiveData<Note> get() = _note

    fun getNoteById(noteId: Long) {
        viewModelScope.launch {
            try {
                val note = repository.getNoteById(noteId)
                _note.value = note
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }


    fun deleteNote(noteId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repository.delete(noteId)

            } catch (_: Exception) {

            }
        }
    }


}