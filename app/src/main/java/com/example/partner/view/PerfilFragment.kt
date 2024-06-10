package com.example.partner.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.hello_world.R
import com.example.hello_world.databinding.FragmentPerfilBinding
import com.example.partner.model.Info
import com.example.partner.model.User
import com.example.partner.util.Converters
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
class PerfilFragment : Fragment() {

    private lateinit var database: DatabaseReference
    private lateinit var binding: FragmentPerfilBinding
    private lateinit var user: User
    private val TAG = "PerfilFragment"

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPerfilBinding.inflate(inflater, container, false)

        // Inicializando o Firebase Database
        database = FirebaseDatabase.getInstance().reference

        // Inicializando o historyModel
        user = User()

        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Recuperar os dados do usuário do Bundle
        Log.i(TAG, "User vindo do login: ${(arguments?.getSerializable("USER_DATA") as User?)!!}")
        user = (arguments?.getSerializable("USER_DATA") as User?)!!

        // Exibir o nome do usuário no TextView com o id textView3
        val textView3 = view.findViewById<TextView>(R.id.textView3)
        textView3.text = user.name
    }



    private fun readDataFromFirebase() {
        /*
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
        */
    }

}
