package com.example.uaskelompok4.data.entity

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.uaskelompok4.R


class DocumentAdapter(
    private var documents: List<ProjectDocument>,
    private val onDeleteClick: (ProjectDocument) -> Unit
) : RecyclerView.Adapter<DocumentAdapter.DocumentViewHolder>() {

    class DocumentViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvDocName: TextView = view.findViewById(R.id.tvDocName)
        val tvDocPath: TextView = view.findViewById(R.id.tvDocPath)
        val tvUploadDate: TextView = view.findViewById(R.id.tvUploadDate)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DocumentViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_document, parent, false)
        return DocumentViewHolder(view)
    }

    override fun onBindViewHolder(holder: DocumentViewHolder, position: Int) {
        val document = documents[position]
        holder.tvDocName.text = document.docName
        holder.tvDocPath.text = document.docPath
        holder.tvUploadDate.text = document.uploadDate

        holder.itemView.setOnLongClickListener {
            onDeleteClick(document)
            true
        }
    }

    override fun getItemCount(): Int = documents.size

    fun updateData(newDocuments: List<ProjectDocument>) {
        documents = newDocuments
        notifyDataSetChanged()
    }
}