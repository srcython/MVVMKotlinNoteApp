package com.sercancelik.noteappmvvm.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sercancelik.noteappmvvm.R
import com.sercancelik.noteappmvvm.data.local.db.Note

interface NoteClickListener {
    fun onNoteClick(note: Note)
}

class NoteAdapter(private val clickListener: NoteClickListener) :
    ListAdapter<Note, NoteAdapter.NoteViewHolder>(
        NoteDiffCallback()
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_note, parent, false)
        return NoteViewHolder(view)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val note = getItem(position)
        holder.bind(note, clickListener)
    }

    class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleTextView: TextView = itemView.findViewById(R.id.textViewTitle)
        private val contentTextView: TextView = itemView.findViewById(R.id.textViewContent)

        fun bind(note: Note, clickListener: NoteClickListener) {
            titleTextView.text = note.title
            contentTextView.text = note.content

            itemView.setOnClickListener {
                clickListener.onNoteClick(note)
            }
        }
    }

    class NoteDiffCallback : DiffUtil.ItemCallback<Note>() {
        override fun areItemsTheSame(oldItem: Note, newItem: Note): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean {
            return oldItem == newItem
        }
    }
}
