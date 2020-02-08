package com.prateek.myapplicationshekharkhandeparkar.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.prateek.myapplicationshekharkhandeparkar.model.Country
import com.prateek.myapplicationshekharkhandeparkar.network.RestApiService
import kotlinx.coroutines.*
import retrofit2.HttpException

/*
* CountryRepository class
* repository for fetching data
* */
open class CountryRepository(context: Context) {

    // variable declaration & initialisation
    private var mContext: Context = context
    private var countries = mutableListOf<Country>()
    private var mutableLiveData = MutableLiveData<List<Country>>()
    private val mRequestTimeout = MutableLiveData<Boolean>()
    private var completableJob = Job()
    private var coroutinesScope = CoroutineScope(Dispatchers.IO + completableJob)

    init {
        mRequestTimeout.value = false
    }

    private val thisApiCorService by lazy {
        // lazy instance of retrofit client
        RestApiService.createService(mContext)
    }

    fun isRequestTimedOut(): LiveData<Boolean> {
        return mRequestTimeout
    }

    fun fetchLiveDataFromServer(size:Int): MutableLiveData<List<Country>> {   // fetch data from server
        if (coroutinesScope == null) {
            if (completableJob == null) {
                completableJob = Job()
            }
            coroutinesScope = CoroutineScope(Dispatchers.IO + completableJob)
        }
        val serverJob = coroutinesScope.async {
            val request = thisApiCorService.fetchAsync()
            withContext(Dispatchers.IO) {
                try {
                    val response = request.await()
                    if (response != null) {
                        countries = response as MutableList<Country>

                        var batchCountry = countries.subList(0, size + 20)
                        GlobalScope.launch(Dispatchers.Main) {
                            // make operations on main thread
                            mutableLiveData.value = batchCountry
                        }
                    }
                } catch (e: HttpException) {
                    e.printStackTrace()
                    errorOccurred()
                } catch (e: Throwable) {
                    e.printStackTrace()
                    errorOccurred()
                }
            }
        }
        return mutableLiveData
    }

    private fun errorOccurred() {                                       // for error handling
        GlobalScope.launch(Dispatchers.Main) {
            mRequestTimeout.value = true
        }
    }
}