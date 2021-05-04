package com.example.testapplication.ui

import com.example.testapplication.data.PRData

sealed class PRItemType {
    class PRItem(val prData: PRData) : PRItemType()
    object Loader : PRItemType()
    object Error : PRItemType()
}
