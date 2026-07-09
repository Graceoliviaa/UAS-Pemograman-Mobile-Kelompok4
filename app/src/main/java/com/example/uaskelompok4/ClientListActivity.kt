package com.example.uaskelompok4

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.uaskelompok4.data.AppDatabase
import com.example.uaskelompok4.data.entity.Client

class ClientListActivity : AppCompatActivity() {

    private lateinit var adapter: ClientAdapter
    private val clients = mutableListOf<Client>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_client_list)

        findViewById<TextView>(R.id.btnBack).setOnClickListener { finish() }
        findViewById<TextView>(R.id.btnAdd).setOnClickListener {
            startActivity(Intent(this, ClientFormActivity::class.java))
        }

        adapter = ClientAdapter(clients) { client ->
            val intent = Intent(this, ClientFormActivity::class.java)
            intent.putExtra("CLIENT_ID", client.id)
            startActivity(intent)
        }

        findViewById<RecyclerView>(R.id.rvClients).apply {
            layoutManager = LinearLayoutManager(this@ClientListActivity)
            adapter = this@ClientListActivity.adapter
        }

        loadClients()
    }

    override fun onResume() {
        super.onResume()
        loadClients()
    }

    private fun loadClients() {
        AppDatabase.getDatabase(this).clientDao().getAll().observe(this) { list ->
            clients.clear()
            clients.addAll(list)
            adapter.notifyDataSetChanged()
        }
    }
}

class ClientAdapter(
    private val list: MutableList<Client>,
    private val onClick: (Client) -> Unit
) : RecyclerView.Adapter<ClientAdapter.VH>() {

    inner class VH(view: View) : RecyclerView.ViewHolder(view) {
        val tvName: TextView = view.findViewById(R.id.tvClientName)
        val tvPic: TextView = view.findViewById(R.id.tvPicName)
        val tvContact: TextView = view.findViewById(R.id.tvContact)
        val card: CardView = view as CardView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_client, parent, false)
        return VH(view)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val client = list[position]
        holder.tvName.text = client.name
        holder.tvPic.text = "PIC: ${client.picName}"
        holder.tvContact.text = client.picContact
        holder.card.setOnClickListener { onClick(client) }
    }

    override fun getItemCount() = list.size
}