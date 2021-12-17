package com.example.fruitsapp

import android.provider.SyncStateContract
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import okhttp3.OkHttpClient
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.lang.Exception
import java.util.ArrayList
import java.util.concurrent.TimeUnit

public class SharedViewModel: ViewModel() {

    val BASE_URL = "https://dev-api.com/"

    private var fruitListLd: MutableLiveData<List<Fruit?>>? = null
    private var fruitLd: MutableLiveData<Fruit?>? = MutableLiveData<Fruit?>()
    private var isLoadedLd = MutableLiveData<Boolean>()
    private var isErrorLd = MutableLiveData<Boolean>()



    fun getFruitListLd(): MutableLiveData<List<Fruit?>>? {
        if (fruitListLd == null) {
            fruitListLd = MutableLiveData()
        }
        return fruitListLd
    }


    fun getFruitLd(): MutableLiveData<Fruit?>? {
        if (fruitLd == null) {
            fruitLd = MutableLiveData()
        }
        return fruitLd
    }

    fun getIsLoadedLd(): MutableLiveData<Boolean>? {
        if (isLoadedLd == null) {
            isLoadedLd = MutableLiveData()
        }
        return isLoadedLd
    }


    fun getIsErrorLd(): MutableLiveData<Boolean>? {
        if (isErrorLd == null) {
            isErrorLd = MutableLiveData()
        }
        return isErrorLd
    }

    fun setFruitLd(fruit: Fruit){
        fruitLd?.value= fruit
    }


    fun getFruits(){

        isLoadedLd.value= false
        isErrorLd.value= false

        val client = OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS).build()

        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()


        val apiFruitsInterface: ApiFruitsIntarface = retrofit.create(ApiFruitsIntarface::class.java)

        val userCall: Call<String?>? = apiFruitsInterface.getFruits()

        userCall!!.enqueue(object : Callback<String?> {
            override fun onResponse(call: Call<String?>, response: Response<String?>) {

                try {
                    isLoadedLd.value= true

                    if(response.code()==200){
                        isErrorLd.value= false
                        val jsonObject = JSONObject(response.body())
                        if(jsonObject.has("fruits")){
                            val jsonArrayFruits: JSONArray? = jsonObject.getJSONArray("fruits")
                            if(jsonArrayFruits!=null && jsonArrayFruits.length()>0){
                                val fruitList= ArrayList<Fruit>()
                                for(i in 0 until  jsonArrayFruits.length()) {
                                    try {
                                        val fruit= Fruit()
                                        var jsoObjectFruit: JSONObject? = jsonArrayFruits.getJSONObject(i)
                                        if(jsoObjectFruit!=null){
                                            if(jsoObjectFruit.has("name")){
                                                val name= jsoObjectFruit.getString("name")
                                                fruit.name= name
                                            }
                                            if(jsoObjectFruit.has("image")){
                                                val image= jsoObjectFruit.getString("image")
                                                fruit.imageUrl= image

                                            }
                                            if(jsoObjectFruit.has("description")){
                                                val description= jsoObjectFruit.getString("description")
                                                fruit.description= description
                                            }
                                            if(jsoObjectFruit.has("name")){
                                                val price= jsoObjectFruit.getDouble("price")
                                                fruit.price= price
                                            }

                                            fruitList.add(fruit)
                                        }
                                    } catch (e: Exception) {
                                    }
                                }
                                fruitListLd!!.value= fruitList
                            }
                        }
                    }
                    else{
                        isErrorLd.value= true
                    }
                } catch (e: Exception) {
                    isLoadedLd.value= true
                    isErrorLd.value= true
                }

            }

            override fun onFailure(call: Call<String?>, t: Throwable) {
                isLoadedLd.value= true
                isErrorLd.value= true
            }
        })
    }
}