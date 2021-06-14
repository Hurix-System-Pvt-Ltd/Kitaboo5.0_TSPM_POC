package com.hurix.kitaboocloud.kitaboosdkrenderer.customviews.toc_standards

import android.content.Context
import android.content.res.Configuration
import android.graphics.*
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.RelativeLayout
import android.widget.SeekBar
import android.widget.SeekBar.GONE
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.TextView
import android.widget.TextView.OnEditorActionListener
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.hurix.commons.Constants.PlayerUIConstants
import com.hurix.commons.sdkDatamodel.SDKManager
import com.hurix.commons.utils.Utils
import com.hurix.customui.iconify.Typefaces
import com.hurix.customui.toc.ThumbListVO
import com.hurix.customui.views.KEditText
import com.hurix.exoplayer3.util.Log
import com.hurix.kitaboocloud.R
import com.hurix.kitaboocloud.kitaboosdkrenderer.customviews.CustomThumbnailsListner
import com.hurix.kitaboocloud.kitaboosdkrenderer.customviews.SeekBarPositionChangeListner
import com.hurix.kitaboocloud.kitaboosdkrenderer.customviews.adapters.EPUBFIXEDThumbnailAdapter
import com.hurix.kitaboocloud.sdkreadertheme.themePojo.ReaderThemeSettingVo
import com.hurix.renderer.utility.Utility
import java.io.File
import java.util.*
import kotlin.math.roundToInt

class SeekBarFragment : Fragment(), View.OnClickListener, View.OnTouchListener, SeekBarPositionChangeListner, EPUBFIXEDThumbnailAdapter.OnThumbnailClickListener {


    private var isThumbnailsExist: Boolean = false
    private lateinit var epubfixedThumbnailAdapter: EPUBFIXEDThumbnailAdapter
    private lateinit var ctx: Context
    private lateinit var thumbVo: ThumbListVO
    private var position: Int = 0
    private var mPageHistPrevious: Button? = null
    private var mPageHistNext: Button? = null
    private var mGotoPgTxt: KEditText? = null
    private var mTypeFace: Typeface? = null
    private var mSeekbar: SeekBar? = null
    private var mControllerButton: RelativeLayout? = null
    private var mPagenavmainID: RelativeLayout? = null
    private var thumbnailImagelayout: RecyclerView? = null
    private var mRootView: View? = null
    private var mDummyView: View? = null
    private var customTypeFace: Typeface? = null
    private var mListner: CustomThumbnailsListner? = null
    private var mShowHistoryButtons = false
    private var mReaderThemeSettingVo: ReaderThemeSettingVo? = null
    private var mTotalPageCount: TextView? = null
    private var tvThumb: TextView? = null
    private var thumbList: ArrayList<ThumbListVO>? = null

    companion object {
        fun newInstance() = SeekBarFragment()
        var thumbnailPosition = 0
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        isThumbnailsExist = File(SDKManager.getInstance().newEPubFixedBaseUrl + activity!!.getString(R.string.epub_fixed_thumbnail_path)).exists()
        if (Utility.isDeviceTypeMobile(activity) && isThumbnailsExist) {
            if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
                return inflater.inflate(R.layout.thumbnail_epub_fixed_mobile, container, false)
            } else {
                return inflater.inflate(R.layout.thumbnail_epub_fixed_mobile_land, container, false)
            }
        }
        return inflater.inflate(R.layout.thumbnail_epubfixed, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setPageData()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        setPageData()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        ctx = context
    }

    private fun initView(view: View) {
        initViews(view)
        setUpIconFonts()

        setupSeekBar()
        customTypeFace = Typefaces.get(activity, activity!!.resources
                .getString(R.string.text_font_file))

    }

    private fun setPageData() {

        mGotoPgTxt!!.post {

            ctx.let {
                if (it.resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {

                    mGotoPgTxt!!.setText(thumbList!!.get(SDKManager.getInstance().historyPageIndex).text)

                } else {
                    var pageIndex = SDKManager.getInstance().historyPageIndex

                    if (thumbList!!.size == pageIndex) {
                        pageIndex = pageIndex - 1
                    } else if (thumbList!!.size - 1 < pageIndex) {
                        return@post
                    }
                    mGotoPgTxt!!.setText(thumbList!!.get(pageIndex).text)
                    Log.e("TAG", "pageIndex Updated: " + pageIndex)

                }
                mGotoPgTxt!!.setSelection(mGotoPgTxt!!.length())
                thumbnailPosition = SDKManager.getInstance().historyPageIndex
            }

        }
    }

    private fun initViews(view: View) {

        mControllerButton = view.findViewById<View>(R.id.controllerbuttons) as RelativeLayout
        tvThumb = view.findViewById<View>(R.id.tvThumb) as TextView
        tvThumb!!.visibility = GONE
        mPageHistPrevious = view.findViewById<View>(R.id.btnHistoryPrevious) as Button
        mPagenavmainID = view.findViewById<View>(R.id.pagenav_mainID) as RelativeLayout
        this.thumbnailImagelayout = view.findViewById<View>(R.id.thumbnailImagelayout) as RecyclerView
        epubfixedThumbnailAdapter = EPUBFIXEDThumbnailAdapter(activity, mReaderThemeSettingVo, this, this)
        if (isThumbnailsExist) {
            thumbList?.let { setAdapter(it) }
            thumbnailImagelayout?.adapter = epubfixedThumbnailAdapter
        } else {
            thumbnailImagelayout?.visibility = GONE
        }

        mPageHistPrevious!!.setTextColor(Color.parseColor(mReaderThemeSettingVo!!.reader.dayMode.thumbnailSlider.iconColor))
        mPageHistPrevious!!.setOnClickListener(this)
        mPageHistNext = view.findViewById<View>(R.id.btnHistoryNext) as Button
        mPageHistNext!!.setTextColor(Color.parseColor(mReaderThemeSettingVo!!.reader.dayMode.thumbnailSlider.iconColor))
        mDummyView = view.findViewById(R.id.dummy_center_view)
        mPageHistNext!!.setOnClickListener(this)

        mTotalPageCount = view.findViewById<View>(R.id.total_page) as TextView
        mTotalPageCount!!.setTextColor(Color.parseColor(mReaderThemeSettingVo!!.reader.dayMode.thumbnailSlider.textColor))
        mGotoPgTxt = view.findViewById<View>(R.id.goto_pg_no) as KEditText
        mGotoPgTxt!!.setTextColor(Color.parseColor(mReaderThemeSettingVo!!.reader.dayMode.thumbnailSlider.textColor))
        //mGotoPgTxt!!.requestFocus()

        if (resources.getBoolean(R.bool.is_BE_Publishing)) {
            mGotoPgTxt!!.setTextColor(Color.BLACK)
            mTotalPageCount!!.setTextColor(Color.BLACK)
            mPageHistPrevious!!.setTextColor(Color.BLACK)
            mPageHistNext!!.setTextColor(Color.BLACK)
        }
        mGotoPgTxt!!.setOnEditorActionListener(OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_GO) {
                val pageNo = mGotoPgTxt!!.text.toString().trim().toLowerCase(Locale.ROOT)
                mListner!!.onGotoClick(pageNo)
                hideKeyboard(context, v)
                return@OnEditorActionListener true
            }
            false
        })


        mSeekbar = view.findViewById<View>(R.id.seekbarProgress) as SeekBar
        mTotalPageCount!!.text = "/" + thumbList!!.get(thumbList!!.size - 1).text
        var paintText: Paint
        paintText = Paint(Paint.ANTI_ALIAS_FLAG)
        paintText.strokeWidth = 1f
        paintText.color = Color.parseColor(mReaderThemeSettingVo!!.help.textColor)
        paintText.isAntiAlias = true
        paintText.textSize = tvThumb!!.textSize


        tvThumb!!.text = thumbList!!.get(0).text

        mSeekbar!!.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onStopTrackingTouch(seekBar: SeekBar) {
                if (::thumbVo.isInitialized && !isThumbnailsExist) {
                    mListner!!.navigate(thumbVo.src)
                }

                tvThumb!!.visibility = View.GONE
                mSeekbar!!.thumb = resources.getDrawable(R.drawable.scrollbar_thumb)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
                mSeekbar!!.thumb = resources.getDrawable(R.drawable.seekbar_thumb_on_progress)
                tvThumb!!.visibility = if (isThumbnailsExist) View.GONE else View.VISIBLE
            }

            override fun onProgressChanged(seekBar: SeekBar, progress: Int,
                                           fromUser: Boolean) {


                if (fromUser) {

                    thumbVo = thumbList!!.get(progress)
                    scrollToPage(progress)
//                    thumbnailPosition = progress
                    thumbnailImagelayout?.scrollToPosition(progress)
                    android.util.Log.d("Page", progress.toString())
                } else {

                    tvThumb!!.post(Runnable {
                        val textWidth: Float = paintText.measureText("Page " + thumbList!!.get(progress).text)
                        val mProgress = progress * (seekBar.width - 2 * seekBar.thumbOffset) / seekBar.max
                        tvThumb!!.text = "Page " + thumbList!!.get(progress).text
                        //X1 is the original x axis value, to add extra,
                        // adding extra X values (30) to fit tvThumb on the 0th position and not cut in screen
                        val X1 = (seekBar.x + mProgress + seekBar.thumbOffset / 2) - (textWidth / 2)

                        val Y1 = (seekBar.y + mProgress + seekBar.thumbOffset / 2) - (textWidth / 2)
                        if (isThumbnailsExist) {
                            tvThumb!!.y = Y1 + 30
                        } else {
                            tvThumb!!.x = X1 + 30
                        }
                    })
                }

                val textWidth: Float = paintText.measureText("Page " + thumbList!!.get(progress).text)
                val mProgress = progress * (seekBar.width - 2 * seekBar.thumbOffset) / seekBar.max
                tvThumb!!.text = "Page " + thumbList!!.get(progress).text
                //X1 is the original x axis value, to add extra,
                // adding extra X values (30) to fit tvThumb on the 0th position and not cut in screen
                val Y1 = (seekBar.y + mProgress + seekBar.thumbOffset / 2) - (textWidth / 2)
                val X1 = (seekBar.x + mProgress + seekBar.thumbOffset / 2) - (textWidth / 2)
                if (isThumbnailsExist) {
                    tvThumb!!.y = Y1 + 30
                } else {
                    tvThumb!!.x = X1 + 30
                }

                tvThumb!!.post(Runnable {

                    val thumbrect = Rect()

                    (tvThumb as View).getGlobalVisibleRect(thumbrect)

                    val rect = Rect()

                    (seekBar.parent as View).getGlobalVisibleRect(rect)
                    rect.left -= (paintText.measureText(thumbList!!.get(progress).text) / 2).roundToInt()

                    if (rect != null) {
                        val isInsideScreen = checkCornersIfStickyNoteButtonSpaceAvail(thumbrect, rect, paintText, progress)

                        if (isThumbnailsExist) {
                            if (!isInsideScreen)
                                tvThumb!!.y = (seekBar.y + mProgress + seekBar.thumbOffset / 2) - (textWidth)

                            if (progress == 0) {
                                tvThumb!!.y = (seekBar.y + mProgress + seekBar.thumbOffset)
                            }
                        } else {
                            if (!isInsideScreen)
                                tvThumb!!.x = (seekBar.x + mProgress + seekBar.thumbOffset / 2) - (textWidth)

                            if (progress == 0) {
                                tvThumb!!.x = (seekBar.x + mProgress + seekBar.thumbOffset)
                            }
                        }

                    }
                })

            }
        })

        mSeekbar!!.post {
            var pageIndex = SDKManager.getInstance().historyPageIndex

            ctx.let {
                if (it.resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {

                    mSeekbar!!.progress = pageIndex

                } else {

                    /*  if (pageIndex == 0) {
                          pageIndex = pageIndex + 1
                      } else {
                          //pageIndex = pageIndex * 2
                         // pageIndex = pageIndex + 1
                      }*/

                    mSeekbar!!.progress = pageIndex
                }
            }
        }
        setHistoryButtonNavigationmode()
        mControllerButton!!.visibility = View.VISIBLE
        if (mShowHistoryButtons) {
            mPageHistPrevious!!.visibility = View.VISIBLE
            mPageHistNext!!.visibility = View.VISIBLE
        } else {
            mPageHistPrevious!!.visibility = View.GONE
            mPageHistNext!!.visibility = View.GONE
        }
    }

    fun setData(thumbListVO: ArrayList<ThumbListVO>) {
        thumbList = thumbListVO
    }

    /**
     * This method will set thumbnail data to the adapter
     */
    private fun setAdapter(thumbListVO: java.util.ArrayList<ThumbListVO>) {
        if (!Utility.isDeviceTypeMobile(context) &&
                context!!.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
        /* && thumbnailPosition == 0*/) {
            thumbListVO[0].isSelected = true
            thumbListVO[1].isSelected = true
            thumbnailPosition = SDKManager.getInstance().historyPageIndex
            if (thumbnailPosition % 2 == 0) {
                thumbnailPosition = thumbnailPosition
            } else {
                thumbnailPosition -= 1
            }
        } else {
            thumbnailPosition = SDKManager.getInstance().historyPageIndex
        }


        epubfixedThumbnailAdapter.setData(thumbListVO)
        epubfixedThumbnailAdapter.notifyDataSetChanged()
        thumbnailImagelayout?.scrollToPosition(thumbnailPosition)
        android.util.Log.d("page", SDKManager.getInstance().historyPageIndex.toString())
        android.util.Log.d("page thumbnailPosition", thumbnailPosition.toString())

    }

    fun setThemeColor(_themeUserSettingVo: ReaderThemeSettingVo?) {
        mReaderThemeSettingVo = _themeUserSettingVo
    }

    fun setThumbListener(listner: CustomThumbnailsListner) {
        mListner = listner
    }

    fun scrollToPage(pageID: Int) {
        position = pageID
    }

    private fun setupSeekBar() {
        val colornew = Color.parseColor(mReaderThemeSettingVo!!.reader.dayMode.thumbnailSlider.sliderFilledColor)
        mSeekbar!!.progressDrawable.setColorFilter(resources.getColor(R.color.transparent), PorterDuff.Mode.SRC_ATOP)
        val gradient = GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP, intArrayOf(colornew, colornew))
        gradient.shape = GradientDrawable.OVAL
        gradient.setSize(resources.getInteger(R.integer.seekbar_thumb_size), resources.getInteger(R.integer.seekbar_thumb_size))
        gradient.setStroke(0, Color.BLACK)
        mSeekbar!!.thumb = resources.getDrawable(R.drawable.scrollbar_thumb)
        mSeekbar!!.progressDrawable = resources.getDrawable(R.drawable.thumbnail_progress_seek)
        mSeekbar!!.max = thumbList!!.size - 1
        mListner?.onSeekbarViewCreated(mSeekbar)

    }


    fun setHistoryButtonNavigationmode() {
        mListner?.onPageHistoryButtonsCreated(mPageHistNext, mPageHistPrevious)
    }

    private fun setUpIconFonts() {
        getReaderTyface()
        mPageHistPrevious!!.typeface = mTypeFace
        mPageHistPrevious!!.isAllCaps = false
        mPageHistPrevious!!.text = PlayerUIConstants.TG_HISTORY_PREV_IC_TEXT
        mPageHistNext!!.typeface = mTypeFace
        mPageHistNext!!.isAllCaps = false
        mPageHistNext!!.text = PlayerUIConstants.TG_HISTORY_NEXT_IC_TEXT
        if (!activity!!.resources.getBoolean(R.bool.show_history_navigation)) {
            mPageHistPrevious!!.visibility = View.GONE
            mPageHistNext!!.visibility = View.GONE
            mDummyView!!.visibility = View.GONE
        }
    }

    override fun onClick(v: View) {
        if (v.id == R.id.btnHistoryPrevious) {
            mListner?.NavigatePreviousPage()
            thumbnailPosition = SDKManager.getInstance().previousPage
            epubfixedThumbnailAdapter.notifyDataSetChanged()
        } else if (v.id == R.id.btnHistoryNext) {
            mListner?.NavigateNextPage()
            thumbnailPosition = SDKManager.getInstance().nextPage
            epubfixedThumbnailAdapter.notifyDataSetChanged()
        } else if (v.id == R.id.goto_pg_btn) {
            val pageNo = mGotoPgTxt!!.text.toString().trim().toLowerCase(Locale.ROOT)
            mListner!!.onGotoClick(pageNo)
            hideKeyboard(context, v)
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        mSeekbar = null
        mRootView = null
        hideKeyboard(context, mGotoPgTxt)
    }

    fun hideKeyboard(context: Context?, view: View?) {

        try {
            if (view != null) {
                val inputManager = context!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputManager.hideSoftInputFromWindow(view.windowToken, 0)
                view.clearFocus()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun getReaderTyface() {
        val fontfile = Utils.getFontFilePath()
        mTypeFace = if (fontfile != null && !fontfile.isEmpty()) {
            Typefaces.get(activity!!.baseContext, fontfile)
        } else {
            Typefaces.get(activity!!.baseContext, "kitabooread.ttf")
        }
    }

    fun showHistoryButtons(b: Boolean) {
        mShowHistoryButtons = b
    }

    override fun changePosition(position: Int) {
        mSeekbar?.let {
            it.progress = position
        }
    }

    override fun onTouch(p0: View?, p1: MotionEvent?): Boolean {
        TODO("Not yet implemented")
    }

    private fun checkCornersIfStickyNoteButtonSpaceAvail(point: Rect, rectA: Rect, paintText: Paint, progress: Int): Boolean {

        var margin = (paintText.measureText("Page " + thumbList!!.get(progress).text)).roundToInt()
        point.right += (margin / 2)

        return rectA.contains(point)
    }

    override fun onThumbnailSelect(position: String) {
        mListner!!.onGotoClick(position.trim { it <= ' ' })
    }

}