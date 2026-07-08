package com.example.uaskelompok4

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<CardView>(R.id.cardProject).setOnClickListener {
            startActivity(Intent(this, ProjectListActivity::class.java))
        }

        findViewById<CardView>(R.id.cardClient).setOnClickListener {
            startActivity(Intent(this, ClientListActivity::class.java))
        }

        findViewById<CardView>(R.id.cardTeam).setOnClickListener {
            startActivity(Intent(this, ProjectListActivity::class.java))
        }

        findViewById<CardView>(R.id.cardDocument).setOnClickListener {
            startActivity(Intent(this, ProjectListActivity::class.java))
        }
    }
}