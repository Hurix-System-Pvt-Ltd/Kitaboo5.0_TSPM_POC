package com.hurix.kitaboocloud.kitaboosdkrenderer

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class MediaBookItem(val bookId:Long, val bookType:String, val bookVersion:String, val isbn:String, val isAudio:Boolean, val audioImageUrl:String): Parcelable
