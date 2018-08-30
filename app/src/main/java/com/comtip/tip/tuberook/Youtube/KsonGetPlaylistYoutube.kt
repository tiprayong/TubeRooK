package com.comtip.tip.tuberook.Youtube

import com.google.gson.annotations.SerializedName

/**
 * Created by TipRayong on 18/4/2561 13:45
 * TubeRooK
 * field:
 */
class KsonGetPlaylistYoutube(@SerializedName("items") val items: List<Items>,
                             @SerializedName("nextPageToken") val nextPageToken: String?,
                             @SerializedName("pageInfo") val pageInfo: PageInfo) {

    data class PageInfo(
            @SerializedName("totalResults") val totalResult: String?
    )

    data class Items(
            @SerializedName("snippet")
            val snippet: Snippet?
    )

    data class Snippet(
            @SerializedName("title") val title: String?,
            @SerializedName("resourceId") val resourceId: ResourceId?
    )

    data class ResourceId(
            @SerializedName("videoId") val videoId: String?
    )
}

