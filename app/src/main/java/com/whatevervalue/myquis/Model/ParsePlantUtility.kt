package com.whatevervalue.myquis.Model

import org.json.JSONArray
import org.json.JSONObject

class ParsePlantUtility {


    fun parsePlantObjectFromJSONData(search: String?) : List<Plant>? {

        var allPlantObjects: ArrayList<Plant> = ArrayList()
        var downloadingObject = DownloadingObject()
        var topLevelPlantJSONData = downloadingObject.downloadJSONDataFromLink("http://plantplaces.com/perl/mobile/flashcard.pl")

        var topLevelJSONObject: JSONObject = JSONObject(topLevelPlantJSONData)
        var plantObjectsArray: JSONArray = topLevelJSONObject.getJSONArray("values")

        var index: Int = 0

        while (index < plantObjectsArray.length()) {
            var plantObject: Plant = Plant()
            var jsonObject = plantObjectsArray.getJSONObject(index)
            /*
            * genus species cultivar common pictureName description difficulty id
            */


            plantObject

            index++
        }

    }


}