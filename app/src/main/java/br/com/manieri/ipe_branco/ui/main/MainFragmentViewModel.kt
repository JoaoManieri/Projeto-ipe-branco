package br.com.manieri.ipe_branco.ui.main

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.manieri.ipe_branco.model.structure.Discussion
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainFragmentViewModel : ViewModel() {

    lateinit var firestore: FirebaseFirestore
    var ObserverListQuestion = MutableLiveData<ArrayList<Discussion>>()


    fun getQuestionList() {

        GlobalScope.launch(Dispatchers.IO) {
            var listDiscussion = arrayListOf<Discussion>()


            firestore = FirebaseFirestore.getInstance()
            firestore.collection("Discussion").get().addOnSuccessListener { documents ->
                documents.forEach {
                    Log.w(TAG, "getQuestionList: ${it.get("body_question").toString()} ")
                    listDiscussion.add(
                        Discussion(
                            discussion_uid = it.get("discussion_uid").toString(),
                            user_uid = it.get("user_uid").toString(),
                            unique_uid = it.get("unique_uid").toString(),
                            type = it.get("type").toString().toInt(),
                            discution_title = it.get("discution_title").toString(),
                            up_votes = it.get("up_votes").toString().toInt(),
                            down_votes = it.get("down_votes").toString().toInt(),
                            body_question = it.get("body_question").toString(),
                            userName = it.get("userName").toString()
                        )
                    )
                }

                ObserverListQuestion.postValue(listDiscussion)

            }
        }
    }
}

