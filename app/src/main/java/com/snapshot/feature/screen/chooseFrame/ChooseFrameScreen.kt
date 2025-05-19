package com.snapshot.feature.screen.chooseFrame

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.materialIcon
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.snapshot.R
import com.snapshot.SnapShotApplication
import com.snapshot.feature.component.bottomBar.BottomNavigationBar
import com.snapshot.feature.component.frame.CrossFrame
import com.snapshot.feature.screen.choosePhoto.OptimizedPhotoItem
import com.snapshot.feature.screen.photo.viewModel.PhotoViewModel
import com.snapshot.res.modifier.ColorTheme
import com.snapshot.res.modifier.coiny
import com.snapshot.res.modifier.pressEffect
import getSetting.getShotTime


@Composable
fun ChooseFrameScreen(
    navigateToPhoto: () -> Unit,
    viewModel: PhotoViewModel
) {
    val images = listOf(
        painterResource(R.drawable.cross_blue),
        painterResource(R.drawable.cross_icon),
        painterResource(R.drawable.cross_pink),
        painterResource(R.drawable.cross_jjang),
        painterResource(R.drawable.cross_white),
        painterResource(R.drawable.cross_cartoon),
        painterResource(R.drawable.cross_black),
        painterResource(R.drawable.cross_rainbow),

        painterResource(R.drawable.inline_blue),
        painterResource(R.drawable.inline_icon),
        painterResource(R.drawable.inline_pink),
        painterResource(R.drawable.inline_black),
        painterResource(R.drawable.inline_black_insta),
        painterResource(R.drawable.inline_white_insta),
        painterResource(R.drawable.inline_retro),
        painterResource(R.drawable.inline_white),

        painterResource(R.drawable.vertical_sea),
        painterResource(R.drawable.vertical_blue),
        painterResource(R.drawable.vertical_flower),
        painterResource(R.drawable.vertical_icon),
        painterResource(R.drawable.vertical_pink),
        painterResource(R.drawable.vertical_black_retro),
        painterResource(R.drawable.vertical_white),
        painterResource(R.drawable.vertical_black),
    )

    LaunchedEffect(Unit) {
        viewModel.reset()
    }

    var selectedIndex by remember { mutableIntStateOf(-1) }

    Scaffold (
        modifier = Modifier
            .fillMaxSize()
            .background(color = ColorTheme.colors.bg),
        bottomBar = {
            if (selectedIndex != -1) {
                Box(
                    modifier = Modifier
                        .pressEffect(
                            onClick = {
                                viewModel.selectFrameIndex(selectedIndex)
                                viewModel.selectedFrame(images[selectedIndex])
                                navigateToPhoto()
                            }
                        )
                        .fillMaxWidth()
                        .padding(8.dp)
                        .background(
                            color = ColorTheme.colors.main,
                            shape = RoundedCornerShape(16.dp)
                        )
                        .height(54.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "선택 완료!",
                        color = Color.White,
                        fontSize = 20.sp
                    )
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(ColorTheme.colors.bg)
                .padding(8.dp)
                .padding(paddingValues)
        ) {
            Spacer(Modifier.height(8.dp))
            Text(
                text = "Let's make a SNAPSHOT!",
                fontSize = 24.sp,
                color = ColorTheme.colors.font,
                style = coiny
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = "프레임을 선택해주세요.",
                fontSize = 14.sp,
                color = ColorTheme.colors.font
            )
            Spacer(Modifier.height(16.dp))

            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(4.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(
                    count = images.size,
                    key = { it }
                ) { index ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(0.75f)
                            .background(
                                if (index == selectedIndex) Color.Gray.copy(alpha = 0.2f)
                                else Color.Transparent
                            )
                            .then(
                                if (index == selectedIndex) Modifier.border(
                                    2.dp,
                                    ColorTheme.colors.main
                                ) else Modifier
                            )
                            .clickable {
                                selectedIndex = index
                            }
                    ) {
                        Image(
                            painter = images[index],
                            contentDescription = "Frame $index",
                            contentScale = ContentScale.Fit,
                            modifier = Modifier
                                .fillMaxSize()
                        )

                        if (index == selectedIndex) {
                            Box(
                                modifier = Modifier
                                    .size(24.dp)
                                    .align(Alignment.TopEnd)
                                    .padding(4.dp)
                                    .background(ColorTheme.colors.main, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    painter = painterResource(R.drawable.check),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(18.dp),
                                    tint = Color.White
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

