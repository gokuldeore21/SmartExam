package com.example.smartexam

data class QuestionModel(
    val id : String,
    val title : String,
    val subtitle : String,
    val time : String,
    val questionList : List<QuestionModel1>
){
    constructor() : this("","","","", emptyList())
}

data class QuestionModel1(
    val question : String,
    val option : List<String>,
    val correct : String,
){
    constructor() : this("", emptyList(),"")
}