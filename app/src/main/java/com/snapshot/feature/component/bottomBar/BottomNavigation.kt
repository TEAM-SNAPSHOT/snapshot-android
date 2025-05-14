package com.snapshot.feature.component.bottomBar

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import com.snapshot.R
import com.snapshot.feature.screen.album.navigation.ALBUM_ROUTE
import com.snapshot.feature.screen.chooseFrame.navigation.CHOOSE_FRAME_ROUTE
import com.snapshot.feature.screen.setting.navigation.SETTING_ROUTE
import com.snapshot.res.modifier.ColorTheme
import com.snapshot.res.modifier.pressEffect

@Composable
fun BottomNavigationBar(
    navController: NavController,
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: ALBUM_ROUTE

    val items = listOf(
        Destination.ALBUM,
        Destination.SETTING
    )

    val shadowColor = ColorTheme.colors.shadow.copy(alpha = 0.4f)
    val shadowArgb = shadowColor.toArgb()

    Box {
        NavigationBar(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = ColorTheme.colors.bg,
                    shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
                )
                .padding(
                    top = 20.dp
                )
                .height(100.dp)
                .navigationBarsPadding()
                .shadow(
                    elevation = 12.dp,
                    shape = RoundedCornerShape(topEnd = 20.dp, topStart = 20.dp),
                    spotColor = ColorTheme.colors.shadow,
                    ambientColor = ColorTheme.colors.shadow
                ),
            windowInsets = WindowInsets.systemBars.only(WindowInsetsSides.Bottom),
            containerColor = ColorTheme.colors.bg,
        ) {
            items.forEach { destination ->
                val isSelected = destination.route == currentRoute
                val animatedColor by animateColorAsState(
                    animationSpec = tween(
                        durationMillis = 200,
                    ),
                    targetValue = if (isSelected) ColorTheme.colors.iconSelected else ColorTheme.colors.iconNotSelected
                )

                NavigationBarItem(
                    icon = {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {
                            Image(
                                painter = painterResource(destination.getIcon()),
                                contentDescription = null,
                                colorFilter = ColorFilter.tint(
                                    animatedColor
                                ),
                                modifier = Modifier
                                    .size(40.dp)
                            )
                            Spacer(Modifier.height(10.dp))
                            Text(
                                modifier = Modifier
                                    .align(Alignment.CenterHorizontally),
                                text = destination.label,
                                color = animatedColor,
                                fontSize = 16.sp
                            )
                        }
                    },
                    selected = isSelected,
                    onClick = {
                        if (!isSelected) {
                            navController.navigate(destination.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    },
                    colors = NavigationBarItemDefaults.colors(
                        indicatorColor = Color.Transparent
                    )
                )
            }
        }
        Box(
            modifier = Modifier
                .size(60.dp)
                .align(Alignment.TopCenter)
                .drawBehind {
                    val radius = this.size.minDimension / 2
                    drawIntoCanvas { canvas ->
                        val paint = Paint().apply {
                            color = Color.Transparent
                            this.asFrameworkPaint().setShadowLayer(
                                20f,
                                0f,
                                0f,
                                shadowArgb
                            )
                        }

                        canvas.drawCircle(
                            center = this.center,
                            radius = radius,
                            paint = paint
                        )
                    }
                }
                .clip(CircleShape)
                .background(color = ColorTheme.colors.main)
                .pressEffect(
                    onClick = {
                        if (currentRoute != CHOOSE_FRAME_ROUTE) {
                            navController.navigate(CHOOSE_FRAME_ROUTE) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    }
                )
        ) {
            Image(
                painter = painterResource(R.drawable.material_icons_24dp),
                modifier = Modifier
                    .size(44.dp)
                    .align(alignment = Alignment.Center),
                contentDescription = null,
                colorFilter = ColorFilter.tint(color = Color.White)
            )
        }

    }
}