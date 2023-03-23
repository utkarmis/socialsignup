package com.cg.lib.socialloginlib.ui.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * Composable function to display the social login option google and facebook login simplyfied
 * Created by Ikram on 22-03-2023.
 * * Copyright (c) 2022 Siemens. All rights reserved.
 * */
@Composable
fun SocialLogin(eventListener: (Int) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Button(onClick = { eventListener(1) }) {
            Text(
                text = "G",
                modifier = Modifier.padding(8.dp),
                style = MaterialTheme.typography.button
            )
        }
        Button(onClick = { eventListener(2) }) {
            Text(
                text = "F",
                modifier = Modifier.padding(8.dp),
                style = MaterialTheme.typography.button
            )
        }
        Button(onClick = { eventListener(3) }) {
            Text(
                text = "T",
                modifier = Modifier.padding(8.dp),
                style = MaterialTheme.typography.button
            )
        }
    }
}