package com.prateek.myapplicationshekharkhandeparkar.view

import android.graphics.drawable.PictureDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.prateek.myapplicationshekharkhandeparkar.R
import com.prateek.myapplicationshekharkhandeparkar.model.Country
import com.prateek.myapplicationshekharkhandeparkar.utils.SvgSoftwareLayerSetter
import kotlinx.android.synthetic.main.activity_country_details.*

class CountryDetailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_country_details)

        val country = intent.extras!!.get("country") as Country

        Glide.with(ivFlag.context)
            .`as`(PictureDrawable::class.java)
            .placeholder(R.mipmap.ic_launcher)
            .transition(DrawableTransitionOptions.withCrossFade())
            .listener(SvgSoftwareLayerSetter()).load(Uri.parse(country.flag)).into(ivFlag)

        tvName.text = country.name
        tvCapital.text = country.capital
        tvBorders.text = "${country.borders.size} Borders"
        tvSubregion.text = country.subregion
    }
}
