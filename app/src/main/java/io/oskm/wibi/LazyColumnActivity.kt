package io.oskm.wibi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.oskm.wibi.ui.theme.WibiTheme

class LazyColumnActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val message = intent.getStringExtra("message") ?: "No message"

        setContent {
            WibiTheme {
                MainScreen(message)
            }
        }
    }
}

@Composable
fun MainScreen(message: String) {
    Scaffold(modifier = Modifier.fillMaxSize())
    { innerPadding ->
        // Sample data
        val itemList = remember { List(100) { "Item $it" } }

        Greeting2(name = "hi", modifier = Modifier.padding(innerPadding))

        // LazyColumn is like a RecyclerView for vertical lists
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(itemList.size) { item ->
                ListItem(itemText = itemList[item])
            }
        }
    }
}

@Composable
fun ListItem(itemText: String) {
    // A simple item view
    Card(
        modifier = Modifier.padding(8.dp),
        //elevation = 4.dp
    ) {
        Text(
            text = itemText,
            modifier = Modifier.padding(16.dp)
        )
    }
}


@Composable
fun Greeting2(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
private fun LazyColumnMainScreenPreview() {
    WibiTheme {
        MainScreen("lazyColumn")
    }
}