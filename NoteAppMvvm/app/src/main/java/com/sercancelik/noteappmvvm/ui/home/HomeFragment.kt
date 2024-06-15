package com.sercancelik.noteappmvvm.ui.home

import android.graphics.Canvas
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.sercancelik.noteappmvvm.R
import com.sercancelik.noteappmvvm.data.local.db.Note
import com.sercancelik.noteappmvvm.databinding.FragmentHomeBinding
import com.sercancelik.noteappmvvm.ui.adapter.NoteAdapter
import com.sercancelik.noteappmvvm.ui.adapter.NoteClickListener
import dagger.hilt.android.AndroidEntryPoint
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var noteAdapter: NoteAdapter
    private val noteViewModel: HomeViewModel by viewModels()
    private var searchQuery: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        noteAdapter = NoteAdapter(object : NoteClickListener {
            override fun onNoteClick(note: Note) {
                val action = HomeFragmentDirections.actionHomeFragmentToShowNoteFragment(note.id)
                findNavController().navigate(action)
            }
        })

        binding.recyclerView.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = noteAdapter
        }

        noteViewModel.allNotes.observe(viewLifecycleOwner, Observer { notes ->
            notes?.let {
                noteAdapter.submitList(it)
                checkEmptyState(it.isEmpty())
            }
        })

        binding.addNoteButton.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_addNoteFragment)
        }

        val itemTouchHelperCallback =
            object :
                ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    return false
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val position = viewHolder.layoutPosition
                    val note = noteAdapter.currentList[position]

                    when (direction) {
                        ItemTouchHelper.LEFT -> {
                            noteViewModel.delete(note)
                            val snackbar =
                                Snackbar.make(binding.root, "Note deleted", Snackbar.LENGTH_LONG)
                            snackbar.setAction("Undo") {
                                noteViewModel.insert(note)
                                checkEmptyState(noteAdapter.itemCount == 0)
                            }
                            snackbar.setActionTextColor(
                                ContextCompat.getColor(
                                    requireContext(),
                                    R.color.white
                                )
                            )
                            snackbar.setBackgroundTint(
                                ContextCompat.getColor(
                                    requireContext(),
                                    R.color.orange
                                )
                            )
                            snackbar.anchorView = binding.addNoteButton
                            snackbar.show()
                        }

                        ItemTouchHelper.RIGHT -> {
                            val action =
                                HomeFragmentDirections.actionHomeFragmentToUpdateFragment(note.id)
                            findNavController().navigate(action)
                        }
                    }
                }

                override fun onChildDraw(
                    c: Canvas,
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    dX: Float,
                    dY: Float,
                    actionState: Int,
                    isCurrentlyActive: Boolean
                ) {
                    RecyclerViewSwipeDecorator.Builder(
                        c,
                        recyclerView,
                        viewHolder,
                        dX,
                        dY,
                        actionState,
                        isCurrentlyActive
                    )
                        .addSwipeLeftBackgroundColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.red
                            )
                        )
                        .addSwipeLeftActionIcon(R.drawable.ic_delete)
                        .addSwipeLeftLabel("Delete")
                        .setSwipeLeftLabelColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.white
                            )
                        )
                        .addSwipeRightBackgroundColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.blue
                            )
                        )
                        .addSwipeRightActionIcon(R.drawable.ic_edit)
                        .addSwipeRightLabel("Edit")
                        .setSwipeRightLabelColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.white
                            )
                        )
                        .create()
                        .decorate()

                    super.onChildDraw(
                        c,
                        recyclerView,
                        viewHolder,
                        dX,
                        dY,
                        actionState,
                        isCurrentlyActive
                    )
                }
            }

        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(binding.recyclerView)

        // Observe swipe delete event
        noteViewModel.swipeDeleteEvent.observe(viewLifecycleOwner, Observer { note ->
            noteViewModel.delete(note)
            checkEmptyState(noteAdapter.itemCount == 0)
        })

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                searchQuery = newText
                searchNotes(newText)
                return true
            }
        })
    }

    private fun searchNotes(query: String?) {
        query?.let { it ->
            noteViewModel.searchNotes("%$it%").observe(viewLifecycleOwner, Observer { notes ->
                notes?.let {
                    noteAdapter.submitList(it)
                    checkEmptyState(it.isEmpty())
                }
            })
        }
    }

    private fun checkEmptyState(isEmpty: Boolean) {
        if (isEmpty) {
            binding.recyclerView.visibility = View.GONE
            binding.tvEmptyMessage.visibility = View.VISIBLE
        } else {
            binding.recyclerView.visibility = View.VISIBLE
            binding.tvEmptyMessage.visibility = View.GONE
        }
    }
}
