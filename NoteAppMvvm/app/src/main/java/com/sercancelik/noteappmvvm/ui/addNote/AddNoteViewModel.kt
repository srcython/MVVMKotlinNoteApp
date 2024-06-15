package com.sercancelik.noteappmvvm.ui.addNote

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
class AddNoteViewModel @Inject constructor(private val repository: NoteRepository) : ViewModel() {
    private val _insertResult = MutableLiveData<Boolean>()
    val insertResult: LiveData<Boolean> get() = _insertResult
    fun insert(note: Note) {
        viewModelScope.launch {
            try {
                repository.insert(note)
                _insertResult.value = true
            } catch (e: Exception) {
                _insertResult.value = false
                e.printStackTrace()
            }
        }
    }

}