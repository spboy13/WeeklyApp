package com.example.weeklyapp.model

class TaskModelClass(
    val id: Int,
    val task_title: String,
    val difficulty: String,
    val recurring: Int,
    val priority: Int,
    val day_allotment: MutableMap<String, Int>
)