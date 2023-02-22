package br.com.manieri.ipe_branco

import android.app.Application
import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore

class MainViewModel(application: Application)
    : AndroidViewModel(application) {

    var name = ""

    val instance = FirebaseFirestore.getInstance()

    val userMap = hashMapOf(
        "name" to "Beatriz",
        "lastName" to "Manieri",
        "age" to "24"
    )

    fun save(){
        instance.collection("Teste").document("João")
            .set(userMap)
            .addOnCompleteListener {
                Log.w(TAG, "onViewCreated: Sucesso ao criar dados")
            }.addOnFailureListener {
                Log.w(TAG, "onViewCreated: Erro ao criar dados")
            }
    }

    fun read(){
        instance.collection("Teste").document("João").addSnapshotListener { value, error ->
            Log.w(TAG, "onViewCreated: ${value?.getString("name")}", )
        }
    }

}