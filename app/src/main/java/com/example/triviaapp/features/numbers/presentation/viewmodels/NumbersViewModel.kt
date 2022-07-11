package com.example.triviaapp.features.numbers.presentation.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.triviaapp.core.NumberTriviaType
import com.example.triviaapp.core.NumberTriviaType.*
import com.example.triviaapp.core.NumberTriviaType.Number
import com.example.triviaapp.features.numbers.domain.NumbersRepository
import com.example.triviaapp.features.numbers.domain.entities.NumberTriviaEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.zip
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NumbersViewModel @Inject constructor(private val numbersRepository: NumbersRepository) :
    ViewModel() {

    private val _currentNumberTriviaMLD = MutableLiveData<NumberTriviaEntity>()
    val currentNumberTriviaLD: LiveData<NumberTriviaEntity> = _currentNumberTriviaMLD

    private val _currentNumberMLD = MutableLiveData<Int>()
    val currentNumberLD: LiveData<Int> = _currentNumberMLD

    private val _currentDateMLD = MutableLiveData<Pair<Int, Int>>()
    val currentDateLD: LiveData<Pair<Int, Int>> = _currentDateMLD
    
    private val _currentTriviaType = MutableLiveData<NumberTriviaType>()
    val currentTriviaType: LiveData<NumberTriviaType> = _currentTriviaType

    private val _isGetTriviaBtnEnabled = MutableLiveData<Boolean>(false)
    val isGetTriviaBtnEnabled: LiveData<Boolean> = _isGetTriviaBtnEnabled

    private fun getNumberTrivia(number: Int){
        viewModelScope.launch {
            numbersRepository.getNumberTrivia(number).collect{ _currentNumberTriviaMLD.value = it }
        }
    }

    private fun getDateTrivia(day: Int, month: Int){
        viewModelScope.launch {
            numbersRepository.getDateTrivia(day, month).collect { _currentNumberTriviaMLD.value = it }
        }
    }

    private fun getMathTrivia(number: Int){
        viewModelScope.launch {
            numbersRepository.getMathNumberTrivia(number).collect { _currentNumberTriviaMLD.value = it}
        }
    }

    private fun getRandomNumberTrivia(){
        viewModelScope.launch {
            numbersRepository.getRandomNumberTrivia().collect{ _currentNumberTriviaMLD.value = it }
        }
    }

    fun setCurrentNumberLD(number: Int){
        _currentNumberMLD.value = number
    }

    fun setCurrentDateLD(day: StateFlow<Int>, month: StateFlow<Int>){
        viewModelScope.launch{ day.zip(month){ day, month -> Pair(day, month) }
                .collect{
                    _currentDateMLD.value = it
                    _isGetTriviaBtnEnabled.value = true
                }
        }
    }

    fun setCurrentTriviaType(type: NumberTriviaType) {
        _currentTriviaType.value = type
    }

    fun getTrivia(){
        Log.d("simi", "in get trivia")
        when(currentTriviaType.value){
            Number -> {
                Log.d("simi", "in get trivia outside number viewmodel ")
                currentNumberLD.value?.let {
                Log.d("simi", "in get trivia number $it viewmodel ")
                getNumberTrivia(it)
            } }
            Random -> getRandomNumberTrivia()
            Math -> { currentNumberLD.value?.let { getMathTrivia(it) } }
            Date -> { currentDateLD.value?.let { getDateTrivia(it.first, it.second) } }
            null -> {}
        }
    }

    fun setGetTriviaBtnEnabled(isEnabled: Boolean){
        _isGetTriviaBtnEnabled.value = isEnabled
    }
}