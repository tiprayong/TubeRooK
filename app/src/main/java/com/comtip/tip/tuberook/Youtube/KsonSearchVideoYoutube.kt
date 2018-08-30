package com.comtip.tip.tuberook.Youtube

import com.google.gson.annotations.SerializedName
import java.util.*

/**
 * Created by TipRayong on 18/4/2561 13:17
 * TubeRooK
 */
class KsonSearchVideoYoutube {
    @SerializedName("items")
    var items: List<Items> = ArrayList()

    data class Items(
            @SerializedName("id") val id: Id?,
            @SerializedName("snippet") val snippet:Snippet?
    )

    data class Id(
            @SerializedName("videoId") val videoId:String?
    )

    data class Snippet(
            @SerializedName("title") val title:String?
    )
}
