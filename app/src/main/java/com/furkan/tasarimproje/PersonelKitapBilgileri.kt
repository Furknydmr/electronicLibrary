package com.furkan.tasarimproje

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.furkan.tasarimproje.databinding.ActivityPersonelKitapBilgileriBinding
import com.google.firebase.firestore.FirebaseFirestore

class PersonelKitapBilgileri : AppCompatActivity() {
    private lateinit var binding: ActivityPersonelKitapBilgileriBinding
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPersonelKitapBilgileriBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val firestore = FirebaseFirestore.getInstance()
        val barkodSonuc = intent.getStringExtra("barkodSonuc")

        binding.btnAnasayfa.setOnClickListener{
            startActivity(Intent(this@PersonelKitapBilgileri,PersonelAnasayfa::class.java))
        }
        binding.btnOkurEkle.setOnClickListener{
            startActivity(Intent(this@PersonelKitapBilgileri, PersonelOkurEkle::class.java))
        }
        binding.btnKitapEkle.setOnClickListener{
            startActivity(Intent(this@PersonelKitapBilgileri, PersonelKitapEkle::class.java))
        }
        binding.btnKitapSil.setOnClickListener{
            kitapSil()
            startActivity(Intent(this@PersonelKitapBilgileri, PersonelAnasayfa::class.java))
        }
        binding.btnOduncVer.setOnClickListener {
            val barkodSonuc = binding.txtKitapBarkodu.text.toString()
            val kitapAdi = binding.txtKitapAdi.text.toString()
            val kitapAdet = binding.txtKitapAdet.text.toString()
            val intent = Intent(this@PersonelKitapBilgileri, PersonelRezervayonIslemi::class.java)
            intent.putExtra("barkodSonuc", barkodSonuc)
            intent.putExtra("KitapAdi", kitapAdi)
            intent.putExtra("kitapAdet", kitapAdet)
            startActivity(intent)
        }
        binding.btnOduncAlanlar.setOnClickListener {
            val barkodSonuc = binding.txtKitapBarkodu.text.toString()
            val intent = Intent(this@PersonelKitapBilgileri, PersonelOduncAlanAll::class.java)
            intent.putExtra("barkodSonuc", barkodSonuc)
            startActivity(intent)
        }
        if (!barkodSonuc.isNullOrBlank()) {
            // Barkod sonucunu kullan
            binding.txtKitapBarkodu.text = "$barkodSonuc"
        } else {
            // Veri alınamazsa, kullanıcıyı bilgilendir
            Toast.makeText(this, "Barkod sonucu alınamadı", Toast.LENGTH_SHORT).show()
        }
        firestore.collection("Kutuphane").document(barkodSonuc.toString())
            .get()
            .addOnSuccessListener { document ->
                if (document != null){
                    binding.txtKitapAdi.text = document.getString("kitapAdi")
                    binding.txtYayinevi.text = document.getString("yayinEvi")
                    binding.txtKitabinYazari.text = document.getString("yazar")
                    binding.txtBasimTarihi.text = document.getString("basimTarih")
                    binding.txtKitapTuru.text = document.getString("kitapturu")
                    binding.txtKitapRafYeri.text = document.getString("kitapRafYeri")
                    binding.txtKitapAdet.text = document.getString("kitapAdet")
                }
                else {
                    Log.d("Firestore", "Dokuman bulunamadı!")
                }
            }
            .addOnFailureListener{
                Log.w("Firestore", "Dokuman okuma hatası!")
            }
    }
    private fun kitapSil(){
        val firestore = FirebaseFirestore.getInstance()
        val barcod = binding.txtKitapBarkodu.text.toString()
        firestore.collection("Kutuphane").document(barcod)
            .delete()
            .addOnSuccessListener {
                Toast.makeText(this, "İşlem başarılı", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener{

            }
    }
}