package com.example.testapplication.data

import com.example.testapplication.remote.PRApi
import com.example.testapplication.ui.PRItemType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.lang.Exception
import javax.inject.Inject
import javax.inject.Named

class RepoRepository @Inject constructor(private val mPRRemoteDataSource: PRApi, @Named("user") private val mUser: String, @Named("repo") private val mRepo: String) {
    private var mMasterList = mutableListOf<PRItemType>()

    suspend fun getClosedPR(cursor: Int): List<PRItemType>? {
        val list = try {
            mPRRemoteDataSource.getClosedPR(mUser, mRepo, cursor)
        } catch (e: Exception) {
            null
        }
        return if (list != null) {
            mMasterList.apply {
//                for (i in 0..10) {
//                    add(PRItemType.PRItem(list!![0]))
//                }
                addAll(list.map { PRItemType.PRItem(it) })
            }
        } else {
            null
        }
    }

    suspend fun getClosedPR__(cursor: Int, isForce: Boolean) = flow {
        emit(PagedData(false, mMasterList.toMutableList().apply {
            if (isForce) {
                removeAt(mMasterList.lastIndex)
            }
            add(PRItemType.Loader)
        }.also {
            mMasterList = it
        }, mMasterList.lastIndex, if (isForce) OperationType.CHANGED else OperationType.ADDED))
        delay(3000)
        val list = try {
            mPRRemoteDataSource.getClosedPR(mUser, mRepo, cursor)
        } catch (e: Exception) {
            null
        }

        if (list == null) {
            val lastIndex = mMasterList.lastIndex
            val newList = mMasterList.toMutableList().apply {
                removeAt(mMasterList.lastIndex)
                add(PRItemType.Error)
            }.also {
                mMasterList = it
            }
            emit(PagedData(null, newList, lastIndex, OperationType.CHANGED))
            return@flow
        }

        val newSize = list.size
        val lastIndex = mMasterList.lastIndex
        val newList = mMasterList.toMutableList().apply {
            removeAt(mMasterList.lastIndex)
//            for (i in 0 .. 10) {
//                if (list.isNotEmpty()) {
//                    add(PRItemType.PRItem(list!![0]))
//                }
//            }
            addAll(list.map { PRItemType.PRItem(it) })
        }.also {
            mMasterList = it
        }

        if (newSize <= 0) {
            emit(PagedData(true, newList, lastIndex, OperationType.REMOVED))
        } else {
            emit(PagedData(false, newList, lastIndex, OperationType.ADDED))
        }
    }.flowOn(Dispatchers.Default)
}
