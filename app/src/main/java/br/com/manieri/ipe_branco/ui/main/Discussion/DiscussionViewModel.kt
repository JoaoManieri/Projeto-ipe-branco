package br.com.manieri.ipe_branco.ui.main.Discussion

import android.content.ContentValues.TAG
import android.util.Log
import androidx.annotation.WorkerThread
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.manieri.ipe_branco.dataBase.AppDataBase
import br.com.manieri.ipe_branco.model.structure.Discussion
import br.com.manieri.ipe_branco.util.Constants.Companion.DOWN_VOTE_SETED
import br.com.manieri.ipe_branco.util.Constants.Companion.NO_VOTE
import br.com.manieri.ipe_branco.util.Constants.Companion.QUESTION
import br.com.manieri.ipe_branco.util.Constants.Companion.RESPONSE
import br.com.manieri.ipe_branco.util.Constants.Companion.UP_VOTE_SETED
import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await


//Assim - Teo?, Mirella Costa (song)
//Water - SunniColón (song)

class DiscussionViewModel : ViewModel() {


    var fireStore = FirebaseFirestore.getInstance()
    var getDataDiscussion = MutableLiveData<ArrayList<Discussion>>()


    companion object {
        lateinit var discussion: Discussion
    }

    fun setVote(
        discussion_unique_uid: String,
        vote_type: Int
    ) {

        val old_vote = getVote(AppDataBase.getInstance().userDao().getToken(), discussion_unique_uid)
        val user_unique_uid = AppDataBase.getInstance().userDao().getToken()

        if (old_vote == UP_VOTE_SETED && vote_type == DOWN_VOTE_SETED) {
            fireStore.collection("Discussion").document(discussion_unique_uid).update(
                mapOf(
                    "up_votes" to FieldValue.increment(-1),
                    "down_votes" to FieldValue.increment(1)
                )
            )
            fireStore.collection("VotesController")
                .document("$user_unique_uid+$discussion_unique_uid").update(
                    mapOf("vote_type" to DOWN_VOTE_SETED)
                )
        } else if (old_vote == DOWN_VOTE_SETED && vote_type == UP_VOTE_SETED) {
            fireStore.collection("Discussion").document(discussion_unique_uid).update(
                mapOf(
                    "down_votes" to FieldValue.increment(-1),
                    "up_votes" to FieldValue.increment(1)
                )
            )
            fireStore.collection("VotesController")
                .document("$user_unique_uid+$discussion_unique_uid").update(
                    mapOf("vote_type" to UP_VOTE_SETED)
                )
        } else if (old_vote == NO_VOTE) {
            val bd = fireStore.collection("VotesController")
            bd.document("$user_unique_uid+$discussion_unique_uid").set(
                mapOf(
                    user_unique_uid to discussion_unique_uid,
                    "vote_type" to vote_type
                )
            )
            if (vote_type == UP_VOTE_SETED){
                fireStore.collection("Discussion").document(discussion_unique_uid).update(
                    mapOf(
                        "up_votes" to FieldValue.increment(1)
                    )
                )
            }
            else if (vote_type == DOWN_VOTE_SETED){
                fireStore.collection("Discussion").document(discussion_unique_uid).update(
                    mapOf(
                        "down_votes" to FieldValue.increment(1)
                    )
                )
            }
        }
    }

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


    @OptIn(DelicateCoroutinesApi::class)
    fun createDiscussion() {
        GlobalScope.launch(Dispatchers.IO) {

            val out: ArrayList<Discussion> = arrayListOf()

            // Create a reference to the cities collection
            val discussionCollection = fireStore.collection("Discussion")

            val data = discussionCollection.whereEqualTo(
                "discussion_uid",
                discussion.discussion_uid.toString()
            )
                .orderBy("type").limit(40)

            data.get().addOnSuccessListener { it ->

                var i = 0

                it.forEach { it ->

                    if (i == 1) {
                        out.add(Discussion("LabelResponse", "", "", 0, "", 0, 0, 0, "0", "", 0))
                    }


                    val upVote = it.data["up_votes"].toString()
                    val downVotes = it.data["down_votes"].toString()

                    val userId = AppDataBase.getInstance().userDao().getToken()
                    val uniqueId = it.data["unique_uid"].toString()


                    val discussion = Discussion(
                        discussion_uid = it.data["discussion_uid"]?.toString(),
                        user_uid = it.data["user_uid"].toString(),
                        unique_uid = uniqueId,
                        type = QUESTION,
                        discution_title = it.data["discution_title"].toString(),
                        responses = 0, //it.data["reponses"].toString().toInt(),
                        up_votes = upVote.toInt(),
                        down_votes = downVotes.toInt(),
                        body_question = it.data["body_question"].toString(),
                        userName = it.data["userName"].toString(),
                        userVote = getVote(userId, uniqueId)
                    )

                    out.add(discussion)
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