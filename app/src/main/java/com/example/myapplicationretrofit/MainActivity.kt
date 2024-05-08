package com.example.myapplicationretrofit

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.myapplicationretrofit.ui.theme.MyApplicationRetrofitTheme
import kotlinx.coroutines.launch
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationRetrofitTheme {
                InsertInputData()
//                val coroutineScope = rememberCoroutineScope()
//                val retrofitInstance = RetrofitInstance.getInstance().create(MyApi::class.java)
//
//                Box(
//                    modifier = Modifier.fillMaxSize(),
//                    contentAlignment = Alignment.Center
//                ){
//                    Button(
//                        onClick = {
//                            coroutineScope.launch {
//                                val response = retrofitInstance.getPost1()
//                                Log.d("MainActivity", response.body().toString())
//                            }
//                        }
//                    ) {
//                        Text(
//                            text = "call api"
//                        )
//                    }
//                }
            }
        }
    }

    @Composable
    fun InsertInputData(){

        //입력하는 숫자
        var inputNumber by remember{ mutableStateOf("") }

        //받아온 데이터 모델
        var post by remember { mutableStateOf<Post?>(null) }

        // API 통신
        val coroutineScope = rememberCoroutineScope()

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TextField(
                value = inputNumber,
                onValueChange = {inputNumber = it},
                label = { Text(text = "input number")}
            )
            Button(
                onClick = {
                    val number = inputNumber.toIntOrNull()
                    if(number != null){
                        coroutineScope.launch {
                            post = getPostData(number)
                        }
                    }
                }
            ) {
                Text(text = "getData")
            }

            post?.let{
                Text(text = "userId : " + it.userId)
                Text(text = "ID : " + it.id)
                Text(text = "Title : " + it.title)
                Text(text = "Body : " + it.body)
            }
        }
    }

    private suspend fun getPostData(number : Int) : Post? {
        val retrofitInstance = RetrofitInstance.getInstance().create(MyApi::class.java)
        val response : Response<Post> = retrofitInstance.getPostNumber(number)

        return if (response.isSuccessful) response.body() else null
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

    @GET("posts/{number}")
    suspend fun getPostNumber(
        @Path("number") number: Int
    ): Response<Post>
}
@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MyApplicationRetrofitTheme {

    }
}