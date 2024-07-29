package com.furkan.tasarimproje

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.furkan.tasarimproje.databinding.ActivityBilgiGuncelleBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class OkurBilgiGuncelle : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private var databaseRefence: DatabaseReference? = null
    private var database: FirebaseDatabase? = null
    private lateinit var binding: ActivityBilgiGuncelleBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBilgiGuncelleBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        databaseRefence = database?.reference!!.child("Users")
        val currentUser = auth.currentUser
        binding.bgMail.setText(currentUser?.email)
        val userReference = databaseRefence?.child(currentUser?.uid!!)
        userReference?.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                binding.bgIsim.setText(snapshot.child("isim").value.toString())
                binding.bgSoyisim.setText(snapshot.child("soyisim").value.toString())
                binding.bgTckimlik.setText(snapshot.child("tcKimlik").value.toString())
                binding.bgMeslek.setText(snapshot.child("meslek").value.toString())
                binding.bgTelefonNumarasi.setText(snapshot.child("telefon").value.toString())
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }


        })
        binding.btnBilgiGuncelle.setOnClickListener {
            val guncelEmail = binding.bgMail.text.toString()
            currentUser!!.updateEmail(guncelEmail).addOnCompleteListener {}
            val currentUserDb = currentUser.let { it1 -> databaseRefence?.child(it1.uid) }
            currentUserDb?.removeValue()
            currentUserDb?.child("isim")?.setValue(binding.bgIsim.text.toString())
            currentUserDb?.child("soyisim")?.setValue(binding.bgSoyisim.text.toString())
            currentUserDb?.child("mail")?.setValue(binding.bgMail.text.toString())
            currentUserDb?.child("tcKimlik")?.setValue(binding.bgTckimlik.text.toString())
            currentUserDb?.child("telefon")?.setValue(binding.bgTelefonNumarasi.text.toString())
            currentUserDb?.child("meslek")?.setValue(binding.bgMeslek.text.toString())

            Toast.makeText(applicationContext, "Bilgileriniz Güncellendi.", Toast.LENGTH_LONG)
                .show()
            startActivity(Intent(this@OkurBilgiGuncelle, OkurKullaniciBilgilerim::class.java))
            finish()
        }
    }
}