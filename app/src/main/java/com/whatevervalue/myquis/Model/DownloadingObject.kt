package com.whatevervalue.myquis.Model

import java.io.BufferedInputStream
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.lang.StringBuilder
import java.net.HttpURLConnection
import java.net.URL

class DownloadingObject {

        @Throws(IOException::class)
        fun downloadJSONDataFromLink(link: String): String {
            val stringBuilder = StringBuilder()

            val url = URL(link)
            val urlConnection = url.openConnection() as HttpURLConnection
            try{
                val bufferedInputString = BufferedInputStream(urlConnection.inputStream)
                val bufferedReader = BufferedReader(InputStreamReader(bufferedInputString))
               //temporary string to hold each line read from the Buffered Reader

                var inputLineString: String?
                inputLineString = bufferedReader.readLine()
                while (inputLineString !=null){

                    stringBuilder.append(inputLineString)
                    inputLineString = bufferedReader.readLine()
                }

            }finally {
                //regardless of success or failure, we will disconnect from the URLConnection
            urlConnection.disconnect()
            }
            return stringBuilder.toString()
        }




}