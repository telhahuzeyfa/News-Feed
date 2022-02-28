package com.example.newsfeed
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class SourceAdapter(private val sources: List<Source>) :
    RecyclerView.Adapter<SourceAdapter.ViewHolder>() {

    private var pendingCheckedList: ArrayList<String> = ArrayList()

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val sourceTitle: TextView = itemView.findViewById(R.id.titleSource)
        val content: TextView = itemView.findViewById(R.id.content)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater: LayoutInflater = LayoutInflater.from(parent.context)
        val itemView: View = layoutInflater.inflate(R.layout.row_sources, parent, false)

        return ViewHolder(itemView)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        // Put the data in the each rows
        val currentSource = sources[position]
        holder.sourceTitle.text = currentSource.titleSource
        holder.content.text = currentSource.content
    }
    override fun getItemCount(): Int {
        return sources.size
    }
    public fun getCheckList(): ArrayList<String> {
        return pendingCheckedList
    }
    public fun checkListSize(): Int {
        return pendingCheckedList.size
    }
}