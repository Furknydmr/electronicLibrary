package com.furkan.tasarimproje

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.furkan.tasarimproje.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // FirebaseAuth instance oluşturuluyor
        auth = FirebaseAuth.getInstance()

        // Kullanıcı daha önce oturum açtıysa ve hatırlama işareti varsa
        val currentUser = auth.currentUser
        val rememberMeChecked = getRememberMePreference()

        // Eğer kullanıcı daha önce oturum açtı ve "Beni Hatırla" işareti varsa, otomatik olarak Anasayfa'ya yönlendir
        if (currentUser != null && rememberMeChecked) {
            val intent = Intent(this@MainActivity, OkurAnasayfa::class.java)
            startActivity(intent)
            finish()
        }
        binding.btnKayitOl.setOnClickListener {
            val intent = Intent(applicationContext, OkurKayitOl::class.java)
            startActivity(intent)
            finish()
        }
        binding.btnSifremiUnuttum.setOnClickListener {
            val intent = Intent(applicationContext, OkurSifremiUnuttum::class.java)
            startActivity(intent)
            finish()
        }
        binding.btnGirisYap.setOnClickListener {
            loginUser()

        }
        binding.btnPersonelGiris.setOnClickListener{
            startActivity(Intent(this@MainActivity, PersonelGiris::class.java))
        }


    }

    private fun loginUser() {
        // Giriş yapmak için e-posta ve şifre alınır
        val email = binding.etGirisEmail.text.toString()
        val sifre = binding.etGirisParola.text.toString()


        if (email.isNotEmpty() && sifre.isNotEmpty()) {  // Eğer e-posta ve şifre boş değilse işlemleri yap

            // Firebase Authentication ile kullanıcı giriş yapmaya çalışır
            auth.signInWithEmailAndPassword(email, sifre).addOnCompleteListener(this) {task->
                if (task.isSuccessful) { // Giriş başarılı ise kullanıcıyı Anasayfa'ya yönlendirir


                    Toast.makeText(this, "Giriş Başarılı!", Toast.LENGTH_LONG).show()


                    if (binding.beniHatRla.isChecked) { // Kullanıcı "Beni Hatırla" işaretliyse, tercihi sakla
                        setRememberMePreference(true)
                    } else {
                        setRememberMePreference(false) // Eğer işaretli değilse false olarak sakla
                    }
                    val intent = Intent(applicationContext, OkurAnasayfa::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(applicationContext, "Email veya şifre hatalı!", Toast.LENGTH_LONG)
                        .show()
                }
            }
        } else {
            Toast.makeText(this, "Email veya şifre boş olamaz!", Toast.LENGTH_LONG).show()
        }
    }

    // "Beni Hatırla" tercihini SharedPreferences'ten okur
    private fun getRememberMePreference(): Boolean {
        val sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE)
        return sharedPreferences.getBoolean("rememberMe", false)
    }

    // "Beni Hatırla" tercihini SharedPreferences'e yazar
    private fun setRememberMePreference(value: Boolean) {
        val sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putBoolean("rememberMe", value)
        editor.apply()
    }
}

