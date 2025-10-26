package com.nanit.bday.presentation.bdaycard

import com.nanit.bday.R
import com.nanit.bday.domain.Age
import com.nanit.bday.domain.AgeUnit




data class BdayUiState(
    val name: String = "Nanit",
    val themeResources: ThemRes = ThemRes(
        backgroundDrawable = R.drawable.bg_android_elephant,
        backgroundColor = R.color.background_yellow,
        borderedIcon = R.drawable.ic_baby_face_circle_yellow_bordered,
        filledIcon = R.drawable.ic_baby_face_circle_yellow_filled,
        smallIcon = R.drawable.ic_baby_face_yellow_small,
        cameraIcon = R.drawable.ic_camera_yellow
    ),
    val age: Age = Age(0, AgeUnit.MONTHS),
    val numberIconResource: Int = 0.toNumberIconResource(),
    val ageText: String = "",
    val photoUri: String? = null,
    val error: String? = null
)
