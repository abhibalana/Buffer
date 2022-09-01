package com.example.buffer.Models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TopSongs(

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("type")
	val type: String? = null,

	@field:SerializedName("permalinkUrl")
	val permalinkUrl: String? = null,

	@field:SerializedName("tracks")
	val tracks: Tracks? = null,

	@field:SerializedName("status")
	val status: Boolean? = null
) : Parcelable

@Parcelize
data class SearchResponse(

	@field:SerializedName("genreStats")
	val genreStats: List<GenreStatsItem?>? = null,

	@field:SerializedName("totalCount")
	val totalCount: Int? = null,

	@field:SerializedName("tracks")
	val tracks: Tracks? = null,

	@field:SerializedName("status")
	val status: Boolean? = null
):Parcelable




@Parcelize
data class GenreStatsItem(

	@field:SerializedName("count")
	val count: Int? = null,

	@field:SerializedName("value")
	val value: String? = null
):Parcelable

@Parcelize
data class ItemsItem(

	@field:SerializedName("durationText")
	val durationText: String? = null,

	@field:SerializedName("caption")
	val caption: String? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("likeCount")
	val likeCount: Int? = null,

	@field:SerializedName("type")
	val type: String? = null,

	@field:SerializedName("title")
	val title: String? = null,

	@field:SerializedName("purchaseUrl")
	val purchaseUrl: String? = null,

	@field:SerializedName("createdAt")
	val createdAt: String? = null,

	@field:SerializedName("waveformUrl")
	val waveformUrl: String? = null,

	@field:SerializedName("genre")
	val genre: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("repostCount")
	val repostCount: Int? = null,

	@field:SerializedName("labelName")
	val labelName: String? = null,

	@field:SerializedName("commentable")
	val commentable: Boolean? = null,

	@field:SerializedName("visuals")
	val visuals: List<String?>? = null,

	@field:SerializedName("releaseDate")
	val releaseDate: String? = null,

	@field:SerializedName("artworkUrl")
	val artworkUrl: String? = null,

	@field:SerializedName("goPlus")
	val goPlus: Boolean? = null,

	@field:SerializedName("stationPermalink")
	val stationPermalink: String? = null,

	@field:SerializedName("purchaseTitle")
	val purchaseTitle: String? = null,

	@field:SerializedName("commentCount")
	val commentCount: Int? = null,

	@field:SerializedName("tags")
	val tags: List<String?>? = null,

	@field:SerializedName("license")
	val license: String? = null,

	@field:SerializedName("playCount")
	val playCount: Int? = null,

	@field:SerializedName("publisher")
	val publisher: Publisher? = null,

	@field:SerializedName("lastModified")
	val lastModified: String? = null,

	@field:SerializedName("permalink")
	val permalink: String? = null,

	@field:SerializedName("durationMs")
	val durationMs: Int? = null,

	@field:SerializedName("user")
	val user: User? = null
) : Parcelable

@Parcelize
data class Publisher(

	@field:SerializedName("writerComposer")
	val writerComposer: String? = null,

	@field:SerializedName("cLine")
	val cLine: String? = null,

	@field:SerializedName("artist")
	val artist: String? = null,

	@field:SerializedName("upcOrEan")
	val upcOrEan: String? = null,

	@field:SerializedName("albumTitle")
	val albumTitle: String? = null,

	@field:SerializedName("isrc")
	val isrc: String? = null,

	@field:SerializedName("pLine")
	val pLine: String? = null
) : Parcelable

@Parcelize
data class User(

	@field:SerializedName("avatarUrl")
	val avatarUrl: String? = null,

	@field:SerializedName("city")
	val city: String? = null,

	@field:SerializedName("verified")
	val verified: Boolean? = null,

	@field:SerializedName("type")
	val type: String? = null,

	@field:SerializedName("pro")
	val pro: Boolean? = null,

	@field:SerializedName("stationPermalink")
	val stationPermalink: String? = null,

	@field:SerializedName("proUnlimited")
	val proUnlimited: Boolean? = null,

	@field:SerializedName("countryCode")
	val countryCode: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("lastModified")
	val lastModified: String? = null,

	@field:SerializedName("permalink")
	val permalink: String? = null,

	@field:SerializedName("followerCount")
	val followerCount: Int? = null
) : Parcelable

@Parcelize
data class Tracks(

	@field:SerializedName("nextOffset")
	val nextOffset: Int? = null,

	@field:SerializedName("items")
	val items: List<ItemsItem?>? = null
) : Parcelable


@Parcelize
data class TrackResponse(

	@field:SerializedName("durationText")
	val durationText: String? = null,

	@field:SerializedName("caption")
	val caption: String? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("likeCount")
	val likeCount: Int? = null,

	@field:SerializedName("type")
	val type: String? = null,

	@field:SerializedName("title")
	val title: String? = null,

	@field:SerializedName("purchaseUrl")
	val purchaseUrl: String? = null,

	@field:SerializedName("createdAt")
	val createdAt: String? = null,

	@field:SerializedName("waveformUrl")
	val waveformUrl: String? = null,

	@field:SerializedName("genre")
	val genre: String? = null,

	@field:SerializedName("audio")
	val audio: List<AudioItem?>? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("repostCount")
	val repostCount: Int? = null,

	@field:SerializedName("labelName")
	val labelName: String? = null,

	@field:SerializedName("commentable")
	val commentable: Boolean? = null,

	@field:SerializedName("visuals")
	val visuals: List<String?>? = null,

	@field:SerializedName("releaseDate")
	val releaseDate: String? = null,

	@field:SerializedName("artworkUrl")
	val artworkUrl: String? = null,

	@field:SerializedName("goPlus")
	val goPlus: Boolean? = null,

	@field:SerializedName("stationPermalink")
	val stationPermalink: String? = null,

	@field:SerializedName("purchaseTitle")
	val purchaseTitle: String? = null,

	@field:SerializedName("commentCount")
	val commentCount: Int? = null,

	@field:SerializedName("tags")
	val tags: List<String?>? = null,

	@field:SerializedName("license")
	val license: String? = null,

	@field:SerializedName("playCount")
	val playCount: Int? = null,

	@field:SerializedName("publisher")
	val publisher: Publisher? = null,

	@field:SerializedName("lastModified")
	val lastModified: String? = null,

	@field:SerializedName("permalink")
	val permalink: String? = null,

	@field:SerializedName("durationMs")
	val durationMs: Int? = null,

	@field:SerializedName("user")
	val user: User? = null,

	@field:SerializedName("status")
	val status: Boolean? = null
) : Parcelable



@Parcelize
data class AudioItem(

	@field:SerializedName("durationText")
	val durationText: String? = null,

	@field:SerializedName("extension")
	val extension: String? = null,

	@field:SerializedName("mimeType")
	val mimeType: String? = null,

	@field:SerializedName("durationMs")
	val durationMs: Int? = null,

	@field:SerializedName("url")
	val url: String? = null,

	@field:SerializedName("quality")
	val quality: String? = null
) : Parcelable
