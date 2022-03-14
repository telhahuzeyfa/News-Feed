package com.example.newsfeed
import android.content.Intent
import android.media.Image
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class SourceAdapter(private val sources: List<Source>) :
    RecyclerView.Adapter<SourceAdapter.ViewHolder>() {

    private var pendingCheckedList: ArrayList<String> = ArrayList()


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val sourceCard: CardView = itemView.findViewById(R.id.sourceCard)
        val contents: TextView = itemView.findViewById(R.id.contents)
        val titleSource: TextView = itemView.findViewById(R.id.titleSource)
        val source: TextView = itemView.findViewById(R.id.source)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater: LayoutInflater = LayoutInflater.from(parent.context)
        val itemView: View = layoutInflater.inflate(R.layout.row_sources, parent, false)

        return ViewHolder(itemView)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        // Put the data in the each rows
        val currentSource = sources[position]
        holder.titleSource.text = currentSource.titleSource
        holder.contents.text = currentSource.content
        holder.sourceCard.setOnClickListener {
            var url: Intent = Intent (Intent.ACTION_VIEW, Uri.parse(currentSource.url))
            it.context.startActivity(url)
        }
        Picasso.get().setIndicatorsEnabled(true)
        Picasso
            .get()
            .load(currentSource.url)
    }
    override fun getItemCount(): Int {
        return sources.size
    }
    public fun getCheckList(): ArrayList<String> {
        return pendingCheckedList
    }
}