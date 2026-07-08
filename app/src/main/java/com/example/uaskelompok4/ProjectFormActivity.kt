package com.example.uaskelompok4

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.uaskelompok4.data.AppDatabase
import com.example.uaskelompok4.data.entity.Client
import com.example.uaskelompok4.data.entity.Project
import java.util.concurrent.Executors

class ProjectFormActivity : AppCompatActivity() {

    private var projectId: Int = -1
    private var clientList = listOf<Client>()
    private val executor = Executors.newSingleThreadExecutor()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_project_form)

        projectId = intent.getIntExtra("PROJECT_ID", -1)

        findViewById<TextView>(R.id.btnBack).setOnClickListener { finish() }

        // Setup spinner status
        val statusOptions = listOf("Perencanaan", "Berjalan", "Selesai", "Ditunda")
        val statusAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, statusOptions)
        statusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        findViewById<Spinner>(R.id.spinnerStatus).adapter = statusAdapter

        // Load klien dari database
        val db = AppDatabase.getDatabase(this)
        db.clientDao().getAll().observe(this) { clients ->
            clientList = clients
            val clientNames = clients.map { it.name }.toMutableList()
            clientNames.add(0, "-- Pilih Klien --")
            val clientAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, clientNames)
            clientAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            findViewById<Spinner>(R.id.spinnerClient).adapter = clientAdapter
        }

        // Kalau edit, isi form dengan data yang ada
        if (projectId != -1) {
            findViewById<TextView>(R.id.tvTitle).text = "Edit Proyek"
            executor.execute {
                val project = db.projectDao().getById(projectId)
                runOnUiThread {
                    project?.let {
                        findViewById<EditText>(R.id.etProjectName).setText(it.name)
                        findViewById<EditText>(R.id.etDescription).setText(it.description)
                        findViewById<EditText>(R.id.etStartDate).setText(it.startDate)
                        findViewById<EditText>(R.id.etEndDate).setText(it.endDate ?: "")
                        val statusIndex = statusOptions.indexOf(it.status)
                        if (statusIndex >= 0) findViewById<Spinner>(R.id.spinnerStatus).setSelection(statusIndex)
                    }
                }
            }
        }

        findViewById<Button>(R.id.btnSave).setOnClickListener {
            saveProject()
        }
    }

    private fun saveProject() {
        val name = findViewById<EditText>(R.id.etProjectName).text.toString().trim()
        val description = findViewById<EditText>(R.id.etDescription).text.toString().trim()
        val startDate = findViewById<EditText>(R.id.etStartDate).text.toString().trim()
        val endDate = findViewById<EditText>(R.id.etEndDate).text.toString().trim()
        val status = findViewById<Spinner>(R.id.spinnerStatus).selectedItem.toString()
        val clientIndex = findViewById<Spinner>(R.id.spinnerClient).selectedItemPosition

        if (name.isEmpty()) {
            Toast.makeText(this, "Nama proyek tidak boleh kosong", Toast.LENGTH_SHORT).show()
            return
        }
        if (startDate.isEmpty()) {
            Toast.makeText(this, "Tanggal mulai tidak boleh kosong", Toast.LENGTH_SHORT).show()
            return
        }

        val clientId = if (clientIndex > 0 && clientList.isNotEmpty()) clientList[clientIndex - 1].id else null

        val db = AppDatabase.getDatabase(this)
        executor.execute {
            if (projectId == -1) {
                db.projectDao().insert(
                    Project(
                        name = name,
                        description = description,
                        status = status,
                        startDate = startDate,
                        endDate = endDate.ifEmpty { null },
                        clientId = clientId
                    )
                )
            } else {
                val existing = db.projectDao().getById(projectId)
                existing?.let {
                    db.projectDao().update(
                        it.copy(
                            name = name,
                            description = description,
                            status = status,
                            startDate = startDate,
                            endDate = endDate.ifEmpty { null },
                            clientId = clientId
                        )
                    )
                }
            }
            runOnUiThread {
                Toast.makeText(this, "Proyek berhasil disimpan", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }
}