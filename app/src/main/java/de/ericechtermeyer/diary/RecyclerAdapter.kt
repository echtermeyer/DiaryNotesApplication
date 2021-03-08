package de.ericechtermeyer.diary

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import de.ericechtermeyer.diary.data.DiaryEntry

class RecyclerAdapter : RecyclerView.Adapter<RecyclerAdapter.RecyclerViewHolder>() {

    private var entryList = emptyList<DiaryEntry>()

    class RecyclerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val dateText: TextView = itemView.findViewById(R.id.date_rv)
        val entryText: TextView = itemView.findViewById(R.id.entry_rv)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.recycler_view_item, parent, false)

        return RecyclerViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        val currentItem = entryList[position]

        holder.dateText.text = currentItem.date
        holder.entryText.text = currentItem.entry
    }

    override fun getItemCount() = entryList.size

    fun setData(entry: List<DiaryEntry>) {
        this.entryList = entry
        notifyDataSetChanged()
    }
}