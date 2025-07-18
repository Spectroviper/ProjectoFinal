package com.example.projecttest.helpComposeFiles.info

import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.snapping.SnapPosition
import androidx.compose.foundation.layout.Arrangement.Center
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.motionEventSpy
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.projecttest.AppConfig
import com.example.projecttest.data.model.Challenge
import com.example.projecttest.data.model.Game

@Composable
fun ChallengeInfo(challenge: Challenge) {

    val BASE_URL = AppConfig.DOMAIN + AppConfig.PORT

    Column (
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        Image(
            painter = rememberAsyncImagePainter(model = BASE_URL + challenge.imageUrl),
            contentDescription = challenge.challengeName,
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp) // or whatever height you want
                .align(Alignment.CenterHorizontally)

        )
        Spacer(Modifier.width(16.dp))
        Text(challenge.challengeName!!,

            modifier = Modifier
                .padding(32.dp)
                .align(Alignment.CenterHorizontally),

            fontSize = TextUnit(24F, TextUnitType.Sp),
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.width(16.dp))

        if (challenge.isStarted == true) {
            Text(
                "This challenge has started have fun everyone!!",

                modifier = Modifier
                    .padding(32.dp)
                    .align(Alignment.CenterHorizontally),

                fontSize = TextUnit(24F, TextUnitType.Sp),
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        }
        Text("Created By: ${challenge.createdBy}", Modifier.padding(16.dp).align(Alignment.End))
    }
}