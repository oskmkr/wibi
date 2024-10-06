package io.oskm.wibi

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.VerticalDivider
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import io.oskm.wibi.ui.theme.WibiTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * ref :
 * https://developer.android.com/develop/ui/compose/components/snackbar?hl=ko&_gl=1*1ozc4cp*_up*MQ..*_ga*MTMwMjg3OTUzOS4xNzI3MDEzOTA2*_ga_6HH9YJMN9M*MTcyNzAxMzkwNS4xLjAuMTcyNzAxMzkwNS4wLjAuMjE0MTg4MjczMw..
 * https://developer.android.com/codelabs/jetpack-compose-basics?hl=ko#1
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WibiTheme {
                MainScreen()
            }
        }
    }

}

@Preview(showBackground = true)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    var showBottomSheet by remember {
        mutableStateOf(false)
    }

    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    var navigationSelectedItem by remember {
        mutableStateOf(0)
    }
    val navController = rememberNavController()
    val context = LocalContext.current


    Scaffold(modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    Text("Small Top App Bar")
                },
                navigationIcon = {
                    IconButton(onClick = { /* do something */ }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Localized description"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { /* do something */ }) {
                        Icon(
                            imageVector = Icons.Filled.Menu,
                            contentDescription = "Localized description"
                        )
                    }
                },
            )
        },
        bottomBar = {


            NavigationBar {


                //getting the list of bottom navigation items for our data class
                BottomNavigationItem().bottomNavigationItems()
                    .forEachIndexed { index, navigationItem ->

                        //iterating all items with their respective indexes
                        NavigationBarItem(
                            selected = index == navigationSelectedItem,
                            label = {
                                Text(navigationItem.label)
                            },
                            icon = {
                                Icon(
                                    navigationItem.icon,
                                    contentDescription = navigationItem.label
                                )
                            },
                            onClick = {
                                navigationSelectedItem = index
                                navController.navigate(navigationItem.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        )
                    }
            }
        }
    ) { innerPadding ->

        Column(
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Center
        ) {

            NavHost(
                navController = navController,
                startDestination = Screens.Home.route,
                modifier = Modifier.padding(paddingValues = innerPadding)
            ) {
                composable(Screens.Home.route) {
                    HomeScreen(
                        navController,
                        onClickedOpenBottomSheetButtion = {
                            showBottomSheet = true
                        }
                    )
                }

                composable(Screens.Search.route) {
                    SearchScreen(
                        navController
                    )
                }
                composable(Screens.Profile.route) {
                    ProfileScreen(
                        navController
                    )
                }

            }


            if (showBottomSheet) {
                MyModalBottomSheet(sheetState, scope, onDismiss = { showBottomSheet = false })
            }
        }

    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun MyModalBottomSheet(
    sheetState: SheetState,
    scope: CoroutineScope,
    onDismiss: () -> Unit = {}
) {

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState
    ) {
        Text(text = "modalBottomSheet")
        Button(onClick = {
            scope.launch { sheetState.hide() }.invokeOnCompletion {
                if (!sheetState.isVisible) {
                    onDismiss()
                }
            }
        }) {
            Text(text = "hide")
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

fun myToast(context: Context, message: String) {
    val toast = Toast.makeText(context, message, Toast.LENGTH_SHORT)
    toast.show()
}

@Composable
fun MyToast(context: Context, message: String) {
    myToast(context, message)
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    WibiTheme {
        Greeting("Android")
    }
}

@ExperimentalMaterial3Api
@Preview
@Composable
fun MyModalBottomSheetPreview() {
    WibiTheme {
        MyModalBottomSheet(
            sheetState = rememberModalBottomSheetState(),
            scope = rememberCoroutineScope()
        )
    }
}


@Composable
fun HorizontalDividerExample() {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text("First item in list")
        HorizontalDivider(thickness = 2.dp)
        Text("Second item in list")
    }
}

@Composable
fun VerticalDividerExample() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Text("First item in row")
        VerticalDivider(color = MaterialTheme.colorScheme.secondary)
        Text("Second item in row")
    }
}

/**
 * https://medium.com/@bharadwaj.rns/bottom-navigation-in-jetpack-compose-using-material3-c153ccbf0593
 */
//initializing the data class with default parameters
data class BottomNavigationItem(
    val label: String = "",
    val icon: ImageVector = Icons.Filled.Home,
    val route: String = ""
) {

    //function to get the list of bottomNavigationItems
    fun bottomNavigationItems(): List<BottomNavigationItem> {
        return listOf(
            BottomNavigationItem(
                label = "Home",
                icon = Icons.Filled.Home,
                route = Screens.Home.route
            ),
            BottomNavigationItem(
                label = "Search",
                icon = Icons.Filled.Search,
                route = Screens.Search.route
            ),
            BottomNavigationItem(
                label = "Profile",
                icon = Icons.Filled.AccountCircle,
                route = Screens.Profile.route
            ),
        )
    }
}

sealed class Screens(val route: String) {
    object Home : Screens("home_route")
    object Search : Screens("search_route")
    object Profile : Screens("profile_route")
}

@Composable
fun HomeScreen(navController: NavController, onClickedOpenBottomSheetButtion: () -> Unit) {

    val context = LocalContext.current

    WibiTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(15.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .padding(horizontal = 15.dp, vertical = 10.dp)
                        .clip(MaterialTheme.shapes.large)
                ) {
                    Image(
                        painter = painterResource(R.drawable.ic_launcher_background),
                        contentDescription = "home_screen_bg",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }
                Text(
                    "Home Screen",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(vertical = 20.dp)
                )
                val clicked = remember {
                    mutableStateOf(false)
                }

                val state = remember {
                    mutableMapOf(Pair("key", "value"), Pair("key2", "value2"))
                }

                Greeting(
                    name = "Jetpack Compose",
                )

                Greeting(
                    "Android",
                )

                HorizontalDividerExample()
                VerticalDividerExample()

                Button(
                    onClick = {
                        myToast(context, "Button Clicked ${state["key"]}")
                        clicked.value = !clicked.value
                    }
                ) {
                    Text(text = "Button Clicked ${clicked.value}")

                }

                ElevatedButton(
                    onClick = {
                        // .also 는??
                        val intent = Intent(context, LazyColumnActivity::class.java).apply {
                            putExtra("message", "Hello from MainActivity!")
                        }

                        context.startActivity(intent)


                    }) {
                    Text(text = "Elevated Button")
                }

                Button(onClick = {
                    myToast(context = context, message = "open dialog")

                    onClickedOpenBottomSheetButtion()

                }) {
                    Text("open bottom sheet dialog")
                }

                MyToast(context = context, message = "auto message")

            }
        }
    }
}

@Composable
fun SearchScreen(navController: NavController) {
    WibiTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(15.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp)
                        .padding(horizontal = 15.dp, vertical = 10.dp)
                        .clip(MaterialTheme.shapes.large)
                ) {
                    Image(
                        painter = painterResource(R.drawable.ic_launcher_background),
                        contentDescription = "search_screen_bg",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }
                Text(
                    "Search Screen",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(vertical = 20.dp)
                )
            }
        }
    }
}

@Composable
fun ProfileScreen(navController: NavController) {
    WibiTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(15.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .padding(horizontal = 15.dp, vertical = 10.dp)
                        .clip(MaterialTheme.shapes.large)
                ) {
                    Image(
                        painter = painterResource(R.drawable.ic_launcher_background),
                        contentDescription = "profile_screen_bg",
                        contentScale = ContentScale.Crop
                    )
                }
                Text(
                    "Profile Screen",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(vertical = 20.dp)
                )
            }
        }
    }
}