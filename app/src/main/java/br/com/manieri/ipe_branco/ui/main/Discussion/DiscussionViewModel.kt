package br.com.manieri.ipe_branco.ui.main.Discussion

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.manieri.ipe_branco.dataBase.AppDataBase
import br.com.manieri.ipe_branco.model.structure.Discussion
import br.com.manieri.ipe_branco.model.structure.Question
import br.com.manieri.ipe_branco.util.Constants.Companion.QUESTION
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class DiscussionViewModel : ViewModel() {

    var fireStore = FirebaseFirestore.getInstance()

    var getDataDiscussion = MutableLiveData<ArrayList<Discussion>>()

    companion object{
        lateinit var discussion : Discussion
    }

    fun createDiscussion() {

        Log.w(TAG, "createDiscussion ZZZZZZZZZZZZZZZZZZZZZZZZ: ${discussion.unique_uid.toString()}", )

        GlobalScope.launch(Dispatchers.IO) {

            val doc = fireStore.collection("Discussion").document(discussion.unique_uid.toString())
            doc.get().addOnSuccessListener {

                Log.w(TAG, "createDiscussion: ${it.data}")

                val upVote = it.data?.get("up_votes").toString()
                val downVotes = it.data?.get("down_votes").toString()

                Log.w(TAG, "createDiscussion: ${upVote} ......... ${downVotes}", )

                val out = arrayListOf(
                    Discussion(
                        discussion_uid = it.data?.get("discussion_uid")?.toString(),
                        user_uid = it.data?.get("user_uid").toString(),
                        unique_uid = it.data?.get("discussion_uid")?.toString(),
                        type = QUESTION,
                        discution_title = it.data?.get("discution_title").toString(),
                        up_votes = upVote.toInt(),
                        down_votes = downVotes.toInt(),
                        body_question = it.data?.get("body_question").toString(),
                        userName = it.data?.get("userName").toString()
                    )
                )
                getDataDiscussion.postValue(out)
            }

        }

    }


    fun newDiscussion(text: String, title: String) : Boolean {

        fireStore = FirebaseFirestore.getInstance()

        val id = fireStore.collection("Discussion").document().getId()

        val out = hashMapOf(
            "discussion_uid" to id,
            "user_uid" to AppDataBase.getInstance().userDao().getToken(),
            "unique_uid" to id,
            "type" to QUESTION,
            "discution_title" to title,
            "up_votes" to 0,
            "down_votes" to 0,
            "body_question" to text,
            "userName" to AppDataBase.getInstance().userDao().getName()
        )

        GlobalScope.launch(Dispatchers.IO) {
            fireStore.collection("Discussion").document(id).set(out).addOnCompleteListener {

            }
        }

        return true
    }


}