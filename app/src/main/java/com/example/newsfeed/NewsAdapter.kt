package com.example.newsfeed

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.newsfeed.News
import com.squareup.picasso.Picasso

class NewsAdapter(private val news: List<News> ): RecyclerView.Adapter<NewsAdapter.ViewHolder>(){

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val cardView: CardView = itemView.findViewById(R.id.sourceCard)
        val newsTitleSource: TextView = itemView.findViewById(R.id.newsTitleSource)
        val newsThumbnail: ImageView = itemView.findViewById(R.id.newsThumbnail)
        val source: TextView = itemView.findViewById(R.id.newsTitleSource)
        val newsContents: TextView = itemView.findViewById(R.id.newsContents)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater: LayoutInflater = LayoutInflater.from(parent.context)
        val itemView: View = layoutInflater.inflate(R.layout.row_news, parent, false)
        return ViewHolder(itemView)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // load data into the row
        val currentNews = news[position]
        holder.newsTitleSource.text = currentNews.title
        holder.source.text = currentNews.source
        holder.newsContents.text = currentNews.content
        holder.cardView.setOnClickListener{
        var intUrl: Intent = Intent(Intent.ACTION_VIEW, Uri.parse(currentNews.url))
            it.context.startActivity(intUrl)
        }
            Picasso.get().setIndicatorsEnabled(true)
            Picasso
                .get()
                .load(currentNews.thumbnailUrl)
                .into(holder.newsThumbnail)
    }
    override fun getItemCount(): Int {
        return news.size
    }
}