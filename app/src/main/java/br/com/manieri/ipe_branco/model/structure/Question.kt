package br.com.manieri.ipe_branco.model.structure

data class Question(
    var uid: String,
    var questionsName : String,
    var upVotes: Int,
    var downVotes: Int,
    var questionsBody: String
)

{

}