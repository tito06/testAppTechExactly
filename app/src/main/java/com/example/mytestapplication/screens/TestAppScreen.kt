package com.example.mytestapplication.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.example.mytestapplication.R
import com.example.mytestapplication.helper.AppListState
import kotlinx.coroutines.launch
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.ui.draw.clip
import com.google.accompanist.systemuicontroller.rememberSystemUiController


@Composable
fun TestAppScreen(){

    val appList = remember { mutableStateOf(sampleAppList()) }
    val searchText = remember { mutableStateOf("") }
    val pagerState = rememberPagerState(initialPage = 0, pageCount = { 2 })

    val systemUiController = rememberSystemUiController()

    LaunchedEffect(Unit) {
        systemUiController.setStatusBarColor(
            color = Color(0xFF7E57C2) // Set your desired color
        )
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = androidx.compose.ui.graphics.Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF7E57C2), // Purple
                        Color(0xFF2196F3)  // Light Blue
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {

            HeaderSection()
            SearchBar(searchText, appList)

            if(searchText.value != "") {

                    LazyColumn(
                        modifier = Modifier.fillMaxSize()
                            .padding(16.dp,0.dp)// Add background to make it visible)

                    ) {
                        // If the list is empty, show a message indicating no results
                        if (appList.value.isEmpty()) {
                            item {
                                Text(
                                    text = "No results found",
                                    modifier = Modifier.padding(16.dp),
                                    color = Color.White,
                                    fontSize = 18.sp,

                                    )
                            }
                        } else {
                            // Display the filtered items from the appList
                            items(appList.value) { app ->
                                Text(
                                    text = app,
                                    modifier = Modifier.padding(vertical = 8.dp),
                                    color = Color.White,
                                    fontSize = 18.sp,

                                    )
                            }
                        }
                    }
                }


            Spacer(modifier = Modifier.height(4.dp))
            TabsAndContent(pagerState)
        }
    }
}

@Composable
fun TabsAndContent(pagerState: PagerState) {
    val coroutineScope = rememberCoroutineScope()
    val tabs = listOf("Apps", "Package")

    Column(modifier = Modifier.fillMaxSize()) {
        // Tabs Row
        TabRow(selectedTabIndex = pagerState.currentPage,
            containerColor = Color.Transparent) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = pagerState.currentPage == index,
                    onClick = { coroutineScope.launch { pagerState.animateScrollToPage(index) } },
                    text = { Text(title, color = Color.White) }
                )
            }
        }

        // Pager Content
        HorizontalPager(
            //count = tabs.size,

            state = pagerState,
            modifier = Modifier.fillMaxSize()
                .padding(16.dp,0.dp)
        ) { page ->
            when (page) {
                0 -> TestAppBody() // Apps Tab
                1 -> TestAppBodyPackage() // Favorites Tab Example
            }
        }
    }
}

@Composable
fun TestAppBody(vm: TestAppVm = hiltViewModel()) {

    val appListState by vm.appListState.collectAsState()

    // Trigger the API call
    LaunchedEffect(Unit) {
        vm.fetchAppList(378)
    }

    when (appListState) {
        is AppListState.Loading -> {
            // Show a centered loading indicator
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        is AppListState.Success -> {
            val appList = (appListState as AppListState.Success).appList
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp)
            ) {
                items(appList) { app ->
                    AppCard(appName = app.app_name, appIcon = app.app_icon)
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }

        is AppListState.Error -> {
            val message = (appListState as AppListState.Error).message
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = "Error: $message", color = Color.Red, fontSize = 16.sp)
            }
        }
    }
}


@Composable
fun TestAppBodyPackage(vm: TestAppVm = hiltViewModel()) {

    val appListState by vm.appListState.collectAsState()

    // Trigger the API call
    LaunchedEffect(Unit) {
        vm.fetchAppList(378)
    }

    when (appListState) {
        is AppListState.Loading -> {
            // Show a centered loading indicator
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        is AppListState.Success -> {
            val appList = (appListState as AppListState.Success).appList
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp)
            ) {
                items(appList) { app ->
                    AppCard(appName = app.app_package_name, appIcon = app.app_icon)
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }

        is AppListState.Error -> {
            val message = (appListState as AppListState.Error).message
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = "Error: $message", color = Color.Red, fontSize = 16.sp)
            }
        }
    }
}

@Composable
fun AppCard(appName: String, appIcon: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(8.dp)
        ) {
            // App Icon
            Image(
                painter = rememberAsyncImagePainter(appIcon),
                contentDescription = "App Icon",
                modifier = Modifier
                    .size(64.dp)
                    .padding(8.dp),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(8.dp))

            // App Name
            Text(
                text = appName,
                fontSize = 18.sp,
                color = Color.Black
            )
        }
    }
}


@Composable
fun HeaderSection() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Avatar
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .background(Color.White, CircleShape)
            ){
                Image(
                    painter = painterResource(id = R.drawable.apps_image),
                    contentDescription = "Avatar",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.FillBounds)
            }

        Spacer(modifier = Modifier.width(18.dp))

        // Title
        Text(
            text = "App List",
            fontSize = 24.sp,
            color = Color.White
        )
    }
}

@Composable
fun SearchBar(searchText: MutableState<String>, appList: MutableState<List<String>>) {
    OutlinedTextField(
        value = searchText.value,
        onValueChange = { query ->
            searchText.value = query
            appList.value = filterAppList(query)
        },
        label = { Text("Search Apps") },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedTextColor = Color.White,
            focusedTextColor = Color.White,
            focusedBorderColor = Color.White,
            focusedLabelColor = Color.White,
            unfocusedBorderColor = Color.White,
            unfocusedLabelColor = Color.White,
            unfocusedLeadingIconColor = Color.White
        ),
        singleLine = true
    )
}

fun sampleAppList(): List<String> {
    return listOf(
        "Amazon", "Apple", "Adobe", "Airbnb", "Asana", "BBC", "Binance", "Bing", "Discord", "Dropbox"
    )
}

fun filterAppList(query: String): List<String> {
    val originalList = sampleAppList()
    return if (query.isEmpty()) {
        originalList
    } else {
        originalList.filter { it.startsWith(query, ignoreCase = true) }
    }
}

@Preview
@Composable
fun TestAppScreenPreview() {
    TestAppScreen()
}
