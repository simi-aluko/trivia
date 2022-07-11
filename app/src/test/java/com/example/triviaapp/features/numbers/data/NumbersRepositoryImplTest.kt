package com.example.triviaapp.features.numbers.data

import com.example.triviaapp.core.NetworkCall
import com.example.triviaapp.core.Resource
import com.example.triviaapp.features.numbers.data.models.DateTriviaModel
import com.example.triviaapp.features.numbers.data.models.NumberTriviaModel
import com.example.triviaapp.features.numbers.domain.NumbersRepository
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Assert.*
import org.junit.Before

import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.any
import org.mockito.kotlin.verify
import retrofit2.Call

@RunWith(MockitoJUnitRunner::class)
class NumbersRepositoryImplTest {

    lateinit var numbersRepository: NumbersRepository
    @Mock
    lateinit var numbersNetworkCall: NetworkCall<NumberTriviaModel>
    @Mock
    lateinit var dateNetworkCall: NetworkCall<DateTriviaModel>
    @Mock
    lateinit var numbersNetworkServiceMock: NumbersNetworkService
    @Mock
    lateinit var callNumberMock: Call<NumberTriviaModel>
    @Mock
    lateinit var callDateMock: Call<DateTriviaModel>
    @Before
    fun setUp(){
        numbersRepository = NumbersRepositoryImpl(numbersNetworkServiceMock, numbersNetworkCall, dateNetworkCall)
    }

    @Test
    fun `getMathNumberTriva should return a flow result on success`() {
        //arrange
        val number = 1
        val mathNumberTrivia = NumberTriviaModel(true, number, "test", "test")
        val successResponse = Resource.success(mathNumberTrivia)
        val flowResult = MutableStateFlow(successResponse)

        Mockito.`when`(numbersNetworkServiceMock.getMathNumberTrivia(any())).thenReturn(callNumberMock)
        Mockito.`when`(numbersNetworkCall.makeNetworkCall(callNumberMock)).thenReturn(flowResult)
        //act
        val mathTrivia = numbersRepository.getMathNumberTrivia(number)
        //assert
        verify(numbersNetworkServiceMock).getMathNumberTrivia(number)
        verify(numbersNetworkCall).makeNetworkCall(callNumberMock)
        verify(numbersNetworkCall, Mockito.times(0)).cancelNetworkCall()
        assertEquals(flowResult, mathTrivia)
    }

    @Test
    fun getRandomNumberTrivia() {
        //arrange
        val number = 1
        val randomNumberTrivia = NumberTriviaModel(true, number, "test", "test")
        val successResponse = Resource.success(randomNumberTrivia)
        val flowResult = MutableStateFlow(successResponse)

        Mockito.`when`(numbersNetworkServiceMock.getRandomNumberTrivia()).thenReturn(callNumberMock)
        Mockito.`when`(numbersNetworkCall.makeNetworkCall(callNumberMock)).thenReturn(flowResult)
        //act
        val randomTrivia = numbersRepository.getRandomNumberTrivia()
        //assert
        verify(numbersNetworkServiceMock).getRandomNumberTrivia()
        verify(numbersNetworkCall).makeNetworkCall(callNumberMock)
        verify(numbersNetworkCall, Mockito.times(0)).cancelNetworkCall()
        assertEquals(flowResult, randomTrivia)
    }

    @Test
    fun getNumberTrivia() {
        //arrange
        val number = 1
        val numberTriviaModel = NumberTriviaModel(true, number, "test", "test")
        val successResponse = Resource.success(numberTriviaModel)
        val flowResult = MutableStateFlow(successResponse)

        Mockito.`when`(numbersNetworkServiceMock.getNumberTrivia(number)).thenReturn(callNumberMock)
        Mockito.`when`(numbersNetworkCall.makeNetworkCall(callNumberMock)).thenReturn(flowResult)
        //act
        val numberTrivia = numbersRepository.getNumberTrivia(number)
        //assert
        verify(numbersNetworkServiceMock).getNumberTrivia(number)
        verify(numbersNetworkCall).makeNetworkCall(callNumberMock)
        verify(numbersNetworkCall, Mockito.times(0)).cancelNetworkCall()
        assertEquals(flowResult, numberTrivia)
    }

    @Test
    fun getDateTrivia() {
        //arrange
        val number = 1
        val dateTriviaModel = DateTriviaModel(true, number, "test", "test", 2000)
        val successResponse = Resource.success(dateTriviaModel)
        val flowResult = MutableStateFlow(successResponse)

        Mockito.`when`(numbersNetworkServiceMock.getDateTrivia(1,1)).thenReturn(callDateMock)
        Mockito.`when`(dateNetworkCall.makeNetworkCall(callDateMock)).thenReturn(flowResult)
        //act
        val dateTrivia = numbersRepository.getDateTrivia(1,1)
        //assert
        verify(numbersNetworkServiceMock).getDateTrivia(1,1)
        verify(dateNetworkCall).makeNetworkCall(callDateMock)
        verify(dateNetworkCall, Mockito.times(0)).cancelNetworkCall()
        assertEquals(flowResult, dateTrivia)
    }
}