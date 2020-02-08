package com.prateek.myapplicationshekharkhandeparkar.view

import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.prateek.myapplicationshekharkhandeparkar.R
import com.prateek.myapplicationshekharkhandeparkar.model.ComparatorOne
import com.prateek.myapplicationshekharkhandeparkar.model.Country
import com.prateek.myapplicationshekharkhandeparkar.viewmodel.CountryRepository
import com.prateek.myapplicationshekharkhandeparkar.viewmodel.CountryViewModel
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private var mCountryList: ArrayList<Country> = arrayListOf()     // for list
    private var mCountryAdapter: CountryAdapter? = null             // adapter for the recyclerview
    private var mCountryViewModel: CountryViewModel? = null         // viewmodel instance
    private var mContext: Context? = null         // viewmodel instance

    private var isLoading = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mContext = this

        mCountryViewModel =
            ViewModelProviders.of(this, CountryViewModelFactory(CountryRepository(mContext!!)))
                .get(CountryViewModel::class.java)

        initRecyclerView()                                      // initialise all the views other view work required
        observeViewModel(mCountryViewModel!!)                     // add observer for the viewmodel data (for any data change)
        fetchCountries()                                            // fetch countries data call
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_sort, menu)
        // return true so that the menu pop up is opened
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId === R.id.sort) {
            if (mCountryList.isNullOrEmpty()) {
                Toast.makeText(
                    mContext!!,
                    getString(R.string.some_error_occurred),
                    Toast.LENGTH_LONG
                )
                    .show()
            } else {
                val sortedCountries = mCountryList.sortedWith(ComparatorOne())
                mCountryList.clear()
                mCountryList.addAll(sortedCountries)

                mCountryAdapter?.notifyDataSetChanged()
            }
        }
        return true
    }

    private fun initRecyclerView() {
        mCountryAdapter =
            CountryAdapter(
                mCountryList
            )
        rvCountries.layoutManager = LinearLayoutManager(mContext!!)
        rvCountries.itemAnimator = DefaultItemAnimator()
        val dividerItemDecoration = DividerItemDecoration(
            rvCountries.context,
            (rvCountries.layoutManager as LinearLayoutManager).orientation
        )
        rvCountries.addItemDecoration(dividerItemDecoration)
        rvCountries.adapter = mCountryAdapter

        rvCountries.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrolled(
                @NonNull recyclerView: RecyclerView, dx: Int,
                dy: Int
            ) {
                super.onScrolled(recyclerView, dx, dy)
                val linearLayoutManager =
                    recyclerView.layoutManager as LinearLayoutManager?
                if (!isLoading) {
                    if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == mCountryList.size - 1) { //bottom of list!
                        fetchMoreCountries()
                        isLoading = true
                    }
                }
            }
        })
    }

    private fun fetchCountries() {
        showLoader()
        mCountryViewModel!!.fetchCountryData(mContext!!)
    }

    private fun fetchMoreCountries() {
        showLoader()
        mCountryViewModel!!.fetchMoreCountryData(mCountryList.size)
    }

    private fun observeViewModel(mCountryViewModel: CountryViewModel) {
        mCountryViewModel.countries.observe(this,
            Observer<List<Country>> { countryList ->
                if (countryList != null && countryList.isNotEmpty()) {
                    isLoading = false
                    prepareList(countryList)
                }
            })

        mCountryViewModel.isRequestTimedOut()
            .observe(this, Observer { t -> if (t) hideLoaderWithMessage() })
    }

    private fun prepareList(countriesList: List<Country>?) {
        mCountryList.clear()
        if (countriesList != null) {
            mCountryList.addAll(countriesList)
        }
        mCountryAdapter?.notifyDataSetChanged()
        hideLoader()
    }

    private fun showLoader() {
        progressBar.visibility = View.VISIBLE
        textViewEmpty.visibility = View.GONE
    }

    private fun hideLoaderWithMessage() {
        textViewEmpty.visibility = View.VISIBLE
        Toast.makeText(mContext!!, getString(R.string.some_error_occurred), Toast.LENGTH_LONG)
            .show()
        hideLoader()
    }

    private fun hideLoader() {
        progressBar.visibility = View.GONE
    }
}
