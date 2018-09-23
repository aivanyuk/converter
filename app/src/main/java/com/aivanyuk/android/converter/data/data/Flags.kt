package com.aivanyuk.android.converter.data.data

object Flags {
    fun get(key: String): String {
        val shortName = key.substring(0, 2).toLowerCase()
        return "http://flagpedia.net/data/flags/normal/$shortName.png"
    }

    val MAP = mapOf(
            "AUD" to "au",
            "BGN" to "bg",
            "BRL" to "br"
    )
}
