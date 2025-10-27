package com.nanit.bday.presentation.bdaycard

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.drawable.VectorDrawable
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.content.res.AppCompatResources
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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import coil.compose.AsyncImage
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.constraintlayout.compose.Dimension
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.nanit.bday.R
import java.io.File
import kotlin.math.sqrt


@Composable
fun BirthdayScreen(
    viewModel: BirthdayViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    var showPhotoDialog by remember { mutableStateOf(false) }
    var photoUri by remember { mutableStateOf<Uri?>(null) }


    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            viewModel.updatePhotoUri(it.toString())
        }
    }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            photoUri?.let {
                viewModel.updatePhotoUri(it.toString())
            }
        }
    }

    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            val file =
                File(context.externalCacheDir, "temp_photo_${System.currentTimeMillis()}.jpg")
            photoUri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                file
            )
            photoUri?.let {
                cameraLauncher.launch(it)
            }
        }
    }


    if (showPhotoDialog) {
        PhotoSelectionDialog(
            onGalleryClick = {
                showPhotoDialog = false
                galleryLauncher.launch("image/*")
            },
            onCameraClick = {
                showPhotoDialog = false

                when (PackageManager.PERMISSION_GRANTED) {
                    ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) -> {
                        val file = File(
                            context.externalCacheDir,
                            "temp_photo_${System.currentTimeMillis()}.jpg"
                        )
                        photoUri = FileProvider.getUriForFile(
                            context,
                            "${context.packageName}.fileprovider",
                            file
                        )
                        photoUri?.let {
                            cameraLauncher.launch(it)
                        }
                    }

                    else -> {
                        cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                    }
                }
            },
            onDismiss = {
                showPhotoDialog = false
            }
        )
    }

    BirthdayScreen(
        uiState = uiState,
        onPhotoClick = { showPhotoDialog = true },
    )
}


@Composable
private fun BirthdayScreen(
    uiState: BdayUiState,
    onPhotoClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val density = LocalDensity.current


    val cameraSize = remember(uiState.themeResources.cameraIcon, context, density) {
        (AppCompatResources.getDrawable(context, uiState.themeResources.cameraIcon) as? VectorDrawable)
            ?.let { with(density) { it.intrinsicWidth.toDp() } } ?: 0.dp
    }

    val babySize = remember(uiState.themeResources.borderedIcon, context, density) {
        (AppCompatResources.getDrawable(context, uiState.themeResources.borderedIcon) as? VectorDrawable)
            ?.let { with(density) { it.intrinsicWidth.toDp() } } ?: 0.dp
    }

    val radius = remember(babySize) { babySize / 2 }

    val cameraOffset = remember(babySize, cameraSize, density) {
        derivedStateOf {
            val rPx = with(density) { radius.toPx() }
            val camPx = with(density) { cameraSize.toPx() }

            // 45° placement on the ring:
            val hyp = rPx * kotlin.math.sqrt(2.0)
            val hypSmall = hyp - rPx
            val side = (hypSmall / kotlin.math.sqrt(2.0)).toFloat()

            val xPx = with(density) { (babySize - side.toDp()).toPx() } - camPx / 2f
            val yPx = with(density) { side.toDp().toPx() } - camPx / 2f
            IntOffset(xPx.toInt(), yPx.toInt())
        }
    }

    Box(
        modifier = modifier.fillMaxSize()
    ) {

        ConstraintLayout(
            modifier = Modifier
                .fillMaxSize()
                .background(color = colorResource(id = uiState.themeResources.backgroundColor))
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
                            .size(cameraSize)
                            .offset {
                                cameraOffset.value
                            }
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


@Composable
private fun PhotoSelectionDialog(
    onGalleryClick: () -> Unit,
    onCameraClick: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text("Select Photo")
        },
        text = {
            Text("Choose how to add a photo")
        },
        confirmButton = {
            TextButton(onClick = onGalleryClick) {
                Text("Gallery")
            }
        },
        dismissButton = {
            TextButton(onClick = onCameraClick) {
                Text("Camera")
            }
        }
    )
}
