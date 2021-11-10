package com.example.myapplication.downloadablefonts

import java.lang.StringBuilder

internal class QueryBuilder(
    val familyName: String,
    val width: Float? = null,
    val weight: Int? = null,
    val italic: Float? = null,
    val bestefoort: Boolean? = null
) {
    fun build(): String {
        if (weight == null && width == null && italic == null && bestefoort == null) {
            return familyName
        }
        val builder = StringBuilder()
        builder.append("name=").append(familyName)
        weight?.let { builder.append("&weight=").append(weight) }
        width?.let { builder.append("&width=").append(width) }
        italic?.let { builder.append("&italic=").append(italic) }
        bestefoort?.let { builder.append("&besteffort=").append(bestefoort) }
        return builder.toString()
    }

}