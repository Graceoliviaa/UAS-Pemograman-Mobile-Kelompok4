package com.example.uaskelompok4

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.uaskelompok4.data.AppDatabase
import com.example.uaskelompok4.data.entity.Project

class ProjectListActivity : AppCompatActivity() {

    private lateinit var adapter: ProjectAdapter
    private val projects = mutableListOf<Project>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_project_list)

        findViewById<TextView>(R.id.btnBack).setOnClickListener { finish() }

        findViewById<TextView>(R.id.btnAdd).setOnClickListener {
            startActivity(Intent(this, ProjectFormActivity::class.java))
        }

        adapter = ProjectAdapter(projects) { project ->
            val intent = Intent(this, ProjectDetailActivity::class.java)
            intent.putExtra("PROJECT_ID", project.id)
            startActivity(intent)
        }

        findViewById<RecyclerView>(R.id.rvProjects).apply {
            layoutManager = LinearLayoutManager(this@ProjectListActivity)
            adapter = this@ProjectListActivity.adapter
        }

        loadProjects()
    }

    override fun onResume() {
        super.onResume()
        loadProjects()
    }

    private fun loadProjects() {
        val db = AppDatabase.getDatabase(this)
        db.projectDao().getAll().observe(this) { list ->
            projects.clear()
            projects.addAll(list)
            adapter.notifyDataSetChanged()
        }
    }
}

class ProjectAdapter(
    private val list: MutableList<Project>,
    private val onClick: (Project) -> Unit
) : RecyclerView.Adapter<ProjectAdapter.VH>() {

    inner class VH(view: View) : RecyclerView.ViewHolder(view) {
        val tvName: TextView = view.findViewById(R.id.tvProjectName)
        val tvClient: TextView = view.findViewById(R.id.tvClientName)
        val tvStatus: TextView = view.findViewById(R.id.tvStatus)
        val statusBar: View = view.findViewById(R.id.statusBar)
        val card: CardView = view as CardView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_project, parent, false)
        return VH(view)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val project = list[position]
        holder.tvName.text = project.name
        holder.tvClient.text = "Klien: ID ${project.clientId ?: "-"}"
        holder.tvStatus.text = project.status

        val color = when (project.status) {
            "Berjalan" -> android.graphics.Color.parseColor("#1D9E75")
            "Perencanaan" -> android.graphics.Color.parseColor("#EF9F27")
            "Selesai" -> android.graphics.Color.parseColor("#378ADD")
            else -> android.graphics.Color.GRAY
        }
        holder.statusBar.setBackgroundColor(color)
        holder.tvStatus.setTextColor(color)
        holder.card.setOnClickListener { onClick(project) }
    }

    override fun getItemCount() = list.size
}