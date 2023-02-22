package br.com.manieri.ipe_branco.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.com.manieri.ipe_branco.R
import br.com.manieri.ipe_branco.model.structure.TopSearches

class HorizontalAdapter(val topSearches: ArrayList<TopSearches>) : RecyclerView.Adapter<HorizontalAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val output = LayoutInflater.from(parent.context).inflate(R.layout.card_top_searches, parent, false)
        return ViewHolder(output)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.textViewTopSearchs.text   = topSearches[position].label
    }

    override fun getItemCount(): Int = topSearches.size


    inner class ViewHolder (itemView : View) : RecyclerView.ViewHolder(itemView) {

        var textViewTopSearchs : TextView = itemView.findViewById(R.id.topSearchesLabel)

    }
}