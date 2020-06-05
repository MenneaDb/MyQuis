package com.whatevervalue.myquis.Model

class Plant(genus: String, species: String, cultivar: String, common: String, picture_name: String, description: String, difficulty: Int, id: Int= 0) {

    constructor() : this("", "", "", "", "", "", 0, 0)


    private var _plantName: String? = null

    var plantName: String?
        get() = _plantName
        set(value){
        _plantName = value

    }
}