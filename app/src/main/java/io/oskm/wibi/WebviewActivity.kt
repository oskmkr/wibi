package io.oskm.wibi

import android.os.Bundle
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import android.widget.EditText
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import io.oskm.wibi.ui.theme.WibiTheme

class WebviewActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            WibiTheme {
                WebViewMainScreen()
            }
        }

    }
}

@Preview
@Composable
fun WebViewMainScreen() {
    Scaffold(modifier = Modifier.fillMaxSize())
    { innerPadding ->

        var url by remember {
            mutableStateOf("https://m.naver.com")
        }

        var textFieldUrl by remember {
            mutableStateOf("https://m.naver.com")
        }

        //var tmpUrl: String = "https://m.naver.com"

        Column(verticalArrangement = Arrangement.Center) {
            TextField(
                value = textFieldUrl,
                label = { Text("URL") },
                singleLine = true,
                maxLines = 1,
                onValueChange = { value -> textFieldUrl = value },
                modifier = Modifier.onKeyEvent { evt ->
                    if(evt.key == Key.Enter) {
                        url = textFieldUrl
                        true
                    } else {
                        false
                    }
                }

            )

            Button(onClick = {
                url = textFieldUrl
            }) {
                Text(text = "이동")
            }

            MyWebView(Modifier.padding(innerPadding), onUpdate = { webView ->
                webView.loadUrl(url)
            })

        }

    }
}


@Composable
fun MyWebView(modifier: Modifier, onUpdate: (WebView) -> Unit) {
    val url = "https://m.naver.com"

    AndroidView(
        factory = { context ->

            WebView(context).apply {
                this.layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
                this.webViewClient = CustomWebViewClient()
            }

        },
        modifier = modifier,
        update = onUpdate
    )
}

class CustomWebViewClient : WebViewClient() {
    override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
        view.loadUrl(url)
        return true
    }

}