package com.prateek.myapplicationshekharkhandeparkar.model

import android.os.Parcel
import android.os.Parcelable

data class Country(
    var name: String,
    var capital: String,
    var flag: String,
    var subregion: String,
    var borders: List<String>
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.createStringArrayList()!!
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(capital)
        parcel.writeString(flag)
        parcel.writeString(subregion)
        parcel.writeStringList(borders)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Country> {
        override fun createFromParcel(parcel: Parcel): Country {
            return Country(parcel)
        }

        override fun newArray(size: Int): Array<Country?> {
            return arrayOfNulls(size)
        }
    }
}

class ComparatorOne : Comparator<Country> {
    override fun compare(o1: Country?, o2: Country?): Int {
        if (o1 == null || o2 == null) {
            return 0
        }
        return o2.borders.size.compareTo(o1.borders.size)
    }
}
