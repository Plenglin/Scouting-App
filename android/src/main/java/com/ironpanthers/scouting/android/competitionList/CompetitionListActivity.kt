package com.ironpanthers.scouting.android.competitionList

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.NavUtils
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.ironpanthers.scouting.android.*
import com.ironpanthers.scouting.android.competitionList.dummy.DummyContent
import com.ironpanthers.scouting.common.CompetitionFileSummary
import kotlinx.android.synthetic.main.activity_competition_list.*
import kotlinx.android.synthetic.main.competition_list.*
import kotlinx.android.synthetic.main.row_competition_list.view.*
import kotlinx.coroutines.runBlocking
import org.slf4j.LoggerFactory
import java.io.File
import java.text.SimpleDateFormat

/**
 * An activity representing a list of Pings. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a [CompetitionDetailActivity] representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
class CompetitionListActivity : AppCompatActivity() {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private var twoPane: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_competition_list)

        setSupportActionBar(toolbar)
        toolbar.title = title

        btn_create.setOnClickListener {
            logger.info("Clicked on create button, spawning dialog")
            val editText = EditText(this)
            AlertDialog.Builder(this)
                    .setMessage("TBA Event ID")
                    .setView(editText)
                    .setPositiveButton("Create") { d, _ ->
                        val id = editText.text.toString()
                        logger.debug("fetching TBA data for {}", id)
                        val result = runBlocking { fetchTBAData(id, TBA_API_KEY) }
                        logger.info("retrieved {}", result)
                        val dir = File(filesDir, "competitions/$id.json")
                        writeCompetitionFileData(dir, result, jacksonObjectMapper())
                        updateFileList(list_competitions)
                        d.dismiss()
                    }
                    .setCancelable(true)
                    .create()
                    .show()
        }
        // Show the Up button in the action bar.
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        if (competition_detail_container != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            twoPane = true
        }
        updateFileList(list_competitions)
    }

    override fun onOptionsItemSelected(item: MenuItem) =
            when (item.itemId) {
                android.R.id.home -> {
                    // This ID represents the Home or Up button. In the case of this
                    // activity, the Up button is shown. Use NavUtils to allow users
                    // to navigate up one level in the application structure. For
                    // more details, see the Navigation pattern on Android Design:
                    //
                    // http://developer.android.com/design/patterns/navigation.html#up-vs-back
                    NavUtils.navigateUpFromSameTask(this)
                    true
                }
                else -> super.onOptionsItemSelected(item)
            }

    private fun updateFileList(recyclerView: RecyclerView) {
        recyclerView.adapter = CompetitionFileListAdapter(this, getCompetitions(), twoPane)
    }

    private fun getCompetitions(): List<CompetitionFileSummary> {
        val dir = File(filesDir, "competitions")
        val mapper = jacksonObjectMapper()
        dir.mkdirs()
        return dir.listFiles().map {
            loadCompetitionFileData(it, mapper)
        }
    }

    inner class CompetitionFileListAdapter(private val parentActivity: CompetitionListActivity,
                                     private val values: List<CompetitionFileSummary>,
                                     private val twoPane: Boolean) :
            RecyclerView.Adapter<CompetitionFileListAdapter.CompetitionFileViewHolder>() {

        private val onClickListener: View.OnClickListener

        init {
            onClickListener = View.OnClickListener { v ->
                val item = v.tag as DummyContent.DummyItem
                if (twoPane) {
                    val fragment = CompetitionDetailFragment().apply {
                        arguments = Bundle().apply {
                            putString(CompetitionDetailFragment.ARG_DATA, item.id)
                        }
                    }
                    parentActivity.supportFragmentManager
                            .beginTransaction()
                            .replace(R.id.competition_detail_container, fragment)
                            .commit()
                } else {
                    val intent = Intent(v.context, CompetitionDetailActivity::class.java).apply {
                        putExtra(CompetitionDetailFragment.ARG_DATA, item.id)
                    }
                    v.context.startActivity(intent)
                }
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CompetitionFileViewHolder {
            val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.row_competition_list, parent, false)
            return CompetitionFileViewHolder(view)
        }

        override fun onBindViewHolder(holder: CompetitionFileViewHolder, position: Int) {
            val item = values[position]
            holder.writeData(item)

            with(holder.itemView) {
                tag = item
                setOnClickListener(onClickListener)
            }
        }

        override fun getItemCount() = values.size

        inner class CompetitionFileViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            fun writeData(item: CompetitionFileSummary) {
                itemView.text_name.text = item.name
                itemView.text_date.text = SimpleDateFormat.getInstance().format(item.date)
            }
        }
    }


    companion object {
        @JvmStatic
        val logger = LoggerFactory.getLogger(CompetitionListActivity::class.java)
    }
}
