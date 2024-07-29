package com.furkan.tasarimproje

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.furkan.tasarimproje.databinding.ActivitySifremiUnuttumBinding
import com.google.firebase.auth.FirebaseAuth

class OkurSifremiUnuttum : AppCompatActivity() {
    private lateinit var binding: ActivitySifremiUnuttumBinding
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySifremiUnuttumBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()

        binding.btnSifremiUnuttumKayitOl.setOnClickListener {
            val intent = Intent(this@OkurSifremiUnuttum, OkurKayitOl::class.java)
            startActivity(intent)
        }
        binding.btnsifremiUnuttumLinkGonder.setOnClickListener {
            val sPassword = binding.etSifremiUnuttumEmail.text.toString()
            if (sPassword == "") {
                Toast.makeText(applicationContext, "Geçerli bir mail adresi giriniz.",Toast.LENGTH_LONG).show()
            } else {

                auth.sendPasswordResetEmail(sPassword)
                    .addOnCompleteListener(this) { sifirlama ->
                        if (sifirlama.isSuccessful){
                            Toast.makeText(applicationContext,"Email adresinizi kontrol ediniz.",Toast.LENGTH_LONG).show()
                        }else{
                            Toast.makeText(applicationContext,"Şifre sıfırlama işlemi başarısız",Toast.LENGTH_LONG).show()
                        }


                    }.addOnFailureListener {
                        Toast.makeText(this, it.toString(), Toast.LENGTH_LONG).show()
                    }


            }
        }
    }

}
