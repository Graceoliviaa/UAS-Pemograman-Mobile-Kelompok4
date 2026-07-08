package com.example.uaskelompok4

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.uaskelompok4.data.AppDatabase
import com.example.uaskelompok4.data.entity.ProjectDocument
import com.example.uaskelompok4.databinding.ActivityDocumentFormBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DocumentFormActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDocumentFormBinding
    private var projectId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDocumentFormBinding.inflate(layoutInflater)
        setContentView(binding.root)

        projectId = intent.getIntExtra("PROJECT_ID", -1)

        binding.btnSave.setOnClickListener {
            val name = binding.etDocName.text.toString().trim()
            val path = binding.etDocPath.text.toString().trim()
            val currentDate = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault()).format(Date())

            if (name.isEmpty() || path.isEmpty()) {
                Toast.makeText(this, "Semua kolom harus diisi!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            CoroutineScope(Dispatchers.IO).launch {
                val newDoc = ProjectDocument(
                    projectId = projectId,
                    docName = name,
                    docPath = path,
                    uploadDate = currentDate
                )
                AppDatabase.getDatabase(this@DocumentFormActivity).projectDocumentDao().insert(newDoc)

                withContext(Dispatchers.Main) {
                    Toast.makeText(this@DocumentFormActivity, "Dokumen berhasil disimpan!", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
        }
    }
}