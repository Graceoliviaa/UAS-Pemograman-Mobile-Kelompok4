package com.example.uaskelompok4

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.uaskelompok4.data.AppDatabase
import com.example.uaskelompok4.data.entity.Employee
import com.example.uaskelompok4.data.entity.ProjectTeamMember
import java.util.concurrent.Executors

class AssignMemberActivity : AppCompatActivity() {

    private var projectId: Int = -1
    private val employees = mutableListOf<Employee>()
    private lateinit var adapter: EmployeeAdapter
    private val executor = Executors.newSingleThreadExecutor()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_assign_member)

        projectId = intent.getIntExtra("PROJECT_ID", -1)

        findViewById<TextView>(R.id.btnBack).setOnClickListener { finish() }

        adapter = EmployeeAdapter(employees) { employee ->
            showRoleDialog(employee)
        }

        findViewById<RecyclerView>(R.id.rvEmployees).apply {
            layoutManager = LinearLayoutManager(this@AssignMemberActivity)
            adapter = this@AssignMemberActivity.adapter
        }

        loadEmployees()
    }

    private fun loadEmployees() {
        executor.execute {
            val list = AppDatabase.getDatabase(this).employeeDao().getAll()
            runOnUiThread {
                employees.clear()
                employees.addAll(list)
                adapter.notifyDataSetChanged()
            }
        }
    }

    private fun showRoleDialog(employee: Employee) {
        val input = EditText(this)
        input.hint = "Contoh: Frontend Developer"
        AlertDialog.Builder(this)
            .setTitle("Role ${employee.name} di proyek ini")
            .setView(input)
            .setPositiveButton("Tambahkan") { _, _ ->
                val role = input.text.toString().trim()
                if (role.isEmpty()) {
                    Toast.makeText(this, "Role tidak boleh kosong", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }
                executor.execute {
                    AppDatabase.getDatabase(this).projectTeamMemberDao().insert(
                        ProjectTeamMember(
                            projectId = projectId,
                            employeeId = employee.id,
                            roleInProject = role
                        )
                    )
                    runOnUiThread {
                        Toast.makeText(this, "${employee.name} berhasil ditambahkan", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                }
            }
            .setNegativeButton("Batal", null)
            .show()
    }
}

class EmployeeAdapter(
    private val list: MutableList<Employee>,
    private val onAssign: (Employee) -> Unit
) : RecyclerView.Adapter<EmployeeAdapter.VH>() {

    inner class VH(view: View) : RecyclerView.ViewHolder(view) {
        val tvInitial: TextView = view.findViewById(R.id.tvInitial)
        val tvName: TextView = view.findViewById(R.id.tvName)
        val tvPosition: TextView = view.findViewById(R.id.tvPosition)
        val btnAssign: TextView = view.findViewById(R.id.btnAssign)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_employee, parent, false)
        return VH(view)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val employee = list[position]
        holder.tvInitial.text = employee.name.take(2).uppercase()
        holder.tvName.text = employee.name
        holder.tvPosition.text = employee.position
        holder.btnAssign.setOnClickListener { onAssign(employee) }
    }

    override fun getItemCount() = list.size
}