package com.snapshot.feature.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import com.snapshot.ui.theme.GREY
import com.snapshot.ui.theme.SERVE
import com.snapshot.ui.theme.coiny

@Composable
fun TopBar(){
    Column (
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier
            .padding(top = 52.dp, start = 8.dp, bottom = 8.dp)
    ) {
        Row (
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ){
            Image(painter = painterResource(R.drawable.snapshot_icon), contentDescription = null,
                modifier = Modifier
                    .size(40.dp))
            Text(
                text = "SNAPSHOT",
                color = SERVE,
                fontFamily = coiny,
                fontSize = 28.sp
            )
        }
        HorizontalDivider(thickness = 1.dp, color = GREY)
    }
}