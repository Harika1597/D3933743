package uk.ac.tees.mad.sq.data

import androidx.annotation.DrawableRes

data class Category(
    val id : String,
    val name : String,
    @DrawableRes val image : Int
)
