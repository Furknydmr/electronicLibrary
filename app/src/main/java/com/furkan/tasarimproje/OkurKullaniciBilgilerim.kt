package com.furkan.tasarimproje

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.furkan.tasarimproje.databinding.ActivityKullaniciBilgilerimBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class OkurKullaniciBilgilerim : AppCompatActivity() {
    private lateinit var binding: ActivityKullaniciBilgilerimBinding
    private lateinit var auth: FirebaseAuth
    private var databaseRefence: DatabaseReference? = null
    private var database: FirebaseDatabase? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityKullaniciBilgilerimBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        databaseRefence = database?.reference!!.child("Users")
        val currentUser = auth.currentUser
        binding.txtKullaniciBilgilerimEposta.text = currentUser?.email

        //realtime database users child a ulaşıp id den bilgiilier alma
        val userReference = databaseRefence?.child(currentUser?.uid!!)
        userReference?.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                binding.txtKullaniciBilgilerimIsim.text = snapshot.child("isim").value.toString()
                binding.txtKullaniciBilgilerimTelefon.text = snapshot.child("telefon").value.toString()
                binding.txtKullaniciBilgilerimTCKimlik.text = snapshot.child("tcKimlik").value.toString()
                binding.txtKullaniciBilgilerimMeslek.text = snapshot.child("meslek").value.toString()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
        binding.btnhesabimiSil.setOnClickListener{
           currentUser?.delete()?.addOnCompleteListener{
               if (it.isSuccessful){
                   val currentUserDb = currentUser.let { it1 -> databaseRefence?.child(it1.uid) }
                   currentUserDb?.removeValue()
                   Toast.makeText(applicationContext,"Hesabiniz silindi.",Toast.LENGTH_LONG).show()
                   auth.signOut()
                   startActivity(Intent(this@OkurKullaniciBilgilerim, MainActivity::class.java))
                   finish()

               }
           }
        }
        binding.btnBilgileriDuzenle.setOnClickListener{
            startActivity(Intent(this@OkurKullaniciBilgilerim, OkurBilgiGuncelle::class.java))
            finish()
        }


    }
    /*private fun bilgileriAl(){
        val auth = user.currentUser
        val key = auth?.uid.toString()
        val databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(key)
        databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val isim = snapshot.child("isim").value
                val mail = snapshot.child("mail").value
                val tcKimlik = snapshot.child("kimlik").value
                val telefonNumarasi = snapshot.child("telefon").value
                val meslek = snapshot.child("meslek").value
                binding.txtKullaniciBilgilerimIsim.text = isim.toString()
                binding.txtKullaniciBilgilerimEposta.text = mail.toString()
                binding.txtKullaniciBilgilerimTCKimlik.text = tcKimlik.toString()
                binding.txtKullaniciBilgilerimTelefon.text = telefonNumarasi.toString()
                binding.txtKullaniciBilgilerimMeslek.text = meslek.toString()
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Firebase", "Error: ${error.message}")
            }
        })
    }*/
}
