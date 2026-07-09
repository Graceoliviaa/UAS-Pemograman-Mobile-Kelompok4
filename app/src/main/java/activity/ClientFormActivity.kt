package com.example.uaskelompok4.activity

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.uaskelompok4.R

class ClientFormActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_client_form)

        val btnSimpan = findViewById<Button>(R.id.btnSimpan)

        btnSimpan.setOnClickListener {
            Toast.makeText(this, "Data Client berhasil disimpan", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}