package com.sercancelik.noteappmvvm.ui.updateNote

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
class UpdateNoteViewModel @Inject constructor(private val repository: NoteRepository) :
    ViewModel() {
    private val _note = MutableLiveData<Note>()
    val note: LiveData<Note> get() = _note

    private val _updateSuccess = MutableLiveData<Boolean>()
    val updateSuccess: LiveData<Boolean> get() = _updateSuccess

    fun getNoteById(noteId: Long) {
        viewModelScope.launch {
            val fetchedNote = repository.getNoteById(noteId)
            _note.postValue(fetchedNote)
        }
    }

    fun update(note: Note) = viewModelScope.launch {
        val updateSuccess = repository.update(note)
        _updateSuccess.postValue(updateSuccess)
    }
}
