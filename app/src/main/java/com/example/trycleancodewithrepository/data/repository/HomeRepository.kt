package com.example.trycleancodewithrepository.data.repository

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.trycleancodewithrepository.ResponseState
import com.example.trycleancodewithrepository.data.network.ApiServices
import com.example.trycleancodewithrepository.models.MealResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject


class HomeRepository @Inject constructor(private val apiServices: ApiServices) {

    suspend fun getMeals(): Flow<ResponseState<MealResponse>> {
        return flow {
                        emit(ResponseState.Loading())
            try {
                val response = apiServices.getMealApi()
                if (response.isSuccessful) {
                    Log.d("testApp", "success to call api")
                    response.body()?.let {
                         emit(ResponseState.Success(it))
                    } ?: emit(ResponseState.NullData())
                    if (response.body()?.meals?.isEmpty() == true)
                        emit(ResponseState.EmptyData())
                } else {
                    Log.d("testApp", "failed to call api response code is ${response.code()}")
                        emit(ResponseState.Error(response.message().toString()))
                        when(response.code())
                        {
                            401 -> emit(ResponseState.Unauthorized())
                        }
                }
            } catch (e: Exception) {
                Log.d("testApp", e.message.toString())
                        emit(ResponseState.Error(e.message.toString()))
            }
        }
    }


    @SuppressLint("SuspiciousIndentation")
    suspend fun getMealsWithLiveData(): LiveData<ResponseState<MealResponse>> {
        val liveData = MutableLiveData<ResponseState<MealResponse>>()
            liveData.postValue(ResponseState.Loading())
        try {
            val response = apiServices.getMealApi()
            if (response.isSuccessful) {
                response.body()?.let {
                     liveData.postValue(ResponseState.Success(it))
                } ?: liveData.postValue(ResponseState.NullData())
                if (response.body()?.meals?.isEmpty() == true) {
                     liveData.postValue(ResponseState.EmptyData())
                }
            } else {
                     liveData.postValue(ResponseState.Error(message = response.message().toString()))
            }
        } catch (e: Exception) {
            Log.d("testApp", e.message.toString())
                    liveData.postValue(ResponseState.Error(message = e.message.toString()))
        }
        return liveData
    }


}