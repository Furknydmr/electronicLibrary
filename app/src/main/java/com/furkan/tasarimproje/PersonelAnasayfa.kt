package com.furkan.tasarimproje

//noinspection SuspiciousImport
import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.furkan.tasarimproje.databinding.ActivityPersonelAnasayfaBinding
import com.google.firebase.firestore.FirebaseFirestore
import java.util.Locale

class PersonelAnasayfa : AppCompatActivity() {
    private lateinit var binding: ActivityPersonelAnasayfaBinding
    private val firestore = FirebaseFirestore.getInstance()
    private val kitapListesi = ArrayList<OrtakAnsayfaKitapList>()
    private lateinit var sharedPreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPersonelAnasayfaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getKitapListesi()
        binding.edtArama.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                // Girilen metne göre verileri filtrele
                filterKitapList(s.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Bu durumda kullanılmaz
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Bu durumda kullanılmaz
            }
        })




        binding.btnAnasayfa.setOnClickListener{
        startActivity(Intent(this@PersonelAnasayfa,PersonelAnasayfa::class.java))
        }
        binding.btnOkurEkle.setOnClickListener{
            startActivity(Intent(this@PersonelAnasayfa, PersonelOkurEkle::class.java))
        }
        binding.btnKitapEkle.setOnClickListener{
        startActivity(Intent(this@PersonelAnasayfa, PersonelKitapEkle::class.java))
        }

        binding.btnQr.setOnClickListener{
            startActivity(Intent(this@PersonelAnasayfa, PersonelBarcod::class.java))
        }
        binding.btnDuyuruEkle.setOnClickListener{
            startActivity(Intent(this@PersonelAnasayfa, PersonelDuyuruEkle::class.java))
        }
        binding.btnMenu.setOnClickListener {
            PersonelMenuPopup.showPopupMenu(this@PersonelAnasayfa, it)
        }


    }
    @SuppressLint("NotifyDataSetChanged")
    private fun getKitapListesi(){
        val koleksiyonRef = firestore.collection("Kutuphane")
        koleksiyonRef.get()
            .addOnSuccessListener { result ->
                kitapListesi.clear()
                for (document in result) {
                    val kullaniciData = document.data
                    val kitap = OrtakAnsayfaKitapList(
                        kitapAdi = kullaniciData["kitapAdi"] as? String,
                        yazar = kullaniciData["yazar"] as? String,
                        tarih = kullaniciData["basimTarih"] as? String,
                        adet = kullaniciData["kitapAdet"] as? String,
                        ilkAdet = kullaniciData["ilkKitapAdet"] as? String
                    )
                    kitapListesi.add(kitap)
                }
                    // RecyclerView güncelleme işlemleri
                val recyclerView = binding.recyclerView
                recyclerView.layoutManager = LinearLayoutManager(this)
                recyclerView.adapter = OrtakAnasayfaKitapAdapter(kitapListesi)

            }
            .addOnFailureListener{ exception ->
                Log.w("Firestore", "Error getting documents.", exception)
            }
    }
    private fun filterKitapList(query: String) {
        val filteredList = ArrayList<OrtakAnsayfaKitapList>()

        for (kitap in kitapListesi) {
            val kitapAdi = kitap.kitapAdi?.lowercase(Locale.getDefault()) ?: ""
            if (kitapAdi.contains(query.lowercase(Locale.getDefault()))) {
                filteredList.add(kitap)
            }
        }

        // RecyclerView'ı filtrelenmiş verilerle güncelle
        val recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = OrtakAnasayfaKitapAdapter(filteredList)
    }
}