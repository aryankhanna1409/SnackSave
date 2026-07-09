package com.example.snacksave

import android.content.Context
import android.util.Log
import okhttp3.*
import org.json.JSONObject
import java.io.IOException

object CafeDetector {

    private const val GOOGLE_PLACES_API_KEY = "ENTER API KEY HERE"
    private const val RADIUS = 100  // meters

    fun checkNearbyCafes(
        context: Context,
        latitude: Double,
        longitude: Double,
        callback: (Boolean, String?) -> Unit
    ) {
        val url =
            "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=$latitude,$longitude&radius=$RADIUS&type=cafe&key=$GOOGLE_PLACES_API_KEY"

        val request = Request.Builder().url(url).build()
        val client = OkHttpClient()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("CafeDetector", "API call failed", e)
                callback(false, null)
            }

            override fun onResponse(call: Call, response: Response) {
                response.body?.let { responseBody ->
                    val json = JSONObject(responseBody.string())
                    val results = json.getJSONArray("results")
                    if (results.length() > 0) {
                        val firstCafe = results.getJSONObject(0)
                        val name = firstCafe.getString("name")
                        callback(true, name)
                    } else {
                        callback(false, null)
                    }
                } ?: callback(false, null)
            }
        })
    }
}
