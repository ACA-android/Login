package com.dm.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.room.Room
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {

    private val TAG = "MainActivity"

    private val db by lazy {
        Room.databaseBuilder(
            applicationContext,
            UserDatabase::class.java, "user_database"
        ).build()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        registerBtn.setOnClickListener {
            val login = loginEditText.text.toString()
            val pass = passEditText.text.toString()
            if (validateInput(login, pass)) {
                registerUser(login, pass)
            }
        }

        loginBtn.setOnClickListener {
            val login = loginEditText.text.toString()
            val pass = passEditText.text.toString()
            if (validateInput(login, pass)) {
                GlobalScope.launch(Dispatchers.IO) {
                    loginUser(login, pass)
                }
            }
        }
    }

    private fun validateInput(login: String, pass: String): Boolean {
        return when {
            login.isEmpty() -> {
                loginEditText.error = "please enter login"
                false
            }
            pass.isEmpty() -> {
                false
            }
            else -> {
                true
            }
        }
    }

    private fun registerUser(login: String, pass: String) {
        GlobalScope.launch(Dispatchers.IO) {
            var user = db.userDao().findUser(login)
            if (user == null) {
                user = User(login, pass)
                db.userDao().insertAll(user)
            }
            loginUser(login, pass)
        }
    }

    private suspend fun loginUser(login: String, pass: String) {
        val user = db.userDao().findUser(login)
        when {
            user == null -> {
                withContext(Dispatchers.Main) {
                    loginEditText.error = "wrong login"
                }
            }
            pass == user.password -> {
                startActivity(Intent(this, ProfileActivity::class.java).apply {
                    putExtra("user", user)
                })
            }
            else -> {
                withContext(Dispatchers.Main) {
                    passEditText.error = "wrong password"
                }
            }
        }
    }
}