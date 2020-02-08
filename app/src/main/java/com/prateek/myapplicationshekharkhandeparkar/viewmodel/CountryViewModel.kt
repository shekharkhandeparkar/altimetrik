package com.prateek.myapplicationshekharkhandeparkar.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.prateek.myapplicationshekharkhandeparkar.model.Country

open class CountryViewModel(var countryRepository: CountryRepository) : ViewModel() {

    lateinit var context: Context
    var countries: LiveData<List<Country>> = countryRepository.fetchLiveDataFromServer(0)

    var reloadTrigger = MutableLiveData<Boolean>()

    init {
        reloadTrigger.value = true
    }

    /*
    * called from fragment for reloading the data
    * */
    fun fetchCountryData(activity: Context): LiveData<List<Country>> {
        context = activity
        return countries
    }

    fun fetchMoreCountryData(size:Int): LiveData<List<Country>> {
        countries = countryRepository.fetchLiveDataFromServer(size)
        return countries
    }

    fun isRequestTimedOut(): LiveData<Boolean> {
        return countryRepository.isRequestTimedOut()
    }
}