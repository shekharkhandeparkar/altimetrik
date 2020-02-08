package com.prateek.myapplicationshekharkhandeparkar.view


import android.content.Context
import android.content.Intent
import android.graphics.drawable.PictureDrawable
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.prateek.myapplicationshekharkhandeparkar.R
import com.prateek.myapplicationshekharkhandeparkar.model.Country
import com.prateek.myapplicationshekharkhandeparkar.utils.SvgSoftwareLayerSetter


/*
* CountryAdapter class
* adapter class for binding data to recyclerview
* */
class CountryAdapter(private val countryList: List<Country>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var mContext: Context? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        this.mContext = parent.context
        return CountryViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_country, parent, false)
        )
    }

    override fun getItemCount() = countryList.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val mCountry = countryList[position]
        val countryViewHolder = holder as CountryViewHolder
        countryViewHolder.tvTitle.text = when (mCountry.name.isNullOrBlank()) {
            true -> countryViewHolder.tvTitle.context.resources.getString(R.string.na)
            else -> mCountry.name
        }
        countryViewHolder.tvCapital.text = when (mCountry.capital.isNullOrBlank()) {
            true -> countryViewHolder.tvCapital.context.resources.getString(R.string.na)
            else -> mCountry.capital
        }
        countryViewHolder.tvBorders.text = when (mCountry.borders.isNullOrEmpty()) {
            true -> countryViewHolder.tvCapital.context.resources.getString(R.string.na)
            else -> "${mCountry.borders.size}"
        }

        Glide.with(countryViewHolder.itemView.context)
            .`as`(PictureDrawable::class.java)
            .placeholder(R.mipmap.ic_launcher)
            .transition(withCrossFade())
            .listener(SvgSoftwareLayerSetter()).load(Uri.parse(mCountry.flag))
            .into(countryViewHolder.ivFlag)

        countryViewHolder.itemView.setOnClickListener {
            val i = Intent(countryViewHolder.itemView.context, CountryDetailsActivity::class.java)
            i.putExtra("country", mCountry)
            countryViewHolder.itemView.context.startActivity(i)
        }
    }

    /*
    * CountryViewHolder class
    * */
    class CountryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTitle: TextView = itemView.findViewById(R.id.tvName)
        val tvCapital: TextView = itemView.findViewById(R.id.tvCapital)
        val tvBorders: TextView = itemView.findViewById(R.id.tvBorders)
        val ivFlag: ImageView = itemView.findViewById(R.id.ivFlag)
    }

}