package com.furkan.tasarimproje

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class PersonelRezervasyonAllAdapter(private val rezervasyonList: List<PersonelRezervasyonAllList>):
RecyclerView.Adapter<PersonelRezervasyonAllAdapter.ViewHolder>(){

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val teslimAlan: TextView = itemView.findViewById(R.id.txtTeslimAlan)
        val alinanTarih: TextView = itemView.findViewById(R.id.txtTeslimEdilenTarih)
        val geriAlinacakTarih: TextView = itemView.findViewById(R.id.txtGeriAlinacakTarih)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.personel_rezervasyon_item, parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val rezervasyonlar = rezervasyonList[position]
        holder.teslimAlan.text = rezervasyonlar.ad
        holder.alinanTarih.text = rezervasyonlar.alinanTarih
        holder.geriAlinacakTarih.text = rezervasyonlar.geriAlinacakTarihTarih
    }

    override fun getItemCount(): Int {
        return rezervasyonList.size
    }
}