package com.example.uaskelompok4

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.uaskelompok4.data.AppDatabase
import com.example.uaskelompok4.data.entity.Client
import java.util.concurrent.Executors

class ClientFormActivity : AppCompatActivity() {

    private var clientId: Int = -1
    private val executor = Executors.newSingleThreadExecutor()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_client_form)

        clientId = intent.getIntExtra("CLIENT_ID", -1)

        findViewById<TextView>(R.id.btnBack).setOnClickListener { finish() }

        if (clientId != -1) {
            findViewById<TextView>(R.id.tvTitle).text = "Edit Klien"
            val db = AppDatabase.getDatabase(this)
            executor.execute {
                val client = db.clientDao().getById(clientId)
                runOnUiThread {
                    client?.let {
                        findViewById<EditText>(R.id.etClientName).setText(it.name)
                        findViewById<EditText>(R.id.etPicName).setText(it.picName)
                        findViewById<EditText>(R.id.etPicContact).setText(it.picContact)
                        findViewById<EditText>(R.id.etEmail).setText(it.email)
                        findViewById<EditText>(R.id.etAddress).setText(it.address)
                    }
                }
            }
        }

        findViewById<Button>(R.id.btnSave).setOnClickListener {
            saveClient()
        }
    }

    private fun saveClient() {
        val name = findViewById<EditText>(R.id.etClientName).text.toString().trim()
        val picName = findViewById<EditText>(R.id.etPicName).text.toString().trim()
        val picContact = findViewById<EditText>(R.id.etPicContact).text.toString().trim()
        val email = findViewById<EditText>(R.id.etEmail).text.toString().trim()
        val address = findViewById<EditText>(R.id.etAddress).text.toString().trim()

        if (name.isEmpty()) {
            Toast.makeText(this, "Nama klien tidak boleh kosong", Toast.LENGTH_SHORT).show()
            return
        }

        val db = AppDatabase.getDatabase(this)
        executor.execute {
            if (clientId == -1) {
                db.clientDao().insert(
                    Client(
                        name = name,
                        picName = picName,
                        picContact = picContact,
                        email = email,
                        address = address
                    )
                )
            } else {
                val existing = db.clientDao().getById(clientId)
                existing?.let {
                    db.clientDao().update(
                        it.copy(
                            name = name,
                            picName = picName,
                            picContact = picContact,
                            email = email,
                            address = address
                        )
                    )
                }
            }
            runOnUiThread {
                Toast.makeText(this, "Klien berhasil disimpan", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }
}