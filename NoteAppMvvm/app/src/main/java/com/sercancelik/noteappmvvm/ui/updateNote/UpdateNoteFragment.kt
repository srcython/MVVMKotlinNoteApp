package com.sercancelik.noteappmvvm.ui.updateNote

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.sercancelik.noteappmvvm.R
import com.sercancelik.noteappmvvm.databinding.FragmentUpdateNoteBinding
import com.sercancelik.noteappmvvm.ui.showNote.ShowNoteFragmentArgs
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UpdateNoteFragment : Fragment() {
    private lateinit var binding: FragmentUpdateNoteBinding
    private val args: ShowNoteFragmentArgs by navArgs()
    private val viewModel: UpdateNoteViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUpdateNoteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val noteId = args.noteId
        viewModel.getNoteById(noteId)

        viewModel.note.observe(viewLifecycleOwner) { note ->
            binding.etNoteTitle.setText(note.title)
            binding.etNoteContent.setText(note.content)
        }

        viewModel.updateSuccess.observe(viewLifecycleOwner) { success ->
            if (success) {
                Toast.makeText(requireContext(), "Update successful", Toast.LENGTH_SHORT).show()
                findNavController().navigate(R.id.action_updateFragment_to_homeFragment)
            } else {
                Toast.makeText(requireContext(), "Update failed", Toast.LENGTH_SHORT).show()
            }
        }

        binding.backButton.setOnClickListener {
            onBackPressed()
        }

        binding.buttonUpdate.setOnClickListener {
            val updatedNote = viewModel.note.value?.copy(
                title = binding.etNoteTitle.text.toString(),
                content = binding.etNoteContent.text.toString()
            )

            updatedNote?.let {
                if (it.title.isEmpty() || it.content.isEmpty()) {
                    Toast.makeText(
                        requireContext(),
                        "Title or content cannot be empty",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    viewModel.update(it)
                }
            }
        }
    }

    private fun onBackPressed() {
        findNavController().navigate(R.id.action_updateFragment_to_homeFragment)
    }
}
