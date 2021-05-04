package com.example.testapplication.data

sealed class OperationType {
    object CHANGED : OperationType()
    object ADDED : OperationType()
    object REMOVED : OperationType()
}