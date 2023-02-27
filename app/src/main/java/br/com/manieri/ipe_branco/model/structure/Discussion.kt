package br.com.manieri.ipe_branco.model.structure

data class Discussion(

    var discussion_uid: String?,
    var user_uid: String,
    var unique_uid: String? = "Question",

    var type: Int,
    var discution_title: String,
    var responses : Int,

    var up_votes: Int,
    var down_votes: Int,
    var body_question: String,

    var userName: String,

    )
