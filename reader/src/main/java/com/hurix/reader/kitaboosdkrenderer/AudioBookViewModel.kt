package com.hurix.reader.kitaboosdkrenderer
import androidx.lifecycle.ViewModel
import java.util.*
import kotlin.collections.ArrayList

class AudioBookViewModel : ViewModel() {

    // calculates time for audio
    fun stringForTime(timeMs: Int): String? {
        val mFormatter: Formatter
        val mFormatBuilder: StringBuilder = StringBuilder()
        mFormatter = Formatter(mFormatBuilder, Locale.getDefault())
        val totalSeconds = timeMs / 1000
        val seconds = totalSeconds % 60
        val minutes = totalSeconds / 60 % 60
        val hours = totalSeconds / 3600
        mFormatBuilder.setLength(0)
        return if (hours > 0) {
            mFormatter.format("%d:%02d:%02d", hours, minutes, seconds).toString()
        } else {
            mFormatter.format("%02d:%02d", minutes, seconds).toString()
        }
    }


}
