package com.example.testapplication.ui

sealed class ViewState {
    object Loading : ViewState()
    class DataLoaded(val prList: List<PRItemType>) : ViewState()
    object Failed : ViewState()
}
