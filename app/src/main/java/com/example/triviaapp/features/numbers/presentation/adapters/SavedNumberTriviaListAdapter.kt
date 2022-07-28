package com.example.triviaapp.features.numbers.presentation.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageButton
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.triviaapp.R
import com.example.triviaapp.features.numbers.domain.entities.NumberTriviaEntity

class SavedNumberTriviaListAdapter(private val clickListener: SavedNumberTriviaItemClickListener)
    : ListAdapter<NumberTriviaEntity, SavedNumberTriviaListAdapter
.SavedNumberTriviaItemViewHolder>(NumberTriviaEntityComparator()) {

    class SavedNumberTriviaItemViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val savedTriviaTextView = view.findViewById<TextView>(R.id.trivia_text)
        val favouriteBtn = view.findViewById<AppCompatImageButton>(R.id.favourite_btn)
    }

    class NumberTriviaEntityComparator : DiffUtil.ItemCallback<NumberTriviaEntity>(){
        override fun areItemsTheSame(oldItem: NumberTriviaEntity, newItem: NumberTriviaEntity): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: NumberTriviaEntity, newItem: NumberTriviaEntity): Boolean {
            return oldItem.number == newItem.number
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SavedNumberTriviaItemViewHolder {
        val savedNumberTriviaView = LayoutInflater.from(parent.context).inflate(R.layout
        .view_saved_trivia, parent,
            false)
        return SavedNumberTriviaItemViewHolder(savedNumberTriviaView)
    }

    override fun onBindViewHolder(holder: SavedNumberTriviaItemViewHolder, position: Int) {
        val viewData = getItem(position)
        holder.savedTriviaTextView.text = viewData.text
        holder.favouriteBtn.setOnClickListener {
            clickListener.onClick(viewData)
        }
    }

    interface SavedNumberTriviaItemClickListener {
        fun onClick(trivia: NumberTriviaEntity)
    }
}