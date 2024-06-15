package com.sercancelik.noteappmvvm.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sercancelik.noteappmvvm.data.local.db.Note
import com.sercancelik.noteappmvvm.repository.NoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val repository: NoteRepository) : ViewModel() {
    val allNotes: LiveData<List<Note>> = repository.allNotes

    fun searchNotes(query: String): LiveData<List<Note>> {
        return repository.searchNotes(query)
    }

    private val _swipeDeleteEvent = MutableLiveData<Note>()
    val swipeDeleteEvent: LiveData<Note> get() = _swipeDeleteEvent

    fun delete(note: Note) = viewModelScope.launch {
        repository.delete(note.id)
    }

    fun insert(note: Note) {
        viewModelScope.launch {
            try {
                repository.insert(note)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
