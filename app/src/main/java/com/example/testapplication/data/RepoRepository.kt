package com.example.testapplication.data

import com.example.testapplication.remote.PRApi
import com.example.testapplication.ui.PRItemType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Named

class RepoRepository @Inject constructor(private val mPRRemoteDataSource: PRApi, @Named("user") private val mUser: String, @Named("repo") private val mRepo: String) {
    private var mMasterList = mutableListOf<PRItemType>()

    suspend fun getClosedPR(cursor: Int): List<PRItemType>? {
        // this delay is just for emulating network delay
        delay(1000)
        val list = try {
            mPRRemoteDataSource.getClosedPR(mUser, mRepo, cursor)
        } catch (e: Exception) {
            null
        }
        return withContext(Dispatchers.Default) {
            if (list != null) {
                mMasterList.apply {
                    addAll(list.map { PRItemType.PRItem(it) })
                }
            } else {
                null
            }
        }
    }

    suspend fun getPaginatedClosedPR(cursor: Int, isForce: Boolean) = flow {
        emit(PagedData(false, mMasterList.apply {
            if (isForce) {
                removeAt(mMasterList.lastIndex)
            }
            add(PRItemType.Loader)
        }, mMasterList.lastIndex, if (isForce) OperationType.CHANGED else OperationType.ADDED))

        // this delay is just for emulating network delay
        delay(1000)
        val list = try {
            mPRRemoteDataSource.getClosedPR(mUser, mRepo, cursor)
        } catch (e: Exception) {
            null
        }

        if (list == null) {
            val lastIndex = mMasterList.lastIndex
            val newList = mMasterList.apply {
                removeAt(mMasterList.lastIndex)
                add(PRItemType.Error)
            }
            emit(PagedData(null, newList, lastIndex, OperationType.CHANGED))
            return@flow
        }

        val newSize = list.size
        val lastIndex = mMasterList.lastIndex
        val newList = mMasterList.apply {
            removeAt(mMasterList.lastIndex)
            if (newSize > 0) {
                addAll(list.map { PRItemType.PRItem(it) })
            }
        }

        when {
            newSize <= 0 -> {
                emit(PagedData(true, newList, lastIndex, OperationType.REMOVED))
            }
            newSize < PRApi.PAGE_SIZE -> {
                emit(PagedData(true, newList, lastIndex, OperationType.ADDED))
            }
            else -> {
                emit(PagedData(false, newList, lastIndex, OperationType.ADDED))
            }
        }
    }.flowOn(Dispatchers.Default)
}
