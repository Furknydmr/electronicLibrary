package com.furkan.tasarimproje

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.furkan.tasarimproje.databinding.ActivityOkurDuyurularBinding
import com.google.firebase.firestore.FirebaseFirestore

class OkurDuyurular : AppCompatActivity() {
    private lateinit var binding : ActivityOkurDuyurularBinding
    private val firestore = FirebaseFirestore.getInstance()
    val duyuruListesi = ArrayList<OrtakDuyuruEkleList>()
    private lateinit var okurDuyuruAdapter: OkurDuyuruAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOkurDuyurularBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)
        okurDuyuruAdapter = OkurDuyuruAdapter(duyuruListesi)
        recyclerView.adapter = okurDuyuruAdapter
        duyuruOku()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun duyuruOku() {
        val koleksiyonRef = firestore?.collection("Duyurular")
        koleksiyonRef?.get()
            ?.addOnSuccessListener {result ->
                duyuruListesi.clear()
                for (document in result){
                    val kullaniciData = document.data
                    val duyuru = OrtakDuyuruEkleList(
                        duyuruIcerigi = kullaniciData["duyuruIcerigi"] as? String,
                    )
                    duyuruListesi.add(duyuru)

                }
                okurDuyuruAdapter.notifyDataSetChanged()
            }
            ?.addOnFailureListener{exception ->
                Log.w("Firestore", "Error getting documents.", exception)
            }
    }
}