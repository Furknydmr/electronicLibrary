package com.furkan.tasarimproje

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.furkan.tasarimproje.databinding.ActivityPersonelAllRezervasyonBinding
import com.google.firebase.firestore.FirebaseFirestore

class PersonelRezervasyonAll : AppCompatActivity() {
    private lateinit var binding: ActivityPersonelAllRezervasyonBinding
    private val firestore = FirebaseFirestore.getInstance()
    private val rezevsayonListesi = ArrayList<PersonelRezervasyonAllList>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPersonelAllRezervasyonBinding.inflate(layoutInflater)
        setContentView(binding.root)
        rezervasyonOku()
    }

    private fun rezervasyonOku() {
        val koleksiyonRef = firestore.collection("TumRezervasyonlarList")
        koleksiyonRef.get()
            .addOnSuccessListener { task ->
                for (document in task) {
                    val rezervasyonData = document.data
                    val rezervasyon = PersonelRezervasyonAllList(
                        ad = rezervasyonData["oduncAlanIsim"] as String,
                        alinanTarih = rezervasyonData["oduncAlınanTarih"] as String,
                        geriAlinacakTarihTarih = rezervasyonData["oduncGeriAlinacakTarih"] as? String
                    )
                    rezevsayonListesi.add(rezervasyon)
                    val recyclerView = binding.recyclerView
                    recyclerView.layoutManager = LinearLayoutManager(this)
                    recyclerView.adapter = PersonelRezervasyonAllAdapter(rezevsayonListesi)
                }
            }
            .addOnFailureListener { exception ->
                Log.w("Firestore", "Error getting documents.", exception)
            }
    }
}