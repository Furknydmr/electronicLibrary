package com.furkan.tasarimproje
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class OrtakAnasayfaKitapAdapter(private val kitapList: List<OrtakAnsayfaKitapList>) :
    RecyclerView.Adapter<OrtakAnasayfaKitapAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val kitapAdi: TextView = itemView.findViewById(R.id.kitapAdiTextView)
        val yazar: TextView = itemView.findViewById(R.id.yazarTextView)
        val tarih: TextView = itemView.findViewById(R.id.basimTarihTextView)
        val adet: TextView = itemView.findViewById(R.id.txtKitapAdet)
        val ilkAdet: TextView = itemView.findViewById(R.id.txtIlkKitapAdet)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.personel_anasayfa_kitap_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val kitap = kitapList[position]

        holder.kitapAdi.text = kitap.kitapAdi
        holder.yazar.text = kitap.yazar
        holder.tarih.text = kitap.tarih
        holder.adet.text = kitap.adet
        holder.ilkAdet.text = kitap.ilkAdet
    }

    override fun getItemCount(): Int {
        return kitapList.size
    }


}