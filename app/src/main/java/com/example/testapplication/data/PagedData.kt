package com.example.testapplication.data

import com.example.testapplication.ui.PRItemType

data class PagedData(val pageValidity: Boolean?, val dataList: List<PRItemType>, val index: Int, val operationType: OperationType)