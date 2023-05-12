package com.example.trycleancodewithrepository.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.trycleancodewithrepository.ResponseState
import com.example.trycleancodewithrepository.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val homeMvvm : HomeViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        stateFlowExample()
        sharedFlowExample()
        liveData()

    }

    private fun liveData() {
        homeMvvm.getMealsLiveData.observe(this){
            when(it)
            {
                is ResponseState.Loading -> {
                    Log.d("testApp" , "live is loading")
                }
                is ResponseState.Success -> {
                    Log.d("testApp" , "live is success")
                }
                is ResponseState.EmptyData -> {

                }
                is ResponseState.Error -> {

                }
                else -> Unit
            }
        }
    }

    private fun sharedFlowExample() {
        lifecycleScope.launchWhenStarted {
            homeMvvm.fetchMealsResponseStateSharedFLow.collect{
                when(it)
                {
                    is ResponseState.Loading -> {
                        Log.d("testApp" , "loading with shared flow")
                    }
                    is ResponseState.Success -> {
                        Log.d("testApp" , "success with shared flow")
                        Log.d("testApp" , it.data?.meals.toString())

                    } else -> Unit
                }
            }
        }
    }

    private fun stateFlowExample() {
        lifecycleScope.launchWhenStarted {
            homeMvvm.fetchMealsResponseState.collect{
                when(it)
                {
                    is ResponseState.Loading -> {
                        binding.progressCircular.visibility = View.VISIBLE
                        binding.tvMeal.visibility = View.GONE
                        Log.d("testApp" , "loading with state flow")
                    }
                    is ResponseState.Success -> {
                        Log.d("testApp" , "success with state flow")

                        binding.progressCircular.visibility = View.GONE
                        binding.tvMeal.visibility = View.VISIBLE
//                        Log.d("testApp" , it.data?.meals.toString())
                        val meal_Name = it.data?.meals?.first()?.strMeal
                        binding.tvMeal.text = meal_Name
                    }
                    is ResponseState.Unauthorized -> {
                        Toast.makeText(applicationContext, "Unauthorized", Toast.LENGTH_SHORT).show()
                    }

                    else -> Unit
                }
            }
        }
    }
}