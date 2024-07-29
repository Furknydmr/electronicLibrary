package com.furkan.tasarimproje

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.furkan.tasarimproje.databinding.ActivityPersonelKitapEkleBinding
import com.google.firebase.firestore.FirebaseFirestore

class PersonelKitapEkle : AppCompatActivity() {
    private lateinit var binding: ActivityPersonelKitapEkleBinding
    private var firestore: FirebaseFirestore? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPersonelKitapEkleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firestore = FirebaseFirestore.getInstance()
        binding.btnDuyuruEkle.setOnClickListener {
            startActivity(Intent(this@PersonelKitapEkle, PersonelDuyuruEkle::class.java))
        }
        binding.btnAnasayfa.setOnClickListener {
            startActivity(Intent(this@PersonelKitapEkle, PersonelAnasayfa::class.java))
        }
        binding.btnOkurEkle.setOnClickListener {
            startActivity(Intent(this@PersonelKitapEkle, PersonelOkurEkle::class.java))
        }
        binding.btnKitapEkle.setOnClickListener {
            startActivity(Intent(this@PersonelKitapEkle, PersonelKitapEkle::class.java))
        }
        binding.btnMenu.setOnClickListener{
            binding.btnMenu.setOnClickListener {
                PersonelMenuPopup.showPopupMenu(this@PersonelKitapEkle, it)
            }
        }
        binding.btnKaydet.setOnClickListener {
            addBook()
            clearEditTextFields()
        }
    }

    private fun addBook() {
        val kitapAdi = binding.edtKitapAdi.text.toString()
        val yayinEvi = binding.edtYayinevi.text.toString()
        val yazar = binding.edtKitabinYazari.text.toString()
        val basimTarih = binding.edtBasimTarihi.text.toString()
        val kitapturu = binding.edtKitapTuru.text.toString()
        val kitapRafYeri = binding.edtKitapRafYeri.text.toString()
        val kitapBarkod = binding.edtKitapBarkodu.text.toString()
        val kitapAdet = binding.edtKitapAdet.text.toString()
        val ilkKitapAdet = binding.edtKitapAdet.text.toString()

        // Firestore veri modelini oluştur
        val kitapMap = hashMapOf(
            "kitapAdi" to kitapAdi,
            "yayinEvi" to yayinEvi,
            "yazar" to yazar,
            "basimTarih" to basimTarih,
            "kitapturu" to kitapturu,
            "kitapRafYeri" to kitapRafYeri,
            "kitapBarkod" to kitapBarkod,
            "kitapAdet" to kitapAdet,
            "ilkKitapAdet" to ilkKitapAdet
        )

        firestore?.collection("Kutuphane")?.document(kitapBarkod)
            ?.set(kitapMap)
            ?.addOnSuccessListener {
                Toast.makeText(this, "Kitap başarıyla eklendi.", Toast.LENGTH_LONG).show()
            }
            ?.addOnFailureListener { e ->
                Toast.makeText(
                    this,
                    "Kitap eklenirken hata oluştu: ${e.message}",
                    Toast.LENGTH_LONG
                ).show()

            }
    }

    private fun clearEditTextFields() {
        binding.edtKitapAdi.text.clear()
        binding.edtYayinevi.text.clear()
        binding.edtKitabinYazari.text.clear()
        binding.edtBasimTarihi.text.clear()
        binding.edtKitapTuru.text.clear()
        binding.edtKitapRafYeri.text.clear()
        binding.edtKitapBarkodu.text.clear()
        binding.edtKitapAdet.text.clear()
        // İsteğe bağlı olarak diğer EditText alanlarını da ekleyebilirsiniz.
    }
}