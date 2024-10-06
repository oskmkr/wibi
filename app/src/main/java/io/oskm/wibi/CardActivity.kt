package io.oskm.wibi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.oskm.wibi.ui.theme.WibiTheme

class CardActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WibiTheme {
                CardScreen()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CardScreen() {
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Column(verticalArrangement = Arrangement.Center) {
            Greeting(
                name = "Android",
                modifier = Modifier.padding(innerPadding)
            )

            Row(modifier = Modifier.padding(4.dp)) {
                Card(modifier = Modifier.fillMaxWidth(), onClick = { /*TODO*/ }) {
                    Text(text = "hi")
                    Button(onClick = { /*TODO*/ }) {
                        Text(text = "button")
                    }
                }
            }

        }
    }
}
