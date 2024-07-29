package com.furkan.tasarimproje

//noinspection SuspiciousImport
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.furkan.tasarimproje.databinding.ActivityPersonelOduncAlanlarBinding
import com.google.firebase.firestore.FirebaseFirestore

class PersonelOduncAlanAll : AppCompatActivity() {
    private lateinit var binding: ActivityPersonelOduncAlanlarBinding
    private val firestore = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPersonelOduncAlanlarBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val kullaniciListesi = ArrayList<PersonelOduncAlanAllList>()

        val barkodSonuc = intent.getStringExtra("barkodSonuc").toString()
        val koleksiyonRef = firestore.collection("Kutuphane")
            .document(barkodSonuc).collection("odunc_alanlar")

        koleksiyonRef.get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val kullaniciData = document.data
                    val kullanici = PersonelOduncAlanAllList(
                        ad = kullaniciData["oduncAlanIsim"] as? String,
                        alinanTarih = kullaniciData["oduncAlınanTarih"] as? String,
                        teslimTarih = kullaniciData["oduncGeriAlinacakTarih"] as? String
                    )
                    kullaniciListesi.add(kullanici)

                    // RecyclerView güncelleme işlemleri
                    val recyclerView = binding.recyclerView
                    recyclerView.layoutManager = LinearLayoutManager(this)
                    recyclerView.adapter = PersonelOduncAlanallAdapter(kullaniciListesi)


                }
            }
            .addOnFailureListener { exception ->
                Log.w("Firestore", "Error getting documents.", exception)
            }
    }
}
