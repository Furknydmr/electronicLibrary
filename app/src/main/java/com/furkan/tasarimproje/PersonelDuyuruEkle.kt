package com.furkan.tasarimproje

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.furkan.tasarimproje.databinding.ActivityPersonelDuyuruEkleBinding
import com.google.firebase.firestore.FirebaseFirestore
private const val PREFS_NAME = "MyPrefs"
private const val DUUYURU_NUMARASI_KEY = "duyuruNumarasi"

class PersonelDuyuruEkle : AppCompatActivity() {
    private lateinit var binding : ActivityPersonelDuyuruEkleBinding
    private val firestore = FirebaseFirestore.getInstance()
    private var duyuruNumarasi = 1
    private val duyuruListesi = ArrayList<OrtakDuyuruEkleList>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPersonelDuyuruEkleBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
        duyuruNumarasi = prefs.getInt(DUUYURU_NUMARASI_KEY, 1)


        duyuruOku()
        binding.btnDuyuruEkleYayinla.setOnClickListener {
            duyuruEkle()
        }
    }

    private fun duyuruEkle(){
        val duyuruIcerigi = binding.edtDuyuru.text.toString()

        if (duyuruIcerigi.isNotEmpty()){
            // Firestore veri modelini oluştur
            val kitapMap = hashMapOf(
                "duyuruIcerigi" to duyuruIcerigi,
                "numara" to duyuruNumarasi
            )
            firestore.collection("Duyurular").document(duyuruNumarasi.toString())
                .set(kitapMap)
                .addOnSuccessListener {
                    Toast.makeText(this, "Duyuru başarıyla Eklendi", Toast.LENGTH_LONG).show()
                    duyuruNumarasi++
                    // SharedPreferences'te duyuruNumarasi değerini güncelle
                    val editor = getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit()
                    editor.putInt(DUUYURU_NUMARASI_KEY, duyuruNumarasi)
                    editor.apply()
                    binding.edtDuyuru.text.clear()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Duyuru eklenirken hata oluştu: ${e.message}", Toast.LENGTH_LONG).show()

                }
        }else{
            Toast.makeText(this, "Duyuru Yazılmadı", Toast.LENGTH_LONG).show()
        }

    }
    @SuppressLint("NotifyDataSetChanged")
    private fun duyuruOku(){
        val koleksiyonRef = firestore.collection("Duyurular")
        koleksiyonRef.get()
            .addOnSuccessListener {result ->
                for (document in result){
                    val kullaniciData = document.data
                    val duyuru = OrtakDuyuruEkleList(
                        duyuruIcerigi = kullaniciData["duyuruIcerigi"] as? String,
                    )
                    duyuruListesi.add(duyuru)
                    val recyclerView = binding.recyclerView
                    recyclerView.layoutManager = LinearLayoutManager(this)
                    recyclerView.adapter = PersonelDuyuruEkleAdapter(duyuruListesi)

                }

            }
            .addOnFailureListener{exception ->
                Log.w("Firestore", "Error getting documents.", exception)
            }
    }


}