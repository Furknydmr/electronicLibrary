package com.furkan.tasarimproje

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.furkan.tasarimproje.databinding.ActivityPersonelRezervayonIslemiBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore

class PersonelRezervayonIslemi : AppCompatActivity() {
    private lateinit var binding: ActivityPersonelRezervayonIslemiBinding
    private lateinit var auth: FirebaseAuth
    private var databaseRefence: DatabaseReference? = null
    private var database: FirebaseDatabase? = null
    private val firestore = FirebaseFirestore.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPersonelRezervayonIslemiBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        databaseRefence = database?.reference!!.child("Users")
        val kitapAdi = intent.getStringExtra("KitapAdi")
        binding.txtRezervasyonKitapAdi.text = kitapAdi.toString()

        binding.btnRezervasyonOlustur.setOnClickListener {
            oduncVer()
        }
    }

    private fun oduncVer() {
        val barkodSonuc = intent.getStringExtra("barkodSonuc")
        val ilkAdetStr = intent.getStringExtra("kitapAdet")

        // Kitap bilgilerini al
        val kitapBarkodu = barkodSonuc.toString()
        val oduncAlanTelefon = binding.edtRezervasyonTelefon.text.toString()
        val oduncAlinanTarih = binding.edtRezervasyonVerilenTarih.text.toString()
        val oduncGeriAlinacakTarih = binding.edtRezervasyonAlNacakTarih.text.toString()
        fetchUserName(oduncAlanTelefon) { userName ->
            // Veritabanına eklemek için bir veri haritası oluştur
            val oduncBilgisi = hashMapOf(
                "oduncAlanIsim" to userName,
                "oduncAlınanTarih" to oduncAlinanTarih,
                "oduncGeriAlinacakTarih" to oduncGeriAlinacakTarih
            )

            // Firestore'a ödünç bilgilerini ekleyin
            firestore.collection("Kutuphane")
                .document(kitapBarkodu)
                .collection("odunc_alanlar")
                .add(oduncBilgisi)
                .addOnSuccessListener {
                    // Eklenme başarılı
                    Toast.makeText(this, "Kitap ödünç verildi.", Toast.LENGTH_SHORT).show()

                }
                .addOnFailureListener { e ->
                    // Hata durumu
                    Log.w("TAG", "Hata oluştu: $e")
                    Toast.makeText(this, "Kitap ödünç verilemedi.", Toast.LENGTH_SHORT).show()
                }
            val rezervasyonBilgisi = hashMapOf(
                "oduncAlanIsim" to userName,
                "oduncAlınanTarih" to oduncAlinanTarih,
                "oduncGeriAlinacakTarih" to oduncGeriAlinacakTarih
            )
            firestore.collection("TumRezervasyonlarList")
                .add(rezervasyonBilgisi)
                .addOnSuccessListener {
                    if (ilkAdetStr != null){
                        var ilkAdet = ilkAdetStr.toInt()
                        if (ilkAdet > 0){
                            ilkAdet -= 1
                            val adet = ilkAdet.toString()
                            firestore.collection("Kutuphane").document(kitapBarkodu)
                                .update("kitapAdet", adet)
                                .addOnSuccessListener {

                                }
                                .addOnFailureListener{

                                }
                        }else{
                            Toast.makeText(this, "Rezerve yapılabilir kitap sayısı yetersiz!", Toast.LENGTH_SHORT).show()
                        }
                        
                    }
                }
                .addOnFailureListener { e ->
                    // Hata durumu
                    Log.w("TAG", "Hata oluştu: $e")
                }
        }
    }

    private fun fetchUserName(phoneNumber: String, callback: (String) -> Unit) {
        // Users düğümünden telefon numarasına göre kullanıcının adını çekme
        databaseRefence?.orderByChild("telefon")?.equalTo(phoneNumber)
            ?.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    for (userSnapshot in dataSnapshot.children) {
                        // Veritabanındaki kullanıcının adını çekme
                        val ad = userSnapshot.child("isim").getValue(String::class.java)
                        val soyad = userSnapshot.child("soyisim").getValue(String::class.java)
                        val ad1 = ad + " " + soyad
                        if (ad1 != null) {
                            // Kullanıcı adını callback fonksiyonu aracılığıyla döndürme
                            callback(ad1)
                        } else {
                            // Veri bulunamadı durumu
                            callback("Bilinmiyor")
                        }
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Log.w(
                        "RealtimeDatabase",
                        "Failed to read user value.",
                        databaseError.toException()
                    )
                }
            })
    }
}
