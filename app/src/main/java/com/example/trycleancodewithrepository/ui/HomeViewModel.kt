package com.example.trycleancodewithrepository.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.trycleancodewithrepository.ResponseState
import com.example.trycleancodewithrepository.data.repository.HomeRepository
import com.example.trycleancodewithrepository.models.MealResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val homeRepository: HomeRepository
) : ViewModel() {


    private val _fetchMealsResponseStateSharedFLow = MutableSharedFlow<ResponseState<MealResponse>>()
    val fetchMealsResponseStateSharedFLow  = _fetchMealsResponseStateSharedFLow.asSharedFlow()

    private val _fetchMealsResponseState = MutableStateFlow<ResponseState<MealResponse>>(ResponseState.Idle())
    val fetchMealsResponseState  = _fetchMealsResponseState.asStateFlow()

    init {
        fetchMeals()
    }
   private fun fetchMeals() = viewModelScope.launch(Dispatchers.IO)
    {
        homeRepository.getMeals().collect{
            _fetchMealsResponseState.emit(it)
            _fetchMealsResponseStateSharedFLow.emit(it)
//            _fetchMealsResponseState.value = it
        }
    }

    private val _getMealsLiveData = MutableLiveData<ResponseState<MealResponse>>()
    val getMealsLiveData  :LiveData<ResponseState<MealResponse>> = _getMealsLiveData

    init {
        getMealsLiveData()
    }
    private fun getMealsLiveData() = viewModelScope.launch(Dispatchers.IO)
    {
        val response = homeRepository.getMealsWithLiveData().value
        _getMealsLiveData.postValue(response)
    }



}