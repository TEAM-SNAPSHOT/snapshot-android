package com.snapshot.feature.component.topbar

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.snapshot.R
import com.snapshot.res.modifier.bg_light
import com.snapshot.res.modifier.coiny
import com.snapshot.res.modifier.gray
import com.snapshot.res.modifier.serve

@Composable
fun TopBar(){
    Column ( // bgcolor는 app의 scaffold에서 지정해줘야함
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier
            .background(bg_light) // 나중에 모드 처리 해줘야함
            .padding(top = 52.dp, start = 8.dp, bottom = 8.dp)
    ) {
        Row (
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ){
            Image(
                painter = painterResource(R.drawable.snapshot_icon),
                contentDescription = null,
                modifier = Modifier
                    .size(40.dp))
            Text(
                text = "SNAPSHOT",
                color = serve,
                style = coiny,
                fontSize = 28.sp
            )
        }
        HorizontalDivider(thickness = 1.dp, color = gray)
    }
}