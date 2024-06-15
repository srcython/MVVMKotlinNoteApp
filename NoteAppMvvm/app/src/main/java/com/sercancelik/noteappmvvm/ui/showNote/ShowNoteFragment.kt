package com.sercancelik.noteappmvvm.ui.showNote

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.sercancelik.noteappmvvm.R
import com.sercancelik.noteappmvvm.databinding.FragmentShowNoteBinding
import com.sercancelik.noteappmvvm.ui.home.HomeFragmentArgs
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ShowNoteFragment : Fragment() {
    private lateinit var binding: FragmentShowNoteBinding
    private val args: HomeFragmentArgs by navArgs()
    private val viewModel: ShowNoteViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentShowNoteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val noteId = args.noteId
        viewModel.getNoteById(noteId)

        binding.editButton.setOnClickListener {
            val action =
                ShowNoteFragmentDirections.actionShowNoteFragmentToUpdateFragment(noteId)
            findNavController().navigate(action)
        }
        binding.deleteButton.setOnClickListener {
            showDeleteConfirmationDialog(noteId)
        }

        binding.backButton.setOnClickListener {
            onBackPressed()
        }

        viewModel.note.observe(viewLifecycleOwner) { note ->
            note?.let {
                binding.tvTitle.text = note.title
                binding.tvContent.text = note.content
            }
        }
    }

    private fun showDeleteConfirmationDialog(noteId: Long) {
        AlertDialog.Builder(requireContext())
            .setTitle("Delete Note")
            .setMessage("Are you sure you want to delete this note?")
            .setPositiveButton("Yes") { dialog, _ ->
                viewModel.deleteNote(noteId)
                Toast.makeText(requireContext(), "Note deleted", Toast.LENGTH_SHORT).show()
                findNavController().navigate(R.id.action_showNoteFragment_to_homeFragment)
                dialog.dismiss()
            }
            .setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }

    private fun onBackPressed() {
        findNavController().navigate(R.id.action_showNoteFragment_to_homeFragment)
    }
}
