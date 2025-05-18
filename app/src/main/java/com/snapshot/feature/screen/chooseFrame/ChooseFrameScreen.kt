package com.snapshot.feature.screen.chooseFrame

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.snapshot.R
import com.snapshot.feature.component.bottomBar.BottomNavigationBar
import com.snapshot.feature.component.frame.CrossFrame
import com.snapshot.feature.screen.choosePhoto.OptimizedPhotoItem
import com.snapshot.res.modifier.ColorTheme
import com.snapshot.res.modifier.coiny

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChooseFrameScreen(
    navigateToPhoto: () -> Unit
) {
    val image = listOf(
        painterResource(R.drawable.vertical_sea),
        painterResource(R.drawable.vertical_blue),
        painterResource(R.drawable.vertical_flow),
        painterResource(R.drawable.vertical_icon),
        painterResource(R.drawable.vertical_pink),
        painterResource(R.drawable.vertical_black_text),
        painterResource(R.drawable.vertical_white),
        painterResource(R.drawable.vertical_black),

        painterResource(R.drawable.inline_blue),
        painterResource(R.drawable.inline_icon),
        painterResource(R.drawable.inline_pink),
        painterResource(R.drawable.inline_black),
        painterResource(R.drawable.inline_black_text),
        painterResource(R.drawable.inline_black_retro),
        painterResource(R.drawable.inline_white_text),
        painterResource(R.drawable.inline_white),
        painterResource(R.drawable.inline_white_text),

        painterResource(R.drawable.cross_blue),
        painterResource(R.drawable.cross_icon),
        painterResource(R.drawable.cross_pink),
        painterResource(R.drawable.cross_jjang),
        painterResource(R.drawable.cross_white),
        painterResource(R.drawable.cross_cartoon),
        painterResource(R.drawable.cross_black),
        painterResource(R.drawable.cross_rainbow),
    )
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = ColorTheme.colors.bg)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = ColorTheme.colors.bg)
                .padding(8.dp)
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
                text = "사진을 선택해주세요 (최대 4개)",
                fontSize = 14.sp,
                color = ColorTheme.colors.font
            )
            Spacer(Modifier.height(16.dp))

//            LazyVerticalGrid(
//                columns = GridCells.Fixed(3),
//                modifier = Modifier.fillMaxWidth(),
//                contentPadding = PaddingValues(4.dp)
//            ) {
//                items(
//                    count = photoBitmaps.size,
//                    key = { it }
//                ) { index ->
//                    val isSelected = selectedPhotosOrder.contains(index)
//                    val selectionOrder = viewModel.getSelectionOrder(index)
//
//                    key(index, isSelected, selectionOrder) {
//                        OptimizedPhotoItem(
//                            bitmap = photoBitmaps[index],
//                            index = index,
//                            isSelected = isSelected,
//                            selectionOrder = selectionOrder,
//                            onPhotoSelected = {
//                                viewModel.selectPhoto(it)
//                            }
//                        )
//                    }
//                }
//            }
        }
    }
}