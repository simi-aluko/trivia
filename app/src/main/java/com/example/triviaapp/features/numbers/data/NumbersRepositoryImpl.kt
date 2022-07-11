package com.example.triviaapp.features.numbers.data

import com.example.triviaapp.core.NetworkCall
import com.example.triviaapp.core.Resource
import com.example.triviaapp.core.strings.numberTriviaErrorMessage
import com.example.triviaapp.features.numbers.data.models.DateTriviaModel
import com.example.triviaapp.features.numbers.data.models.NumberTriviaModel
import com.example.triviaapp.features.numbers.domain.NumbersRepository
import com.example.triviaapp.features.numbers.domain.entities.NumberTriviaEntity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class NumbersRepositoryImpl @Inject constructor(private val numbersNetworkService: NumbersNetworkService,
                                                private val numberNetworkCall: NetworkCall<NumberTriviaModel>,
                                                private val dateNetworkCall: NetworkCall<DateTriviaModel>) : NumbersRepository{

    @OptIn(ExperimentalCoroutinesApi::class)
    override suspend fun getMathNumberTrivia(number: Int): Flow<NumberTriviaEntity?> {
       return numberNetworkCall.makeNetworkCall(numbersNetworkService.getMathNumberTrivia(number))
           .flatMapLatest { getNumberEntity(it)}
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override suspend fun getRandomNumberTrivia(): Flow<NumberTriviaEntity?> {
        return numberNetworkCall.makeNetworkCall(numbersNetworkService.getRandomNumberTrivia())
            .flatMapLatest { getNumberEntity(it)
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override suspend fun getNumberTrivia(number: Int): Flow<NumberTriviaEntity?> {
        return numberNetworkCall.makeNetworkCall(numbersNetworkService.getNumberTrivia(number))
            .flatMapLatest { getNumberEntity(it) }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override suspend fun getDateTrivia(month: Int, day: Int): Flow<NumberTriviaEntity?> {
        return dateNetworkCall.makeNetworkCall(numbersNetworkService.getDateTrivia(month, day))
            .flatMapLatest { getDateEntity(it) }
    }

    private fun getNumberEntity(resource: Resource<NumberTriviaModel>): Flow<NumberTriviaEntity?> {
        return flow{
            when(resource.status){
                Resource.Status.Success -> {
                    emit(NumberTriviaEntity(resource.data?.number ?: 0,
                        resource.data?.text ?: numberTriviaErrorMessage)
                    )
                }
                Resource.Status.Error -> {
                    emit(
                        NumberTriviaEntity(resource.data?.number ?: 0, numberTriviaErrorMessage)
                    )
                }
                Resource.Status.Loading -> {
                    emit(null)
                }
            }
        }
    }

    private fun getDateEntity(resource: Resource<DateTriviaModel>):
            Flow<NumberTriviaEntity?> {
        return flow{
            when(resource.status){
                Resource.Status.Success -> {
                    emit(NumberTriviaEntity(resource.data?.number ?: 0,
                        resource.data?.text ?: numberTriviaErrorMessage)
                    )
                }
                Resource.Status.Error -> {
                    emit(
                        NumberTriviaEntity(resource.data?.number ?: 0, numberTriviaErrorMessage)
                    )
                }
                Resource.Status.Loading -> {
                    emit(null)
                }
            }
        }
    }
}