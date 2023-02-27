package br.com.manieri.ipe_branco.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import br.com.manieri.ipe_branco.R
import br.com.manieri.ipe_branco.model.structure.Discussion

class QuestionsAdapter(var discussionMainVisualizer: ArrayList<Discussion>, val selectCard : MutableLiveData<Discussion>) :
    RecyclerView.Adapter<QuestionsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val output =
            LayoutInflater.from(parent.context).inflate(R.layout.card_questions, parent, false)
        return ViewHolder(output)
    }



    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.questionsName.text = discussionMainVisualizer[position].discution_title
        holder.textResume.text = discussionMainVisualizer[position].body_question

        if (discussionMainVisualizer[position].up_votes > discussionMainVisualizer[position].down_votes) {
            holder.upVote.setImageResource(R.drawable.positive_vote)
        } else if (discussionMainVisualizer[position].down_votes > discussionMainVisualizer[position].up_votes) {
            holder.downVote.setImageResource(R.drawable.negative_vote)
        }

        holder.count_responses.text = discussionMainVisualizer[position].responses.toString()

        holder.qttUpVote.text = discussionMainVisualizer[position].up_votes.toString()
        holder.qttDownVote.text = discussionMainVisualizer[position].down_votes.toString()

        holder.card.setOnClickListener {
            selectCard.postValue(discussionMainVisualizer[position])
        }
    }


    fun update(discussions: ArrayList<Discussion>){
        this.discussionMainVisualizer = discussions
        this.notifyDataSetChanged()
    }

    override fun getItemCount(): Int = discussionMainVisualizer.size


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var questionsName: TextView = itemView.findViewById(R.id.titleQuestion)
        var textResume: TextView = itemView.findViewById(R.id.TextResume)

        var upVote: ImageView = itemView.findViewById(R.id.upVote)
        var downVote: ImageView = itemView.findViewById(R.id.downVote)

        var qttUpVote: TextView = itemView.findViewById(R.id.qttUpVote)
        var qttDownVote: TextView = itemView.findViewById(R.id.qttDown)

        var count_responses : TextView = itemView.findViewById(R.id.count_responses)

        var card: CardView = itemView.findViewById(R.id.cardquestions)

    }
}