package com.furkan.tasarimproje

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class PersonelDuyuruEkleAdapter (private val duyuruList: List<OrtakDuyuruEkleList>):
    RecyclerView.Adapter<PersonelDuyuruEkleAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val duyuruIcerigi: TextView = itemView.findViewById(R.id.txtDuyuruIcerigi)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.personel_duyuru_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return duyuruList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val duyurular = duyuruList[position]
        holder.duyuruIcerigi.text = duyurular.duyuruIcerigi
    }
}


