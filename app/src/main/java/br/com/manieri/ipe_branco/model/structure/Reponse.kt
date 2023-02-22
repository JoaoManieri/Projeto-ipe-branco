package br.com.manieri.ipe_branco.model.structure

data class Reponse(

    val uid : Long,
    val questionUid : Long,
    var upVotes: Int,
    var downVotes: Int,
    var questionsBody: String

)
