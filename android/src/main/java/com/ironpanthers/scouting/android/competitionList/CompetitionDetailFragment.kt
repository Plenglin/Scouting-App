package com.ironpanthers.scouting.android.competitionList

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ironpanthers.scouting.android.MatchListAdapter
import com.ironpanthers.scouting.android.R
import com.ironpanthers.scouting.common.Competition
import kotlinx.android.synthetic.main.competition_detail.view.*

/**
 * A fragment representing a single Competition detail screen.
 * This fragment is either contained in a [CompetitionListActivity]
 * in two-pane mode (on tablets) or a [CompetitionDetailActivity]
 * on handsets.
 */
class CompetitionDetailFragment : Fragment() {

    private var item: Competition? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            if (it.containsKey(ARG_DATA)) {
                item = it.getSerializable(ARG_DATA) as Competition?
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.competition_detail, container, false)

        item?.let {
            rootView.competition_detail.list_matches.adapter = MatchListAdapter(inflater, it.matches)
        }

        return rootView
    }

    companion object {
        const val ARG_DATA = "comp_data"
    }
}
