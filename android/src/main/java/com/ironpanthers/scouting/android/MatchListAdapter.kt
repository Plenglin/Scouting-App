package com.ironpanthers.scouting.android

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.ironpanthers.scouting.common.Match
import kotlinx.android.synthetic.main.row_match_list.view.*

class MatchListAdapter(private val inflater: LayoutInflater, val matchList: List<Match>) : RecyclerView.Adapter<MatchViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MatchViewHolder {
        val view = inflater.inflate(R.layout.row_match_list, parent)
        return MatchViewHolder(view)
    }

    override fun getItemCount(): Int = matchList.size

    override fun onBindViewHolder(holder: MatchViewHolder, position: Int) {
        holder.writeItemData(position + 1, matchList[position])
    }
}

class MatchViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    fun writeItemData(number: Int, item: Match) {
        itemView.apply {
            text_red0.text = item.red[0].team.toString()
            text_red1.text = item.red[1].team.toString()
            text_red2.text = item.red[2].team.toString()
            text_blue0.text = item.blue[0].team.toString()
            text_blue1.text = item.blue[1].team.toString()
            text_blue2.text = item.blue[2].team.toString()

            text_match_number.text = number.toString()
        }

    }

}
