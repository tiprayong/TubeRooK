package com.comtip.tip.tuberook

import com.comtip.tip.tuberook.RoomDatabase.YoutubeDataView

/**
 * Created by TipRayong on 14/4/2561 14:20
 * TubeRooK
 */
const val YOUTUBE_API_KEY = "YOUTUBE_API_KEY"
const val VIDEO = "VIDEO"
const val PLAYLIST = "PLAYLIST"
const val LIVE = "LIVE"
const val FIRST = "FIRST"
const val LAST = "LAST"
const val ALPHABET = "ALPHABET"
const val BY_DATE = "&order=date"
const val BY_COUNT = "&order=videoCount"
const val BY_VIEW = "&order=viewCount"

var  presentMode:String = "";
var  lastPlay:String = "";
var  sleepTime:Int = 60;
var  setSleep:Boolean = false;

lateinit var youtubeDataView: YoutubeDataView