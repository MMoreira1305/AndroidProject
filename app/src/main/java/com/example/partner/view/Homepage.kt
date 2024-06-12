package com.example.partner.view

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ScrollView
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.hello_world.R
import com.example.hello_world.databinding.ActivityHomepageBinding
import com.example.partner.model.Info
import com.example.partner.model.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.SimpleDateFormat
import java.util.Locale

class Homepage : AppCompatActivity() {
    private lateinit var binding: ActivityHomepageBinding
    private var TAG: String = "HomePage"
    private lateinit var user: User
    private lateinit var database: DatabaseReference
    private lateinit var tableLayout: TableLayout

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
        this.database = FirebaseDatabase.getInstance().reference
        this.user = (intent.getSerializableExtra("USER_DATA") as User?)!!
        this.tableLayout = binding.tableLayout
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

    @Suppress("DEPRECATION")
    private fun readDataFromFirebase() {
        Log.i(TAG, "readDataFromFirebase: Started")
        this.user = (intent.getSerializableExtra("USER_DATA") as User?)!!
        Log.i(TAG, "Usuário: ${user}")
        try{
            database.child("activities").addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (dataSnapshot in snapshot.children) {
                        var nameActivity: String? = null
                        var dateActivity: String? = null
                        var infoActivity: Info? = null
                        Log.i(TAG, "Loop: Entrou no loop")

                        val idAluno = dataSnapshot.child("aluno").getValue(String::class.java)
                        val idTurma = dataSnapshot.child("turma").getValue(String::class.java)
                        Log.i(TAG, "IdAluno: ${idAluno} , IdTurma: ${idTurma}")
                        if (user.turma == idTurma.toString()) {
                            Log.i(TAG, "Entrou aqui")
                            nameActivity = dataSnapshot.child("activityName").getValue(String::class.java)
                            dateActivity = dataSnapshot.child("date").getValue(String::class.java)
                            infoActivity = dataSnapshot.child("info").getValue(Info::class.java)
                        }

                        if (nameActivity != null && dateActivity != null && infoActivity != null) {
                            /*
                            historyModel.nameActivity = nameActivity
                            historyModel.dateActivity = dateActivity
                            historyModel.infoActivity = infoActivity

                            addRowToTable(historyModel.nameActivity, historyModel.dateActivity, historyModel.infoActivity)

                             */
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e(TAG, "Failed to read data", error.toException())
                }
            })
        }catch (e: Exception){
            Log.e(TAG, "Error to read history")
        }
    }

    @SuppressLint("WeekBasedYear")
    private fun addRowToTable(activityName: String, date: String, info: Info) {
        val dateFormat = SimpleDateFormat("DD/MM/YYYY", Locale.getDefault())
        // Transformando String em Date
        val dateActivityCompleted: java.util.Date? = dateFormat.parse(date)
        var nd : String = "";

        val tableRow = TableRow(baseContext).apply {
            layoutParams = TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.MATCH_PARENT
            )
            if(dateActivityCompleted != null){
                setBackgroundColor(Color.parseColor("#3CB7AF"))
            }else{
                nd = "N/F"
                setBackgroundColor(Color.parseColor("#FFFFFF"))
            }
            setHorizontalGravity(Gravity.CENTER)
            setVerticalGravity(Gravity.CENTER)
            minimumHeight = 60
        }

        val textViewActivityName = TextView(baseContext).apply {
            textAlignment = View.TEXT_ALIGNMENT_CENTER
            text = activityName
            minHeight = 54
            textSize = 16f
            setPadding(16, 16, 16, 16)
        }

        val textViewDate = TextView(baseContext).apply {
            textAlignment = View.TEXT_ALIGNMENT_CENTER
            minHeight = 54
            text = if(nd == "N/F"){
                "N/F"
            }else{
                date
            }
            textSize = 16f
            setPadding(16, 16, 16, 16)
        }

        val textViewInfo = AppCompatButton(baseContext).apply {
            textAlignment = View.TEXT_ALIGNMENT_CENTER
            text = "INFO"
            textSize = 14f
            gravity = Gravity.CENTER
            setTextColor(Color.parseColor("#3CB7AF"))
            setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.rounded_button))
            setOnClickListener {
                // Inflate the custom layout/view
                val inflater = LayoutInflater.from(context)
                val view = inflater.inflate(R.layout.custom_dialog, null)
                // Set the activity data to the dialog's views
                view.findViewById<TextView>(R.id.title).text = info.nameActivity
                view.findViewById<TextView>(R.id.date).text = info.dateActivity
                view.findViewById<TextView>(R.id.grade).text = info.resultActivity.toString()
                view.findViewById<TextView>(R.id.description).text = info.descriptionInfo

                // Create the AlertDialog
                val dialog = AlertDialog.Builder(context)
                    .setView(view)
                    .setPositiveButton("CONFIRMAR") { dialog, _ -> dialog.dismiss() }
                    .setPositiveButton("VOLTAR") {dialog, _ -> dialog.dismiss() }
                    .create()

                // Show the AlertDialog
                dialog.show()
            }
        }

        tableRow.addView(textViewActivityName)
        tableRow.addView(textViewDate)
        tableRow.addView(textViewInfo)
        tableLayout.addView(tableRow)
    }
}
