package com.whatevervalue.myquis.Model

import org.json.JSONArray
import org.json.JSONObject

class ParsePlantUtility {

    fun parsePlantObjectFromJSONData(): List<Plant>? {

        val allPlantObjects: ArrayList<Plant> = ArrayList()
        val downloadingObject = DownloadingObject()
        val topLevelPlantJSONData = downloadingObject.downloadJSONDataFromLink("http://plantplaces.com/perl/mobile/flashcard.pl")

        val topLevelJSONObject = JSONObject(topLevelPlantJSONData)
        val plantObjectsArray: JSONArray = topLevelJSONObject.getJSONArray("values")

        var index = 0

        while (index < plantObjectsArray.length()) {
            val plantObject = Plant()
            val jsonObject = plantObjectsArray.getJSONObject(index)

            with(jsonObject) {
                plantObject.genus = getString("genus")
                plantObject.species = getString("species")
                plantObject.cultivar = getString("cultivar")
                plantObject.common = getString("common")
                plantObject.picture_name = getString("picture_name")
                plantObject.description = getString("description")
                plantObject.difficulty = getInt("difficulty")
                plantObject.id = getInt("id")
            }
            allPlantObjects.add(plantObject)

            index++
        }
        return allPlantObjects
    }


}