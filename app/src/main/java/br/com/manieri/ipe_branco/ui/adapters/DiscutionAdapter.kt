package br.com.manieri.ipe_branco.ui.adapters

import android.content.ContentValues.TAG
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import br.com.manieri.ipe_branco.R
import br.com.manieri.ipe_branco.model.structure.Discussion
import br.com.manieri.ipe_branco.ui.main.Discussion.DiscussionViewModel
import br.com.manieri.ipe_branco.util.Constants.Companion.DOWN_VOTE_SETED
import br.com.manieri.ipe_branco.util.Constants.Companion.LABEL_ITEM_DISCUSSION
import br.com.manieri.ipe_branco.util.Constants.Companion.LABEL_ITEM_POSITION
import br.com.manieri.ipe_branco.util.Constants.Companion.RESPOSNSES_ITEM_DISCUSSION
import br.com.manieri.ipe_branco.util.Constants.Companion.TOP_ITEM_DISCUSSION
import br.com.manieri.ipe_branco.util.Constants.Companion.TOP_ITEM_POSITION
import br.com.manieri.ipe_branco.util.Constants.Companion.UP_VOTE_SETED

class DiscutionAdapter(val discutionList: ArrayList<Discussion>, val viewModel: DiscussionViewModel) :
    RecyclerView.Adapter<DiscutionAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        discutionList.forEach {
            Log.w("Depuração resposta", "onCreateViewHolder: ${it.body_question}")
        }

        lateinit var output: View

        if (viewType == TOP_ITEM_DISCUSSION) {
            output = LayoutInflater.from(parent.context)
                .inflate(R.layout.card_main_question, parent, false)
        } else if (viewType == LABEL_ITEM_DISCUSSION) {
            output = LayoutInflater.from(parent.context)
                .inflate(R.layout.card_label_response, parent, false)
        } else{
            output = LayoutInflater.from(parent.context)
                .inflate(R.layout.card_responses, parent, false)

        }
        return ViewHolder(output, viewType)
    }


    fun update() {
        this.notifyDataSetChanged()
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        if (getItemViewType(position) == TOP_ITEM_DISCUSSION) {
            holder.discussionTitle.text = discutionList[position].discution_title
            holder.bodyContenttext.text = discutionList[position].body_question
            holder.userNameSignature.text = discutionList[position].userName

            val vote = discutionList[position].user_uid?.let { discutionList[position].unique_uid?.let { it1 ->
                viewModel.getVote(it,
                    it1
                )
            } }
            Log.w(TAG, "onBindViewHolder: $vote", )

            if (vote == UP_VOTE_SETED){
                holder.upVote.setImageResource(R.drawable.positive_vote)
            }

            if (vote == DOWN_VOTE_SETED){
                holder.downVote.setImageResource(R.drawable.negative_vote)
            }

            holder.upVote.setOnClickListener {
                discutionList[position].unique_uid?.let { it1 ->
                    viewModel.setVote(discutionList[position].user_uid,
                        it1,
                        UP_VOTE_SETED)
                }
                holder.downVote.setImageResource(R.drawable.neutral_negative)
                holder.upVote.setImageResource(R.drawable.positive_vote)

            }

            holder.downVote.setOnClickListener {
                discutionList[position].unique_uid?.let { it1 ->
                    viewModel.setVote(discutionList[position].user_uid,
                        it1,
                        DOWN_VOTE_SETED)
                }
                holder.upVote.setImageResource(R.drawable.neutral_positive)
                holder.downVote.setImageResource(R.drawable.negative_vote)
            }
        } else if (getItemViewType(position) == LABEL_ITEM_DISCUSSION) {
        } else {
            holder.bodyResponse.text = discutionList[position].body_question
            holder.name_user_question.text = discutionList[position].userName

            holder.upVote.setOnClickListener {
                discutionList[position].unique_uid?.let { it1 ->
                    viewModel.setVote(discutionList[position].user_uid,
                        it1,
                        UP_VOTE_SETED)
                }
                holder.downVote.setImageResource(R.drawable.neutral_negative)
                holder.upVote.setImageResource(R.drawable.positive_vote)
            }

            holder.downVote.setOnClickListener {
                discutionList[position].unique_uid?.let { it1 ->
                    viewModel.setVote(discutionList[position].user_uid,
                        it1,
                        DOWN_VOTE_SETED)
                }
                holder.upVote.setImageResource(R.drawable.neutral_positive)
                holder.downVote.setImageResource(R.drawable.negative_vote)
            }
        }


    }

    override fun getItemViewType(position: Int): Int {
        if (position == TOP_ITEM_POSITION) {
            return TOP_ITEM_DISCUSSION
        } else if (position == LABEL_ITEM_POSITION) {
            return LABEL_ITEM_DISCUSSION
        } else {
            return RESPOSNSES_ITEM_DISCUSSION
        }
    }

    override fun getItemCount(): Int = discutionList.size


    class ViewHolder(itemView: View, val viewType: Int) : RecyclerView.ViewHolder(itemView) {

        lateinit var discussionTitle: TextView
        lateinit var bodyContenttext: TextView
        lateinit var userNameSignature: TextView

        lateinit var bodyResponse: TextView
        lateinit var name_user_question: TextView

        lateinit var upVote: ImageView
        lateinit var downVote: ImageView


        init {
            if (viewType == TOP_ITEM_DISCUSSION) {
                discussionTitle = itemView.findViewById(R.id.discussionTitle)
                bodyContenttext = itemView.findViewById(R.id.bodyContenttext)
                userNameSignature = itemView.findViewById(R.id.userNameSignature)

                upVote = itemView.findViewById(R.id.upVote)
                downVote = itemView.findViewById(R.id.downVote)

            } else if (viewType == LABEL_ITEM_DISCUSSION) {
            } else {
                bodyResponse = itemView.findViewById(R.id.bodyResponse)
                name_user_question = itemView.findViewById(R.id.name_user_question)

                upVote = itemView.findViewById(R.id.upVote)
                downVote = itemView.findViewById(R.id.downVote)
            }
        }

    }
}