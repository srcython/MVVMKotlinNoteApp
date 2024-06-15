package com.sercancelik.noteappmvvm.ui.addNote

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.sercancelik.noteappmvvm.data.local.db.Note
import com.sercancelik.noteappmvvm.databinding.FragmentAddNoteBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddNoteFragment : Fragment() {
    private lateinit var binding: FragmentAddNoteBinding
    private val viewModel: AddNoteViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddNoteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnAddNote.setOnClickListener {
            val title = binding.etNoteTitle.text.toString().trim()
            val content = binding.etNoteContent.text.toString().trim()

            if (title.isEmpty() || content.isEmpty()) {
                Toast.makeText(
                    requireContext(),
                    "Please fill in both title and content",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            val note = Note(0, title, content)
            viewModel.insert(note)
        }

        viewModel.insertResult.observe(viewLifecycleOwner, Observer { success ->
            if (success) {
                Toast.makeText(requireContext(), "Note added", Toast.LENGTH_SHORT).show()
                findNavController().navigateUp()
            } else {
                Toast.makeText(requireContext(), "Failed to add note", Toast.LENGTH_SHORT).show()
            }
        })
        binding.backButton.setOnClickListener {
            findNavController().navigateUp()
        }
    }
}
