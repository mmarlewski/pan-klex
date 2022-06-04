package com.marcin.panklex

class JsonLevel
{
    var name = ""
    var floors = 0
    var width = 0
    var height = 0
    var blocks = arrayOf<Array<Array<Int>>>()
}

class KlexLevel
{
    var name = ""
    var floors = 0
    var height = 0
    var width = 0
    var blocks = mutableListOf<List<List<Int>>>()
}
