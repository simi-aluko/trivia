package com.example.triviaapp.core

import kotlinx.coroutines.flow.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class Resource<T> private constructor(val status: Status, val data: T?, val netwrokError: NetwrokError?) {
    enum class Status { Success, Error, Loading }

    companion object {
        fun <T> success(data: T?) = Resource(Status.Success, data, null)
        fun <T> error(netwrokError: NetwrokError?): Resource<T> = Resource(Status.Error, null, netwrokError)
        fun <T> loading(data: T?) = Resource(Status.Loading, data, null)
    }
}

data class NetwrokError(val error: Boolean, val message: String)

interface NetworkCall<T> {
    fun makeNetworkCall(call: Call<T>): StateFlow<Resource<T>>
    fun cancelNetworkCall()
}

class NetworkCallImpl<T> @Inject constructor(): NetworkCall<T> {
    private lateinit var call: Call<T>

    override fun makeNetworkCall(call: Call<T>): StateFlow<Resource<T>> {
        this.call = call
        val networkCallback = NetworkCallback<T>()
        this.call.clone().enqueue(networkCallback)
        return networkCallback.result
    }

    override fun cancelNetworkCall() {
        if(::call.isInitialized) call.cancel()
    }

    class NetworkCallback<T> : Callback<T>{
        val result = MutableStateFlow<Resource<T>>(Resource.loading(null))

        override fun onResponse(call: Call<T>, response: Response<T>) {
            result.value = if (response.isSuccessful) Resource.success(response.body()) else errorResult()
        }

        override fun onFailure(call: Call<T>, t: Throwable) {
            result.value = errorResult()
            t.printStackTrace()
        }

        private fun errorResult(): Resource<T> = Resource.error(NetwrokError(true, "Something went wrong"))
    }
}