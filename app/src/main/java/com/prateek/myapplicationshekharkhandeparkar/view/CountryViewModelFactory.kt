package com.prateek.myapplicationshekharkhandeparkar.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.prateek.myapplicationshekharkhandeparkar.viewmodel.CountryRepository
import com.prateek.myapplicationshekharkhandeparkar.viewmodel.CountryViewModel

class CountryViewModelFactory(private val countryRepository: CountryRepository) :
    ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return CountryViewModel(countryRepository) as T
    }

}