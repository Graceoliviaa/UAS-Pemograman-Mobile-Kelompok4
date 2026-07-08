package com.example.uaskelompok4

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.uaskelompok4.data.AppDatabase
import com.example.uaskelompok4.data.entity.DocumentAdapter
import com.example.uaskelompok4.databinding.ActivityProjectDocumentBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProjectDocumentActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProjectDocumentBinding
    private lateinit var adapter: DocumentAdapter
    private var projectId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProjectDocumentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        projectId = intent.getIntExtra("PROJECT_ID", -1)

        adapter = DocumentAdapter(emptyList()) { document ->
            CoroutineScope(Dispatchers.IO).launch {
                AppDatabase.getDatabase(this@ProjectDocumentActivity)
                    .projectDocumentDao().delete(document)
                loadDocuments()
            }
        }
        binding.rvDocuments.adapter = adapter

        binding.fabAddDocument.setOnClickListener {
            val intent = Intent(this, DocumentFormActivity::class.java)
            intent.putExtra("PROJECT_ID", projectId)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        loadDocuments()
    }

    private fun loadDocuments() {
        CoroutineScope(Dispatchers.IO).launch {
            val docs = AppDatabase.getDatabase(this@ProjectDocumentActivity)
                .projectDocumentDao().getByProject(projectId)
            withContext(Dispatchers.Main) {
                adapter.updateData(docs)
            }
        }
    }
}