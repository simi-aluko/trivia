package com.example.triviaapp.features.numbers.presentation.viewmodels

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.*
import com.example.triviaapp.core.NumberTriviaType
import com.example.triviaapp.core.NumberTriviaType.*
import com.example.triviaapp.core.NumberTriviaType.Number
import com.example.triviaapp.core.strings.currentNumberTrivia
import com.example.triviaapp.features.numbers.domain.NumbersRepository
import com.example.triviaapp.features.numbers.domain.entities.NumberTriviaEntity
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.zip
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NumbersViewModel @Inject constructor(private val numbersRepository: NumbersRepository,
                                           private val sharedPreferences: SharedPreferences) : ViewModel() {

    private val _currentNumberTriviaMLD = MutableLiveData<NumberTriviaEntity>()
    val currentNumberTriviaLD: LiveData<NumberTriviaEntity> = _currentNumberTriviaMLD

    private val _currentNumberMLD = MutableLiveData<Int>()
    val currentNumberLD: LiveData<Int> = _currentNumberMLD

    private val _currentDateMLD = MutableLiveData<Pair<Int, Int>>()
    val currentDateLD: LiveData<Pair<Int, Int>> = _currentDateMLD
    
    private val _currentTriviaType = MutableLiveData(Number)
    val currentTriviaType: LiveData<NumberTriviaType> = _currentTriviaType

    private val _isGetTriviaBtnEnabled = MutableLiveData<Boolean>(false)
    val isGetTriviaBtnEnabled: LiveData<Boolean> = _isGetTriviaBtnEnabled

    private val _isFavourite = MutableLiveData(false)
    val isFavourite: LiveData<Boolean> = _isFavourite

    private fun getNumberTrivia(number: Int){
        viewModelScope.launch {
            numbersRepository.getNumberTrivia(number).collect{ it?.let { saveCurrentNumberTrivia(it) } }
        }
    }

    private fun getDateTrivia(day: Int, month: Int){
        viewModelScope.launch {
            numbersRepository.getDateTrivia(day, month).collect { it?.let { saveCurrentNumberTrivia(it) } }
        }
    }

    private fun getMathTrivia(number: Int){
        viewModelScope.launch {
            numbersRepository.getMathNumberTrivia(number).collect { it?.let {
                saveCurrentNumberTrivia(it) } }
        }
    }

    private fun getRandomNumberTrivia(){
        viewModelScope.launch {
            numbersRepository.getRandomNumberTrivia().collect{ it?.let { saveCurrentNumberTrivia(it) } }
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
        when(currentTriviaType.value){
            Number -> { currentNumberLD.value?.let { getNumberTrivia(it) } }
            Random -> getRandomNumberTrivia()
            Math -> { currentNumberLD.value?.let { getMathTrivia(it) } }
            Date -> { currentDateLD.value?.let { getDateTrivia(it.first, it.second) } }
            null -> {}
        }
    }

    fun setGetTriviaBtnEnabled(isEnabled: Boolean){
        _isGetTriviaBtnEnabled.value = isEnabled
    }

    fun updateCurrentNumberTrivia(triviaString: String){
        val numberTrivia = Gson().fromJson(triviaString, NumberTriviaEntity::class.java)
        numberTrivia?.let {
            _currentNumberTriviaMLD.value = it
        }
    }

    private fun saveCurrentNumberTrivia(trivia: NumberTriviaEntity){
        with(sharedPreferences.edit()){
            val triviaString = Gson().toJson(trivia)
            triviaString?.let { this.putString(currentNumberTrivia, it) }
            apply()
        }
    }

    fun saveTriviaToDB(){
        currentNumberTriviaLD.value?.let {
            viewModelScope.launch{
                numbersRepository.saveTriviaToDB(it)
            }
        }
    }

    fun deleteTriviaFromDB(){
        currentNumberTriviaLD.value?.let { deleteTriviaFromDB(it) }
    }

    fun deleteTriviaFromDB(numberTriviaEntity: NumberTriviaEntity){
        viewModelScope.launch {
            numbersRepository.deleteTriviaFromDB(numberTriviaEntity)
        }
    }

    fun getAllSavedTrivia(): LiveData<List<NumberTriviaEntity>> {
        return numbersRepository.getAllTrivia().asLiveData()
    }

    fun toggleFavourite() {
        _isFavourite.value = !(_isFavourite.value ?: false)
    }
}