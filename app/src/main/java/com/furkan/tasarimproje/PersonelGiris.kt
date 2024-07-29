package com.furkan.tasarimproje

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.furkan.tasarimproje.databinding.ActivityPersonelGirisBinding

class PersonelGiris : AppCompatActivity() {
    private lateinit var binding : ActivityPersonelGirisBinding
    private lateinit var sharedPreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPersonelGirisBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sharedPreferences = getSharedPreferences("user_prefs", Context.MODE_PRIVATE)

        val isUserLoggedIn = sharedPreferences.getBoolean("isUserLoggedIn", false)

        if (isUserLoggedIn) {
            sharedPreferences.edit().putBoolean("isUserLoggedIn", false).apply()
            startActivity(Intent(this@PersonelGiris, PersonelAnasayfa::class.java))
            finish()
        }

        binding.btnGirisYap.setOnClickListener{
            val personelKullaniciAdi = binding.etGirisEmail.text.toString()
            val personelSifre = binding.etGirisParola.text.toString()
            if(personelKullaniciAdi =="admin" && personelSifre == "123"){
                sharedPreferences.edit().putBoolean("isUserLoggedIn", true).apply()
                startActivity(Intent(this@PersonelGiris, PersonelAnasayfa::class.java))
                finish()
            }else{
                Toast.makeText(this,"YETKİNLİK GEÇERSİZ!", Toast.LENGTH_SHORT).show()
            }
        }
        binding.btnAnasayfayaDon.setOnClickListener{
            startActivity(Intent(this@PersonelGiris,MainActivity::class.java))
        }
    }
}