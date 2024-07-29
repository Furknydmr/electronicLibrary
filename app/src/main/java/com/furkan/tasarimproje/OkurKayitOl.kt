package com.furkan.tasarimproje

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.furkan.tasarimproje.databinding.ActivityKayitOlBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class OkurKayitOl : AppCompatActivity() {
    private lateinit var binding: ActivityKayitOlBinding
    private lateinit var auth: FirebaseAuth
    private var databaseRefence: DatabaseReference? = null
    private var database: FirebaseDatabase? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityKayitOlBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        databaseRefence = database?.reference!!.child("Users")

        binding.btnUyeOl.setOnClickListener {
            registerUser()
        }
        binding.btnGirisSayfasinaDon.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    private fun registerUser() {
        val email = binding.epostaKayit.text.toString()
        val sifre = binding.parolaKayit.text.toString()
        val isim = binding.isim.text.toString()
        val soyisim = binding.soyisim.text.toString()
        val etkimlik = binding.tckimlikKayit.text.toString()
        val ettelefon = binding.telefonNumarasKayit.text.toString()
        val etmeslek = binding.meslekKayit.text.toString()


        if (email.isNotEmpty() && sifre.isNotEmpty() && isim.isNotEmpty() && soyisim.isNotEmpty() && etkimlik.isNotEmpty()
            && ettelefon.isNotEmpty() && etmeslek.isNotEmpty()
        ) {
            auth.createUserWithEmailAndPassword(email, sifre).addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // şuanki kullanıcı bilgilerini alır
                    val currentUser = auth.currentUser
                    // kullanici uid sini alıp o uid altında bilgileri kaydet
                    val currentUserDb = currentUser?.let { it1 -> databaseRefence?.child(it1.uid) }
                    currentUserDb?.child("isim")?.setValue(isim)
                    currentUserDb?.child("soyisim")?.setValue(soyisim)
                    currentUserDb?.child("email")?.setValue(email)
                    currentUserDb?.child("tcKimlik")?.setValue(etkimlik)
                    currentUserDb?.child("telefon")?.setValue(ettelefon)
                    currentUserDb?.child("meslek")?.setValue(etmeslek)

                    Toast.makeText(this, "Kayıt Başarlı!", Toast.LENGTH_LONG).show()
                    startActivity(Intent(this@OkurKayitOl, OkurAnasayfa::class.java))
                    finish()
                } else {
                    Toast.makeText(this, "Kayıt Hatalı", Toast.LENGTH_LONG)
                        .show()
                }
            }
        } else {
            Toast.makeText(this, "Lütfen tüm kısımları doldurunuz!", Toast.LENGTH_LONG).show()
        }
    }
}