package com.furkan.tasarimproje

import android.content.Intent
import android.view.View
import android.widget.PopupMenu
import androidx.appcompat.app.AppCompatActivity

object PersonelMenuPopup {
    fun showPopupMenu(activity: AppCompatActivity, view: View) {
        val popupMenu = PopupMenu(activity, view)
        popupMenu.menuInflater.inflate(R.menu.personel_ansayfa_sol_kose_menu, popupMenu.menu)

        popupMenu.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.action_Rezervasyonlar -> {
                    activity.startActivity(Intent(activity, PersonelRezervasyonAll::class.java))
                    activity.finish()
                    true
                }
                R.id.action_CikisYap -> {
                    activity.getSharedPreferences("Your_Preferences_Name", AppCompatActivity.MODE_PRIVATE)
                        .edit().putBoolean("isUserLoggedIn", false).apply()
                    activity.startActivity(Intent(activity, MainActivity::class.java))
                    activity.finish()
                    true
                }
                else -> false
            }
        }

        // PopupMenu'yu göster
        popupMenu.show()
    }
}