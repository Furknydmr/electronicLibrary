package com.furkan.tasarimproje

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.furkan.tasarimproje.databinding.ActivityPersonelOkurEkleBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class PersonelOkurEkle : AppCompatActivity() {
    private lateinit var binding: ActivityPersonelOkurEkleBinding
    private lateinit var auth: FirebaseAuth
    private var databaseRefence: DatabaseReference? = null
    private var database: FirebaseDatabase? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPersonelOkurEkleBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        databaseRefence = database?.reference!!.child("Users")
        binding.btnKaydet.setOnClickListener{
            registerUser()
        }
        binding.btnAnasayfa.setOnClickListener{
            startActivity(Intent(this@PersonelOkurEkle,PersonelAnasayfa::class.java))
        }
        binding.btnOkurEkle.setOnClickListener{
            startActivity(Intent(this@PersonelOkurEkle, PersonelOkurEkle::class.java))
        }
        binding.btnKitapEkle.setOnClickListener{
            startActivity(Intent(this@PersonelOkurEkle, PersonelKitapEkle::class.java))
        }
    }
    private fun registerUser() {
        val email = binding.etOkucuEkleEposta.text.toString()
        val sifre = binding.etOkucuEkleTCKimlik.text.toString()
        val isim = binding.etOkucuEkleAd.text.toString()
        val soyisim = binding.etOkucuEkleSoyad.text.toString()
        val etkimlik = binding.etOkucuEkleTCKimlik.text.toString()
        val ettelefon = binding.etOkucuEkleTelefon.text.toString()
        val etmeslek = binding.etOkucuEkleMeslek.text.toString()


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
                    sifreYenile()
                    Toast.makeText(this, "Kayıt Başarlı!", Toast.LENGTH_LONG).show()
                    val intent = Intent(this, PersonelOkurEkle::class.java)
                    startActivity(intent)
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
    private fun sifreYenile(){
        val sPassword = binding.etOkucuEkleEposta.text.toString()
        if (sPassword == "") {
            Toast.makeText(
                applicationContext,
                "Geçerli bir mail adresi giriniz.",
                Toast.LENGTH_LONG
            ).show()
        } else {

            auth.sendPasswordResetEmail(sPassword)
                .addOnCompleteListener(this) { sifirlama ->
                    if (sifirlama.isSuccessful){
                        Toast.makeText(applicationContext, "İşlem Başarılı.", Toast.LENGTH_LONG).show()
                    }else{
                        Toast.makeText(applicationContext, "İşlem başarısız", Toast.LENGTH_LONG).show()
                    }


                }.addOnFailureListener {
                    Toast.makeText(this, it.toString(), Toast.LENGTH_LONG).show()
                }


        }
    }
}