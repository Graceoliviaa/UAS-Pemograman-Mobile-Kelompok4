package com.example.uaskelompok4

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.example.uaskelompok4.data.AppDatabase
import java.util.concurrent.Executors

class ProjectDetailActivity : AppCompatActivity() {

    private var projectId: Int = -1
    private val executor = Executors.newSingleThreadExecutor()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_project_detail)

        projectId = intent.getIntExtra("PROJECT_ID", -1)

        findViewById<TextView>(R.id.btnBack).setOnClickListener { finish() }

        findViewById<TextView>(R.id.btnEdit).setOnClickListener {
            val intent = Intent(this, ProjectFormActivity::class.java)
            intent.putExtra("PROJECT_ID", projectId)
            startActivity(intent)
        }

        findViewById<CardView>(R.id.cardTeam).setOnClickListener {
            val intent = Intent(this, ProjectTeamActivity::class.java)
            intent.putExtra("PROJECT_ID", projectId)
            startActivity(intent)
        }

        findViewById<CardView>(R.id.cardDocument).setOnClickListener {
            val intent = Intent(this, ProjectDocumentActivity::class.java)
            intent.putExtra("PROJECT_ID", projectId)
            startActivity(intent)
        }

        loadDetail()
    }

    override fun onResume() {
        super.onResume()
        loadDetail()
    }

    private fun loadDetail() {
        val db = AppDatabase.getDatabase(this)
        executor.execute {
            val project = db.projectDao().getById(projectId)
            val client = project?.clientId?.let { db.clientDao().getById(it) }
            runOnUiThread {
                project?.let {
                    findViewById<TextView>(R.id.tvProjectName).text = it.name
                    findViewById<TextView>(R.id.tvStatus).text = it.status
                    findViewById<TextView>(R.id.tvStartDate).text = it.startDate
                    findViewById<TextView>(R.id.tvDescription).text = it.description
                }
                findViewById<TextView>(R.id.tvClientName).text = client?.name ?: "-"
                findViewById<TextView>(R.id.tvPicName).text = client?.picName ?: "-"
            }
        }
    }
}