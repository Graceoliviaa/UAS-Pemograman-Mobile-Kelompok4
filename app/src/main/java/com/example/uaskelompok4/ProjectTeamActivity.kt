package com.example.uaskelompok4

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.uaskelompok4.data.AppDatabase
import com.example.uaskelompok4.data.Employee
import com.example.uaskelompok4.data.entity.ProjectTeamMember
import java.util.concurrent.Executors

class ProjectTeamActivity : AppCompatActivity() {

    private var projectId: Int = -1
    private lateinit var adapter: TeamAdapter
    private val members = mutableListOf<Pair<ProjectTeamMember, Employee?>>()
    private val executor = Executors.newSingleThreadExecutor()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_project_team)

        projectId = intent.getIntExtra("PROJECT_ID", -1)

        findViewById<TextView>(R.id.btnBack).setOnClickListener { finish() }

        findViewById<TextView>(R.id.btnAssign).setOnClickListener {
            val intent = Intent(this, AssignMemberActivity::class.java)
            intent.putExtra("PROJECT_ID", projectId)
            startActivity(intent)
        }

        adapter = TeamAdapter(members) { member ->
            executor.execute {
                AppDatabase.getDatabase(this).projectTeamMemberDao().delete(member)
                runOnUiThread { loadTeam() }
            }
        }

        findViewById<RecyclerView>(R.id.rvTeam).apply {
            layoutManager = LinearLayoutManager(this@ProjectTeamActivity)
            adapter = this@ProjectTeamActivity.adapter
        }

        loadTeam()
    }

    override fun onResume() {
        super.onResume()
        loadTeam()
    }

    private fun loadTeam() {
        val db = AppDatabase.getDatabase(this)
        executor.execute {
            val teamMembers = db.projectTeamMemberDao().getByProject(projectId)
            val result = teamMembers.map { member ->
                val employee = db.employeeDao().getAll().find { it.id == member.employeeId }
                Pair(member, employee)
            }
            runOnUiThread {
                members.clear()
                members.addAll(result)
                adapter.notifyDataSetChanged()
            }
        }
    }
}

class TeamAdapter(
    private val list: MutableList<Pair<ProjectTeamMember, Employee?>>,
    private val onDelete: (ProjectTeamMember) -> Unit
) : RecyclerView.Adapter<TeamAdapter.VH>() {

    inner class VH(view: View) : RecyclerView.ViewHolder(view) {
        val tvInitial: TextView = view.findViewById(R.id.tvInitial)
        val tvName: TextView = view.findViewById(R.id.tvEmployeeName)
        val tvRole: TextView = view.findViewById(R.id.tvRole)
        val btnDelete: TextView = view.findViewById(R.id.btnDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_team_member, parent, false)
        return VH(view)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val (member, employee) = list[position]
        val name = employee?.name ?: "Unknown"
        holder.tvInitial.text = name.take(2).uppercase()
        holder.tvName.text = name
        holder.tvRole.text = member.roleInProject
        holder.btnDelete.setOnClickListener { onDelete(member) }
    }

    override fun getItemCount() = list.size
}