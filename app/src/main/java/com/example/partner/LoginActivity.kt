package com.example.partner

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.hello_world.R
import com.example.hello_world.databinding.ActivityLoginBinding
import com.example.partner.model.EMAIL_REGEX
import com.example.partner.model.isFieldValid
import com.example.partner.view.Cadastro
import com.example.partner.view.Homepage
import com.google.firebase.auth.FirebaseAuth


class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide() // Adicionado para remover a barra de título
        enableEdgeToEdge()
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()
        setUpView()
    }

    private fun setUpView() {
        binding.apply {
            btnLogin.setOnClickListener {
                goToHomePageActivity()
            }
            gotocadastro.setOnClickListener {
                val intent = Intent(this@LoginActivity, Cadastro::class.java)
                startActivity(intent)
            }
        }
    }

    private fun goToHomePageActivity() {
        var result = true
        val email = binding.edtEmail.text.toString()
        val password = binding.edtPwd.text.toString()
        if (email.isFieldValid(Regex(EMAIL_REGEX)).not()) {
            Toast.makeText(
                this@LoginActivity,
                getString(R.string.email_field),
                Toast.LENGTH_LONG
            ).show()
            result = false
        }
        if (result) {
            val intent = Intent(this@LoginActivity, Homepage::class.java)
            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    startActivity(intent)
                } else {
                    Toast.makeText(
                        this@LoginActivity,
                        getString(R.string.login_error),
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }
}