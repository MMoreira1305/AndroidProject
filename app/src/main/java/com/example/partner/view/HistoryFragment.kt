package com.example.partner.view

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.hello_world.R
import com.example.hello_world.databinding.FragmentHistoricoBinding
import com.example.partner.model.History
import com.example.partner.model.Info
import com.example.partner.model.User
import com.example.partner.util.Converters
import com.google.firebase.database.*
import java.text.SimpleDateFormat
import java.util.Locale

class HistoryFragment : Fragment() {

    private lateinit var database: DatabaseReference
    private lateinit var tableLayout: TableLayout
    private lateinit var binding: FragmentHistoricoBinding
    private lateinit var historyModel: History
    private val TAG = "HistoryFragment"
    private var converters = Converters()
    private lateinit var user: User

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHistoricoBinding.inflate(inflater, container, false)
        tableLayout = binding.tableLayout

        // Inicializando o Firebase Database
        database = FirebaseDatabase.getInstance().reference

        // Inicializando o historyModel
        historyModel = History()

        // Lendo os dados do Firebase
        readDataFromFirebase()


        return binding.root
    }
    @Suppress("DEPRECATION")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Recuperar os dados do usuário do Bundle
        Log.i(TAG,"User vindo do login: ${(arguments?.getSerializable("USER_DATA") as User?)!!}")
        user = (arguments?.getSerializable("USER_DATA") as User?)!!

    }


    private fun readDataFromFirebase() {
        Log.i(TAG, "readDataFromFirebase: Started")
        this.user = (arguments?.getSerializable("USER_DATA") as User?)!!
        Log.i(TAG, "Usuário: ${user}")
        try{
            database.child("log_activities").addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (dataSnapshot in snapshot.children) {
                        var nameActivity: String? = null
                        var dateActivity: String? = null
                        var infoActivity: Info? = null
                        Log.i(TAG, "Loop: Entrou no loop")

                        val idAluno = dataSnapshot.child("aluno").getValue(String::class.java)
                        val idTurma = dataSnapshot.child("turma").getValue(String::class.java)
                        Log.i(TAG, "IdAluno: ${idAluno} , IdTurma: ${idTurma}")
                        if (user.id == idAluno.toString() && user.turma == idTurma.toString()) {
                            Log.i(TAG, "Entrou aqui")
                            nameActivity = dataSnapshot.child("activityName").getValue(String::class.java)
                            dateActivity = dataSnapshot.child("date").getValue(String::class.java)
                            infoActivity = dataSnapshot.child("info").getValue(Info::class.java)
                        }

                        if (nameActivity != null && dateActivity != null && infoActivity != null) {
                            historyModel.nameActivity = nameActivity
                            historyModel.dateActivity = dateActivity
                            historyModel.infoActivity = infoActivity

                            addRowToTable(historyModel.nameActivity, historyModel.dateActivity, historyModel.infoActivity)
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

        val tableRow = TableRow(requireContext()).apply {
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

        val textViewActivityName = TextView(requireContext()).apply {
            textAlignment = View.TEXT_ALIGNMENT_CENTER
            text = activityName
            minHeight = 54
            textSize = 16f
            setPadding(16, 16, 16, 16)
        }

        val textViewDate = TextView(requireContext()).apply {
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

        val textViewInfo = AppCompatButton(requireContext()).apply {
            textAlignment = View.TEXT_ALIGNMENT_CENTER
            text = "INFO"
            textSize = 14f
            gravity = Gravity.CENTER
            setTextColor(Color.parseColor("#3CB7AF"))
            setBackgroundDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.rounded_button))
            setOnClickListener {
                // Inflate the custom layout/view
                val inflater = LayoutInflater.from(requireContext())
                val view = inflater.inflate(R.layout.custom_dialog, null)
                // Set the activity data to the dialog's views
                view.findViewById<TextView>(R.id.title).text = info.nameActivity
                view.findViewById<TextView>(R.id.date).text = info.dateActivity
                view.findViewById<TextView>(R.id.grade).text = info.resultActivity.toString()
                view.findViewById<TextView>(R.id.description).text = info.descriptionInfo

                // Create the AlertDialog
                val dialog = AlertDialog.Builder(requireContext())
                    .setView(view)
                    .setPositiveButton("VOLTAR") { dialog, _ -> dialog.dismiss() }
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
