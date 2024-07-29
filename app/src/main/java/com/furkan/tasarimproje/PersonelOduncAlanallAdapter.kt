package com.furkan.tasarimproje

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class PersonelOduncAlanallAdapter(private val kitapAlanList: List<PersonelOduncAlanAllList>) :
    RecyclerView.Adapter<PersonelOduncAlanallAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val telTextView: TextView = itemView.findViewById(R.id.telTextView)
        val alinanTarihTextView: TextView = itemView.findViewById(R.id.alinanTarihTextView)
        val teslimTarihTextView: TextView = itemView.findViewById(R.id.teslimTarihTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.custom_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val kitapAlan = kitapAlanList[position]

        holder.telTextView.text = kitapAlan.ad
        holder.alinanTarihTextView.text = kitapAlan.alinanTarih
        holder.teslimTarihTextView.text = kitapAlan.teslimTarih
    }

    override fun getItemCount(): Int {
        return kitapAlanList.size
    }
}