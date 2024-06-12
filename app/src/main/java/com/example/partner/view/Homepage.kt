package com.example.partner.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ScrollView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.hello_world.R
import com.example.hello_world.databinding.ActivityHomepageBinding
import com.example.partner.model.User

class Homepage : AppCompatActivity() {
    private lateinit var binding: ActivityHomepageBinding
    @SuppressLint("WrongViewCast")
    @Suppress("DEPRECATION")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide() // Adicionado para remover a barra de título
        enableEdgeToEdge()
        binding = ActivityHomepageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val homepageScroll: ScrollView = findViewById(R.id.homepage_scroll)

        val user = intent.getSerializableExtra("USER_DATA") as User?

        // Referência ao botão e ao layout container
        val buttonAtividades: Button = findViewById(R.id.buttonatividades)
        val buttonPerfil: Button = findViewById(R.id.buttonperfil)

        buttonPerfil.setOnClickListener {
            val perfilFragment = PerfilFragment()
            val bundle = Bundle()
            bundle.putSerializable("USER_DATA", user)
            perfilFragment.arguments = bundle
            homepageScroll.visibility = View.INVISIBLE


            // Substituir o contêiner de fragmentos pelo HistoryFragment
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, perfilFragment)
                .addToBackStack(null) // Adiciona a transação à pilha de volta para permitir navegação
                .commit()
        }

        // Configura o listener para o botão
        buttonAtividades.setOnClickListener {
            // Criar uma instância do HistoryFragment
            homepageScroll.visibility = View.INVISIBLE
            val historyFragment = HistoryFragment()
            val bundle = Bundle()
            bundle.putSerializable("USER_DATA", user)
            historyFragment.arguments = bundle
            binding.homepageScroll.visibility

            // Substituir o contêiner de fragmentos pelo HistoryFragment
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, historyFragment)
                .addToBackStack(null) // Adiciona a transação à pilha de volta para permitir navegação
                .commit()
        }
    }
}
