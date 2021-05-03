package com.hurix.reader.kitaboosdkrenderer


import android.app.AlarmManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.database.ContentObserver
import android.graphics.Color
import android.graphics.Typeface
import android.net.ConnectivityManager
import android.os.Bundle
import android.os.Handler
import android.provider.Settings
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.viewpager.widget.ViewPager
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
import com.hurix.commons.utils.Utils.getSharedPreferenceIntValue
import com.hurix.customui.datamodel.BookMarkVO
import com.hurix.downloadbook.controller.UserController
import com.hurix.reader.kitaboosdkrenderer.constants.Constants
import com.hurix.reader.kitaboosdkrenderer.constants.ShelfUIConstants
import com.hurix.reader.kitaboosdkrenderer.notifier.GlobalDataManager
import com.hurix.reader.kitaboosdkrenderer.utils.DialogUtils
import com.hurix.reader.kitaboosdkrenderer.utils.Utils
import com.hurix.service.Interface.IServiceResponse
import com.hurix.service.Interface.IServiceResponseListener
import com.hurix.service.datamodel.UGCFetchResponseObject
import com.hurix.service.exception.ServiceException
import kotlinx.android.synthetic.main.activity_audio_book_tab.*
import java.util.*

/*

open class AudioBookActivity : AppCompatActivity(), IHDAudioTOCControllerCallBacks,
        IBookMarkCallBacks, IHDNarrationSpeedCallBack, IHDKitabooAudioControllerCallBack,
        HDAudioBookPlayer, ServiceCompletedListener, IServiceResponseListener, HDKitabooAudioBookController.OnSleepTimerSetListener {
    private var count: Int = 0
    private lateinit var mNetworkReceiver: NetworkChangeReceiver
    private var accountType: String = ""
    private lateinit var folioTimeIndex: ArrayList<FolioTimeIndex>
    private lateinit var bookPagerAdapter: AudioBookPagerAdapter
    prate var customAudioBookPlayer: CustomAudioBookPlayers? = null


    private lateinit var hdAutoBookModel: HDAudioBookModel
    private lateinit var mAudioUrl: String
    private lateinit var allFilesPath: String
    private var audioImageUrl: String? = null
    private var bookId: Long = 0
    private var userId: Long = 0
    private var token: String? = null
    private var bookVersion: String? = null
    private var isbn: String = ""
    private var lastPlayedPosition = 0
    private var refreshPlayerFilter: IntentFilter? = null
    private var mLocale: Locale? = null


    var isAudio = false
    var isOnlinePlaying = false
    var isFullScreen = 0
    var isAlreadyLand = false
    var isVideoPlayingSong = false
    var videoSpeedPosition: Int = -1

    private lateinit var hdVideoBookModel: HDVideoBookModel

    private var contentServerUrl: String? = ""

    private lateinit var audioVideoBookTheme: AudioVideoBookThemeVo

    private var alarmManager: AlarmManager? = null
    private var bookExpiryDate = ""
    var currentTimeInMillis: Long = 0
    var thumb = ""

    var isVideoOriented = false


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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        *//*if (resources.getBoolean(R.bool.is_multi_lang_support)) {
            setLocale(Utils.getSharedPreferenceStringValue(
                    this@AudioBookActivity, Constants.SHELF_PREFS_NAME, Constants.DEFAULT_APP_LANGUAGE, ""))
        }


        Log.e("orietation", "Changedoc")*//*


        com.hurix.epubreader.Utility.Utils.setSecureWindowFlags(this@AudioBookActivity)
        isFullScreen = Utils.getSharedPreferenceIntValue(this@AudioBookActivity, Constants.SHELF_PREFS_NAME, "FulScreenStatus", 0)


        val bookDataBundle = intent.extras
        bookDataBundle?.let {
            mAudioUrl = it.getString("mediapath")!!
            allFilesPath = it.getString("allFilePaths")!!

            isAudio = it.getBoolean("isAudio")
            isOnlinePlaying = it.getBoolean("isOnline")
            if (isAudio) {
                hdAutoBookModel = it.getParcelable<HDAudioBookModel>("AudioTocDataContent") as HDAudioBookModel
            } else {
                contentServerUrl = it.getString("ContentServerUrl")
                hdVideoBookModel = it.getParcelable<HDVideoBookModel>("VideoTocDataContent") as HDVideoBookModel
            }
            folioTimeIndex = it.getParcelableArrayList<FolioTimeIndex>("TimeIndexData") as ArrayList<FolioTimeIndex>

            audioImageUrl = it.getString("image")
            bookId = it.getLong("bookId")

            userId = it.getLong("UserID")
            bookVersion = it.getString("bookVersion")
            isbn = it.getString("isbn") as String
            token = it.getString("token")
            accountType = it.getString("Rolename") as String

            bookExpiryDate = it.getString("expiryDate") as String
            currentTimeInMillis = it.getLong("currentTime")
            thumb = it.getString("mediathumbnail") as String


            // send the specific user id to sdk to keep track of UGC data for User
            KitabooSDKModel.getInstance().userID = userId
            KitabooSDKModel.getInstance().userToken = token
            SDKManager.getInstance().currentBookISBN = isbn
        }

        setAudioBookLayouts()


        if (isAudio) {

            toplayout.visibility = View.VISIBLE
        } else {

            toplayout.visibility = View.GONE
            val displayMode: Int = resources.configuration.orientation



            if (!Utils.isMobile(this@AudioBookActivity) && displayMode == Configuration.ORIENTATION_LANDSCAPE &&
                    isFullScreen == 1) {

                *//*toplayout.visibility = View.GONE
                tabsRelativeLayout.visibility=View.GONE

                var params: LinearLayout.LayoutParams = playerControllerRelativeLayout.layoutParams as LinearLayout.LayoutParams
                params.width = RelativeLayout.LayoutParams.MATCH_PARENT
                params.height = RelativeLayout.LayoutParams.MATCH_PARENT
                params.weight=10f
                playerControllerRelativeLayout.layoutParams = params
                Log.e("full","called")*//*

                setOrientationToLand()
            } else if (!Utils.isMobile(this@AudioBookActivity) && displayMode == Configuration.ORIENTATION_LANDSCAPE) {

                toplayout.visibility = View.VISIBLE
                Log.e("full", "called")
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


        audioVideoBookTheme = AudioVideoBookThemeController.getInstance(this@AudioBookActivity).audioVideoBookPlayerTheme
        initCallbacks()

        lastPlayedPosition = Utils.getSharedPreferenceIntValue(this, Constants.SHELF_PREFS_NAME, bookId.toString(), 0).toInt()
        playerControllerRelativeLayout.addView(initializePlayerController(savedInstanceState))
        initUI(audioVideoBookTheme)
        setTopPanelTheme(audioVideoBookTheme)

        fetchUGCRequest(savedInstanceState)
        val displayMode: Int = resources.configuration.orientation

    }


    private fun setAudioBookLayouts() {

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
    }



    private fun fetchUGCRequest(savedInstanceState: Bundle?) {

        if (savedInstanceState == null) {
            mServicehandler = if (this.resources.getString(R.string.server_pointing).isEmpty()) {
                ServiceHandler(this, KitabooSDKModel.getInstance().clientID)
            } else {
                ServiceHandler(this, KitabooSDKModel.getInstance().clientID,
                        this.resources.getString(R.string.server_pointing))
            }
            mServicehandler.setServiceCompletedListener(this)
            mServicehandler.sendBackgroundServiceforUGC(bookId, userId, accountType, bookVersion, null, this)
        }

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putInt("speed", customAudioBookPlayer?.getSpeed()!!)
        // outState.putBoolean("isPlaying", if (isAudio) customAudioBookPlayer?.getPlaying()!! else !customAudioBookPlayer?.isManualPause!!)
        outState.putSerializable("BookMarkList", customAudioBookPlayer?.getBookMarkList())
        outState.putBoolean("isPlaying", customAudioBookPlayer?.getPlaying()!!)

        if (!isAudio) {

            var isVidPlaying = Utils.getSharedPreferenceBooleanValue(this@AudioBookActivity, "isVideoPlaying", false)
            outState.putBoolean("isPlaying", isVidPlaying)
        }
    }


    private fun initCallbacks() {
        HDAudioTOCController.addCallBack(this)
        HDAudioBookMarkController.addCallBack(this)
        HDNarrationSpeedController.addCallBack(this)
        HDKitabooAudioBookController.addCallBack(this)
        HDKitabooAudioBookController.addPlayerCallBack(this)
        HDKitabooAudioBookController.setBookId(bookId)
        HDKitabooAudioBookController.setUser(UserController.getInstance(this).userVO.userID)
        HDKitabooAudioBookController.setThumb(thumb)
        HDKitabooAudioBookController.setContentServerUrl(contentServerUrl)

    }


    private fun initializePlayerController(savedInstanceState: Bundle?): CustomAudioBookPlayers? {
        var isPlayingSong: Int = -1
        var speedPosition: Int = -1
        var bookMarkVOs = ArrayList<BookMarkVO>()

        if (savedInstanceState != null) {
            isPlayingSong = if (savedInstanceState.getBoolean("isPlaying")) 1 else 0
            speedPosition = savedInstanceState.getInt("speed")
            bookMarkVOs = savedInstanceState.getSerializable("BookMarkList") as ArrayList<BookMarkVO>
        }

        if (isAudio) {

            customAudioBookPlayer = CustomAudioBookPlayers(this, isOnlinePlaying, mAudioUrl, allFilesPath, audioImageUrl, lastPlayedPosition, bookId, userId, isPlayingSong, speedPosition,
                    isFullScreen, isAudio, audioVideoBookTheme, bookMarkVOs, hdAutoBookModel, folioTimeIndex)
        } else {

            customAudioBookPlayer = CustomAudioBookPlayers(this, isOnlinePlaying, mAudioUrl, allFilesPath, audioImageUrl, lastPlayedPosition, bookId, userId, isPlayingSong, speedPosition,
                    isFullScreen, isAudio, audioVideoBookTheme, bookMarkVOs, hdVideoBookModel, folioTimeIndex)
        }



        return customAudioBookPlayer
    }


    private fun initUI(audioVideoBookTheme: AudioVideoBookThemeVo) {
        bookPagerAdapter = AudioBookPagerAdapter(this, supportFragmentManager)
        if (isAudio) {
            HDKitabooAudioBookController.initializeAudios(hdAutoBookModel, isAudio, audioVideoBookTheme)
        } else {

            HDKitabooAudioBookController.initializeVideos(hdVideoBookModel, isAudio, audioVideoBookTheme)
        }
        audioBookPager.adapter = bookPagerAdapter
        tabs.setupWithViewPager(audioBookPager)
        Handler().postDelayed({
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

        val typeFace: Typeface = Typefaces.get(this, resources.getString(R.string.kitaboo_bookshelf_font_file_name))
        backTextViewView.typeface = typeFace
        backTextViewView.text = ShelfUIConstants.AUDIO_BOOK_BACK

        backTextViewView.setOnClickListener {

            HDKitabooAudioBookController.backButtonPressed()

        }

        val typeFaceSleep: Typeface = Typefaces.get(this, resources.getString(R.string.kitaboo_bookshelf_font_file_name))
        tvSleepTimer.typeface = typeFaceSleep
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

*//*


        var playerLinearParams: LinearLayout.LayoutParams = playerLinearLayout.layoutParams as LinearLayout.LayoutParams
        playerLinearParams.width = RelativeLayout.LayoutParams.MATCH_PARENT
        playerLinearParams.height = RelativeLayout.LayoutParams.MATCH_PARENT

        playerLinearParams.weight=10f

        playerLinearLayout.layoutParams = playerLinearParams
*//*

        toplayout.visibility = View.GONE
        tabsRelativeLayout.visibility = View.GONE

    }

    override fun setOrientationToLandReq() {
        Utils.insertSharedPrefernceIntValues(this, Constants.SHELF_PREFS_NAME, "FulScreenStatus", 1)

        // customAudioBookPlayer?.setFullScreenIconToZoomOut()

        // landscape and set weight

        setOrientationToLand()
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE


    }

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

    override fun didTapOnApply(speedPosition: Int) {

        customAudioBookPlayer!!.setAudioSpeed(speedPosition)

    }


    *//*Callback for narration speed cancel*//*
    override fun didTapOnCancelSpeed() {
        customAudioBookPlayer!!.speedSetCancel()
    }

    *//*Back button pressed*//*
    override fun didTapOnBackButton() {


        Utils.insertSharedPrefernceIntValues(this, Constants.SHELF_PREFS_NAME, "FulScreenStatus", 0)
        val intent = Intent()

        setResult(RESULT_OK, intent)
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
            if (isOnlinePlaying) {
                mNetworkReceiver = NetworkChangeReceiver(this)
                registerNetworkBroadcastForNougat()
            }


            customAudioBookPlayer?.onRestoreInstanceState(isVideoPlayingSong, videoSpeedPosition)

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

        if (!isAudio) {

            Utils.insertSharedPreferenceBooleanValue(this@AudioBookActivity, "isVideoPlaying", customAudioBookPlayer!!.getPlaying())
            isVideoPlayingSong = !customAudioBookPlayer?.isManualPause!!
            videoSpeedPosition = customAudioBookPlayer?.getSpeed()!!
            if (isOnlinePlaying) {
                unregisterNetworkChanges()
            }
            contentResolver.unregisterContentObserver(contentObserver)
        }
        LocalBroadcastManager.getInstance(this).unregisterReceiver(refreshReceiver)
        Utils.insertSharedPrefernceIntValues(this, Constants.SHELF_PREFS_NAME, bookId.toString(), customAudioBookPlayer?.getCurrentPosition()!!.toInt())
        super.onPause()
    }

    override fun onStop() {
        customAudioBookPlayer?.onStopCalled()
        super.onStop()

    }

    override fun onDestroy() {

        if (!isChangingConfigurations) {
          //  BaseActivity.isReaderOpen = false
        }
        customAudioBookPlayer!!.onDestroyCalled(isChangingConfigurations)
        GlobalDataManager.getInstance().setCurrentSelectedBookLaunch(false)
        super.onDestroy()
    }

    override fun playOrPause() {

        customAudioBookPlayer!!.playPause()
    }

    override fun playNext() {
        customAudioBookPlayer!!.nextChapter()

    }

    override fun playPrevious() {


        customAudioBookPlayer!!.previousChapter()
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

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)


        isFullScreen = Utils.getSharedPreferenceIntValue(this@AudioBookActivity, Constants.SHELF_PREFS_NAME, "FulScreenStatus", 0)


        if (!isAudio) {
            if (Settings.System.getInt(
                            contentResolver,
                            Settings.System.ACCELEROMETER_ROTATION, 0
                    ) == 1
            ) {

                if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE && isFullScreen == 1) {
                    // customAudioBookPlayer?.setFullScreenValue(true)
                    // hideNavigationBar()
                    val playerLayoutParams = playerControllerRelativeLayout.layoutParams as LinearLayout.LayoutParams
                    playerLayoutParams.weight = 10f

                    Log.e("orietation", "Changed" + isFullScreen)

                    playerLayoutParams.height = LinearLayout.LayoutParams.MATCH_PARENT
                    playerLayoutParams.width = LinearLayout.LayoutParams.MATCH_PARENT

                    playerControllerRelativeLayout.layoutParams = playerLayoutParams
                    tabsRelativeLayout.visibility = View.GONE
                    toplayout.visibility = View.GONE

                } else {
                    customAudioBookPlayer?.setFullScreenValue(false)
                    //showNavigationBar()
                    tabsRelativeLayout.visibility = View.VISIBLE
                }
            }
            *//* customAudioBookPlayer?.setFullScreenIcon()
             super.onConfigurationChanged(newConfig)
             clickOnZoomInOut(true)

             isVideoOriented = true*//*

        }


    }

    override fun setOrientationSensor() {
        // requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR
    }

    override fun clickOnZoomInOut(status: Boolean) {

*//*        var params: RelativeLayout.LayoutParams
        val recyclerParamas: RelativeLayout.LayoutParams
        val animationRequired: Boolean

        if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            // titleContainer!!.visibility = View.GONE
            // txt_book_title!!.visibility = View.GONE
            // (findViewById<View>(R.id.toc_holder) as LinearLayout).visibility = View.GONE
            var params: RelativeLayout.LayoutParams = playerControllerRelativeLayout.layoutParams as RelativeLayout.LayoutParams
            params.width = RelativeLayout.LayoutParams.MATCH_PARENT

            if (Utils.isMobile(this@AudioBookActivity)) {

                params.height = (this@AudioBookActivity.resources.displayMetrics.density * 250).toInt()
            } else {

                params.height = (this@AudioBookActivity.resources.displayMetrics.density * 300).toInt()
            }

            playerControllerRelativeLayout.setLayoutParams(params);
            animationRequired = false

            tabsRelativeLayout.visibility = View.VISIBLE


//            else {
//                txt_book_title!!.visibility = View.VISIBLE
//                params = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, 1.2f)
//                (findViewById<View>(R.id.toc_holder) as LinearLayout).visibility = View.VISIBLE
//                titleContainer!!.visibility = View.VISIBLE
//                animationRequired = false
//            }
        } else {
//            (findViewById<View>(R.id.toc_holder) as LinearLayout).visibility = View.GONE
//            titleContainer!!.visibility = View.GONE
//            txt_book_title!!.visibility = View.GONE

//            if(isAlreadyLand){
//
//                // (findViewById<View>(R.id.toc_holder) as LinearLayout).visibility = View.GONE
//                var params: RelativeLayout.LayoutParams = playerControllerRelativeLayout.layoutParams as RelativeLayout.LayoutParams
//                params.width = RelativeLayout.LayoutParams.MATCH_PARENT
//                params.height= (this@AudioBookActivity.resources.displayMetrics.density*300).toInt()
//                playerControllerRelativeLayout.layoutParams = params;
//                animationRequired = false
//
//                playerControllerRelativeLayout!!.layoutParams = params
//                tabsRelativeLayout.visibility=View.VISIBLE
//
//
//            }
//            else{

            params = RelativeLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT
            )
            animationRequired = false

            playerControllerRelativeLayout!!.layoutParams = params
            tabsRelativeLayout.visibility = View.GONE

//            }

            Log.e("getting gone", "here")


        }
//        @SuppressLint("ObjectAnimatorBinding") val transAnimation =
//                ObjectAnimator.ofFloat(this, "translationY", playerControllerRelativeLayout!!.y, 0f)
//        transAnimation.interpolator = DecelerateInterpolator()
//        transAnimation.addListener(object : Animator.AnimatorListener {
//            override fun onAnimationStart(animation: Animator) {
//                val animationm = AnimationUtils.loadAnimation(
//                        baseContext,
//                        R.anim.video_zoom_out
//                )
//                animationm.duration = 1000
//                playerControllerRelativeLayout!!.layoutParams = params
//                playerControllerRelativeLayout!!.animation = animationm
//            }
//
//            override fun onAnimationEnd(animation: Animator) {}
//            override fun onAnimationCancel(animation: Animator) {}
//            override fun onAnimationRepeat(animation: Animator) {}
//        })
//        if (animationRequired) {
//            transAnimation.start()
//        } else {
//            playerControllerRelativeLayout!!.layoutParams = params
//        }*//*
    }

    fun dialog(value: Boolean) {
        if (value) {
            if (count == 0) {
                count++
                return
            }
            tv_check_connection.text = "We are back !!!"
            tv_check_connection.setBackgroundColor(Color.GREEN)
            tv_check_connection.setTextColor(Color.WHITE)
            val handler = Handler()
            val delayrunnable = object : Runnable {
                override fun run() {
                    tv_check_connection.visibility = View.GONE
                }
            }
            handler.postDelayed(delayrunnable, 3000)
        } else {
            tv_check_connection.visibility = View.VISIBLE
            tv_check_connection.text = "internet connect lost"
            tv_check_connection.setBackgroundColor(Color.RED)
            tv_check_connection.setTextColor(Color.WHITE)
        }
    }

    private fun registerNetworkBroadcastForNougat() {
        registerReceiver(mNetworkReceiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
    }

    protected fun unregisterNetworkChanges() {
        try {
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


}*/





