package com.example.myapplicationretrofit

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.myapplicationretrofit.ui.theme.MyApplicationRetrofitTheme
import kotlinx.coroutines.launch
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationRetrofitTheme {

                val coroutineScope = rememberCoroutineScope()
                val retrofitInstance = RetrofitInstance.getInstance().create(MyApi::class.java)

                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ){
                    Button(
                        onClick = {
                            coroutineScope.launch {
                                val response = retrofitInstance.getPost1()
                                Log.d("MainActivity", response.body().toString())
                            }
                        }
                    ) {
                        Text(
                            text = "call api"
                        )
                    }
                }
            }
        }
    }
}

data class Post(
    val userId : Int,
    val id : Int,
    val title : String,
    val body : String
)

object RetrofitInstance{

    val BASE_URL = "https://jsonplaceholder.typicode.com/"

    val client = Retrofit
        .Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    fun getInstance() : Retrofit{
        return client
    }
}

interface MyApi{

    @GET("posts/1")
    suspend fun getPost1() : Response<Post>
}
@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MyApplicationRetrofitTheme {

    }
}