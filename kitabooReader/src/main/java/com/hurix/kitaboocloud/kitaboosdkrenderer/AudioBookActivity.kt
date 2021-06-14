package com.hurix.kitaboocloud.kitaboosdkrenderer


import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.content.res.Resources
import android.database.ContentObserver
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.text.TextUtils
import android.view.Gravity
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.viewpager.widget.ViewPager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.hurix.bookreader.utils.Typefaces
import com.hurix.bookreader.views.audiobook.CustomAudioBookPlayers
import com.hurix.bookreader.views.audiobook.adapter.AudioBookPagerAdapter
import com.hurix.bookreader.views.audiobook.controllers.*
import com.hurix.bookreader.views.audiobook.model.*
import com.hurix.bookreader.views.audiobook.services.AudioBookService.Companion.ACTIVITY_UPDATE_ACTION
import com.hurix.bookreader.views.audiobook.theme.AudioVideoBookThemeController
import com.hurix.bookreader.views.audiobook.theme.AudioVideoBookThemeVo
import com.hurix.commons.KitabooSDKModel
import com.hurix.commons.sdkDatamodel.SDKManager
import com.hurix.customui.datamodel.BookMarkVO

import com.hurix.exoplayer3.util.Log
import com.hurix.kitaboo.constants.Constants
import com.hurix.kitaboo.constants.ShelfUIConstants
import com.hurix.kitaboo.constants.dialog.DialogUtils
import com.hurix.kitaboo.constants.utils.Utils
import com.hurix.kitaboocloud.PlayerController
import com.hurix.kitaboocloud.R

import com.hurix.kitaboocloud.kitaboosdkrenderer.sdkUtils.NetworkChangeReceiver
import com.hurix.kitaboocloud.notifier.GlobalDataManager
import com.hurix.service.Interface.IServiceResponse
import com.hurix.service.Interface.IServiceResponseListener
import com.hurix.service.datamodel.UGCFetchResponseObject
import com.hurix.service.exception.ServiceException
import com.hurix.service.response.BookDownloadServiceResponse
import com.hurix.service.response.ToCTimeIndexResponse
import kotlinx.android.synthetic.main.activity_audio_book_scroll.*
import kotlinx.android.synthetic.main.activity_audio_book_scroll.tv_check_connection
import kotlinx.android.synthetic.main.activity_audio_book_tab.*
import kotlinx.android.synthetic.main.activity_audio_book_tab.audioBookPager
import kotlinx.android.synthetic.main.activity_audio_book_tab.backRelativeLayout
import kotlinx.android.synthetic.main.activity_audio_book_tab.backTextViewView
import kotlinx.android.synthetic.main.activity_audio_book_tab.playerControllerRelativeLayout
import kotlinx.android.synthetic.main.activity_audio_book_tab.progressPreview
import kotlinx.android.synthetic.main.activity_audio_book_tab.tabLineView
import kotlinx.android.synthetic.main.activity_audio_book_tab.tabs
import kotlinx.android.synthetic.main.activity_audio_book_tab.tabsRelativeLayout
import kotlinx.android.synthetic.main.activity_audio_book_tab.toplayout
import kotlinx.android.synthetic.main.activity_audio_book_tab.tvSleepTimer
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.nio.charset.StandardCharsets
import java.util.*
import kotlin.collections.ArrayList


open class AudioBookActivity : AppCompatActivity(), IHDAudioTOCControllerCallBacks,
        IBookMarkCallBacks, IHDNarrationSpeedCallBack, IHDKitabooAudioControllerCallBack,
        HDAudioBookPlayer, ServiceCompletedListener, IServiceResponseListener, HDKitabooAudioBookController.OnSleepTimerSetListener {
    private var currentMediaPosition: Int = 0
    private var mediaBookItemList = java.util.ArrayList<MediaBookItem>()
    private lateinit var mNetworkReceiver: NetworkChangeReceiver
    private var accountType: String = ""
    private var folioTimeIndex: ArrayList<FolioTimeIndex>? = null
    private lateinit var bookPagerAdapter: AudioBookPagerAdapter
    private var customAudioBookPlayer: CustomAudioBookPlayers? = null


    private var isPlayNextPrev = 0;
    private lateinit var mAudioUrl: String
    private var audioImageUrl: String? = null
    private var bookId: Long = 0
    private var userId: Long = 0
    private var token: String? = null
    private var bookVersion: String? = null
    private var isbn: String = ""
    private var lastPlayedPosition = 0
    private var refreshPlayerFilter: IntentFilter? = null
    private var mLocale: Locale? = null
    private lateinit var mServicehandler: ServiceHandler

    private var isAudio = false
    private var isOnlinePlaying = false
    private var isFullScreen = 0
    private var isAlreadyLand = false
    private var isVideoPlayingSong = false
    private var videoSpeedPosition: Int = -1
    private lateinit var audioVideoBookTheme: AudioVideoBookThemeVo

    private lateinit var hdAutoBookModel: HDAudioBookModel
    private lateinit var hdVideoBookModel: HDVideoBookModel

    private var contentServerUrl: String? = ""
    private var alarmManager: AlarmManager? = null
    private var thumb = ""
    private var parentBookDir: File? = null
    private lateinit var typeFace: Typeface
    private val refreshReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {

            intent?.extras?.let {
                val playPause = it.getBoolean("isPlayPause")
                val playPauseAction = it.getBoolean("isPlayPauseAction")
                val seekTime = it.getLong("SeekValue")
                val nextPreviousIndex = it.getInt("nextPreviousIndex")

                customAudioBookPlayer!!.updatePlayAndProgress(playPauseAction, playPause, seekTime, nextPreviousIndex)
            }

        }
    }

    open fun setLocale(localeName: String?) {
        mLocale = Locale(localeName)
        val res = resources
        val dm = res.displayMetrics
        val conf = res.configuration
        conf.locale = mLocale
        res.updateConfiguration(conf, dm)
        //onConfigurationChanged(conf);
        Utils.insertSharedPrefernceStringValues(this,
                Constants.SHELF_PREFS_NAME, Constants.DEFAULT_APP_LANGUAGE, localeName)
    }

    private fun setMediaBookLayouts() {

        val mode: Int = resources.configuration.orientation
        if (Utils.isMobile(this@AudioBookActivity)) {
            if (mode == Configuration.ORIENTATION_LANDSCAPE) {
                if (isAudio)
                    setContentView(R.layout.activity_audio_book__mob_land)
                else
                    setContentView(R.layout.activity_video_book_mob_land)
            } else {
                setContentView(R.layout.activity_audio_book_scroll)
            }
        } else {
            setContentView(R.layout.activity_audio_book_tab)
        }

        updateLayout()
    }


    private fun updateLayout() {
        if (isAudio) {
            toplayout.visibility = View.VISIBLE
        } else {
            toplayout.visibility = View.GONE
            val displayMode: Int = resources.configuration.orientation

            if (!Utils.isMobile(this@AudioBookActivity) && displayMode == Configuration.ORIENTATION_LANDSCAPE && isFullScreen == 1) {
                setOrientationToLand()
            } else if (!Utils.isMobile(this@AudioBookActivity) && displayMode == Configuration.ORIENTATION_LANDSCAPE) {
                toplayout.visibility = View.VISIBLE
            } else if (Utils.isMobile(this@AudioBookActivity) && displayMode == Configuration.ORIENTATION_LANDSCAPE) {
                toplayout.visibility = View.VISIBLE
            } else if (Utils.isMobile(this@AudioBookActivity) && displayMode == Configuration.ORIENTATION_PORTRAIT) {

                tabsRelativeLayout.visibility = View.VISIBLE
                toplayout.visibility = View.GONE

                val playerLayoutParams = playerControllerRelativeLayout.layoutParams as LinearLayout.LayoutParams
                playerLayoutParams.weight = 5.5f
                playerLayoutParams.height = LinearLayout.LayoutParams.WRAP_CONTENT
                playerLayoutParams.width = LinearLayout.LayoutParams.MATCH_PARENT

                playerControllerRelativeLayout.layoutParams = playerLayoutParams

                val tocLayoutParams = tabsRelativeLayout.layoutParams as LinearLayout.LayoutParams
                tocLayoutParams.weight = 4.5f
                tocLayoutParams.height = LinearLayout.LayoutParams.WRAP_CONTENT
                tocLayoutParams.width = LinearLayout.LayoutParams.MATCH_PARENT
                tabsRelativeLayout.layoutParams = tocLayoutParams

                tabsRelativeLayout.visibility = View.VISIBLE
                toplayout.visibility = View.VISIBLE
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        android.util.Log.e("TAG", "AudioActivity onCreate: ")
        com.hurix.epubreader.Utility.Utils.setSecureWindowFlags(this@AudioBookActivity)


        if (resources.getBoolean(R.bool.is_multi_lang_support)) {
            setLocale(Utils.getSharedPreferenceStringValue(
                    this@AudioBookActivity, Constants.SHELF_PREFS_NAME, Constants.DEFAULT_APP_LANGUAGE, ""))
        }

        intent.extras?.let {

            mediaBookItemList = it.getParcelableArrayList<MediaBookItem>("MediaPlayList")!!
            if (mediaBookItemList.size == 0) {
                finish()
            }
            currentMediaPosition = savedInstanceState?.getInt("currentMediaPosition")
                    ?: it.getInt("MediaPosition")


            isAudio = mediaBookItemList[currentMediaPosition].isAudio
            audioImageUrl = mediaBookItemList[currentMediaPosition].audioImageUrl
            bookId = mediaBookItemList[currentMediaPosition].bookId
            bookVersion = mediaBookItemList[currentMediaPosition].bookVersion
            isbn = mediaBookItemList[currentMediaPosition].isbn

            userId = PlayerController.getInstance(this).userID
            token = PlayerController.getInstance(this).token
            accountType = PlayerController.getInstance(this).roleName

            KitabooSDKModel.getInstance().userID = userId
            KitabooSDKModel.getInstance().userToken = token
            SDKManager.getInstance().currentBookISBN = isbn
            HDKitabooAudioBookController.setBookId(bookId)
            HDKitabooAudioBookController.setUser(userId)
            HDKitabooAudioBookController.isAudio = isAudio
        }

        typeFace = Typefaces.get(this, resources.getString(R.string.kitaboo_bookshelf_font_file_name))

        HDKitabooAudioBookController.setTypeface(typeFace)
        setMediaBookLayouts()
        initUI()
        initCallbacks()

        mServicehandler = if (this.resources.getString(R.string.server_pointing).isEmpty()) {
            ServiceHandler(this, KitabooSDKModel.getInstance().clientID)
        } else {
            ServiceHandler(this, KitabooSDKModel.getInstance().clientID,
                    this.resources.getString(R.string.server_pointing))
        }

        audioVideoBookTheme = AudioVideoBookThemeController.getInstance(this@AudioBookActivity).audioVideoBookPlayerTheme
        isFullScreen = Utils.getSharedPreferenceIntValue(this@AudioBookActivity, Constants.SHELF_PREFS_NAME, "FulScreenStatus", 0)
        lastPlayedPosition = Utils.getSharedPreferenceIntValue(this, Constants.SHELF_PREFS_NAME, bookId.toString(), 0)
        playerControllerRelativeLayout.addView(getMediaPlayer())

        setTopPanelTheme(audioVideoBookTheme)

        if (savedInstanceState == null) {
            isPlayNextPrev = 0
            openBook(bookId, bookVersion!!, isbn, isAudio, audioImageUrl!!)
        }

    }


    private fun fetchUGCRequest() {

        mServicehandler.setServiceCompletedListener(this)
        mServicehandler.sendBackgroundServiceforUGC(bookId, userId, accountType, bookVersion, null, this)
    }


    private fun getMediaPlayer(): CustomAudioBookPlayers {
        if (customAudioBookPlayer == null)
            customAudioBookPlayer = CustomAudioBookPlayers(this, audioVideoBookTheme, isAudio, typeFace!!)
        return customAudioBookPlayer!!
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        android.util.Log.e("TAG", "AudioActivity onRestoreInstanceState: ")
        updateAndStartMediaPlayer(savedInstanceState)

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        if (this::mAudioUrl.isInitialized && !mAudioUrl.contentEquals("") && folioTimeIndex != null) {

            outState.putInt("speed", customAudioBookPlayer?.getSpeed()!!)
            outState.putSerializable("BookMarkList", customAudioBookPlayer?.getBookMarkList())
            outState.putBoolean("isPlaying", customAudioBookPlayer?.getPlaying()!!)
            outState.putInt("currentMediaPosition", currentMediaPosition);

            outState.putString("MediaUrl", mAudioUrl)
            outState.putString("MediaThumbnail", audioImageUrl)
            outState.putParcelableArrayList("folioTimeIndex", folioTimeIndex)
            outState.putBoolean("isOnlinePlaying", isOnlinePlaying)

            if (!isAudio) {

                val isVidPlaying = Utils.getSharedPreferenceBooleanValue(this@AudioBookActivity, "isVideoPlaying", false)
                outState.putBoolean("isPlaying", isVidPlaying)
                outState.putParcelable("hdVideoBookModel", hdVideoBookModel)
                outState.putInt("isFullScreen", isFullScreen)
            } else {
                outState.putParcelable("hdAutoBookModel", hdAutoBookModel)
            }
        }

    }


    private fun initCallbacks() {
        HDAudioTOCController.addCallBack(this)
        HDAudioBookMarkController.addCallBack(this)
        HDNarrationSpeedController.addCallBack(this)
        HDKitabooAudioBookController.addCallBack(this)
        HDKitabooAudioBookController.addPlayerCallBack(this)
    }


    private fun updateAndStartMediaPlayer(savedInstanceState: Bundle) {

        val audioUrl = savedInstanceState.getString("MediaUrl")
        if (!TextUtils.isEmpty(audioUrl) && savedInstanceState.getParcelableArrayList<FolioTimeIndex>("folioTimeIndex") != null) {
            var isPlayingSong: Int = -1
            var speedPosition: Int = -1
            var bookMarkVOs = ArrayList<BookMarkVO>()

            isPlayingSong = if (savedInstanceState.getBoolean("isPlaying")) 1 else 0
            speedPosition = savedInstanceState.getInt("speed")
            bookMarkVOs = savedInstanceState.getSerializable("BookMarkList") as ArrayList<BookMarkVO>

            mAudioUrl = savedInstanceState.getString("MediaUrl")!!
            audioImageUrl = savedInstanceState.getString("MediaThumbnail")
            isOnlinePlaying = savedInstanceState.getBoolean("isOnlinePlaying")

            folioTimeIndex = savedInstanceState.getParcelableArrayList<FolioTimeIndex>("folioTimeIndex") as ArrayList<FolioTimeIndex>
            lastPlayedPosition = Utils.getSharedPreferenceIntValue(this, Constants.SHELF_PREFS_NAME, bookId.toString(), 0)
            if (isAudio) {
                hdAutoBookModel = savedInstanceState.getParcelable<HDAudioBookModel>("hdAutoBookModel")!!
            } else {
                hdVideoBookModel = savedInstanceState.getParcelable<HDVideoBookModel>("hdVideoBookModel")!!
                isFullScreen = savedInstanceState.getInt("isFullScreen")
            }

            if (isAudio) {
                customAudioBookPlayer!!.initAudio(bookId, isOnlinePlaying, lastPlayedPosition, mAudioUrl, audioImageUrl, isPlayingSong, speedPosition, isAudio, bookMarkVOs, hdAutoBookModel, folioTimeIndex!!, typeFace!!)
            } else {
                customAudioBookPlayer!!.initVideo(currentMediaPosition == 0, (mediaBookItemList.size == 1 || currentMediaPosition == mediaBookItemList.size - 1), isPlayNextPrev, bookId, isOnlinePlaying, lastPlayedPosition, mAudioUrl, audioImageUrl, isPlayingSong, speedPosition, isFullScreen, isAudio, bookMarkVOs, hdVideoBookModel, folioTimeIndex!!, typeFace!!)
            }
            initViewPager()
        } else {
            isPlayNextPrev = 0
            openBook(bookId, bookVersion!!, isbn, isAudio, audioImageUrl!!)
        }
    }


    private fun initUI() {

        backTextViewView.typeface = typeFace
        backTextViewView.text = ShelfUIConstants.AUDIO_BOOK_BACK

        backTextViewView.setOnClickListener {

            HDKitabooAudioBookController.backButtonPressed()

        }

        tvSleepTimer.typeface = typeFace
        tvSleepTimer.text = ShelfUIConstants.AUDIO_BOOK_SLEEP_TIME


        tvSleepTimer.setOnClickListener {
            HDKitabooAudioBookController.onSleepTimerClicked(this)
        }

        //Lets checkout sleep timer if previously kept on
        val hrs = Utils.getSharedPreferenceIntValue(this,
                Constants.SLEEP_HOURS,
                Constants.SLEEP_HOURS, 0)
        val mins = Utils.getSharedPreferenceIntValue(this,
                Constants.SLEEP_MINS,
                Constants.SLEEP_MINS, 0)
        val seconds = Utils.getSharedPreferenceIntValue(this,
                Constants.SLEEP_SECS,
                Constants.SLEEP_SECS, 0)
        if (hrs != 0 || mins != 0 || seconds != 0) {
            customAudioBookPlayer?.setCountDownTimer(hrs, mins, seconds, false, this)
            Log.d("tick", "create")
            tvSleepTimer.setTextColor(ContextCompat.getColor(this, R.color.white))
            tvSleepTimer.background = ContextCompat.getDrawable(this, R.drawable.audio_sleep_timer_circle)
        } else {
            tvSleepTimer.setTextColor(ContextCompat.getColor(this, R.color.kitaboo_main_color))
            tvSleepTimer.background = null

        }

    }

    private fun setTopPanelTheme(audioVideoBookTheme: AudioVideoBookThemeVo) {

        HDKitabooAudioBookController.initMediaThemeData(audioVideoBookTheme)

        backTextViewView.setTextColor(Color.parseColor(audioVideoBookTheme.audioVideoBookPlayer.toppanel.iconsColor))
        backRelativeLayout.setBackgroundColor(Color.parseColor(audioVideoBookTheme.audioVideoBookPlayer.toppanel.background))


        // tabs theme

        tabsRelativeLayout.setBackgroundColor(Color.parseColor(audioVideoBookTheme.audioVideoBookPlayer.sidepanel.background))
        tabs.setBackgroundColor(Color.parseColor(audioVideoBookTheme.audioVideoBookPlayer.sidepanel.tabBg))
        tabLineView.setBackgroundColor(Color.parseColor(audioVideoBookTheme.audioVideoBookPlayer.sidepanel.tabBorder))

        tabs.setSelectedTabIndicatorColor(Color.parseColor(audioVideoBookTheme.audioVideoBookPlayer.sidepanel.selectedTabBorder))

        tabs.setTabTextColors(Color.parseColor(audioVideoBookTheme.audioVideoBookPlayer.sidepanel.tabTextColor), Color.parseColor(audioVideoBookTheme.audioVideoBookPlayer.sidepanel.selectedTextColor))


    }

    override fun tapOnChapter() {


    }

    override fun setOrientationLandHalf() {

        Utils.insertSharedPrefernceIntValues(this, Constants.SHELF_PREFS_NAME, "FulScreenStatus", 0)


        val topLayoutParams = toplayout.layoutParams as LinearLayout.LayoutParams
        // topLayoutParams.weight = 0.2f;
        topLayoutParams.height = LinearLayout.LayoutParams.WRAP_CONTENT
        topLayoutParams.width = LinearLayout.LayoutParams.MATCH_PARENT
        toplayout.layoutParams = topLayoutParams


        var playerLinearLayoutParams = playerLinearLayout.layoutParams as LinearLayout.LayoutParams
        playerLinearLayoutParams.weight = 9f
        playerLinearLayout.weightSum = 10f
        playerLinearLayoutParams.height = LinearLayout.LayoutParams.WRAP_CONTENT
        playerLinearLayoutParams.width = LinearLayout.LayoutParams.MATCH_PARENT
        playerLinearLayout.layoutParams = playerLinearLayoutParams


        val playerLayoutParams = playerControllerRelativeLayout.layoutParams as LinearLayout.LayoutParams
        playerLayoutParams.weight = 5.5f

        playerLayoutParams.height = LinearLayout.LayoutParams.WRAP_CONTENT
        playerLayoutParams.width = LinearLayout.LayoutParams.MATCH_PARENT

        playerControllerRelativeLayout.layoutParams = playerLayoutParams

        val tocLayoutParams = tabsRelativeLayout.layoutParams as LinearLayout.LayoutParams
        tocLayoutParams.weight = 4.5f
        tocLayoutParams.height = LinearLayout.LayoutParams.WRAP_CONTENT
        tocLayoutParams.width = LinearLayout.LayoutParams.MATCH_PARENT
        tabsRelativeLayout.layoutParams = tocLayoutParams


        //    playerControllerRelativeLayout.layoutParams = params

        tabsRelativeLayout.visibility = View.VISIBLE
        toplayout.visibility = View.VISIBLE
        isAlreadyLand = true
    }

    override fun setOrientationToLand() {

        // customAudioBookPlayer?.setFullScreenIconToZoomOut()
        // view is already landscape.. and we are making video as full mode

        val mode: Int = resources.configuration.orientation

        Utils.insertSharedPrefernceIntValues(this, Constants.SHELF_PREFS_NAME, "FulScreenStatus", 1)


        var params: LinearLayout.LayoutParams = playerControllerRelativeLayout.layoutParams as LinearLayout.LayoutParams
        params.width = RelativeLayout.LayoutParams.MATCH_PARENT
        params.height = RelativeLayout.LayoutParams.MATCH_PARENT

        params.weight = 10f

        playerControllerRelativeLayout.layoutParams = params

        if (!Utils.isMobile(this@AudioBookActivity) && mode == Configuration.ORIENTATION_LANDSCAPE) {

            var playerLinearParams: LinearLayout.LayoutParams = playerLinearLayout.layoutParams as LinearLayout.LayoutParams
            playerLinearParams.width = RelativeLayout.LayoutParams.MATCH_PARENT
            playerLinearParams.height = RelativeLayout.LayoutParams.MATCH_PARENT

            playerLinearParams.weight = 10f

            playerLinearLayout.layoutParams = playerLinearParams
        }

/*


        var playerLinearParams: LinearLayout.LayoutParams = playerLinearLayout.layoutParams as LinearLayout.LayoutParams
        playerLinearParams.width = RelativeLayout.LayoutParams.MATCH_PARENT
        playerLinearParams.height = RelativeLayout.LayoutParams.MATCH_PARENT

        playerLinearParams.weight=10f

        playerLinearLayout.layoutParams = playerLinearParams
*/

        toplayout.visibility = View.GONE
        tabsRelativeLayout.visibility = View.GONE

    }

    @SuppressLint("SourceLockedOrientationActivity")
    override fun setOrientationToLandReq() {
        Utils.insertSharedPrefernceIntValues(this, Constants.SHELF_PREFS_NAME, "FulScreenStatus", 1)

        // customAudioBookPlayer?.setFullScreenIconToZoomOut()

        // landscape and set weight

        setOrientationToLand()
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE


    }

    @SuppressLint("SourceLockedOrientationActivity")
    override fun setOrientationToPort() {

        Utils.insertSharedPrefernceIntValues(this, Constants.SHELF_PREFS_NAME, "FulScreenStatus", 0)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT
        // customAudioBookPlayer?.setFullScreenIconToZoomIn()


        tabsRelativeLayout.visibility = View.VISIBLE
        toplayout.visibility = View.VISIBLE


        val playerLayoutParams = playerControllerRelativeLayout.layoutParams as LinearLayout.LayoutParams
        playerLayoutParams.weight = 5.5f

        playerLayoutParams.height = LinearLayout.LayoutParams.WRAP_CONTENT
        playerLayoutParams.width = LinearLayout.LayoutParams.MATCH_PARENT

        playerControllerRelativeLayout.layoutParams = playerLayoutParams

        val tocLayoutParams = tabsRelativeLayout.layoutParams as LinearLayout.LayoutParams
        tocLayoutParams.weight = 4.5f
        tocLayoutParams.height = LinearLayout.LayoutParams.WRAP_CONTENT
        tocLayoutParams.width = LinearLayout.LayoutParams.MATCH_PARENT
        tabsRelativeLayout.layoutParams = tocLayoutParams

        tabsRelativeLayout.visibility = View.VISIBLE
        toplayout.visibility = View.VISIBLE

    }


    override fun tapOnBookMark(chapter: BookMarkVO) {

        customAudioBookPlayer!!.playBookMark(chapter)
    }


    override fun tapOnTranscript() {
    }

    override fun tapOnBookMarkDelete(chapter: BookMarkVO) {
        customAudioBookPlayer!!.deleteBookMark(chapter)
    }


    override fun tapOnPlayPause(content: TocItem, position: Int, isSelected: Boolean) {
        customAudioBookPlayer!!.tapOnPlayPause(content, position, isSelected)
    }

    override fun tapOnVideoPlayPauseToC(content: Content, position: Int, selected: Boolean) {

        customAudioBookPlayer!!.tapOnVideoPlayPause(content, position, selected)
    }

    override fun didTapOnAdd(title: String, folioId: String, timeInInt: Int) {

        customAudioBookPlayer!!.saveBookMarkData(title, folioId, timeInInt)
    }


    override fun didTapOnCancel() {
        // customAudioBookPlayer!!.bookMarkCancel()
    }


    open fun getScreenHeight(): Int {
        return Resources.getSystem().getDisplayMetrics().heightPixels
    }

    override fun didTapOnApply(speedPosition: Int) {

        customAudioBookPlayer!!.setAudioSpeed(speedPosition)

    }


    /*Callback for narration speed cancel*/
    override fun didTapOnCancelSpeed() {
        customAudioBookPlayer!!.speedSetCancel()
    }

    /*Back button pressed*/
    override fun didTapOnBackButton() {



        Utils.insertSharedPrefernceIntValues(this, Constants.SHELF_PREFS_NAME, "FulScreenStatus", 0)
        val intent = Intent()

        setResult(RESULT_OK, intent)
       // BaseActivity.isReaderOpen = false
        finish()
    }



    private fun getCurrentFragmentFromViewPager(position: Int): Fragment? {
        return supportFragmentManager.findFragmentByTag("android:switcher:" + R.id.audioBookPager + ":" + position)
    }

    override fun onBackPressed() {

        HDKitabooAudioBookController.backButtonPressed()
    }

    override fun onResume() {
        super.onResume()

        if (isAudio) {
            customAudioBookPlayer?.onResume()
        } else {
            // count = 0
            mNetworkReceiver = NetworkChangeReceiver(this)
            registerNetworkBroadcastForNougat()

            lastPlayedPosition = Utils.getSharedPreferenceIntValue(this, Constants.SHELF_PREFS_NAME, bookId.toString(), 0)

            customAudioBookPlayer?.onRestoreInstanceState(isVideoPlayingSong, videoSpeedPosition, lastPlayedPosition)

            contentResolver.registerContentObserver(
                    Settings.System.getUriFor(Settings.System.ACCELEROMETER_ROTATION),
                    true, contentObserver
            )

        }

        if (refreshPlayerFilter == null) {
            refreshPlayerFilter = IntentFilter(ACTIVITY_UPDATE_ACTION)
        }
        LocalBroadcastManager.getInstance(this).registerReceiver(refreshReceiver, refreshPlayerFilter!!)


    }

    override fun onPause() {
        super.onPause()
        if (!isAudio) {
            isVideoPlayingSong = !customAudioBookPlayer?.isManualPause!!
            videoSpeedPosition = customAudioBookPlayer?.getSpeed()!!
            if (isOnlinePlaying) {
                unregisterNetworkChanges()
            }
            contentResolver.unregisterContentObserver(contentObserver)
        }
        LocalBroadcastManager.getInstance(this).unregisterReceiver(refreshReceiver)
        if (customAudioBookPlayer?.getCurrentPosition()!!.toInt() > 0) {
            Utils.insertSharedPrefernceIntValues(this, Constants.SHELF_PREFS_NAME, bookId.toString(), customAudioBookPlayer?.getCurrentPosition()!!.toInt())
        }
        Utils.insertSharedPreferenceBooleanValue(this@AudioBookActivity, "isVideoPlaying", customAudioBookPlayer!!.getPlaying())


    }

    override fun onStop() {
        customAudioBookPlayer?.onStopCalled()
        super.onStop()

    }

    override fun onDestroy() {

        if (!isChangingConfigurations) {
          //  BaseActivity.isReaderOpen = false
          GlobalDataManager.getInstance().setCurrentSelectedBookLaunch(false)
        }
        customAudioBookPlayer!!.onDestroyCalled(isChangingConfigurations)
        super.onDestroy()
    }

    override fun playOrPause() {

        customAudioBookPlayer!!.playPause()
    }

    override fun playNextChapter() {
        customAudioBookPlayer!!.nextChapter()

    }

    override fun playPreviousChapter() {
        customAudioBookPlayer!!.previousChapter()
    }

    override fun playNextVideo() {

        customAudioBookPlayer!!.updatePlayerButtonVisibility(disable = true)
        Utils.insertSharedPrefernceIntValues(this, Constants.SHELF_PREFS_NAME, bookId.toString(), customAudioBookPlayer?.getCurrentPosition()!!.toInt())

        if (mediaBookItemList.size > currentMediaPosition + 1) {

            currentMediaPosition += 1
            isAudio = mediaBookItemList[currentMediaPosition].isAudio
            audioImageUrl = mediaBookItemList[currentMediaPosition].audioImageUrl
            bookId = mediaBookItemList[currentMediaPosition].bookId
            bookVersion = mediaBookItemList[currentMediaPosition].bookVersion
            isbn = mediaBookItemList[currentMediaPosition].isbn

            val isMediaDownloaded = isMediaDownloaded(bookId, isbn)
            if (!Utils.isOnline(this) && !isMediaDownloaded) {
                playNextVideo()
            } else {
                isPlayNextPrev = 1
                openBook(bookId, bookVersion!!, isbn, isAudio, audioImageUrl!!)
                /* val intent = intent
                 intent.putParcelableArrayListExtra("MediaPlayList", mediaBookItemList)
                 intent.putExtra("MediaPosition", currentMediaPosition)
                 intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                 overridePendingTransition(0, 0)
                 finish()
                 overridePendingTransition(0, 0)
                 startActivity(intent)*/
            }

        } else {
            if (!Utils.isOnline(this)) {
                onConnectivityChange(false)
            }
        }
    }

    override fun playPreviousVideo() {
        customAudioBookPlayer!!.updatePlayerButtonVisibility(disable = true)
        Utils.insertSharedPrefernceIntValues(this, Constants.SHELF_PREFS_NAME, bookId.toString(), customAudioBookPlayer?.getCurrentPosition()!!.toInt())

        if (currentMediaPosition > 0) {
            currentMediaPosition -= 1

            isAudio = mediaBookItemList[currentMediaPosition].isAudio
            audioImageUrl = mediaBookItemList[currentMediaPosition].audioImageUrl
            bookId = mediaBookItemList[currentMediaPosition].bookId
            bookVersion = mediaBookItemList[currentMediaPosition].bookVersion
            isbn = mediaBookItemList[currentMediaPosition].isbn

            val isMediaDownloaded = isMediaDownloaded(bookId, isbn)
            if (!Utils.isOnline(this) && !isMediaDownloaded) {
                playPreviousVideo()
            } else {
                isPlayNextPrev = 2
                openBook(bookId, bookVersion!!, isbn, isAudio, audioImageUrl!!)
                /*  val intent = intent
                  intent.putParcelableArrayListExtra("MediaPlayList", mediaBookItemList)
                  intent.putExtra("MediaPosition", currentMediaPosition)
                  intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                  overridePendingTransition(0, 0)
                  finish()
                  overridePendingTransition(0, 0)
                  startActivity(intent)*/
            }


        } else {
            if (!Utils.isOnline(this)) {
                onConnectivityChange(false)
            }
            // customAudioBookPlayer?.updateNextPreviousStatus(false)
        }
    }

    override fun changeSubTitle(lang: Int) {
        customAudioBookPlayer!!.changeSubTitle(lang)
    }

    override fun changeAudioLanguage(lang: Int) {
        customAudioBookPlayer!!.changeAudio(lang)
    }

    override fun onOffSubtitle(isOn: Boolean) {
        customAudioBookPlayer!!.onOffSubtitle(isOn)
    }

    override fun changeVideoQuality(quality: Int) {
        customAudioBookPlayer!!.changeMediaQuality(quality)
    }

    override fun forwardAudio() {
        customAudioBookPlayer!!.forwardChapter()
    }

    override fun rewindAudio() {

        customAudioBookPlayer!!.rewindChapter()
    }

    override fun seekBarDragged(progress: Int, fromUser: Boolean) {

        customAudioBookPlayer!!.doProgressChange(progress, fromUser)

    }

    override fun fetchUGCRequestCompleted(arrayOfUGCIDs: ArrayList<UGCFetchResponseObject>?) {

        customAudioBookPlayer!!.updateBookmark()
    }

    override fun requestErrorOccured(exeption: ServiceException?) {
        Log.e("TAG", "requestErrorOccured")
    }

    override fun requestCompleted(response: IServiceResponse?) {
        Log.e("TAG", "requestCompleted")
    }


    override fun setFullScreenMode() {

        tabsRelativeLayout.visibility = View.GONE

    }

    override fun setDefaultSCreenMode() {
        tabsRelativeLayout.visibility = View.VISIBLE
    }

    override fun onMediaLoaded() {

    }

    override fun updateMediaAspectRatio(hieght: Int, mediaWidth: Int) {
        if (Utils.isMobile(this@AudioBookActivity) && this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            val playerLayoutParams = playerControllerRelativeLayout.layoutParams as LinearLayout.LayoutParams
            playerLayoutParams.height = hieght
            playerLayoutParams.width = mediaWidth
            playerControllerRelativeLayout.minimumWidth = 300

            playerControllerRelativeLayout.layoutParams = playerLayoutParams

            val updtaedHieght = getScreenHeight() - hieght
            val tabsRelativeLayoutParam = tabsRelativeLayout.layoutParams as LinearLayout.LayoutParams
            tabsRelativeLayoutParam.height = updtaedHieght
            tabsRelativeLayoutParam.width = LinearLayout.LayoutParams.MATCH_PARENT


            tabsRelativeLayout.layoutParams = tabsRelativeLayoutParam
        }
    }

    override fun onSleepTimerClicked(sleepTimerSetListener: HDKitabooAudioBookController.OnSleepTimerSetListener) {
        customAudioBookPlayer?.onSleepTimerClicked(sleepTimerSetListener)
    }

    private val contentObserver: ContentObserver = object : ContentObserver(Handler()) {
        override fun onChange(selfChange: Boolean) {
            if (Settings.System.getInt(
                            contentResolver,
                            Settings.System.ACCELEROMETER_ROTATION, 0
                    ) == 1
            ) {
                setOrientationSensor()
            } else {
                requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LOCKED
            }
        }
    }


    override fun setOrientationSensor() {
        // requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR
    }

    override fun clickOnZoomInOut(status: Boolean) {

    }

    fun onConnectivityChange(internetAvailable: Boolean) {

        if (internetAvailable) {
            tv_check_connection.text = "We are back !!!"
            tv_check_connection.setBackgroundColor(Color.GREEN)
            tv_check_connection.setTextColor(Color.WHITE)
            val handler = Handler(Looper.myLooper()!!)
            val delayRunnable = Runnable { tv_check_connection.visibility = View.GONE }

            if (TextUtils.isEmpty(mAudioUrl) || folioTimeIndex == null) {
                openBook(bookId, bookVersion!!, isbn, isAudio, audioImageUrl!!)
            } else {
                updateLayout()
                customAudioBookPlayer?.retryPlaying()
            }
            handler.postDelayed(delayRunnable, 3000)
        } else {
            if (customAudioBookPlayer?.getCurrentPosition()!!.toInt() > 0) {
                Utils.insertSharedPrefernceIntValues(this, Constants.SHELF_PREFS_NAME, bookId.toString(), customAudioBookPlayer?.getCurrentPosition()!!.toInt())
            }
            tv_check_connection.visibility = View.VISIBLE
            tv_check_connection.text = getString(R.string.no_internet)
            tv_check_connection.setBackgroundColor(Color.parseColor("#66FF0000"))
            tv_check_connection.setTextColor(Color.WHITE)
            customAudioBookPlayer?.updatePlayerButtonVisibility(disable = false)
        }
    }

    private fun registerNetworkBroadcastForNougat() {
        registerReceiver(mNetworkReceiver, IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"))
    }

    protected fun unregisterNetworkChanges() {
        try {
            if (this::mNetworkReceiver.isInitialized)
                unregisterReceiver(mNetworkReceiver)
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
        }
    }





    override fun isTimeSet(isTimerSet: Boolean) {
        if (isTimerSet) {
            tvSleepTimer.setTextColor(ContextCompat.getColor(this@AudioBookActivity, R.color.white))
            tvSleepTimer.background = ContextCompat.getDrawable(this@AudioBookActivity, R.drawable.audio_sleep_timer_circle)
        } else {
            tvSleepTimer.setTextColor(ContextCompat.getColor(this@AudioBookActivity, R.color.kitaboo_main_color))
            tvSleepTimer.background = null
        }
    }

    override fun onSleepTimeSetError() {
        DialogUtils.displayToast(this@AudioBookActivity,
                "Audio track is finished, Please start again", Toast.LENGTH_SHORT, Gravity.CENTER)
    }


    private fun isMediaDownloaded(bookId: Long, isbn: String): Boolean {
        parentBookDir = File(Utils
                .getBookFolderPathCompat(bookId.toString() + "", isbn))
        return parentBookDir!!.exists()
    }

    private fun fetchMediaBookUrl(bookid: Long, isAudio: Boolean) {
        com.hurix.commons.notifier.GlobalDataManager.getInstance().cookieManager.cookieStore.removeAll()
        mServicehandler.getUserBookDownload(bookid, PlayerController.getInstance(this).token,  //                userbookVo.getBookType().toString().equalsIgnoreCase("DEFAULT") ? "ANDROID" : mCurrBookSelected.getBookType().toString(), "online",
                "html5", "online",
                object : IServiceResponseListener {
                    override fun requestCompleted(response: IServiceResponse) {
                        val responseObj = response as BookDownloadServiceResponse
                        if (TextUtils.isEmpty(responseObj.bookURL)) {
                            Toast.makeText(applicationContext, "Error loading book", Toast.LENGTH_SHORT).show()
                            updateProgressBar(false)
                            return
                        }

                        mAudioUrl = responseObj.bookURL + "/index.m3u8"
                        val timeIndexUrl = responseObj.bookURL + "/time-index.json"
                        val tocUrl = responseObj.bookURL + "/toc.json"

                        if (resources.getBoolean(R.bool.isPerfomanceEnabled)) {
                            fetchTocAndTimeIndex(isAudio, tocUrl, timeIndexUrl, true)
                        } else {

                            contentServerUrl = responseObj.bookURL
                            HDKitabooAudioBookController.setContentServerUrl(contentServerUrl)
                            val packageId = responseObj.bookURL.substring(responseObj.bookURL.lastIndexOf("/") + 1).toLong()

                            mServicehandler.authContentServer(packageId, PlayerController.getInstance(applicationContext).token, object : IServiceResponseListener {
                                override fun requestCompleted(response: IServiceResponse) {
                                    fetchTocAndTimeIndex(isAudio, tocUrl, timeIndexUrl, true)
                                }

                                override fun requestErrorOccured(response: ServiceException) {
                                    onApiError()
                                    updateProgressBar(false)
                                    try {
                                        if (response == null) {
                                            return
                                        }
                                        if (response.invalidFields != null && !response.invalidFields.isEmpty()) {
                                            val entry: Map.Entry<String, Int> = response.invalidFields
                                                    .entries.iterator().next()
                                            if (PlayerController.getInstance(applicationContext).isAutoLogOffEnabled && entry.value == 103) {
                                                showSessionExpiredAlert()
                                            } else {
                                                if (entry.value == 103) {
                                                    /*KitabooServiceAPI.getObject().serviceAdapter
                                                            .refreshUserToken(UserController.getInstance(applicationContext).userVO.token, response.responseRequestType.toString())*/
                                                } else if (entry.value == 911) {

                                                } else if (response.responseRequestType == Constants.SERVICETYPES.REFRESH_USER_TOKEN) {
                                                    DialogUtils.displayToast(applicationContext, applicationContext.getResources().getString(R.string.alert_session_expired), Toast.LENGTH_SHORT, Gravity.CENTER)
                                                } else {
                                                    DialogUtils.displayToast(applicationContext, Utils.getMessageForError(applicationContext, entry.value),
                                                            Toast.LENGTH_LONG, Gravity.CENTER)
                                                }
                                            }
                                        } else {
                                            DialogUtils.displayToast(applicationContext, response.message, Toast.LENGTH_SHORT, Gravity.CENTER)
                                        }
                                    } catch (e1: Exception) {
                                        if (Constants.IS_DEBUG_ENABLED) {
                                            e1.printStackTrace()
                                        }
                                    }
                                }
                            })
                        }


                    }

                    override fun requestErrorOccured(response: ServiceException) {
                        try {
                            updateProgressBar(false)
                            onApiError()
                            if (response.invalidFields != null && !response.invalidFields.isEmpty()) {
                                val entry: Map.Entry<String, Int> = response.invalidFields
                                        .entries.iterator().next()
                                if (PlayerController.getInstance(applicationContext).isAutoLogOffEnabled && entry.value == 103) {
                                    showSessionExpiredAlert()
                                } else {
                                    if (entry.value == 103) {
                                        /* KitabooServiceAPI.getObject().serviceAdapter
                                                .refreshUserToken(UserController.getInstance(applicationContext).userVO.token, response.responseRequestType.toString(), )*/
                                    } else if (entry.value == 911) {

                                    } else if (response.responseRequestType == Constants.SERVICETYPES.REFRESH_USER_TOKEN) {
                                        DialogUtils.displayToast(applicationContext, applicationContext.getResources().getString(R.string.alert_session_expired), Toast.LENGTH_SHORT, Gravity.CENTER)
                                    } else {
                                        DialogUtils.displayToast(applicationContext, Utils.getMessageForError(applicationContext, entry.value),
                                                Toast.LENGTH_LONG, Gravity.CENTER)
                                    }
                                }
                            } else {
                                DialogUtils.displayToast(applicationContext, response.message, Toast.LENGTH_SHORT, Gravity.CENTER)
                            }
                        } catch (e1: Exception) {
                            if (Constants.IS_DEBUG_ENABLED) {
                                e1.printStackTrace()
                            }
                        }
                    }
                })
    }


    fun fetchTocAndTimeIndex(isAudio: Boolean, tocUrl: String, timeIndexUrl: String, isToc: Boolean) {
        mServicehandler.tocTimeIndexRequest(if (isToc) tocUrl else timeIndexUrl, object : IServiceResponseListener {
            override fun requestCompleted(response: IServiceResponse) {
                val contentServerAuthResponse = response as ToCTimeIndexResponse
                if (isToc) {
                    parseMediaBook(isAudio, contentServerAuthResponse.data)
                    if (isAudio) {
                        val tempUrl: String = hdAutoBookModel.toc.get(0).url
                        mAudioUrl = contentServerUrl + "/" + tempUrl.substring(0, tempUrl.indexOf("#"))
                    }
                    fetchTocAndTimeIndex(isAudio, tocUrl, timeIndexUrl, false)
                } else {
                    parseTimeIndex(contentServerAuthResponse.data)
                    startMedia()
                }
                android.util.Log.e("url", "response" + response.isSuccess())
            }

            override fun requestErrorOccured(response: ServiceException) {
                updateProgressBar(false)
                //showError("Error while playing video")
                try {
                    onApiError()
                    if (response == null) {
                        return
                    }
                    if (response.invalidFields != null && !response.invalidFields.isEmpty()) {
                        val entry: Map.Entry<String, Int> = response.invalidFields
                                .entries.iterator().next()
                        if (PlayerController.getInstance(applicationContext).isAutoLogOffEnabled && entry.value == 103) {
                            showSessionExpiredAlert()
                        } else {
                            if (entry.value == 103) {
                                /* KitabooServiceAPI.getObject().serviceAdapter
                                        .refreshUserToken(UserController.getInstance(applicationContext).userVO.token, response.responseRequestType.toString(), )*/
                            } else if (entry.value == 911) {

                            } else if (response.responseRequestType == Constants.SERVICETYPES.REFRESH_USER_TOKEN) {
                                DialogUtils.displayToast(applicationContext, applicationContext.getResources().getString(R.string.alert_session_expired), Toast.LENGTH_SHORT, Gravity.CENTER)
                            } else {
                                DialogUtils.displayToast(applicationContext, Utils.getMessageForError(applicationContext, entry.value),
                                        Toast.LENGTH_LONG, Gravity.CENTER)
                            }
                        }
                    } else {
                        DialogUtils.displayToast(applicationContext, response.message, Toast.LENGTH_SHORT, Gravity.CENTER)
                    }
                } catch (e1: Exception) {
                    if (Constants.IS_DEBUG_ENABLED) {
                        e1.printStackTrace()
                    }
                }
            }
        })
    }

    private fun openBook(pBookId: Long, pBookVersion: String, pIsbn: String, pIsAudio: Boolean, pAudioImageUrl: String) {
        mAudioUrl = "";
        folioTimeIndex = null
        progressPreview.visibility = VISIBLE
        bookId = pBookId
        bookVersion = pBookVersion
        isbn = pIsbn
        isAudio = pIsAudio
        audioImageUrl = pAudioImageUrl

        val isMediaDownloaded = isMediaDownloaded(bookId, isbn)

        isOnlinePlaying = !isMediaDownloaded

        SDKManager.getInstance().currentBookISBN = isbn

        if (isMediaDownloaded) {
            thumb = parentBookDir!!.absolutePath
            HDKitabooAudioBookController.setThumb(thumb)
            mAudioUrl = fetchMediaUrlFromPackage(bookId, isbn).absolutePath
            parseHDVideoToc(bookId, isbn, isAudio)

            startMedia()
        } else {
            thumb = ""
            HDKitabooAudioBookController.setThumb(thumb)
            fetchMediaBookUrl(bookId, isAudio)
        }

    }

    private fun startMedia() {

        progressPreview.visibility = GONE
        updateLayout()
        lastPlayedPosition = Utils.getSharedPreferenceIntValue(this, Constants.SHELF_PREFS_NAME, bookId.toString(), 0)

        if (isAudio) {
            customAudioBookPlayer!!.initAudio(bookId, isOnlinePlaying, lastPlayedPosition, mAudioUrl, audioImageUrl, -1, 2, isAudio, ArrayList(), hdAutoBookModel, folioTimeIndex!!, typeFace!!)
        } else {
            customAudioBookPlayer!!.initVideo(currentMediaPosition == 0, (mediaBookItemList.size == 1 || currentMediaPosition == mediaBookItemList.size - 1), isPlayNextPrev, bookId, isOnlinePlaying, lastPlayedPosition, mAudioUrl, audioImageUrl, -1, 2, isFullScreen, isAudio, ArrayList(), hdVideoBookModel, folioTimeIndex!!, typeFace!!)
        }

        fetchUGCRequest()
        initViewPager()
    }

    private fun initViewPager() {
        bookPagerAdapter = AudioBookPagerAdapter(this, supportFragmentManager)

        if (isAudio) {
            HDKitabooAudioBookController.initAudioChapterData(hdAutoBookModel)
        } else {
            HDKitabooAudioBookController.initVideoChapterData(hdVideoBookModel)
        }

        audioBookPager.adapter = bookPagerAdapter
        tabs.setupWithViewPager(audioBookPager)
        Handler(Looper.myLooper()!!).postDelayed({
            audioBookPager.currentItem = 0
            getCurrentFragmentFromViewPager(0)?.let {
                customAudioBookPlayer?.setCurrentFragmentData(it)
            }
        }, 500)

        audioBookPager?.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {

            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
                getCurrentFragmentFromViewPager(position)?.let {
                    customAudioBookPlayer?.setCurrentFragmentData(it)
                }
            }
        })

    }

    private fun parseHDVideoToc(bookId: Long, isbn: String, isAudio: Boolean) {
        val mediaTocJsonFile = File(Utils.getBookFolderPathCompat(bookId.toString() + "", isbn) + "/toc.json")
        val mediaToc: String = this.getAssetsJSON(mediaTocJsonFile.absolutePath.toString())
        parseMediaBook(isAudio, mediaToc)

        val timeIndexJsonFile = File(Utils.getBookFolderPathCompat(bookId.toString() + "", isbn) + "/time-index.json")
        val timeIndexJson: String = this.getAssetsJSON(timeIndexJsonFile.absolutePath.toString())
        parseTimeIndex(timeIndexJson)
    }


    private fun parseMediaBook(isAudio: Boolean, jsonData: String) {
        if (isAudio) {
            hdAutoBookModel = Gson().fromJson(jsonData, HDAudioBookModel::class.java)
        } else {
            hdVideoBookModel = Gson().fromJson(jsonData, HDVideoBookModel::class.java)
        }
    }

    private fun parseTimeIndex(jsonStr: String) {
        val listType = object : TypeToken<ArrayList<FolioTimeIndex?>?>() {}.type
        folioTimeIndex = Gson().fromJson(jsonStr, listType)
    }

    open fun getAssetsJSON(fileName: String?): String {
        var json: String? = null
        try {
            val fileInputStream = FileInputStream(fileName)
            val size = fileInputStream.available()
            val buffer = ByteArray(size)
            fileInputStream.read(buffer)
            fileInputStream.close()
            json = String(buffer, StandardCharsets.UTF_8);
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return json.toString()
    }

    private fun updateProgressBar(showProgressBar: Boolean) {
        if (showProgressBar) {
            progressPreview.setVisibility(View.VISIBLE)
            this.window.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        } else {
            this.window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
            progressPreview.setVisibility(View.GONE)
        }
    }

    open fun showSessionExpiredAlert() {
        DialogUtils.showOKAlert(View(this), 1, this, resources
                .getString(R.string.alert_title), resources
                .getString(R.string.alert_session_expired)
        ) {
            /*DBController.getInstance(this).manager.logoutUserByID(UserController
                    .getInstance(this).userVO.userID)*/
            Utils.startLauncherActivity(this)
        }
    }

    private fun fetchMediaUrlFromPackage(bookId: Long, isbn: String): File {
        val parentDir = File(Utils
                .getBookFolderPathCompat(bookId.toString() + "", isbn))

        val fileList = parentDir.listFiles()
        var videoFile: File? = null
        outerLoop@ for (f in fileList) {
            if (f.listFiles() != null && f.listFiles().size > 0) {
                for (f1 in f.listFiles()) {
                    val steMimeType = f1.absolutePath.endsWith("index.m3u8")
                    if (steMimeType) {
                        videoFile = f1
                        break@outerLoop
                    }
                }
            } else {
                val isIndexFile = f.absolutePath.endsWith("index.m3u8")
                if (isIndexFile) {
                    videoFile = f
                    break
                }
            }
        }
        return videoFile!!
    }

    private fun onApiError() {
        customAudioBookPlayer?.retryPlaying()
    }

}




