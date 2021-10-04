package com.nlp.aclpkotlin

import java.util.*

class KVObject {
    var id = UUID.randomUUID().toString()
    var key: Int = 0
    lateinit var value: String
    lateinit var type: String

    //for testing
    fun retrieveKey(): Int{
        return key
    }
}