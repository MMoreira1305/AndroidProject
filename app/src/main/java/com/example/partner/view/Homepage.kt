package com.example.partner.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.hello_world.R

class Homepage : AppCompatActivity() {
    @SuppressLint("WrongViewCast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide() // Adicionado para remover a barra de título
        enableEdgeToEdge()
        setContentView(R.layout.activity_homepage)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Referência ao botão e ao layout container
        val buttonAtividades: Button = findViewById(R.id.buttonatividades)

        // Configura o listener para o botão
        buttonAtividades.setOnClickListener {
            // Criar uma instância do HistoryFragment
            val historyFragment = HistoryFragment()

            // Substituir o contêiner de fragmentos pelo HistoryFragment
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, historyFragment)
                .addToBackStack(null)  // Adiciona a transação à pilha de volta para permitir navegação
                .commit()
        }
    }
}
