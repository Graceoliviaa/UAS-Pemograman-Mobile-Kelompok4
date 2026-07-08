package com.example.uaskelompok4.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.uaskelompok4.R

class ClientListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_client_list)

        val btnTambah = findViewById<Button>(R.id.btnTambahClient)

        btnTambah.setOnClickListener {
            startActivity(Intent(this, ClientFormActivity::class.java))
        }
    }
}