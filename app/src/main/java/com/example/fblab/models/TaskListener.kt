package com.example.fblab.models

interface TaskListener {
    fun onTaskDelete(itemObjectId: String)
    fun toggleTaskState(itemObjectId: String, status: Boolean)
}