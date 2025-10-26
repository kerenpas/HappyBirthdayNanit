package com.nanit.bday.presentation.bdaycard

import androidx.annotation.DrawableRes

data class ThemRes(
    @DrawableRes val backgroundDrawable: Int,
    @DrawableRes val borderedIcon: Int,
    @DrawableRes val filledIcon: Int,
    @DrawableRes val smallIcon: Int,
    @DrawableRes val cameraIcon: Int
)
