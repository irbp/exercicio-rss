package br.ufpe.cin.if710.rss

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.Adapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.itemlista.view.*

class RssListAdapter(private val rssList: List<ItemRSS>,
                     private val context: Context) : Adapter<RssListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context)
                .inflate(R.layout.itemlista, parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = rssList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val itemRss = rssList[position]
        holder.bind(itemRss)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(itemRss: ItemRSS) {
            val title = itemView.item_titulo
            val date = itemView.item_data

            title.text = itemRss.title
            date.text = itemRss.pubDate
        }
    }

}