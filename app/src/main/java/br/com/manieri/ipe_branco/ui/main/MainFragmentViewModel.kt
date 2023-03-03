package br.com.manieri.ipe_branco.ui.main

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.manieri.ipe_branco.dataBase.AppDataBase
import br.com.manieri.ipe_branco.model.structure.Discussion
import br.com.manieri.ipe_branco.util.Constants.Companion.NO_VOTE
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await

class MainFragmentViewModel : ViewModel() {

    lateinit var firestore: FirebaseFirestore
    var ObserverListQuestion = MutableLiveData<ArrayList<Discussion>>()

    var fireStore = FirebaseFirestore.getInstance()

    fun getVote(
        user_unique_uid: String,
        discussion_unique_uid: String
    ): Int {
        val req = runBlocking {
            fireStore.collection("VotesController").document("$user_unique_uid+$discussion_unique_uid").get().addOnSuccessListener{}.await()
        }
        val vote = req.data?.get("vote_type")
        return if(vote != null) vote.toString().toInt()
        else NO_VOTE
    }


    fun getQuestionList() {

        GlobalScope.launch(Dispatchers.IO) {
            var listDiscussion = arrayListOf<Discussion>()

            firestore = FirebaseFirestore.getInstance()
            // Create a reference to the cities collection
            val doc = firestore.collection("Discussion")

            // Create a query against the collection.
            doc.whereEqualTo("type", 0).get().addOnSuccessListener { documents ->
                documents.forEach {
                    listDiscussion.add(
                        Discussion(
                            discussion_uid = it.get("discussion_uid").toString(),
                            user_uid = it.get("user_uid").toString(),
                            unique_uid = it.get("unique_uid").toString(),
                            type = it.get("type").toString().toInt(),
                            discution_title = it.get("discution_title").toString(),
                            up_votes = it.get("up_votes").toString().toInt(),
                            responses = it.data["reponses"].toString().toInt(),
                            down_votes = it.get("down_votes").toString().toInt(),
                            body_question = it.get("body_question").toString(),
                            userName = it.get("userName").toString(),
                            userVote = getVote(AppDataBase.getInstance().userDao().getToken(), it.get("unique_uid").toString())
                        )
                    )
                    Log.w(TAG, "getQuestionList: ${listDiscussion.get(0).userVote}")
                }

                ObserverListQuestion.postValue(listDiscussion)

            }
        }
    }
}

