package br.com.manieri.ipe_branco.ui.main.Discussion

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.manieri.ipe_branco.dataBase.AppDataBase
import br.com.manieri.ipe_branco.model.structure.Discussion
import br.com.manieri.ipe_branco.util.Constants.Companion.QUESTION
import br.com.manieri.ipe_branco.util.Constants.Companion.RESPONSE
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class DiscussionViewModel : ViewModel() {

    var fireStore = FirebaseFirestore.getInstance()

    var getDataDiscussion = MutableLiveData<ArrayList<Discussion>>()

    companion object {
        lateinit var discussion: Discussion
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun createDiscussion() {

        GlobalScope.launch(Dispatchers.IO) {

            val out: ArrayList<Discussion> = arrayListOf()

            // Create a reference to the cities collection
            val doc = fireStore.collection("Discussion")

            val data = doc.whereEqualTo("discussion_uid", discussion.discussion_uid.toString())
                .orderBy("type").limit(40)

            data.get().addOnSuccessListener { it ->

                var i = 0

                it.forEach {

                    if (i == 1) {
                        out.add(Discussion("LabelResponse", "", "", 0, "", 0, 0, 0,"0", ""))
                    }

                    Log.w("Depuração resposta", "createDiscussion: ${it.data}")

                    val upVote = it.data["up_votes"].toString()
                    val downVotes = it.data["down_votes"].toString()

                    out.add(
                        Discussion(
                            discussion_uid = it.data["discussion_uid"]?.toString(),
                            user_uid = it.data["user_uid"].toString(),
                            unique_uid = it.data["discussion_uid"]?.toString(),
                            type = QUESTION,
                            discution_title = it.data["discution_title"].toString(),
                            responses = 0, //it.data["reponses"].toString().toInt(),
                            up_votes = upVote.toInt(),
                            down_votes = downVotes.toInt(),
                            body_question = it.data["body_question"].toString(),
                            userName = it.data["userName"].toString()
                        )
                    )
                    i++
                }

                getDataDiscussion.postValue(out)
            }

        }

    }


    @OptIn(DelicateCoroutinesApi::class)
    fun newDiscussion(text: String, title: String, type: Int = 0): Boolean {

        fireStore = FirebaseFirestore.getInstance()

        val id = fireStore.collection("Discussion").document().getId()
        var discussion_id = id

        if (type == RESPONSE) {
            discussion_id = discussion.discussion_uid.toString()

            fireStore.collection("Discussion").document(discussion_id).update(
                mapOf("reponses" to FieldValue.increment(1))
            )

        }

        val out = hashMapOf(
            "discussion_uid" to discussion_id,
            "user_uid" to AppDataBase.getInstance().userDao().getToken(),
            "unique_uid" to id,
            "type" to type,
            "discution_title" to title,
            "up_votes" to 0,
            "down_votes" to 0,
            "reponses" to 0,
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