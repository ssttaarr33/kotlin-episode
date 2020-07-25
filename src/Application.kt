package com.example

import com.google.gson.Gson
import sun.net.www.protocol.http.HttpURLConnection
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL
import Beer
import com.google.gson.reflect.TypeToken

fun main(args: Array<String>) {
    val url = URL("http://ontariobeerapi.ca/beers/")
    with(url.openConnection() as HttpURLConnection) {
        requestMethod = "GET"  // optional default is GET

        println("\nSent 'GET' request to URL : $url; Response Code : $responseCode")

        BufferedReader(InputStreamReader(inputStream)).use {
            val response = StringBuffer()

            var inputLine = it.readLine()
            while (inputLine != null) {
                response.append(inputLine)
                inputLine = it.readLine()
            }
            it.close()
            println("Response : $response")

            val gson = Gson()
            var beerMap : HashMap<String, Int>
                    = HashMap<String, Int> ()
            val beerArrayType = object : TypeToken<Array<Beer>>(){}.type
            var beers:Array<Beer> = gson.fromJson(response.toString(),beerArrayType)
            beers.forEachIndexed{idx, beer ->
                println("> Beer ${idx}:\n ${beer.country}")
                if(beerMap.containsKey(beer.country))
                    beerMap.set(beer.country!!, beerMap.get(beer.country)!!.plus(1))
                else
                    beerMap.put(beer.country!!, 1)
            }
            val sortedMap = beerMap.toList().sortedByDescending { (_, value) -> value}.toMap()

            for(key in sortedMap.keys){
                println("Beers from country $key : ${beerMap[key]}")
            }
        }
    }
}

