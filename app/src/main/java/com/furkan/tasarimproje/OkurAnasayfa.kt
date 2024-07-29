package com.furkan.tasarimproje

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.PopupMenu
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.furkan.tasarimproje.databinding.ActivityAnasayfaBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.Locale

class OkurAnasayfa : AppCompatActivity() {
    private lateinit var binding: ActivityAnasayfaBinding
    private lateinit var auth: FirebaseAuth
    private val firestore = FirebaseFirestore.getInstance()
    private val kitapListesi = ArrayList<OrtakAnsayfaKitapList>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAnasayfaBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()
        getKitapListesi()
        binding.btnHesabimLogo.setOnClickListener{
            showPopupMenu(it)
        }
        binding.btnAnasayfa.setOnClickListener{
            startActivity(Intent(this@OkurAnasayfa, OkurAnasayfa::class.java))
        }
        binding.btnRezervasyon.setOnClickListener{
        }
        binding.btnbildirim.setOnClickListener{
            startActivity(Intent(this@OkurAnasayfa, OkurDuyurular::class.java))
        }
        binding.btnHesabim.setOnClickListener{
            startActivity(Intent(this@OkurAnasayfa, OkurKullaniciBilgilerim::class.java))
        }

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



    }

    //Methodlar
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
    private fun showPopupMenu(view: View) {
        val popupMenu = PopupMenu(this, view)
        popupMenu.menuInflater.inflate(R.menu.okur_anasayfa_hesabim_logo, popupMenu.menu)

        popupMenu.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.action_kullanici_bilgileri -> {

                    startActivity(Intent(this@OkurAnasayfa, OkurKullaniciBilgilerim::class.java))
                    finish()
                    true
                }
                R.id.action_cikis_yap -> {
                    auth.signOut()
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                    true
                }
                else -> false
            }
        }

        // PopupMenu'yu göster
        popupMenu.show()
    }
}