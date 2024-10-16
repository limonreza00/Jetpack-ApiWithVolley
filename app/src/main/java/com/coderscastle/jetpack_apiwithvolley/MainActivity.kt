package com.coderscastle.jetpack_apiwithvolley

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.coderscastle.jetpack_apiwithvolley.ui.theme.JetpackApiWithVolleyTheme
import org.json.JSONArray
import org.json.JSONObject

class MainActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            JetpackApiWithVolleyTheme {

                VolleyJsonArrayDisplay()
            }
        }
    }
}


@Composable
fun VolleyJsonArrayDisplay() {
    val context = LocalContext.current
    var products by remember { mutableStateOf<List<JSONObject>>(emptyList()) }
    var errorMessage by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {

        val queue = Volley.newRequestQueue(context)

        val url = "https://fakestoreapiserver.reactbd.com/smart"

        val jsonArrayRequest = JsonArrayRequest(
            Request.Method.GET, url, null,
            { response: JSONArray ->
                products = List(response.length()) { i -> response.getJSONObject(i) }
            },
            { error ->
                errorMessage = "Error: ${error.message}"
            }
        )

        queue.add(jsonArrayRequest)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (products.isEmpty()) {
            if (errorMessage.isNotEmpty()) {
                Text(text = errorMessage)
            } else {
                Text(text = "Loading...")
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(products.size) { product ->
                    ProductCard(product = products[product])
                }
            }
        }
    }
}

@Composable
fun ProductCard(product: JSONObject) {

    val title = product.getString("title")
    val price = product.getDouble("price")
    val imageUrl = product.getString("image")

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(5.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            AsyncImage(
                model = imageUrl,
                contentDescription = title,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = title)
            Text(text = "Price: $$price")
        }
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun GreetingPreview() {
    JetpackApiWithVolleyTheme {
        VolleyJsonArrayDisplay()
    }
}