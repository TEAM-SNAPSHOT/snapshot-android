package com.snapshot.feature.screen.setting

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.snapshot.res.modifier.ColorTheme
import getSetting.getAlbumName
import getSetting.getShotTime

@Composable
fun SettingScreen(
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current

    var shotTime by remember { mutableStateOf("8") }
    var albumName by remember { mutableStateOf("스냅샷") }
    LaunchedEffect(Unit) {
        shotTime = getShotTime(context)
        albumName = getAlbumName(context)
    }


    val aosUsers = listOf(
        "sincerxly" to "김성한",
        "kmj5004" to "김민재",
        "jinyong68" to "박진용"
    )
    val aosAnnotated = buildAnnotatedString {
        aosUsers.forEachIndexed { index, (id, name) ->
            val tag = "AOS_$index"
            pushStringAnnotation(tag = tag, annotation = "https://github.com/$id")
            withStyle(
                style = SpanStyle(
                    color = ColorTheme.colors.blue,
                    textDecoration = TextDecoration.Underline
                )
            ) {
                append("$id ($name)\n")
            }
            pop()
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(color = ColorTheme.colors.bg)
            .padding(horizontal = 16.dp)
    ) {
        Column {
            Spacer(modifier = modifier.height(16.dp))
            Column(
                modifier = modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "설정",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isSystemInDarkTheme()) ColorTheme.colors.white else ColorTheme.colors.black
                )
                Column(
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = modifier
                        .fillMaxWidth()
                        .background(color = ColorTheme.colors.bg, shape = RoundedCornerShape(8.dp))
                        .padding(horizontal = 8.dp, vertical = 16.dp)
                ) {
                    // 앨범 이름
                    Row(
                        modifier = modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "앨범 이름",
                            fontSize = 16.sp,
                            color = if (isSystemInDarkTheme()) ColorTheme.colors.white else ColorTheme.colors.black
                        )
                        TextField(
                            modifier = modifier.width(144.dp),
                            shape = RoundedCornerShape(8.dp),
                            colors = TextFieldDefaults.colors(
                                focusedIndicatorColor = ColorTheme.colors.gray,
                                unfocusedIndicatorColor = ColorTheme.colors.gray,
                                disabledIndicatorColor = ColorTheme.colors.gray,
                                focusedContainerColor = ColorTheme.colors.gray,
                                unfocusedContainerColor = ColorTheme.colors.gray,
                                disabledContainerColor = ColorTheme.colors.gray,
                            ),
                            textStyle = LocalTextStyle.current.copy(
                                fontSize = 16.sp,
                                color = if (isSystemInDarkTheme()) ColorTheme.colors.white else ColorTheme.colors.black
                            ),
                            singleLine = true,
                            value = albumName,
                            onValueChange = { albumName = it }
                        )
                    }

                    HorizontalDivider(thickness = 1.dp, color = ColorTheme.colors.gray)

                    // 촬영 시간
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = buildAnnotatedString {
                                append("촬영 시간\n")
                                withStyle(
                                    style = SpanStyle(
                                        fontSize = 12.sp,
                                        color = ColorTheme.colors.gray
                                    )
                                ) {
                                    append("3초 이상, 16초 이하로 입력해 주세요.")
                                }
                            },
                            fontSize = 16.sp,
                            color = if (isSystemInDarkTheme()) ColorTheme.colors.white else ColorTheme.colors.black
                        )
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            TextField(
                                modifier = modifier.width(55.dp),
                                shape = RoundedCornerShape(8.dp),
                                colors = TextFieldDefaults.colors(
                                    focusedIndicatorColor = ColorTheme.colors.gray,
                                    unfocusedIndicatorColor = ColorTheme.colors.gray,
                                    disabledIndicatorColor = ColorTheme.colors.gray,
                                    focusedContainerColor = ColorTheme.colors.gray,
                                    unfocusedContainerColor = ColorTheme.colors.gray,
                                    disabledContainerColor = ColorTheme.colors.gray,
                                ),
                                textStyle = LocalTextStyle.current.copy(
                                    fontSize = 16.sp,
                                    textAlign = TextAlign.End,
                                    color = if (isSystemInDarkTheme()) ColorTheme.colors.white else ColorTheme.colors.black
                                ),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                value = shotTime,
                                onValueChange = {
                                    if (it.length <= 2 && (it.isEmpty() || it.all { c -> c.isDigit() })) {
                                        shotTime = it
                                    }
                                },
                                singleLine = true,
                            )
                            Text(
                                text = "초",
                                fontSize = 16.sp,
                                color = if (isSystemInDarkTheme()) ColorTheme.colors.white else ColorTheme.colors.black
                            )
                        }
                    }
                }

                // 크레딧
                Column(
                    modifier = modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "크레딧",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isSystemInDarkTheme()) ColorTheme.colors.white else ColorTheme.colors.black
                    )
                    Column(
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = modifier
                            .fillMaxWidth()
                            .background(color = ColorTheme.colors.bg, shape = RoundedCornerShape(8.dp))
                            .padding(horizontal = 8.dp, vertical = 16.dp)
                    ) {
                        Row(
                            modifier = modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "만든사람",
                                fontSize = 16.sp,
                                color = if (isSystemInDarkTheme()) ColorTheme.colors.white else ColorTheme.colors.black
                            )
                            ClickableText(
                                text = aosAnnotated,
                                style = LocalTextStyle.current.copy(
                                    fontSize = 16.sp,
                                    textAlign = TextAlign.End,
                                    color = ColorTheme.colors.blue,
                                    textDecoration = TextDecoration.None
                                ),
                                onClick = { offset ->
                                    aosAnnotated.getStringAnnotations(start = offset, end = offset).firstOrNull()?.let {
                                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(it.item))
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                        context.startActivity(intent) // Aplication어쩌구로 안됨
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}
