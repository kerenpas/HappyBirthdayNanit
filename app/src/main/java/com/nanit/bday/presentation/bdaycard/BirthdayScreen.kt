package com.nanit.bday.presentation.bdaycard

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import coil.compose.AsyncImage
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.constraintlayout.compose.Dimension
import com.nanit.bday.R


@Composable
fun BirthdayScreen(
    viewModel: BirthdayViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    BirthdayScreen(
        uiState = uiState,
        onPhotoClick = { }
    )
}


@Composable
private fun BirthdayScreen(
    uiState: BdayUiState,
    onPhotoClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize()
    ) {

        ConstraintLayout(
            modifier = Modifier.fillMaxSize().background(color = colorResource (id = uiState.themeResources.backgroundColor))
        ) {
            Image(
                painter = painterResource(id = uiState.themeResources.backgroundDrawable),
                contentDescription = "",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            val (titleText, ageRow, ageTextRef, iconGroup) = createRefs()

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.constrainAs(iconGroup) {
                    centerHorizontallyTo(parent)
                    centerVerticallyTo(parent)
                }
            ) {
                Box(
                    modifier = Modifier
                        .clickable { onPhotoClick() }
                ) {
                    if (uiState.photoUri != null) {

                        AsyncImage(
                            model = uiState.photoUri,
                            contentDescription = "",
                            modifier = Modifier
                                .size(200.dp)
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = uiState.themeResources.borderedIcon),
                            tint = Color.Unspecified,
                            contentDescription = "baby icon"
                        )
                    }
                    Image(
                        painter = painterResource(id = uiState.themeResources.cameraIcon),
                        contentDescription = "Camera",
                        modifier = Modifier
                            .align(Alignment.Center)
                            .offset(x = 70.dp, y = (-70).dp)
                    )
                }

                Spacer(modifier = Modifier.height(15.dp))

                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.ic_nanit_logo),
                    tint = Color.Unspecified,
                    contentDescription = "Nanit logo"
                )
            }

            Text(
                text = "TODAY ${uiState.name.uppercase()} IS",
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier.constrainAs(titleText) {
                    top.linkTo(parent.top, margin = 14.dp)
                    start.linkTo(iconGroup.start)
                    end.linkTo(iconGroup.end)
                    width = Dimension.fillToConstraints
                }
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(22.dp),
                modifier = Modifier.constrainAs(ageRow) {
                    top.linkTo(titleText.bottom, margin = 13.dp)
                    centerHorizontallyTo(parent)
                }
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.ic_wind_left),
                    tint = Color.Unspecified,
                    contentDescription = ""
                )

                Icon(
                    imageVector = ImageVector.vectorResource(id = uiState.numberIconResource),
                    tint = Color.Unspecified,
                    contentDescription = "Age number: ${uiState.age.value}"
                )

                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.ic_wind_right),
                    tint = Color.Unspecified,
                    contentDescription = ""
                )
            }

            Text(
                text = uiState.ageText,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.constrainAs(ageTextRef) {
                    top.linkTo(ageRow.bottom, margin = 14.dp)
                    centerHorizontallyTo(parent)
                }
            )
        }
    }


}
