package com.hurix.kitaboocloud.helpscreen

import android.content.Context
import android.content.res.Configuration
import android.graphics.Color
import android.text.SpannableString
import android.text.TextUtils
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.util.DisplayMetrics
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.hurix.commons.Constants.Constants
import com.hurix.commons.Constants.EBookType
import com.hurix.commons.Constants.PlayerUIConstants
import com.hurix.commons.datamodel.IBook
import com.hurix.commons.sdkDatamodel.SDKManager
import com.hurix.commons.utils.Utils
import com.hurix.customui.actionbar.KitabooActionItemView


import com.hurix.kitaboo.constants.ShelfUIConstants
import com.hurix.kitaboo.iconify.Typefaces
import com.hurix.kitaboocloud.PlayerController
import com.hurix.kitaboocloud.R
import com.hurix.kitaboocloud.common.HelpVo

import com.hurix.kitaboocloud.kitaboosdkrenderer.PlayerActivity
import com.hurix.kitaboocloud.kitaboosdkrenderer.customviews.CustomCompoundView
import com.hurix.kitaboocloud.kitaboosdkrenderer.customviews.CustomKitabooActionbar
import com.hurix.kitaboocloud.kitaboosdkrenderer.customviews.CustomKitabooTopActionbar
import com.hurix.kitaboocloud.kitaboosdkrenderer.customviews.CustomPlayerUIConstants
import com.hurix.kitaboocloud.model.JWTokenResponseVO
import com.hurix.kitaboocloud.sdkreadertheme.InsightReaderThemeController
import com.hurix.kitaboocloud.sdkreadertheme.themePojo.ReaderThemeSettingVo
import com.hurix.renderer.utility.Utility
import java.util.*

object ActionbarUtils {
    private lateinit var mTeacherRedo: KitabooActionItemView
    private lateinit var mTeacherUndo: KitabooActionItemView
    private lateinit var mTeacherEraser: KitabooActionItemView
    private lateinit var mPageZoomOutIcon: KitabooActionItemView
    private lateinit var mZoomPercentageText: KitabooActionItemView
    private lateinit var mPageZoomIcon: KitabooActionItemView
    private lateinit var mPageModeIcon: KitabooActionItemView
    var jwTokenDetails: JWTokenResponseVO? = null
    private var bottomContainerHeight: Int = 0
    private var bottomContainerHeightNext: Int = 0
    private var extraWeightAdded: Boolean = false
    private var extraPreviousWeightAdded: Boolean = false
    private var bottomCount: Int = 0
    private var marginTop: Int = 0
    private var marginTopNextPosition: Int = 0
    private var textViewId = 10001
    private var textDescriptionId = 20001
    lateinit var bookVo: IBook
    private lateinit var mEpubSettingPanel: KitabooActionItemView
    var reflowPrintEanablePageList: ArrayList<String>? = arrayListOf()
    var reflowPrintEnable: String = ""
    private lateinit var mClearAllImage: KitabooActionItemView
    private lateinit var mReadAloudIcon: KitabooActionItemView
    var isMobile: Boolean = false
    var audioBookType: String = ""
    var topActionbar: CustomKitabooTopActionbar? = null
    private var mBottomActionBarItemColor: Int = 0
    private var mBottomActionBarBackGroundColor: Int = 0
    var protractorenable: String = ""
    var IsClassAccociated: Boolean = false
    var IsHelpScreenVisible: Boolean = false
    var accountType: String = ""
    private var readerThemeSettingVo: ReaderThemeSettingVo? = null
    lateinit var bottomActionbar: CustomKitabooActionbar
    private var mTopActionbarBackGroundColor = 0
    private var mTopActionBarItemColor: kotlin.Int = 0
    private var mPentoolToolbarBackgroundColor: kotlin.Int = 0
    private var mPentoolToolbarItemColor: kotlin.Int = 0
    var mReaderType: EBookType = EBookType.DEFAULT
    var bottomActionBarItems = 0
    private var pageOneCount: Int = 2
    var pageTwoCount: Int = 2

    /**
     * Customize the bottom actionbar
     */
    fun setUpBottomBar(
            context: Context,
            bottomActionbar: CustomKitabooActionbar) {
        this.bottomActionbar = bottomActionbar
        mEpubSettingPanel = KitabooActionItemView(context)

        setActionbarGroundColor(context)

        if (SDKManager.getInstance().isNewTeacherReviewModeOn) {
            ActionbarUtils.bottomActionbar.visibility = View.INVISIBLE

            mTeacherEraser = KitabooActionItemView(context)
            mTeacherUndo = KitabooActionItemView(context)
            mTeacherRedo = KitabooActionItemView(context)
            var mTeacherPrevious = KitabooActionItemView(context)
            var mTeacherNext = KitabooActionItemView(context)
            var mTeacherReviewGreen = KitabooActionItemView(context)
            var mTeacherReviewRed = KitabooActionItemView(context)
            var mTeacherReviewDone = KitabooActionItemView(context)
            var mTeacherReviewFirstname = KitabooActionItemView(context)
            var mTeacherReviewPage = KitabooActionItemView(context)
            var mTeacherReviewProfileImage = ImageView(context)
            var mTeacherClearAll = KitabooActionItemView(context)


            setCustomViewActionbar(mTeacherReviewProfileImage, R.id.teacher_review_profile_image, "", mPentoolToolbarItemColor, Gravity.RIGHT, 100, context)

            setBottomActionbar(mTeacherReviewGreen, R.id.teacher_review_green, "F", CustomPlayerUIConstants.PT_COLOR_IC_TEXT, Color.parseColor(readerThemeSettingVo!!.reader.dayMode.teacherSettings.pen2Color), Gravity.LEFT, context.resources.getInteger(R.integer.teacher_review_bottombar_margin), 18, context)

            setBottomActionbar(mTeacherReviewRed, R.id.teacher_review_red, "F", CustomPlayerUIConstants.PT_COLOR_IC_TEXT, Color.parseColor(readerThemeSettingVo!!.reader.dayMode.teacherSettings.pen1Color), Gravity.LEFT, context.resources.getInteger(R.integer.teacher_review_bottombar_margin), 18, context)

            /*    mTeacherReviewRed.setPadding(2, 2, 2, 2)
                drawable.setShape(GradientDrawable.OVAL)
                drawable.setStroke(2, Color.parseColor(readerThemeSettingVo!!.reader.dayMode.pentool.toolbar.selectedIconBackground))
                mTeacherReviewRed.setBackgroundDrawable(drawable)*/

            setBottomActionbar(mTeacherEraser, R.id.teacher_review_eraser, "D", CustomPlayerUIConstants.TEACHER_ACTIONBAR_ERASER, mPentoolToolbarItemColor, Gravity.LEFT, context.resources.getInteger(R.integer.teacher_review_bottombar_margin), 20, context)

            setBottomActionbar(mTeacherUndo, R.id.teacher_review_undo, "A", "<", mPentoolToolbarItemColor, Gravity.LEFT, context.resources.getInteger(R.integer.teacher_review_bottombar_margin), 20, context)

            if (context.resources.getBoolean(R.bool.enable_TeacherReview_clearAll)) {
                setBottomActionbar(mTeacherClearAll, R.id.teacher_review_clear_all, "", CustomPlayerUIConstants.TEACHER_ACTIONBAR_CLEARALL, mPentoolToolbarItemColor, Gravity.LEFT, context.resources.getInteger(R.integer.teacher_review_bottombar_margin), 20, context)
            }

            setBottomActionbar(mTeacherReviewDone, R.id.teacher_review_done, "G", context.resources.getString(R.string.done), mPentoolToolbarItemColor, Gravity.LEFT, context.resources.getInteger(R.integer.teacher_review_bottombar_margin), 15, context)
        } else {
            var actionItemHome: KitabooActionItemView? = null
            val textAnnotation: KitabooActionItemView? = null
            var item1: KitabooActionItemView? = null
            var item2: KitabooActionItemView? = null
            var item3: KitabooActionItemView? = null
            var item4: KitabooActionItemView? = null
            var item5: KitabooActionItemView? = null
            var item6: KitabooActionItemView? = null
            var item7: KitabooActionItemView? = null
            var item8: KitabooActionItemView? = null
            actionItemHome = KitabooActionItemView(context)
            item1 = KitabooActionItemView(context)
            item2 = KitabooActionItemView(context)
            item3 = KitabooActionItemView(context)
            item4 = KitabooActionItemView(context)
            item5 = KitabooActionItemView(context)
            item6 = KitabooActionItemView(context)
            item7 = KitabooActionItemView(context)
            item8 = KitabooActionItemView(context)


            /* TODO For Infobase client Menu icons will be differ based on User type*/if (context.resources.getBoolean(R.bool.is_Infobase_Client)) {
                if (jwTokenDetails != null && jwTokenDetails?.userType.equals("GenericAccount", ignoreCase = true)) {
                    /* TODO For Generic users only TOC and Search icons will be visible */
                    addMenuItemsToTheActionbarForGenericAccount(actionItemHome, item1, item2, context)
                } else {
                    /* TODO For user specific account all icons should be visible.*/
                    addMenuItemsToTheActionbar(actionItemHome, item1, item2, item3, item4, item5, item6, item7, item8, context)
                }
            } else if (context.resources.getBoolean(R.bool.is_storylt_client)) {
                addMenuItemsToTheActionbarforstorylt(actionItemHome, item1, item2, item3, item4, context)
            } else if (context.resources.getBoolean(R.bool.is_FOSS)) {
                addMenuItemsToTheActionbarForChromebook(actionItemHome, item1, item2, item3, item4, item5, context)
            } else if (context.resources.getBoolean(R.bool.is_ESE_client)) {
                addMenuItemsToTheActionbarForChromebookESE(actionItemHome, item1, item2, item3, item4, item5, item6, item7, context)
            } else if (context.resources.getBoolean(R.bool.is_ADA_Client)) {
                addMenuItemsToTheActionbarForADA(actionItemHome, item1, item2, item3, item4, item5, item6, item7, context)
            } else {
                addMenuItemsToTheActionbar(actionItemHome, item1, item2, item3, item4, item5, item6, item7, item8, context)
            }
        }
//        bottomActionbar.setBackgroundDrawable(Utils.getRectAngleDrawable(mTopActionbarBackGroundColor, floatArrayOf(2f, 2f, 2f, 2f, 2f, 2f, 2f, 2f), 1, Color.parseColor(readerThemeSettingVo!!.getReader().getDayMode().getTableofcontents().getPopupBorder())))
        bottomActionbar.build()
    }

    private fun addMenuItemsToTheActionbarForADA(actionItemHome: KitabooActionItemView, item1: KitabooActionItemView, item2: KitabooActionItemView, item3: KitabooActionItemView, item4: KitabooActionItemView, item5: KitabooActionItemView, item6: KitabooActionItemView, item7: KitabooActionItemView, context: Context) {
        val item10 = KitabooActionItemView(context)
        if (isMobile) {
            if (context.resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
                if (context.resources.getBoolean(R.bool.is_sparkCapital_client)) setBottomActionbar(actionItemHome, R.id.action_home, "A", CustomPlayerUIConstants.TOP_ACTION_HOME_TEXT, mBottomActionBarItemColor, Gravity.LEFT, 0, context.resources.getInteger(R.integer.action_icon_size), context)
                if (mReaderType == EBookType.FIXEDKITABOO) {
                    if (!context.resources.getBoolean(R.bool.is_sparkCapital_client)) setBottomActionbar(item1, R.id.action_toc, "B", CustomPlayerUIConstants.ACTION_TOC_TEXT, mBottomActionBarItemColor, Gravity.LEFT, context.resources.getInteger(R.integer.mobile_bottombar_pdf_toc_margin), context.resources.getInteger(R.integer.action_icon_size), context)
                    setBottomActionbar(item2, R.id.action_my_data, "C", CustomPlayerUIConstants.ACTION_MYDATA_TEXT, mBottomActionBarItemColor, Gravity.LEFT, context.resources.getInteger(R.integer.mobile_bottombar_pdf_common_margin), context.resources.getInteger(R.integer.action_icon_size), context)
                    setBottomActionbar(item3, R.id.action_search, "D", CustomPlayerUIConstants.ACTION_SEARCH_TEXT, mBottomActionBarItemColor, Gravity.LEFT, context.resources.getInteger(R.integer.mobile_bottombar_pdf_common_margin), context.resources.getInteger(R.integer.action_icon_size), context)
                    setBottomActionbar(item4, R.id.action_pen, "E", CustomPlayerUIConstants.ACTION_PEN_TEXT, mBottomActionBarItemColor, Gravity.LEFT, context.resources.getInteger(R.integer.mobile_bottombar_pdf_common_margin), context.resources.getInteger(R.integer.action_icon_size), context)
                    setBottomActionbar(item5, R.id.action_sticky_note, "B", CustomPlayerUIConstants.NOTE_ICON_TEXT, mBottomActionBarItemColor, Gravity.LEFT, context.resources.getInteger(R.integer.mobile_bottombar_pdf_common_margin), context.resources.getInteger(R.integer.action_icon_size), context)
                    setBottomActionbar(item6, R.id.action_thumbnail, "9", CustomPlayerUIConstants.ACTION_THUMBNAIL_TEXT, mBottomActionBarItemColor, Gravity.LEFT, 10, context.resources.getInteger(R.integer.action_icon_size), context)
                    if (context.resources.getBoolean(R.bool.show_data_submit)) {
                        if (PlayerController.getInstance(context).isUgcShareEnabled) {
                            if (accountType == Constants.TEACHER) {
                                if (context.resources.getBoolean(R.bool.is_voyger_client)) {
                                    if (IsClassAccociated) {
                                        setBottomActionbar(item7, R.id.topbar_review, "G", PlayerUIConstants.TB_TEACHER_IC_TEXT, mBottomActionBarItemColor, Gravity.LEFT, context.resources.getInteger(R.integer.mobile_bottombar_pdf_common_margin), context.resources.getInteger(R.integer.action_icon_size), context)
                                    }
                                } else {
                                    setBottomActionbar(item7, R.id.topbar_review, "G", PlayerUIConstants.TB_TEACHER_IC_TEXT, mBottomActionBarItemColor, Gravity.LEFT, context.resources.getInteger(R.integer.mobile_bottombar_pdf_common_margin), context.resources.getInteger(R.integer.action_icon_size), context)
                                }
                            } else {
                                setBottomActionbar(item7, R.id.topbar_review, "G", PlayerUIConstants.TB_STUDENT_IC_TEXT, mBottomActionBarItemColor, Gravity.LEFT, context.resources.getInteger(R.integer.mobile_bottombar_pdf_common_margin), context.resources.getInteger(R.integer.action_icon_size), context)
                            }
                        }
                    }
                    if (protractorenable != null && protractorenable.equals("yes", ignoreCase = true)) {
                        setBottomActionbar(item10, R.id.text_protractor, "p", PlayerUIConstants.TB_PROTRACTOR_IC_TEXT, mBottomActionBarItemColor, Gravity.LEFT, 0, context.resources.getInteger(R.integer.action_icon_size), context)
                    }
                } else if (mReaderType == EBookType.REFLOWEPUB) {
                    if (!context.resources.getBoolean(R.bool.is_sparkCapital_client)) setBottomActionbar(item1, R.id.action_toc, "B", CustomPlayerUIConstants.ACTION_TOC_TEXT, mBottomActionBarItemColor, Gravity.LEFT, context.resources.getInteger(R.integer.mobile_bottombar_epub_toc_margin), context.resources.getInteger(R.integer.action_icon_size), context)
                    setBottomActionbar(item2, R.id.action_my_data, "C", CustomPlayerUIConstants.ACTION_MYDATA_TEXT, mBottomActionBarItemColor, Gravity.LEFT, context.resources.getInteger(R.integer.mobile_bottombar_epub_common_margin), context.resources.getInteger(R.integer.action_icon_size), context)
                    setBottomActionbar(item3, R.id.action_search, "D", CustomPlayerUIConstants.ACTION_SEARCH_TEXT, mBottomActionBarItemColor, Gravity.LEFT, context.resources.getInteger(R.integer.mobile_bottombar_epub_search_margin), context.resources.getInteger(R.integer.action_icon_size), context)
                    setBottomActionbar(mEpubSettingPanel, R.id.action_font_settings, "C", CustomPlayerUIConstants.ACTION_FONT_SETTING, mBottomActionBarItemColor, Gravity.RIGHT, 5, context.resources.getInteger(R.integer.action_icon_size), context)
                } else if (mReaderType == EBookType.FIXEDEPUB) {
                    if (!context.resources.getBoolean(R.bool.is_sparkCapital_client)) setBottomActionbar(item1, R.id.action_toc, "B", CustomPlayerUIConstants.ACTION_TOC_TEXT, mBottomActionBarItemColor, Gravity.LEFT, context.resources.getInteger(R.integer.mobile_bottombar_fixed_epub_margin), context.resources.getInteger(R.integer.action_icon_size), context)
                    setBottomActionbar(item2, R.id.action_my_data, "C", CustomPlayerUIConstants.ACTION_MYDATA_TEXT, mBottomActionBarItemColor, Gravity.LEFT, context.resources.getInteger(R.integer.mobile_bottombar_fixed_epub_common_margin), context.resources.getInteger(R.integer.action_icon_size), context)
                    setBottomActionbar(item3, R.id.action_search, "D", CustomPlayerUIConstants.ACTION_SEARCH_TEXT, mBottomActionBarItemColor, Gravity.LEFT, context.resources.getInteger(R.integer.mobile_bottombar_fixed_epub_common_margin), context.resources.getInteger(R.integer.action_icon_size), context)
                    setBottomActionbar(item4, R.id.action_pen, "E", CustomPlayerUIConstants.ACTION_PEN_TEXT, mBottomActionBarItemColor, Gravity.LEFT, context.resources.getInteger(R.integer.mobile_bottombar_fixed_epub_common_margin), context.resources.getInteger(R.integer.action_icon_size), context)
                    setBottomActionbar(item5, R.id.action_sticky_note, "B", CustomPlayerUIConstants.NOTE_ICON_TEXT, mBottomActionBarItemColor, Gravity.LEFT, context.resources.getInteger(R.integer.mobile_bottombar_fixed_epub_common_margin), context.resources.getInteger(R.integer.action_icon_size), context)
                }
            } else {
                if (mReaderType == EBookType.FIXEDKITABOO) {
                    if (context.resources.getBoolean(R.bool.is_sparkCapital_client)) setBottomActionbar(actionItemHome, R.id.action_home, "A", CustomPlayerUIConstants.TOP_ACTION_HOME_TEXT, mBottomActionBarItemColor, Gravity.LEFT, context.resources.getInteger(R.integer.mobile_bottombar_left_margin), context.resources.getInteger(R.integer.action_icon_size), context)
                    if (!context.resources.getBoolean(R.bool.is_sparkCapital_client)) setBottomActionbar(item1, R.id.action_toc, "B", "b", mBottomActionBarItemColor, Gravity.LEFT, context.resources.getInteger(R.integer.mobile_bottombar_pdf_land_margin), context.resources.getInteger(R.integer.action_icon_size), context)
                    setBottomActionbar(item2, R.id.action_my_data, "C", CustomPlayerUIConstants.ACTION_MYDATA_TEXT, mBottomActionBarItemColor, Gravity.LEFT, 0, context.resources.getInteger(R.integer.action_icon_size), context)
                    setBottomActionbar(item3, R.id.action_search, "D", CustomPlayerUIConstants.ACTION_SEARCH_TEXT, mBottomActionBarItemColor, Gravity.LEFT, 0, context.resources.getInteger(R.integer.action_icon_size), context)
                    setBottomActionbar(item4, R.id.action_pen, "E", CustomPlayerUIConstants.ACTION_PEN_TEXT, mBottomActionBarItemColor, Gravity.LEFT, 0, context.resources.getInteger(R.integer.action_icon_size), context)
                    setBottomActionbar(item5, R.id.action_sticky_note, "B", CustomPlayerUIConstants.NOTE_ICON_TEXT, mBottomActionBarItemColor, Gravity.LEFT, 0, context.resources.getInteger(R.integer.action_icon_size), context)
                    setBottomActionbar(item6, R.id.action_thumbnail, "9", CustomPlayerUIConstants.ACTION_THUMBNAIL_TEXT, mBottomActionBarItemColor, Gravity.LEFT, 0, context.resources.getInteger(R.integer.action_icon_size), context)
                    if (context.resources.getBoolean(R.bool.show_data_submit)) {
                        if (PlayerController.getInstance(context).isUgcShareEnabled) {
                            if (accountType == Constants.TEACHER) {
                                if (context.resources.getBoolean(R.bool.is_voyger_client)) {
                                    if (bookVo.IsClassAssociated()) {
                                        setBottomActionbar(item7, R.id.topbar_review, "G", PlayerUIConstants.TB_TEACHER_IC_TEXT, mBottomActionBarItemColor, Gravity.LEFT, context.resources.getInteger(R.integer.mobile_bottombar_pdf_common_margin), context.resources.getInteger(R.integer.action_icon_size), context)
                                    }
                                } else {
                                    setBottomActionbar(item7, R.id.topbar_review, "G", PlayerUIConstants.TB_TEACHER_IC_TEXT, mBottomActionBarItemColor, Gravity.LEFT, context.resources.getInteger(R.integer.mobile_bottombar_pdf_common_margin), context.resources.getInteger(R.integer.action_icon_size), context)
                                }
                            } else {
                                setBottomActionbar(item7, R.id.topbar_review, "G", PlayerUIConstants.TB_STUDENT_IC_TEXT, mBottomActionBarItemColor, Gravity.LEFT, context.resources.getInteger(R.integer.mobile_bottombar_pdf_common_margin), context.resources.getInteger(R.integer.action_icon_size), context)
                            }
                        }
                    }
                    /*if (accountType.equals(Constants.TEACHER)) {
                        setBottomActionbar(item7, R.id.topbar_review, "G", PlayerUIConstants.TB_TEACHER_IC_TEXT, mBottomActionBarItemColor, Gravity.LEFT, 30, context.getResources().getInteger(R.integer.action_icon_size),context);
                    }
                    else{
                        setBottomActionbar(item7, R.id.topbar_review, "G", PlayerUIConstants.TB_STUDENT_IC_TEXT, mBottomActionBarItemColor, Gravity.LEFT, 30, context.getResources().getInteger(R.integer.action_icon_size),context);
                    }*/if (protractorenable != null && protractorenable.equals("yes", ignoreCase = true)) {
                        setBottomActionbar(item10, R.id.text_protractor, "p", PlayerUIConstants.TB_PROTRACTOR_IC_TEXT, mBottomActionBarItemColor, Gravity.LEFT, 30, context.resources.getInteger(R.integer.action_icon_size), context)
                    }
                } else if (mReaderType == EBookType.REFLOWEPUB) {
                    if (context.resources.getBoolean(R.bool.is_sparkCapital_client)) setBottomActionbar(actionItemHome, R.id.action_home, "A", CustomPlayerUIConstants.TOP_ACTION_HOME_TEXT, mBottomActionBarItemColor, Gravity.LEFT, context.resources.getInteger(R.integer.mobile_bottombar_left_margin), context.resources.getInteger(R.integer.action_icon_size), context)
                    if (!context.resources.getBoolean(R.bool.is_sparkCapital_client)) {
                        setBottomActionbar(item1, R.id.action_toc, "B", "b", mBottomActionBarItemColor, Gravity.LEFT, context.resources.getInteger(R.integer.mobile_bottombar_epub_land_toc_margin), context.resources.getInteger(R.integer.action_icon_size), context)
                        setBottomActionbar(item2, R.id.action_my_data, "C", CustomPlayerUIConstants.ACTION_MYDATA_TEXT, mBottomActionBarItemColor, Gravity.LEFT, context.resources.getInteger(R.integer.mobile_bottombar_epub_land_common_margin), context.resources.getInteger(R.integer.action_icon_size), context)
                        setBottomActionbar(item3, R.id.action_search, "D", CustomPlayerUIConstants.ACTION_SEARCH_TEXT, mBottomActionBarItemColor, Gravity.LEFT, context.resources.getInteger(R.integer.mobile_bottombar_epub_land_common_margin), context.resources.getInteger(R.integer.action_icon_size), context)
                        setBottomActionbar(mEpubSettingPanel, R.id.action_font_settings, "C", CustomPlayerUIConstants.ACTION_FONT_SETTING, mBottomActionBarItemColor, Gravity.LEFT, context.resources.getInteger(R.integer.mobile_bottombar_epub_land_common_margin), context.resources.getInteger(R.integer.action_icon_size), context)
                    }
                } else if (mReaderType == EBookType.FIXEDEPUB) {
                    if (context.resources.getBoolean(R.bool.is_sparkCapital_client)) setBottomActionbar(actionItemHome, R.id.action_home, "A", CustomPlayerUIConstants.TOP_ACTION_HOME_TEXT, mBottomActionBarItemColor, Gravity.LEFT, context.resources.getInteger(R.integer.mobile_bottombar_left_margin_fixed_epub), context.resources.getInteger(R.integer.action_icon_size), context)
                    if (!context.resources.getBoolean(R.bool.is_sparkCapital_client)) setBottomActionbar(item1, R.id.action_toc, "B", "b", mBottomActionBarItemColor, Gravity.LEFT, context.resources.getInteger(R.integer.mobile_bottombar_epub_land_margin), context.resources.getInteger(R.integer.action_icon_size), context)
                    setBottomActionbar(item2, R.id.action_my_data, "C", CustomPlayerUIConstants.ACTION_MYDATA_TEXT, mBottomActionBarItemColor, Gravity.LEFT, context.resources.getInteger(R.integer.mobile_bottombar_epub_land_common_margin), context.resources.getInteger(R.integer.action_icon_size), context)
                    setBottomActionbar(item3, R.id.action_search, "D", CustomPlayerUIConstants.ACTION_SEARCH_TEXT, mBottomActionBarItemColor, Gravity.LEFT, context.resources.getInteger(R.integer.mobile_bottombar_epub_land_common_margin), context.resources.getInteger(R.integer.action_icon_size), context)
                    setBottomActionbar(item4, R.id.action_pen, "E", CustomPlayerUIConstants.ACTION_PEN_TEXT, mBottomActionBarItemColor, Gravity.LEFT, context.resources.getInteger(R.integer.mobile_bottombar_epub_land_common_margin), context.resources.getInteger(R.integer.action_icon_size), context)
                    setBottomActionbar(item5, R.id.action_sticky_note, "B", CustomPlayerUIConstants.NOTE_ICON_TEXT, mBottomActionBarItemColor, Gravity.LEFT, context.resources.getInteger(R.integer.mobile_bottombar_epub_land_common_margin), context.resources.getInteger(R.integer.action_icon_size), context)
                }
            }

            /*if (mReaderType == EBookType.FIXEDKITABOO) {
                setBottomActionbar(textAnnotation, R.id.text_annotation, "A", "ʧ", mBottomActionBarItemColor, Gravity.LEFT , 0);
            }*/
        } else {
            if (mReaderType == EBookType.FIXEDKITABOO) {
                if (context.resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
                    if (context.resources.getBoolean(R.bool.is_sparkCapital_client)) setBottomActionbar(actionItemHome, R.id.action_home, "A", CustomPlayerUIConstants.TOP_ACTION_HOME_TEXT, mBottomActionBarItemColor, Gravity.LEFT, context.resources.getInteger(R.integer.action_bar_bottom_pdf_margin), context.resources.getInteger(R.integer.action_icon_size), context)
                } else {
                    if (context.resources.getBoolean(R.bool.is_sparkCapital_client)) setBottomActionbar(actionItemHome, R.id.action_home, "A", CustomPlayerUIConstants.TOP_ACTION_HOME_TEXT, mBottomActionBarItemColor, Gravity.LEFT, context.resources.getInteger(R.integer.action_bar_bottom_pdf_land_margin), context.resources.getInteger(R.integer.action_icon_size), context)
                }
            } else {
                if (context.resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
                    if (mReaderType == EBookType.REFLOWEPUB) {
                        if (context.resources.getBoolean(R.bool.is_sparkCapital_client)) setBottomActionbar(actionItemHome, R.id.action_home, "A", CustomPlayerUIConstants.TOP_ACTION_HOME_TEXT, mBottomActionBarItemColor, Gravity.LEFT, context.resources.getInteger(R.integer.action_bar_bottom_epub_margin), context.resources.getInteger(R.integer.action_icon_size), context)
                    } else {
                        if (context.resources.getBoolean(R.bool.is_sparkCapital_client)) setBottomActionbar(actionItemHome, R.id.action_home, "A", CustomPlayerUIConstants.TOP_ACTION_HOME_TEXT, mBottomActionBarItemColor, Gravity.LEFT, context.resources.getInteger(R.integer.action_bar_bottom_epub_margin_fixed), context.resources.getInteger(R.integer.action_icon_size), context)
                    }
                } else {
                    if (context.resources.getBoolean(R.bool.is_sparkCapital_client)) setBottomActionbar(actionItemHome, R.id.action_home, "A", CustomPlayerUIConstants.TOP_ACTION_HOME_TEXT, mBottomActionBarItemColor, Gravity.LEFT, context.resources.getInteger(R.integer.action_bar_bottom_common_margin), context.resources.getInteger(R.integer.action_icon_size), context)
                }
            }
            /*if (!context.getResources().getBoolean(R.bool.is_sparkCapital_client)) {
                if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                    setBottomActionbar(actionItemHome, R.id.action_toc, "B", CustomPlayerUIConstants.ACTION_TOC_TEXT, mBottomActionBarItemColor, Gravity.LEFT, context.getResources().getInteger(R.integer.action_bar_bottom_pdf_margin));
                } else {
                    setBottomActionbar(actionItemHome, R.id.action_toc, "B", CustomPlayerUIConstants.ACTION_TOC_TEXT, mBottomActionBarItemColor, Gravity.LEFT, context.getResources().getInteger(R.integer.action_bar_bottom_pdf_land_margin));
                }
            }*/

            // setBottomActionbar(item2, R.id.action_my_data, "C", CustomPlayerUIConstants.ACTION_MYDATA_TEXT, mBottomActionBarItemColor, Gravity.LEFT, context.getResources().getInteger(R.integer.action_bar_bottom_common_margin));
            // setBottomActionbar(item3, R.id.action_search, "D", CustomPlayerUIConstants.ACTION_SEARCH_TEXT, mBottomActionBarItemColor, Gravity.LEFT, context.getResources().getInteger(R.integer.action_bar_bottom_common_margin));
            if (mReaderType == EBookType.FIXEDKITABOO) {
                if (!context.resources.getBoolean(R.bool.is_sparkCapital_client)) {
                    if (context.resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
                        setBottomActionbar(actionItemHome, R.id.action_toc, "B", CustomPlayerUIConstants.ACTION_TOC_TEXT, mBottomActionBarItemColor, Gravity.LEFT, context.resources.getInteger(R.integer.action_bar_bottom_pdf_margin), context.resources.getInteger(R.integer.action_icon_size), context)
                    } else {
                        setBottomActionbar(actionItemHome, R.id.action_toc, "B", CustomPlayerUIConstants.ACTION_TOC_TEXT, mBottomActionBarItemColor, Gravity.LEFT, context.resources.getInteger(R.integer.action_bar_bottom_pdf_land_margin), context.resources.getInteger(R.integer.action_icon_size), context)
                    }
                }
                setBottomActionbar(item2, R.id.action_my_data, "C", CustomPlayerUIConstants.ACTION_MYDATA_TEXT, mBottomActionBarItemColor, Gravity.LEFT, context.resources.getInteger(R.integer.action_bar_bottom_common_margin), context.resources.getInteger(R.integer.action_icon_size), context)
                setBottomActionbar(item3, R.id.action_search, "D", CustomPlayerUIConstants.ACTION_SEARCH_TEXT, mBottomActionBarItemColor, Gravity.LEFT, context.resources.getInteger(R.integer.action_bar_bottom_common_margin), context.resources.getInteger(R.integer.action_icon_size), context)
                setBottomActionbar(item4, R.id.action_pen, "E", CustomPlayerUIConstants.ACTION_PEN_TEXT, mBottomActionBarItemColor, Gravity.LEFT, context.resources.getInteger(R.integer.action_bar_bottom_common_margin), context.resources.getInteger(R.integer.action_icon_size), context)
                setBottomActionbar(item5, R.id.action_sticky_note, "B", CustomPlayerUIConstants.NOTE_ICON_TEXT, mBottomActionBarItemColor, Gravity.LEFT, context.resources.getInteger(R.integer.action_bar_bottom_common_margin), context.resources.getInteger(R.integer.action_icon_size), context)
                setBottomActionbar(item6, R.id.action_thumbnail, "9", CustomPlayerUIConstants.ACTION_THUMBNAIL_TEXT, mBottomActionBarItemColor, Gravity.LEFT, context.resources.getInteger(R.integer.action_bar_bottom_common_margin), context.resources.getInteger(R.integer.action_icon_size), context)
                // KitabooActionItemView item10 = new KitabooActionItemView(this);
                if (context.resources.getBoolean(R.bool.show_data_submit)) {
                    if (PlayerController.getInstance(context).isUgcShareEnabled) {
                        if (accountType == Constants.TEACHER) {
                            if (context.resources.getBoolean(R.bool.is_voyger_client)) {
                                if (IsClassAccociated) {
                                    setBottomActionbar(item7, R.id.topbar_review, "G", PlayerUIConstants.TB_TEACHER_IC_TEXT, mBottomActionBarItemColor, Gravity.LEFT, context.resources.getInteger(R.integer.mobile_bottombar_pdf_common_margin), context.resources.getInteger(R.integer.action_icon_size), context)
                                }
                            } else {
                                setBottomActionbar(item7, R.id.topbar_review, "G", PlayerUIConstants.TB_TEACHER_IC_TEXT, mBottomActionBarItemColor, Gravity.LEFT, context.resources.getInteger(R.integer.mobile_bottombar_pdf_common_margin), context.resources.getInteger(R.integer.action_icon_size), context)
                            }
                        } else {
                            setBottomActionbar(item7, R.id.topbar_review, "G", PlayerUIConstants.TB_STUDENT_IC_TEXT, mBottomActionBarItemColor, Gravity.LEFT, context.resources.getInteger(R.integer.mobile_bottombar_pdf_common_margin), context.resources.getInteger(R.integer.action_icon_size), context)
                        }
                    }
                }
                /*if (accountType.equals(Constants.TEACHER)) {
                    setBottomActionbar(item7, R.id.topbar_review, "G", PlayerUIConstants.TB_TEACHER_IC_TEXT, mBottomActionBarItemColor, Gravity.LEFT, context.getResources().getInteger(R.integer.action_bar_bottom_common_margin), context.getResources().getInteger(R.integer.action_icon_size),context);
                } else {
                    setBottomActionbar(item7, R.id.topbar_review, "G", PlayerUIConstants.TB_STUDENT_IC_TEXT, mBottomActionBarItemColor, Gravity.LEFT, context.getResources().getInteger(R.integer.action_bar_bottom_common_margin), context.getResources().getInteger(R.integer.action_icon_size),context);
                }*/if (protractorenable != null && protractorenable.equals("yes", ignoreCase = true)) {
                    setBottomActionbar(item10, R.id.text_protractor, "p", PlayerUIConstants.TB_PROTRACTOR_IC_TEXT, mBottomActionBarItemColor, Gravity.LEFT, context.resources.getInteger(R.integer.action_bar_bottom_common_margin), context.resources.getInteger(R.integer.action_icon_size), context)
                }
            } else {
                val displayMetrics = DisplayMetrics()
                (context as PlayerActivity).windowManager.defaultDisplay.getMetrics(displayMetrics)
                val height = displayMetrics.heightPixels
                val width = displayMetrics.widthPixels
                if (mReaderType == EBookType.REFLOWEPUB) {
                    if (!context.getResources().getBoolean(R.bool.is_sparkCapital_client)) {
                        if (context.getResources().configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
                            setBottomActionbar(actionItemHome, R.id.action_toc, "B", CustomPlayerUIConstants.ACTION_TOC_TEXT, mBottomActionBarItemColor, Gravity.LEFT, width / 5, context.getResources().getInteger(R.integer.action_icon_size), context)
                        } else {
                            setBottomActionbar(actionItemHome, R.id.action_toc, "B", CustomPlayerUIConstants.ACTION_TOC_TEXT, mBottomActionBarItemColor, Gravity.LEFT, width / 5, context.getResources().getInteger(R.integer.action_icon_size), context)
                        }
                    }
                    setBottomActionbar(item2, R.id.action_my_data, "C", CustomPlayerUIConstants.ACTION_MYDATA_TEXT, mBottomActionBarItemColor, Gravity.LEFT, width / 10, context.getResources().getInteger(R.integer.action_icon_size), context)
                    setBottomActionbar(item3, R.id.action_search, "D", CustomPlayerUIConstants.ACTION_SEARCH_TEXT, mBottomActionBarItemColor, Gravity.LEFT, width / 10, context.getResources().getInteger(R.integer.action_icon_size), context)
                    setBottomActionbar(mEpubSettingPanel, R.id.action_font_settings, "C", CustomPlayerUIConstants.ACTION_FONT_SETTING, mBottomActionBarItemColor, Gravity.LEFT, width / 10, context.getResources().getInteger(R.integer.action_icon_size), context)
                }
            }
            if (mReaderType == EBookType.FIXEDEPUB) {
                val screenSize: Int = context.resources.configuration.screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK
                val density: Int = context.resources.displayMetrics.densityDpi
                if (!context.resources.getBoolean(R.bool.is_sparkCapital_client)) {
                    if (context.resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
                        if (screenSize == Configuration.SCREENLAYOUT_SIZE_XLARGE && density == DisplayMetrics.DENSITY_XHIGH) {
                            setBottomActionbar(actionItemHome, R.id.action_toc, "B", CustomPlayerUIConstants.ACTION_TOC_TEXT, mBottomActionBarItemColor, Gravity.LEFT, context.resources.getInteger(R.integer.bottom_action_bar_FixedEpub_margin_320), context.resources.getInteger(R.integer.action_icon_size), context)
                        } else setBottomActionbar(actionItemHome, R.id.action_toc, "B", CustomPlayerUIConstants.ACTION_TOC_TEXT, mBottomActionBarItemColor, Gravity.LEFT, context.resources.getInteger(R.integer.bottom_action_bar_FixedEpub_margin), context.resources.getInteger(R.integer.action_icon_size), context)
                    } else {
                        if (screenSize == Configuration.SCREENLAYOUT_SIZE_XLARGE && density == DisplayMetrics.DENSITY_XHIGH) {
                            setBottomActionbar(actionItemHome, R.id.action_toc, "B", CustomPlayerUIConstants.ACTION_TOC_TEXT, mBottomActionBarItemColor, Gravity.LEFT, context.resources.getInteger(R.integer.bottom_action_bar_land_FixedEpub_margin_320), context.resources.getInteger(R.integer.action_icon_size), context)
                        } else setBottomActionbar(actionItemHome, R.id.action_toc, "B", CustomPlayerUIConstants.ACTION_TOC_TEXT, mBottomActionBarItemColor, Gravity.LEFT, context.resources.getInteger(R.integer.bottom_action_bar_land_FixedEpub_margin), context.resources.getInteger(R.integer.action_icon_size), context)
                    }
                    setBottomActionbar(item2, R.id.action_my_data, "C", CustomPlayerUIConstants.ACTION_MYDATA_TEXT, mBottomActionBarItemColor, Gravity.LEFT, context.resources.getInteger(R.integer.action_bar_bottom_fixed_epub_common_margin), context.resources.getInteger(R.integer.action_icon_size), context)
                    setBottomActionbar(item3, R.id.action_search, "D", CustomPlayerUIConstants.ACTION_SEARCH_TEXT, mBottomActionBarItemColor, Gravity.LEFT, context.resources.getInteger(R.integer.action_bar_bottom_fixed_epub_common_margin), context.resources.getInteger(R.integer.action_icon_size), context)
                    setBottomActionbar(item4, R.id.action_pen, "E", CustomPlayerUIConstants.ACTION_PEN_TEXT, mBottomActionBarItemColor, Gravity.LEFT, context.resources.getInteger(R.integer.action_bar_bottom_fixed_epub_common_margin), context.resources.getInteger(R.integer.action_icon_size), context)
                    setBottomActionbar(item5, R.id.action_sticky_note, "B", CustomPlayerUIConstants.NOTE_ICON_TEXT, mBottomActionBarItemColor, Gravity.LEFT, context.resources.getInteger(R.integer.action_bar_bottom_fixed_epub_common_margin), context.resources.getInteger(R.integer.action_icon_size), context)
                }
            }
            /*if (mReaderType == EBookType.FIXEDKITABOO) {

                setBottomActionbar(textAnnotation, R.id.text_annotation, "A", "ʧ", mBottomActionBarItemColor, Gravity.LEFT , context.getResources().getInteger(R.integer.action_bar_bottom_common_margin));
            }*/
        }
    }

    private fun addMenuItemsToTheActionbarForChromebookESE(actionItemHome: KitabooActionItemView, item1: KitabooActionItemView, item2: KitabooActionItemView, item3: KitabooActionItemView, item4: KitabooActionItemView, item5: KitabooActionItemView, item6: KitabooActionItemView, item7: KitabooActionItemView, context: Context) {
        mPageModeIcon = KitabooActionItemView(context)
        mPageZoomIcon = KitabooActionItemView(context)
        mZoomPercentageText = KitabooActionItemView(context)
        mPageZoomOutIcon = KitabooActionItemView(context)

        if (isMobile) {
            if (context.resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
                if (context.resources.getBoolean(R.bool.is_sparkCapital_client)) setBottomActionbar(actionItemHome, R.id.action_home, "A", CustomPlayerUIConstants.TOP_ACTION_HOME_TEXT, mBottomActionBarItemColor, Gravity.LEFT, 0, context.resources.getInteger(R.integer.action_icon_size), context)
                if (mReaderType == EBookType.FIXEDKITABOO) {
                    if (!context.resources.getBoolean(R.bool.is_sparkCapital_client)) setBottomActionbar(item1, R.id.action_toc, "B", CustomPlayerUIConstants.ACTION_TOC_TEXT, mBottomActionBarItemColor, Gravity.LEFT, context.resources.getInteger(R.integer.mobile_bottombar_pdf_toc_margin), context.resources.getInteger(R.integer.action_icon_size), context)
                    setBottomActionbar(item2, R.id.action_my_data, "C", CustomPlayerUIConstants.ACTION_MYDATA_TEXT, mBottomActionBarItemColor, Gravity.LEFT, context.resources.getInteger(R.integer.mobile_bottombar_pdf_common_margin), context.resources.getInteger(R.integer.action_icon_size), context)
                    setBottomActionbar(item3, R.id.action_search, "D", CustomPlayerUIConstants.ACTION_SEARCH_TEXT, mBottomActionBarItemColor, Gravity.LEFT, context.resources.getInteger(R.integer.mobile_bottombar_pdf_common_margin), context.resources.getInteger(R.integer.action_icon_size), context)
                    setBottomActionbar(item4, R.id.action_pen, "E", CustomPlayerUIConstants.ACTION_PEN_TEXT, mBottomActionBarItemColor, Gravity.LEFT, context.resources.getInteger(R.integer.mobile_bottombar_pdf_common_margin), context.resources.getInteger(R.integer.action_icon_size), context)
                    setBottomActionbar(item5, R.id.action_sticky_note, "B", CustomPlayerUIConstants.NOTE_ICON_TEXT, mBottomActionBarItemColor, Gravity.LEFT, context.resources.getInteger(R.integer.mobile_bottombar_pdf_common_margin), context.resources.getInteger(R.integer.action_icon_size), context)
                    setBottomActionbar(item6, R.id.action_thumbnail, "9", CustomPlayerUIConstants.ACTION_THUMBNAIL_TEXT, mBottomActionBarItemColor, Gravity.LEFT, 10, context.resources.getInteger(R.integer.action_icon_size), context)
                    if (context.resources.getBoolean(R.bool.show_data_submit)) {
                        if (PlayerController.getInstance(context).isUgcShareEnabled) {
                            if (accountType == Constants.TEACHER) {
                                if (context.resources.getBoolean(R.bool.is_voyger_client)) {
                                    if (IsClassAccociated) {
                                        setBottomActionbar(item7, R.id.topbar_review, "G", PlayerUIConstants.TB_TEACHER_IC_TEXT, mBottomActionBarItemColor, Gravity.LEFT, context.resources.getInteger(R.integer.mobile_bottombar_pdf_common_margin), context.resources.getInteger(R.integer.action_icon_size), context)
                                    }
                                } else {
                                    setBottomActionbar(item7, R.id.topbar_review, "G", PlayerUIConstants.TB_TEACHER_IC_TEXT, mBottomActionBarItemColor, Gravity.LEFT, context.resources.getInteger(R.integer.mobile_bottombar_pdf_common_margin), context.resources.getInteger(R.integer.action_icon_size), context)
                                }
                            } else {
                                setBottomActionbar(item7, R.id.topbar_review, "G", PlayerUIConstants.TB_STUDENT_IC_TEXT, mBottomActionBarItemColor, Gravity.LEFT, context.resources.getInteger(R.integer.mobile_bottombar_pdf_common_margin), context.resources.getInteger(R.integer.action_icon_size), context)
                            }
                        }
                    }
                }
            }
        } else if (context.resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            val displayMetrics = DisplayMetrics()
            (context as PlayerActivity).windowManager.defaultDisplay.getMetrics(displayMetrics)
            val height = displayMetrics.heightPixels
            val width = displayMetrics.widthPixels
            setBottomActionbar(item1, R.id.action_toc, "B", CustomPlayerUIConstants.ACTION_TOC_TEXT, mBottomActionBarItemColor, Gravity.LEFT, context.getResources().getInteger(R.integer.action_bar_bottom_pdf_margin), context.getResources().getInteger(R.integer.action_icon_size), context)
            setBottomActionbar(item2, R.id.action_my_data, "C", CustomPlayerUIConstants.ACTION_MYDATA_TEXT, mBottomActionBarItemColor, Gravity.LEFT, context.getResources().getInteger(R.integer.action_bar_bottom_common_margin), context.getResources().getInteger(R.integer.action_icon_size), context)
            setBottomActionbar(item3, R.id.action_search, "D", CustomPlayerUIConstants.ACTION_SEARCH_TEXT, mBottomActionBarItemColor, Gravity.LEFT, context.getResources().getInteger(R.integer.action_bar_bottom_common_margin), context.getResources().getInteger(R.integer.action_icon_size), context)
            setBottomActionbar(item4, R.id.action_pen, "E", CustomPlayerUIConstants.ACTION_PEN_TEXT, mBottomActionBarItemColor, Gravity.LEFT, context.getResources().getInteger(R.integer.action_bar_bottom_common_margin), context.getResources().getInteger(R.integer.action_icon_size), context)
            setBottomActionbar(item5, R.id.action_sticky_note, "E", CustomPlayerUIConstants.NOTE_ICON_TEXT, mBottomActionBarItemColor, Gravity.LEFT, context.getResources().getInteger(R.integer.action_bar_bottom_common_margin), context.getResources().getInteger(R.integer.action_icon_size), context)
            setBottomActionbar(item6, R.id.action_thumbnail, "9", CustomPlayerUIConstants.ACTION_THUMBNAIL_TEXT, mBottomActionBarItemColor, Gravity.LEFT, context.getResources().getInteger(R.integer.action_bar_bottom_common_margin), context.getResources().getInteger(R.integer.action_icon_size), context)
            if (context.getResources().getBoolean(R.bool.show_data_submit)) {
                if (PlayerController.getInstance(context).isUgcShareEnabled) {
                    if (accountType == Constants.TEACHER) {
                        if (context.getResources().getBoolean(R.bool.is_voyger_client)) {
                            if (IsClassAccociated) {
                                setBottomActionbar(item7, R.id.topbar_review, "G", PlayerUIConstants.TB_TEACHER_IC_TEXT, mBottomActionBarItemColor, Gravity.LEFT, context.getResources().getInteger(R.integer.mobile_bottombar_pdf_common_margin), context.getResources().getInteger(R.integer.action_icon_size), context)
                            }
                        } else {
                            setBottomActionbar(item7, R.id.topbar_review, "G", PlayerUIConstants.TB_TEACHER_IC_TEXT, mBottomActionBarItemColor, Gravity.LEFT, context.getResources().getInteger(R.integer.mobile_bottombar_pdf_common_margin), context.getResources().getInteger(R.integer.action_icon_size), context)
                        }
                    } else {
                        setBottomActionbar(item7, R.id.topbar_review, "G", PlayerUIConstants.TB_STUDENT_IC_TEXT, mBottomActionBarItemColor, Gravity.LEFT, context.getResources().getInteger(R.integer.mobile_bottombar_pdf_common_margin), context.getResources().getInteger(R.integer.action_icon_size), context)
                    }
                }
            }
        } else {
            val displayMetrics = DisplayMetrics()
            (context as PlayerActivity).windowManager.defaultDisplay.getMetrics(displayMetrics)
            val height = displayMetrics.heightPixels
            val width = displayMetrics.widthPixels
            if (context.packageManager.hasSystemFeature("org.chromium.arc.device_management")) {
                if (context.packageManager.hasSystemFeature("android.hardware.touchscreen")) {
                    setBottomActionbar(item1, R.id.action_toc, "B", CustomPlayerUIConstants.ACTION_TOC_TEXT, mBottomActionBarItemColor, Gravity.LEFT, context.getResources().getInteger(R.integer.toc_land_margin_for_chromebook), context.getResources().getInteger(R.integer.action_icon_size), context)
                    setBottomActionbar(item2, R.id.action_my_data, "C", CustomPlayerUIConstants.ACTION_MYDATA_TEXT, mBottomActionBarItemColor, Gravity.LEFT, context.getResources().getInteger(R.integer.action_bar_bottom_common_margin), context.getResources().getInteger(R.integer.action_icon_size), context)
                    setBottomActionbar(item3, R.id.action_search, "D", CustomPlayerUIConstants.ACTION_SEARCH_TEXT, mBottomActionBarItemColor, Gravity.LEFT, context.getResources().getInteger(R.integer.action_bar_bottom_common_margin), context.getResources().getInteger(R.integer.action_icon_size), context)
                    setBottomActionbar(item4, R.id.action_pen, "E", CustomPlayerUIConstants.ACTION_PEN_TEXT, mBottomActionBarItemColor, Gravity.LEFT, context.getResources().getInteger(R.integer.action_bar_bottom_common_margin), context.getResources().getInteger(R.integer.action_icon_size), context)
                    setBottomActionbar(item5, R.id.action_sticky_note, "E", CustomPlayerUIConstants.NOTE_ICON_TEXT, mBottomActionBarItemColor, Gravity.LEFT, context.getResources().getInteger(R.integer.action_bar_bottom_common_margin), context.getResources().getInteger(R.integer.action_icon_size), context)
                    setBottomActionbar(item6, R.id.action_thumbnail, "9", CustomPlayerUIConstants.ACTION_THUMBNAIL_TEXT, mBottomActionBarItemColor, Gravity.LEFT, context.getResources().getInteger(R.integer.action_bar_bottom_common_margin), context.getResources().getInteger(R.integer.action_icon_size), context)
                    if (context.getResources().getBoolean(R.bool.show_data_submit)) {
                        if (PlayerController.getInstance(context).isUgcShareEnabled) {
                            if (accountType == Constants.TEACHER) {
                                if (context.getResources().getBoolean(R.bool.is_voyger_client)) {
                                    if (IsClassAccociated) {
                                        setBottomActionbar(item7, R.id.topbar_review, "G", PlayerUIConstants.TB_TEACHER_IC_TEXT, mBottomActionBarItemColor, Gravity.LEFT, context.getResources().getInteger(R.integer.action_bar_bottom_common_margin), context.getResources().getInteger(R.integer.action_icon_size), context)
                                    }
                                } else {
                                    setBottomActionbar(item7, R.id.topbar_review, "G", PlayerUIConstants.TB_TEACHER_IC_TEXT, mBottomActionBarItemColor, Gravity.LEFT, context.getResources().getInteger(R.integer.action_bar_bottom_common_margin), context.getResources().getInteger(R.integer.action_icon_size), context)
                                }
                            } else {
                                setBottomActionbar(item7, R.id.topbar_review, "G", PlayerUIConstants.TB_STUDENT_IC_TEXT, mBottomActionBarItemColor, Gravity.LEFT, context.getResources().getInteger(R.integer.action_bar_bottom_common_margin), context.getResources().getInteger(R.integer.action_icon_size), context)
                            }
                        }
                    }
                    setBottomActionbar(mPageModeIcon, R.id.action_page_mode, "R", CustomPlayerUIConstants.ACTIONBAR_SNGLE_PAGE_ICON, mBottomActionBarItemColor, Gravity.LEFT, context.getResources().getInteger(R.integer.action_bar_bottom_common_margin), context.getResources().getInteger(R.integer.action_icon_size), context)
                } else {
                    setBottomActionbar(item1, R.id.action_toc, "B", CustomPlayerUIConstants.ACTION_TOC_TEXT, mBottomActionBarItemColor, Gravity.LEFT, context.getResources().getInteger(R.integer.action_bar_bottom_pdf_land_margin_chrombook), context.getResources().getInteger(R.integer.action_icon_size), context)
                    setBottomActionbar(item2, R.id.action_my_data, "C", CustomPlayerUIConstants.ACTION_MYDATA_TEXT, mBottomActionBarItemColor, Gravity.LEFT, context.getResources().getInteger(R.integer.action_bar_bottom_common_margin), context.getResources().getInteger(R.integer.action_icon_size), context)
                    setBottomActionbar(item3, R.id.action_search, "D", CustomPlayerUIConstants.ACTION_SEARCH_TEXT, mBottomActionBarItemColor, Gravity.LEFT, context.getResources().getInteger(R.integer.action_bar_bottom_common_margin), context.getResources().getInteger(R.integer.action_icon_size), context)
                    setBottomActionbar(item4, R.id.action_pen, "E", CustomPlayerUIConstants.ACTION_PEN_TEXT, mBottomActionBarItemColor, Gravity.LEFT, context.getResources().getInteger(R.integer.action_bar_bottom_common_margin), context.getResources().getInteger(R.integer.action_icon_size), context)
                    setBottomActionbar(item5, R.id.action_sticky_note, "E", CustomPlayerUIConstants.NOTE_ICON_TEXT, mBottomActionBarItemColor, Gravity.LEFT, context.getResources().getInteger(R.integer.action_bar_bottom_common_margin), context.getResources().getInteger(R.integer.action_icon_size), context)
                    setBottomActionbar(item6, R.id.action_thumbnail, "9", CustomPlayerUIConstants.ACTION_THUMBNAIL_TEXT, mBottomActionBarItemColor, Gravity.LEFT, context.getResources().getInteger(R.integer.action_bar_bottom_common_margin), context.getResources().getInteger(R.integer.action_icon_size), context)
                    if (context.getResources().getBoolean(R.bool.show_data_submit)) {
                        if (PlayerController.getInstance(context).isUgcShareEnabled) {
                            if (accountType == Constants.TEACHER) {
                                if (context.getResources().getBoolean(R.bool.is_voyger_client)) {
                                    if (IsClassAccociated) {
                                        setBottomActionbar(item7, R.id.topbar_review, "G", PlayerUIConstants.TB_TEACHER_IC_TEXT, mBottomActionBarItemColor, Gravity.LEFT, context.getResources().getInteger(R.integer.action_bar_bottom_common_margin), context.getResources().getInteger(R.integer.action_icon_size), context)
                                    }
                                } else {
                                    setBottomActionbar(item7, R.id.topbar_review, "G", PlayerUIConstants.TB_TEACHER_IC_TEXT, mBottomActionBarItemColor, Gravity.LEFT, context.getResources().getInteger(R.integer.action_bar_bottom_common_margin), context.getResources().getInteger(R.integer.action_icon_size), context)
                                }
                            } else {
                                setBottomActionbar(item7, R.id.topbar_review, "G", PlayerUIConstants.TB_STUDENT_IC_TEXT, mBottomActionBarItemColor, Gravity.LEFT, context.getResources().getInteger(R.integer.action_bar_bottom_common_margin), context.getResources().getInteger(R.integer.action_icon_size), context)
                            }
                        }
                    }
                    setBottomActionbar(mPageModeIcon, R.id.action_page_mode, "R", CustomPlayerUIConstants.ACTIONBAR_SNGLE_PAGE_ICON, mBottomActionBarItemColor, Gravity.LEFT, context.getResources().getInteger(R.integer.action_bar_bottom_common_margin), context.getResources().getInteger(R.integer.action_icon_size), context)
                    setBottomActionbar(mPageZoomIcon, R.id.action_page_zoom, "x", CustomPlayerUIConstants.ACTIONBAR_PAGE_ZOOM, mBottomActionBarItemColor, Gravity.LEFT, context.getResources().getInteger(R.integer.action_bar_bottom_common_margin), context.getResources().getInteger(R.integer.action_icon_size), context)
                    setBottomActionbar(mZoomPercentageText, R.id.action_page_zoom_percentage, "R", "100", mBottomActionBarItemColor, Gravity.LEFT, context.getResources().getInteger(R.integer.action_bar_bottom_common_margin), context.getResources().getInteger(R.integer.action_icon_size), context)
                    setBottomActionbar(mPageZoomOutIcon, R.id.action_page_zoom_out, "y", CustomPlayerUIConstants.ACTIONBAR_PAGE_ZOOM_OUT, mBottomActionBarItemColor, Gravity.LEFT, context.getResources().getInteger(R.integer.action_bar_bottom_common_margin), context.getResources().getInteger(R.integer.action_icon_size), context)
                }
            } else {
                setBottomActionbar(item1, R.id.action_toc, "B", CustomPlayerUIConstants.ACTION_TOC_TEXT, mBottomActionBarItemColor, Gravity.LEFT, context.getResources().getInteger(R.integer.action_bar_bottom_pdf_land_margin), context.getResources().getInteger(R.integer.action_icon_size), context)
                setBottomActionbar(item2, R.id.action_my_data, "C", CustomPlayerUIConstants.ACTION_MYDATA_TEXT, mBottomActionBarItemColor, Gravity.LEFT, context.getResources().getInteger(R.integer.action_bar_bottom_common_margin), context.getResources().getInteger(R.integer.action_icon_size), context)
                setBottomActionbar(item3, R.id.action_search, "D", CustomPlayerUIConstants.ACTION_SEARCH_TEXT, mBottomActionBarItemColor, Gravity.LEFT, context.getResources().getInteger(R.integer.action_bar_bottom_common_margin), context.getResources().getInteger(R.integer.action_icon_size), context)
                setBottomActionbar(item4, R.id.action_pen, "E", CustomPlayerUIConstants.ACTION_PEN_TEXT, mBottomActionBarItemColor, Gravity.LEFT, context.getResources().getInteger(R.integer.action_bar_bottom_common_margin), context.getResources().getInteger(R.integer.action_icon_size), context)
                setBottomActionbar(item5, R.id.action_sticky_note, "E", CustomPlayerUIConstants.NOTE_ICON_TEXT, mBottomActionBarItemColor, Gravity.LEFT, context.getResources().getInteger(R.integer.action_bar_bottom_common_margin), context.getResources().getInteger(R.integer.action_icon_size), context)
                setBottomActionbar(item6, R.id.action_thumbnail, "9", CustomPlayerUIConstants.ACTION_THUMBNAIL_TEXT, mBottomActionBarItemColor, Gravity.LEFT, context.getResources().getInteger(R.integer.action_bar_bottom_common_margin), context.getResources().getInteger(R.integer.action_icon_size), context)
                if (context.getResources().getBoolean(R.bool.show_data_submit)) {
                    if (PlayerController.getInstance(context).isUgcShareEnabled) {
                        if (accountType == Constants.TEACHER) {
                            if (context.getResources().getBoolean(R.bool.is_voyger_client)) {
                                if (IsClassAccociated) {
                                    setBottomActionbar(item7, R.id.topbar_review, "G", PlayerUIConstants.TB_TEACHER_IC_TEXT, mBottomActionBarItemColor, Gravity.LEFT, context.getResources().getInteger(R.integer.mobile_bottombar_pdf_common_margin), context.getResources().getInteger(R.integer.action_icon_size), context)
                                }
                            } else {
                                setBottomActionbar(item7, R.id.topbar_review, "G", PlayerUIConstants.TB_TEACHER_IC_TEXT, mBottomActionBarItemColor, Gravity.LEFT, context.getResources().getInteger(R.integer.mobile_bottombar_pdf_common_margin), context.getResources().getInteger(R.integer.action_icon_size), context)
                            }
                        } else {
                            setBottomActionbar(item7, R.id.topbar_review, "G", PlayerUIConstants.TB_STUDENT_IC_TEXT, mBottomActionBarItemColor, Gravity.LEFT, context.getResources().getInteger(R.integer.mobile_bottombar_pdf_common_margin), context.getResources().getInteger(R.integer.action_icon_size), context)
                        }
                    }
                }
            }
            if (context.packageManager.hasSystemFeature("org.chromium.arc.device_management")) {
                if (context.packageManager.hasSystemFeature("android.hardware.touchscreen")) {
                    mPageZoomIcon.visibility = View.GONE
                    mPageZoomOutIcon.visibility = View.GONE
                } else {
                    mPageZoomIcon.visibility = View.VISIBLE
                    mZoomPercentageText.visibility = View.VISIBLE
                    mPageZoomOutIcon.visibility = View.VISIBLE
                }
            } else {
                mPageZoomIcon.visibility = View.GONE
                mPageZoomOutIcon.visibility = View.GONE
                mPageModeIcon.visibility = View.GONE
            }
        }
    }

    private fun addMenuItemsToTheActionbarForChromebook(actionItemHome: KitabooActionItemView, item1: KitabooActionItemView, item2: KitabooActionItemView, item3: KitabooActionItemView, item4: KitabooActionItemView, item5: KitabooActionItemView, context: Context) {
        mPageModeIcon = KitabooActionItemView(context)
        mPageZoomIcon = KitabooActionItemView(context)
        mZoomPercentageText = KitabooActionItemView(context)
        mPageZoomOutIcon = KitabooActionItemView(context)
        if (context.resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            val displayMetrics = DisplayMetrics()
            (context as PlayerActivity).windowManager.defaultDisplay.getMetrics(displayMetrics)
            val height = displayMetrics.heightPixels
            val width = displayMetrics.widthPixels
            setBottomActionbar(item1, R.id.action_toc, "B", CustomPlayerUIConstants.ACTION_TOC_TEXT, mBottomActionBarItemColor, Gravity.LEFT, context.getResources().getInteger(R.integer.toc_margin_for_chromebook), context.getResources().getInteger(R.integer.action_icon_size), context)
            setBottomActionbar(item5, R.id.action_thumbnail, "9", CustomPlayerUIConstants.ACTION_THUMBNAIL_TEXT, mBottomActionBarItemColor, Gravity.LEFT, context.getResources().getInteger(R.integer.search_margin_for_chromebook), context.getResources().getInteger(R.integer.action_icon_size), context)
        } else {
            val displayMetrics = DisplayMetrics()
            (context as PlayerActivity).windowManager.defaultDisplay.getMetrics(displayMetrics)
            val height = displayMetrics.heightPixels
            val width = displayMetrics.widthPixels
            if (context.packageManager.hasSystemFeature("org.chromium.arc.device_management")) {
                if (context.packageManager.hasSystemFeature("android.hardware.touchscreen")) {
                    setBottomActionbar(item1, R.id.action_toc, "B", CustomPlayerUIConstants.ACTION_TOC_TEXT, mBottomActionBarItemColor, Gravity.LEFT, context.getResources().getInteger(R.integer.toc_land_margin_for_chrome_book), context.getResources().getInteger(R.integer.action_icon_size), context)
                    setBottomActionbar(item5, R.id.action_thumbnail, "9", CustomPlayerUIConstants.ACTION_THUMBNAIL_TEXT, mBottomActionBarItemColor, Gravity.LEFT, context.getResources().getInteger(R.integer.search_land_margin_for_chrome_book), context.getResources().getInteger(R.integer.action_icon_size), context)
                } else {
                    setBottomActionbar(item1, R.id.action_toc, "B", CustomPlayerUIConstants.ACTION_TOC_TEXT, mBottomActionBarItemColor, Gravity.LEFT, width / 8, context.getResources().getInteger(R.integer.action_icon_size), context)
                    setBottomActionbar(item5, R.id.action_thumbnail, "9", CustomPlayerUIConstants.ACTION_THUMBNAIL_TEXT, mBottomActionBarItemColor, Gravity.LEFT, width / 8, context.getResources().getInteger(R.integer.action_icon_size), context)
                    setBottomActionbar(mPageZoomIcon, R.id.action_page_zoom, "x", CustomPlayerUIConstants.ACTIONBAR_PAGE_ZOOM, mBottomActionBarItemColor, Gravity.LEFT, width / 8, context.getResources().getInteger(R.integer.action_icon_size), context)
                    setBottomActionbar(mZoomPercentageText, R.id.action_page_zoom_percentage, "R", "100", mBottomActionBarItemColor, Gravity.LEFT, width / 8, context.getResources().getInteger(R.integer.action_icon_size), context)
                    setBottomActionbar(mPageZoomOutIcon, R.id.action_page_zoom_out, "y", CustomPlayerUIConstants.ACTIONBAR_PAGE_ZOOM_OUT, mBottomActionBarItemColor, Gravity.LEFT, width / 8, context.getResources().getInteger(R.integer.action_icon_size), context)
                }
            } else {
                setBottomActionbar(item1, R.id.action_toc, "B", CustomPlayerUIConstants.ACTION_TOC_TEXT, mBottomActionBarItemColor, Gravity.LEFT, context.getResources().getInteger(R.integer.toc_land_margin_for_chrome_book), context.getResources().getInteger(R.integer.action_icon_size), context)
                setBottomActionbar(item5, R.id.action_thumbnail, "9", CustomPlayerUIConstants.ACTION_THUMBNAIL_TEXT, mBottomActionBarItemColor, Gravity.LEFT, context.getResources().getInteger(R.integer.search_land_margin_for_chrome_book), context.getResources().getInteger(R.integer.action_icon_size), context)
            }
            if (context.packageManager.hasSystemFeature("org.chromium.arc.device_management")) {
                if (context.packageManager.hasSystemFeature("android.hardware.touchscreen")) {
                    mPageZoomIcon.visibility = View.GONE
                    mPageZoomOutIcon.visibility = View.GONE
                    mZoomPercentageText.visibility = View.GONE
                } else {
                    mPageZoomIcon.visibility = View.VISIBLE
                    mZoomPercentageText.visibility = View.VISIBLE
                    mPageZoomOutIcon.visibility = View.VISIBLE
                }
            } else {
                mPageZoomIcon.visibility = View.GONE
                mPageZoomOutIcon.visibility = View.GONE
                mZoomPercentageText.visibility = View.GONE
            }
        }

    }

    private fun addMenuItemsToTheActionbarforstorylt(actionItemHome: KitabooActionItemView, item1: KitabooActionItemView, item2: KitabooActionItemView, item3: KitabooActionItemView, item4: KitabooActionItemView, context: Context) {
        if (isMobile) {
            var displayMetrics = DisplayMetrics()
            (context as PlayerActivity).windowManager.defaultDisplay.getMetrics(displayMetrics)
            var height = displayMetrics.heightPixels
            var width = displayMetrics.widthPixels
            if (context.getResources().configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
                setBottomActionbar(item1, R.id.action_toc, "B", CustomPlayerUIConstants.ACTION_TOC_TEXT, mBottomActionBarItemColor, Gravity.LEFT, width / 8, context.getResources().getInteger(R.integer.action_icon_size), context)
                setBottomActionbar(item2, R.id.action_my_data, "C", CustomPlayerUIConstants.ACTION_MYDATA_TEXT, mBottomActionBarItemColor, Gravity.LEFT, width / 8, context.getResources().getInteger(R.integer.action_icon_size), context)
                setBottomActionbar(item3, R.id.action_search, "D", CustomPlayerUIConstants.ACTION_SEARCH_TEXT, mBottomActionBarItemColor, Gravity.LEFT, width / 8, context.getResources().getInteger(R.integer.action_icon_size), context)
                setBottomActionbar(item4, R.id.action_thumbnail, "9", CustomPlayerUIConstants.ACTION_THUMBNAIL_TEXT, mBottomActionBarItemColor, Gravity.LEFT, width / 8, context.getResources().getInteger(R.integer.action_icon_size), context)
            } else {
                displayMetrics = DisplayMetrics()
                context.windowManager.defaultDisplay.getMetrics(displayMetrics)
                height = displayMetrics.heightPixels
                width = displayMetrics.widthPixels
                setBottomActionbar(item1, R.id.action_toc, "B", CustomPlayerUIConstants.ACTION_TOC_TEXT, mBottomActionBarItemColor, Gravity.LEFT, width / 7, context.getResources().getInteger(R.integer.action_icon_size), context)
                setBottomActionbar(item2, R.id.action_my_data, "C", CustomPlayerUIConstants.ACTION_MYDATA_TEXT, mBottomActionBarItemColor, Gravity.LEFT, width / 7, context.getResources().getInteger(R.integer.action_icon_size), context)
                setBottomActionbar(item3, R.id.action_search, "D", CustomPlayerUIConstants.ACTION_SEARCH_TEXT, mBottomActionBarItemColor, Gravity.LEFT, width / 7, context.getResources().getInteger(R.integer.action_icon_size), context)
                setBottomActionbar(item4, R.id.action_thumbnail, "9", CustomPlayerUIConstants.ACTION_THUMBNAIL_TEXT, mBottomActionBarItemColor, Gravity.LEFT, width / 7, context.getResources().getInteger(R.integer.action_icon_size), context)
            }
        } else {
            var displayMetrics = DisplayMetrics()
            (context as PlayerActivity).windowManager.defaultDisplay.getMetrics(displayMetrics)
            var height = displayMetrics.heightPixels
            var width = displayMetrics.widthPixels
            if (context.getResources().configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
                setBottomActionbar(item1, R.id.action_toc, "B", CustomPlayerUIConstants.ACTION_TOC_TEXT, mBottomActionBarItemColor, Gravity.LEFT, width / 7, context.getResources().getInteger(R.integer.action_icon_size), context)
                setBottomActionbar(item2, R.id.action_my_data, "C", CustomPlayerUIConstants.ACTION_MYDATA_TEXT, mBottomActionBarItemColor, Gravity.LEFT, width / 7, context.getResources().getInteger(R.integer.action_icon_size), context)
                setBottomActionbar(item3, R.id.action_search, "D", CustomPlayerUIConstants.ACTION_SEARCH_TEXT, mBottomActionBarItemColor, Gravity.LEFT, width / 7, context.getResources().getInteger(R.integer.action_icon_size), context)
                setBottomActionbar(item4, R.id.action_thumbnail, "9", CustomPlayerUIConstants.ACTION_THUMBNAIL_TEXT, mBottomActionBarItemColor, Gravity.LEFT, width / 7, context.getResources().getInteger(R.integer.action_icon_size), context)
            } else {
                displayMetrics = DisplayMetrics()
                context.windowManager.defaultDisplay.getMetrics(displayMetrics)
                height = displayMetrics.heightPixels
                width = displayMetrics.widthPixels
                setBottomActionbar(item1, R.id.action_toc, "B", CustomPlayerUIConstants.ACTION_TOC_TEXT, mBottomActionBarItemColor, Gravity.LEFT, width / 6, context.getResources().getInteger(R.integer.action_icon_size), context)
                setBottomActionbar(item2, R.id.action_my_data, "C", CustomPlayerUIConstants.ACTION_MYDATA_TEXT, mBottomActionBarItemColor, Gravity.LEFT, width / 6, context.getResources().getInteger(R.integer.action_icon_size), context)
                setBottomActionbar(item3, R.id.action_search, "D", CustomPlayerUIConstants.ACTION_SEARCH_TEXT, mBottomActionBarItemColor, Gravity.LEFT, width / 6, context.getResources().getInteger(R.integer.action_icon_size), context)
                setBottomActionbar(item4, R.id.action_thumbnail, "9", CustomPlayerUIConstants.ACTION_THUMBNAIL_TEXT, mBottomActionBarItemColor, Gravity.LEFT, width / 6, context.getResources().getInteger(R.integer.action_icon_size), context)
            }
        }
    }

    private fun addMenuItemsToTheActionbarForGenericAccount(actionItemHome: KitabooActionItemView, item1: KitabooActionItemView, item2: KitabooActionItemView, context: Context) {
        if (context.resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            if (mReaderType == EBookType.FIXEDKITABOO) {
                setBottomActionbar(item1, R.id.action_toc, "B", CustomPlayerUIConstants.ACTION_TOC_TEXT, mBottomActionBarItemColor, Gravity.LEFT, context.resources.getInteger(R.integer.toc_margin_for_generic_user_fixed), context.resources.getInteger(R.integer.action_icon_size), context)
                setBottomActionbar(item2, R.id.action_search, "D", CustomPlayerUIConstants.ACTION_SEARCH_TEXT, mBottomActionBarItemColor, Gravity.LEFT, context.resources.getInteger(R.integer.search_margin_for_generic_user), context.resources.getInteger(R.integer.action_icon_size), context)
            } else {
                setBottomActionbar(item1, R.id.action_toc, "B", CustomPlayerUIConstants.ACTION_TOC_TEXT, mBottomActionBarItemColor, Gravity.LEFT, context.resources.getInteger(R.integer.toc_margin_for_generic_user), context.resources.getInteger(R.integer.action_icon_size), context)
                setBottomActionbar(item2, R.id.action_search, "D", CustomPlayerUIConstants.ACTION_SEARCH_TEXT, mBottomActionBarItemColor, Gravity.LEFT, context.resources.getInteger(R.integer.search_margin_for_generic_user), context.resources.getInteger(R.integer.action_icon_size), context)
                setBottomActionbar(mEpubSettingPanel, R.id.action_font_settings, "C", CustomPlayerUIConstants.ACTION_FONT_SETTING, mBottomActionBarItemColor, Gravity.LEFT, context.resources.getInteger(R.integer.font_margin_for_generic_user), context.resources.getInteger(R.integer.action_icon_size), context)
            }
        } else {
            if (mReaderType == EBookType.FIXEDKITABOO) {
                setBottomActionbar(item1, R.id.action_toc, "B", CustomPlayerUIConstants.ACTION_TOC_TEXT, mBottomActionBarItemColor, Gravity.LEFT, context.resources.getInteger(R.integer.toc_land_margin_for_generic_user_fixed), context.resources.getInteger(R.integer.action_icon_size), context)
                setBottomActionbar(item2, R.id.action_search, "D", CustomPlayerUIConstants.ACTION_SEARCH_TEXT, mBottomActionBarItemColor, Gravity.LEFT, context.resources.getInteger(R.integer.search_land_margin_for_generic_user), context.resources.getInteger(R.integer.action_icon_size), context)
            } else {
                setBottomActionbar(item1, R.id.action_toc, "B", CustomPlayerUIConstants.ACTION_TOC_TEXT, mBottomActionBarItemColor, Gravity.LEFT, context.resources.getInteger(R.integer.toc_land_margin_for_generic_user), context.resources.getInteger(R.integer.action_icon_size), context)
                setBottomActionbar(item2, R.id.action_search, "D", CustomPlayerUIConstants.ACTION_SEARCH_TEXT, mBottomActionBarItemColor, Gravity.LEFT, context.resources.getInteger(R.integer.search_land_margin_for_generic_user), context.resources.getInteger(R.integer.action_icon_size), context)
                setBottomActionbar(mEpubSettingPanel, R.id.action_font_settings, "C", CustomPlayerUIConstants.ACTION_FONT_SETTING, mBottomActionBarItemColor, Gravity.LEFT, context.resources.getInteger(R.integer.font_land_margin_for_generic_user), context.resources.getInteger(R.integer.action_icon_size), context)
            }
        }

    }


    /**
     * Customize the top actionbar
     */
    fun setTopActionbarItem(context: Context,
                            topActionbar: CustomKitabooTopActionbar) {
        this.topActionbar = topActionbar

        if (SDKManager.getInstance().isNewTeacherReviewModeOn) {
            topActionbar.visibility = View.INVISIBLE

            mTeacherEraser = KitabooActionItemView(context)
            mTeacherUndo = KitabooActionItemView(context)
            mTeacherRedo = KitabooActionItemView(context)
            var mTeacherPrevious = KitabooActionItemView(context)
            var mTeacherNext = KitabooActionItemView(context)
            var mTeacherReviewGreen = KitabooActionItemView(context)
            var mTeacherReviewRed = KitabooActionItemView(context)
            var mTeacherReviewDone = KitabooActionItemView(context)
            var mTeacherReviewFirstname = KitabooActionItemView(context)
            var mTeacherReviewPage = KitabooActionItemView(context)
            var mTeacherReviewProfileImage = ImageView(context)
            var mTeacherClearAll = KitabooActionItemView(context)

            setTopActionbar(mTeacherPrevious, R.id.teacher_review_previous, "C", CustomPlayerUIConstants.TEACHER_ACTIONBAR_PREVIOUS, mPentoolToolbarItemColor, Gravity.LEFT, 20, 18, context)

            setTopActionbar(mTeacherReviewPage, R.id.teacher_review_page, "", "Page", mPentoolToolbarItemColor, Gravity.LEFT, 20, 12, context)

            setTopActionbar(mTeacherNext, R.id.teacher_review_next, "E", CustomPlayerUIConstants.TEACHER_ACTIONBAR_NEXT, mPentoolToolbarItemColor, Gravity.LEFT, 20, 18, context)

            setCustomViewActionbar(mTeacherReviewProfileImage, R.id.teacher_review_profile_image, "", mPentoolToolbarItemColor, Gravity.RIGHT, 100, context)

            setTopActionbar(mTeacherReviewFirstname, R.id.teacher_review_firstname, "", "", mPentoolToolbarItemColor, Gravity.RIGHT, 30, 15, context)

            /*    mTeacherReviewRed.setPadding(2, 2, 2, 2)
                drawable.setShape(GradientDrawable.OVAL)
                drawable.setStroke(2, Color.parseColor(readerThemeSettingVo!!.reader.dayMode.pentool.toolbar.selectedIconBackground))
                mTeacherReviewRed.setBackgroundDrawable(drawable)*/
        } else {

            this.topActionbar!!.visibility = View.VISIBLE
            //if (isMobile) {
            val item1 = KitabooActionItemView(context)
            setTopActionbar(item1, R.id.action_home, "A", CustomPlayerUIConstants.TOP_ACTION_HOME_TEXT, mTopActionBarItemColor, Gravity.LEFT, 0, context.resources.getInteger(R.integer.action_icon_size), context)
//        mTextChapterTitle = TextView(context)
//        setCustomViewActionbar(mTextChapterTitle, R.id.action_chapter_title, "", mTopActionBarItemColor, Gravity.CENTER_HORIZONTAL, 0,context)
            if (audioBookType.equals(PlayerActivity.READ_ALOUD_BOOK_, ignoreCase = true) /*|| audioBookType.equalsIgnoreCase("Other")*/) {
                mReadAloudIcon = KitabooActionItemView(context)
//            setTopActionbar(mReadAloudIcon, R.id.read_aloud, "RA", CustomPlayerUIConstants.READ_AUDIO, mPentoolToolbarItemColor, Gravity.CENTER_HORIZONTAL, 0, 22, context)
            }
            // if (!context.getResources().getBoolean(R.bool.is_nanoq_greenland)) {
            var mProfileImage = ImageView(context)
            if (context.resources.getBoolean(R.bool.is_Mills_and_Boon)) {
                mProfileImage.visibility = View.GONE
            }
            mClearAllImage = KitabooActionItemView(context)
            setCustomViewActionbar(mProfileImage, R.id.action_profile_image, "", mTopActionBarItemColor, Gravity.RIGHT, 10, context)
//        setTopActionbar(mClearAllImage, R.id.action_fib_dropdown_clearall, "A", CustomPlayerUIConstants.TOP_ACTION_HOME_TEXT, mTopActionBarItemColor, Gravity.RIGHT, 10, 22,context);
            if (mReaderType == EBookType.REFLOWEPUB && reflowPrintEnable.equals("true", ignoreCase = true)) {
                val print = KitabooActionItemView(context)
                setTopActionbar(print, R.id.action_print_page, "A", CustomPlayerUIConstants.PP_IC_TEXT, mTopActionBarItemColor, Gravity.RIGHT, 25, context.resources.getInteger(R.integer.action_icon_size), context)
            }
            if (mReaderType == EBookType.REFLOWEPUB && !this.reflowPrintEanablePageList!!.isNullOrEmpty()) {
                val print = KitabooActionItemView(context)
//            setTopActionbar(print, R.id.action_print_page, "A", CustomPlayerUIConstants.PP_IC_TEXT, mTopActionBarItemColor, Gravity.RIGHT, 25, context.getResources().getInteger(R.integer.action_icon_size),context)
            }
//        getBitmap()
//        drawImage()

            /* if (DatabaseManager.getInstance(context).checkForActivitySubmitData(userID, IsClassAccociated, GlobalDataManager.getInstance().getLocalBookData().getBookID(), false)) {
        mClearAllImage.setVisibility(VISIBLE);
    } else {
        mClearAllImage.setVisibility(View.GONE);
    }*/
            // }
//        this.topActionbar!!.setBackgroundColor(mTopActionbarBackGroundColor)
            // this.topActionbar.setBackground(context.getResources().getDrawable(R.drawable.top_actionbar_background));
            /*this.topActionbar.setBackgroundDrawable(Utility.Utils.getRectAngleDrawable(mTopActionbarBackGroundColor,
            new float[]{2, 2, 2, 2, 2, 2, 2, 2}, 1, Color.parseColor(themeUserSettingVo.getmKitabooMainColor())));*/
        }
        this.topActionbar!!.build()
        if (mReaderType == EBookType.REFLOWEPUB &&
                this.reflowPrintEanablePageList != null) {
//            this.topActionbar!!.actionBarView.findViewById<View>(R.id.action_print_page).visibility = View.INVISIBLE
        }
    }

    /**
     * Add custom view to action bar with its properties
     *
     * @param item        : Custom Actionbar item view
     * @param itemId      : id of custom Actionbar item view
     * @param itemName    : name of custom Actionbar item view
     * @param itemColor   : color of custom Actionbar item view
     * @param itemGravity : left/right as required respective to custom actionbar
     */
    private fun setCustomViewActionbar(item: View, itemId: Int, itemName: String, itemColor: Int, itemGravity: Int, leftmargin: Int, context: Context) {
        item.id = itemId
        if (item is CustomCompoundView) {
            item.leftMargin = leftmargin
        }
        if (item is TextView) {
            val displayMetrics = DisplayMetrics()
            (context as PlayerActivity).windowManager.defaultDisplay.getMetrics(displayMetrics)
            item.maxWidth = displayMetrics.widthPixels - 300
//            pageNumberDetailsTextView(item, itemColor)
        }
        if (item is ImageView) {
            if (isMobile) {
                item.setMinimumWidth(100)
                item.setMinimumHeight(100)
            } else {
                item.setMinimumWidth(80)
                item.setMinimumHeight(80)
                //((ImageView)item).setLayoutParams(new FrameLayout.LayoutParams(30,30));
            }
        }
        topActionbar!!.addActionItem(item, itemGravity)
        //        topActionbar.addTopActionItem(item, itemGravity);
//        topActionbar!!.addEventCallback(context)
    }

    private fun setTopActionbar(item: KitabooActionItemView, itemId: Int, itemName: String,
                                charManningChar: String, itemColor: Int, itemGravity: Int, leftMargin: Int, textSize: Int,
                                context: Context) {
        item.setId(itemId, item)
        item.uniqueName = itemName
        item.setCharatorManningChar(charManningChar)
        item.setTextColor(itemColor)
        item.gravity = Gravity.CENTER
        item.typeface = if (itemId == R.id.teacher_review_done || itemId == R.id.teacher_review_page ||
                itemId == R.id.teacher_review_firstname || itemId == R.id.read_speed || itemId == R.id.action_page_zoom_percentage)
            null else this.topActionbar!!.defaultActionbarTypeface(context)
        item.isAllCaps = false
        item.textSize = textSize.toFloat()
        item.leftMargin = leftMargin

        if (isMobile) {
            if (itemId == R.id.read_speed) item.minimumWidth = 150 else item.minimumWidth = context.resources.getInteger(R.integer.actionbar_menu_width_mobile)
        } else {
            if (item.id == R.id.teacher_review_page && context.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                item.minimumWidth = 200
            } else {
                if (itemId == R.id.read_speed) item.minimumWidth = 150 else if (itemId == R.id.teacher_review_done) item.minimumWidth = 150 else item.minimumWidth = 100
            }
        }
        //this.topActionbar.setBackgroundColor(context.getResources().getColor(R.color.white));
        this.topActionbar!!.addActionItem(item, itemGravity)
        //        this.topActionbar.addTopActionItem(item, itemGravity);
//        this.topActionbar!!.addEventCallback(context)
    }

    private fun setActionbarGroundColor(context: Context) {
        readerThemeSettingVo = InsightReaderThemeController.getInstance(context).readerThemeUserSettingVo
        mTopActionbarBackGroundColor = Color.parseColor(readerThemeSettingVo!!.reader.dayMode.main.toolbar.top.background)
        mTopActionBarItemColor = Color.WHITE
//        mBottomActionBarBackGroundColor = Color.parseColor(readerThemeSettingVo!!.getReader().getDayMode().getMain().getToolbar().getSideBottom().getBackground())
        mBottomActionBarItemColor = Color.WHITE
        mPentoolToolbarItemColor = Color.WHITE
        mPentoolToolbarBackgroundColor = Color.TRANSPARENT

    }

    private fun addMenuItemsToTheActionbar(actionItemHome: KitabooActionItemView, item1: KitabooActionItemView, item2: KitabooActionItemView, item3: KitabooActionItemView,
                                           item4: KitabooActionItemView, item5: KitabooActionItemView, item6: KitabooActionItemView, item7: KitabooActionItemView, item8: KitabooActionItemView, context: Context) {
        val item10 = KitabooActionItemView(context)
        if (Utility.isDeviceTypeMobile(context)) {
            if (context.resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
                if (context.resources.getBoolean(R.bool.is_sparkCapital_client)) setBottomActionbar(actionItemHome, R.id.action_home, "A", CustomPlayerUIConstants.TOP_ACTION_HOME_TEXT, mBottomActionBarItemColor, Gravity.LEFT, 0, context.resources.getInteger(R.integer.action_icon_size), context)
                if (mReaderType == EBookType.FIXEDKITABOO) {
                    if (!context.resources.getBoolean(R.bool.is_sparkCapital_client)) setBottomActionbar(item1, R.id.action_toc, "B", CustomPlayerUIConstants.ACTION_TOC_TEXT, mBottomActionBarItemColor, Gravity.LEFT, 0, context.resources.getInteger(R.integer.action_icon_size), context)
                    setBottomActionbar(item2, R.id.action_my_data, "C", CustomPlayerUIConstants.ACTION_MYDATA_TEXT, mBottomActionBarItemColor, Gravity.LEFT, 0, context.resources.getInteger(R.integer.action_icon_size), context)
                    setBottomActionbar(item3, R.id.action_search, "D", CustomPlayerUIConstants.ACTION_SEARCH_TEXT, mBottomActionBarItemColor, Gravity.LEFT, 0, context.resources.getInteger(R.integer.action_icon_size), context)
                    setBottomActionbar(item4, R.id.action_pen, "E", CustomPlayerUIConstants.ACTION_PEN_TEXT, mBottomActionBarItemColor, Gravity.LEFT, 0, context.resources.getInteger(R.integer.action_icon_size), context)
                    setBottomActionbar(item5, R.id.action_sticky_note, "B", CustomPlayerUIConstants.NOTE_ICON_TEXT, mBottomActionBarItemColor, Gravity.LEFT, 0, context.resources.getInteger(R.integer.action_icon_size), context)
                    setBottomActionbar(item6, R.id.action_thumbnail, "9", CustomPlayerUIConstants.ACTION_THUMBNAIL_TEXT, mBottomActionBarItemColor, Gravity.LEFT, 0, context.resources.getInteger(R.integer.action_icon_size), context)
                    if (context.resources.getBoolean(R.bool.enable_TeacherReview_clearAll)) {
                        setBottomActionbar(item8, R.id.teacher_review_clear_all, "", CustomPlayerUIConstants.TEACHER_ACTIONBAR_CLEARALL, mPentoolToolbarItemColor, Gravity.LEFT, 0, context.resources.getInteger(R.integer.action_icon_size), context)
                    }
                    if (context.resources.getBoolean(R.bool.show_data_submit)) {
                        if (PlayerController.getInstance(context).isUgcShareEnabled) {
                            if (accountType == Constants.TEACHER) {
                                if (context.resources.getBoolean(R.bool.is_voyger_client)) {
                                    if (IsClassAccociated) {
                                        setBottomActionbar(item7, R.id.topbar_review, "G", PlayerUIConstants.TB_TEACHER_IC_TEXT, mBottomActionBarItemColor, Gravity.LEFT, 0, context.resources.getInteger(R.integer.action_icon_size), context)
                                    }
                                } else {
                                    setBottomActionbar(item7, R.id.topbar_review, "G", PlayerUIConstants.TB_TEACHER_IC_TEXT, mBottomActionBarItemColor, Gravity.LEFT, 0, context.resources.getInteger(R.integer.action_icon_size), context)
                                }
                            } else {
                                setBottomActionbar(item7, R.id.topbar_review, "G", PlayerUIConstants.TB_STUDENT_IC_TEXT, mBottomActionBarItemColor, Gravity.LEFT, 0, context.resources.getInteger(R.integer.action_icon_size), context)
                            }
                        }
                    }
                    if (protractorenable != null && protractorenable.equals("yes", ignoreCase = true)) {
                        setBottomActionbar(item10, R.id.text_protractor, "p", PlayerUIConstants.TB_PROTRACTOR_IC_TEXT, mBottomActionBarItemColor, Gravity.LEFT, 0, context.resources.getInteger(R.integer.action_icon_size), context)
                    }
                } else if (mReaderType == EBookType.REFLOWEPUB) {
                    val displayMetrics = DisplayMetrics()

                    (context as HelpScreenActivity).windowManager.defaultDisplay.getMetrics(displayMetrics)
                    val width = displayMetrics.widthPixels
                    if (SDKManager.getInstance().isMediaOverlay /*|| true*/) {
                        setBottomActionbar(item1, R.id.action_toc, "B", CustomPlayerUIConstants.ACTION_TOC_TEXT, mBottomActionBarItemColor, Gravity.LEFT, width / 12, context.getResources().getInteger(R.integer.action_icon_size), context)
                        setBottomActionbar(item2, R.id.action_my_data, "C", CustomPlayerUIConstants.ACTION_MYDATA_TEXT, mBottomActionBarItemColor, Gravity.LEFT, width / 12, context.getResources().getInteger(R.integer.action_icon_size), context)
                        setBottomActionbar(item3, R.id.action_search, "E", CustomPlayerUIConstants.ACTION_SEARCH_TEXT, mBottomActionBarItemColor, Gravity.LEFT, width / 12, context.getResources().getInteger(R.integer.action_icon_size), context)
                        setBottomActionbar(item4, R.id.read_aloud, "D", CustomPlayerUIConstants.READ_AUDIO_EPUB, mBottomActionBarItemColor, Gravity.LEFT, width / 12, context.getResources().getInteger(R.integer.action_icon_size), context)
//                        mReadAloudIcon = item4
//                        disableReadAloudIcon()
                        setBottomActionbar(mEpubSettingPanel, R.id.action_font_settings, "F", CustomPlayerUIConstants.ACTION_FONT_SETTING, mBottomActionBarItemColor, Gravity.LEFT, width / 12, context.getResources().getInteger(R.integer.action_icon_size), context)
                    } else {
                        if (!context.getResources().getBoolean(R.bool.is_sparkCapital_client)) setBottomActionbar(item1, R.id.action_toc, "B", CustomPlayerUIConstants.ACTION_TOC_TEXT, mBottomActionBarItemColor, Gravity.LEFT, context.getResources().getInteger(R.integer.mobile_bottombar_epub_toc_margin), context.getResources().getInteger(R.integer.action_icon_size), context)
                        setBottomActionbar(item2, R.id.action_my_data, "C", CustomPlayerUIConstants.ACTION_MYDATA_TEXT, mBottomActionBarItemColor, Gravity.LEFT, context.getResources().getInteger(R.integer.mobile_bottombar_epub_common_margin), context.getResources().getInteger(R.integer.action_icon_size), context)
                        setBottomActionbar(item3, R.id.action_search, "D", CustomPlayerUIConstants.ACTION_SEARCH_TEXT, mBottomActionBarItemColor, Gravity.LEFT, context.getResources().getInteger(R.integer.mobile_bottombar_epub_search_margin), context.getResources().getInteger(R.integer.action_icon_size), context)
                        setBottomActionbar(mEpubSettingPanel, R.id.action_font_settings, "C", CustomPlayerUIConstants.ACTION_FONT_SETTING, mBottomActionBarItemColor, Gravity.RIGHT, 5, context.getResources().getInteger(R.integer.action_icon_size), context)
                    }
                } else if (mReaderType == EBookType.FIXEDEPUB) {
                    val displayMetrics = DisplayMetrics()
                    (context as HelpScreenActivity).windowManager.defaultDisplay.getMetrics(displayMetrics)
                    val width = displayMetrics.widthPixels
                    if (!context.getResources().getBoolean(R.bool.is_sparkCapital_client)) {
                        if (context.getResources().configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
                            setBottomActionbar(actionItemHome, R.id.action_toc, "B", CustomPlayerUIConstants.ACTION_TOC_TEXT, mBottomActionBarItemColor, Gravity.LEFT, width / 25, context.getResources().getInteger(R.integer.action_icon_size), context)
                        } else {
                            setBottomActionbar(actionItemHome, R.id.action_toc, "B", CustomPlayerUIConstants.ACTION_TOC_TEXT, mBottomActionBarItemColor, Gravity.LEFT, width / 5, context.getResources().getInteger(R.integer.action_icon_size), context)
                        }
                    }
                    val value = 25
                    setBottomActionbar(item2, R.id.action_my_data, "C", CustomPlayerUIConstants.ACTION_MYDATA_TEXT, mBottomActionBarItemColor, Gravity.LEFT, width / value, context.getResources().getInteger(R.integer.action_icon_size), context)
                    setBottomActionbar(item3, R.id.action_search, "D", CustomPlayerUIConstants.ACTION_SEARCH_TEXT, mBottomActionBarItemColor, Gravity.LEFT, width / value, context.getResources().getInteger(R.integer.action_icon_size), context)
                    setBottomActionbar(item4, R.id.action_pen, "E", CustomPlayerUIConstants.ACTION_PEN_TEXT, mBottomActionBarItemColor, Gravity.LEFT, width / value, context.getResources().getInteger(R.integer.action_icon_size), context)
                    setBottomActionbar(item5, R.id.action_sticky_note, "B", CustomPlayerUIConstants.NOTE_ICON_TEXT, mBottomActionBarItemColor, Gravity.LEFT, width / value, context.getResources().getInteger(R.integer.action_icon_size), context)
//                    mthumbnailIcon = item6
                    setBottomActionbar(item6, R.id.action_thumbnail, "9", CustomPlayerUIConstants.ACTION_THUMBNAIL_TEXT, mBottomActionBarItemColor, Gravity.LEFT, width / value, context.getResources().getInteger(R.integer.action_icon_size), context)
                }
            } else {
                if (mReaderType == EBookType.FIXEDKITABOO) {
                    if (context.resources.getBoolean(R.bool.is_sparkCapital_client)) setBottomActionbar(actionItemHome, R.id.action_home, "A", CustomPlayerUIConstants.TOP_ACTION_HOME_TEXT, mBottomActionBarItemColor, Gravity.LEFT, context.resources.getInteger(R.integer.mobile_bottombar_left_margin), context.resources.getInteger(R.integer.action_icon_size), context)
                    if (!context.resources.getBoolean(R.bool.is_sparkCapital_client)) setBottomActionbar(item1, R.id.action_toc, "B", "b", mBottomActionBarItemColor, Gravity.LEFT, context.resources.getInteger(R.integer.mobile_bottombar_pdf_land_margin), context.resources.getInteger(R.integer.action_icon_size), context)
                    setBottomActionbar(item2, R.id.action_my_data, "C", CustomPlayerUIConstants.ACTION_MYDATA_TEXT, mBottomActionBarItemColor, Gravity.LEFT, 0, context.resources.getInteger(R.integer.action_icon_size), context)
                    setBottomActionbar(item3, R.id.action_search, "D", CustomPlayerUIConstants.ACTION_SEARCH_TEXT, mBottomActionBarItemColor, Gravity.LEFT, 0, context.resources.getInteger(R.integer.action_icon_size), context)
                    setBottomActionbar(item4, R.id.action_pen, "E", CustomPlayerUIConstants.ACTION_PEN_TEXT, mBottomActionBarItemColor, Gravity.LEFT, 0, context.resources.getInteger(R.integer.action_icon_size), context)
                    setBottomActionbar(item5, R.id.action_sticky_note, "B", CustomPlayerUIConstants.NOTE_ICON_TEXT, mBottomActionBarItemColor, Gravity.LEFT, 0, context.resources.getInteger(R.integer.action_icon_size), context)
                    setBottomActionbar(item6, R.id.action_thumbnail, "9", CustomPlayerUIConstants.ACTION_THUMBNAIL_TEXT, mBottomActionBarItemColor, Gravity.LEFT, 0, context.resources.getInteger(R.integer.action_icon_size), context)
                    if (context.resources.getBoolean(R.bool.enable_TeacherReview_clearAll)) {
                        setBottomActionbar(item8, R.id.teacher_review_clear_all, "", CustomPlayerUIConstants.TEACHER_ACTIONBAR_CLEARALL, mPentoolToolbarItemColor, Gravity.LEFT, context.resources.getInteger(R.integer.mobile_bottombar_pdf_common_margin), context.resources.getInteger(R.integer.action_icon_size), context)
                    }
                    if (context.resources.getBoolean(R.bool.show_data_submit)) {
                        if (PlayerController.getInstance(context).isUgcShareEnabled) {
                            if (accountType == Constants.TEACHER) {
                                if (context.resources.getBoolean(R.bool.is_voyger_client)) {
                                    if (bookVo.IsClassAssociated()) {
                                        setBottomActionbar(item7, R.id.topbar_review, "G", PlayerUIConstants.TB_TEACHER_IC_TEXT, mBottomActionBarItemColor, Gravity.LEFT, context.resources.getInteger(R.integer.mobile_bottombar_pdf_common_margin), context.resources.getInteger(R.integer.action_icon_size), context)
                                    }
                                } else {
                                    setBottomActionbar(item7, R.id.topbar_review, "G", PlayerUIConstants.TB_TEACHER_IC_TEXT, mBottomActionBarItemColor, Gravity.LEFT, context.resources.getInteger(R.integer.mobile_bottombar_pdf_common_margin), context.resources.getInteger(R.integer.action_icon_size), context)
                                }
                            } else {
                                setBottomActionbar(item7, R.id.topbar_review, "G", PlayerUIConstants.TB_STUDENT_IC_TEXT, mBottomActionBarItemColor, Gravity.LEFT, context.resources.getInteger(R.integer.mobile_bottombar_pdf_common_margin), context.resources.getInteger(R.integer.action_icon_size), context)
                            }
                        }
                    }
                    /*if (accountType.equals(Constants.TEACHER)) {
                        setBottomActionbar(item7, R.id.topbar_review, "G", PlayerUIConstants.TB_TEACHER_IC_TEXT, mBottomActionBarItemColor, Gravity.LEFT, 30, context.getResources().getInteger(R.integer.action_icon_size),context);
                    }
                    else{
                        setBottomActionbar(item7, R.id.topbar_review, "G", PlayerUIConstants.TB_STUDENT_IC_TEXT, mBottomActionBarItemColor, Gravity.LEFT, 30, context.getResources().getInteger(R.integer.action_icon_size),context);
                    }*/if (protractorenable != null && protractorenable.equals("yes", ignoreCase = true)) {
                        setBottomActionbar(item10, R.id.text_protractor, "p", PlayerUIConstants.TB_PROTRACTOR_IC_TEXT, mBottomActionBarItemColor, Gravity.LEFT, 30, context.resources.getInteger(R.integer.action_icon_size), context)
                    }
                } else if (mReaderType == EBookType.REFLOWEPUB) {
                    if (context.resources.getBoolean(R.bool.is_sparkCapital_client)) setBottomActionbar(actionItemHome, R.id.action_home, "A", CustomPlayerUIConstants.TOP_ACTION_HOME_TEXT, mBottomActionBarItemColor, Gravity.LEFT, context.resources.getInteger(R.integer.mobile_bottombar_left_margin), context.resources.getInteger(R.integer.action_icon_size), context)
                    if (SDKManager.getInstance().isMediaOverlay /*|| true*/) {
                        if (!context.resources.getBoolean(R.bool.is_sparkCapital_client)) {
                            setBottomActionbar(item1, R.id.action_toc, "B", "b", mBottomActionBarItemColor, Gravity.LEFT, context.resources.getInteger(R.integer.mobile_bottombar_epub_land_toc_margin), context.resources.getInteger(R.integer.action_icon_size), context)
                            setBottomActionbar(item2, R.id.action_my_data, "C", CustomPlayerUIConstants.ACTION_MYDATA_TEXT, mBottomActionBarItemColor, Gravity.LEFT, context.resources.getInteger(R.integer.mobile_bottombar_epub_land_common_margin), context.resources.getInteger(R.integer.action_icon_size), context)
                            setBottomActionbar(item3, R.id.action_search, "D", CustomPlayerUIConstants.ACTION_SEARCH_TEXT, mBottomActionBarItemColor, Gravity.LEFT, context.resources.getInteger(R.integer.mobile_bottombar_epub_land_common_margin), context.resources.getInteger(R.integer.action_icon_size), context)
                            setBottomActionbar(item4, R.id.read_aloud, "E", CustomPlayerUIConstants.READ_AUDIO_EPUB, mBottomActionBarItemColor, Gravity.LEFT, context.resources.getInteger(R.integer.mobile_bottombar_aduioSync_common_margin), context.resources.getInteger(R.integer.action_icon_size), context)
//                            mReadAloudIcon = item4
//                            disableReadAloudIcon()
                            setBottomActionbar(mEpubSettingPanel, R.id.action_font_settings, "F", CustomPlayerUIConstants.ACTION_FONT_SETTING, mBottomActionBarItemColor, Gravity.LEFT, context.resources.getInteger(R.integer.mobile_bottombar_epub_land_common_margin), context.resources.getInteger(R.integer.action_icon_size), context)
                        }
                    } else {
                        if (!context.resources.getBoolean(R.bool.is_sparkCapital_client)) {
                            setBottomActionbar(item1, R.id.action_toc, "B", "b", mBottomActionBarItemColor, Gravity.LEFT, context.resources.getInteger(R.integer.mobile_bottombar_epub_land_toc_margin), context.resources.getInteger(R.integer.action_icon_size), context)
                            setBottomActionbar(item2, R.id.action_my_data, "C", CustomPlayerUIConstants.ACTION_MYDATA_TEXT, mBottomActionBarItemColor, Gravity.LEFT, context.resources.getInteger(R.integer.mobile_bottombar_epub_land_common_margin), context.resources.getInteger(R.integer.action_icon_size), context)
                            setBottomActionbar(item3, R.id.action_search, "D", CustomPlayerUIConstants.ACTION_SEARCH_TEXT, mBottomActionBarItemColor, Gravity.LEFT, context.resources.getInteger(R.integer.mobile_bottombar_epub_land_common_margin), context.resources.getInteger(R.integer.action_icon_size), context)
                            setBottomActionbar(mEpubSettingPanel, R.id.action_font_settings, "C", CustomPlayerUIConstants.ACTION_FONT_SETTING, mBottomActionBarItemColor, Gravity.LEFT, context.resources.getInteger(R.integer.mobile_bottombar_epub_land_common_margin), context.resources.getInteger(R.integer.action_icon_size), context)
                        }
                    }
                } else if (mReaderType == EBookType.FIXEDEPUB) {
                    if (context.resources.getBoolean(R.bool.is_sparkCapital_client)) setBottomActionbar(actionItemHome, R.id.action_home, "A", CustomPlayerUIConstants.TOP_ACTION_HOME_TEXT, mBottomActionBarItemColor, Gravity.LEFT, context.resources.getInteger(R.integer.mobile_bottombar_left_margin_fixed_epub), context.resources.getInteger(R.integer.action_icon_size), context)
                    if (!context.resources.getBoolean(R.bool.is_sparkCapital_client)) setBottomActionbar(item1, R.id.action_toc, "B", "b", mBottomActionBarItemColor, Gravity.LEFT, context.resources.getInteger(R.integer.mobile_bottombar_epub_land_margin), context.resources.getInteger(R.integer.action_icon_size), context)
                    setBottomActionbar(item2, R.id.action_my_data, "C", CustomPlayerUIConstants.ACTION_MYDATA_TEXT, mBottomActionBarItemColor, Gravity.LEFT, context.resources.getInteger(R.integer.mobile_bottombar_epub_land_common_margin), context.resources.getInteger(R.integer.action_icon_size), context)
                    setBottomActionbar(item3, R.id.action_search, "D", CustomPlayerUIConstants.ACTION_SEARCH_TEXT, mBottomActionBarItemColor, Gravity.LEFT, context.resources.getInteger(R.integer.mobile_bottombar_epub_land_common_margin), context.resources.getInteger(R.integer.action_icon_size), context)
                    setBottomActionbar(item4, R.id.action_pen, "E", CustomPlayerUIConstants.ACTION_PEN_TEXT, mBottomActionBarItemColor, Gravity.LEFT, context.resources.getInteger(R.integer.mobile_bottombar_epub_land_common_margin), context.resources.getInteger(R.integer.action_icon_size), context)
                    setBottomActionbar(item5, R.id.action_sticky_note, "B", CustomPlayerUIConstants.NOTE_ICON_TEXT, mBottomActionBarItemColor, Gravity.LEFT, context.resources.getInteger(R.integer.mobile_bottombar_epub_land_common_margin), context.resources.getInteger(R.integer.action_icon_size), context)
//                    mthumbnailIcon = item6
                    setBottomActionbar(item6, R.id.action_thumbnail, "9", CustomPlayerUIConstants.ACTION_THUMBNAIL_TEXT, mBottomActionBarItemColor, Gravity.LEFT, context.resources.getInteger(R.integer.mobile_bottombar_epub_land_common_margin), context.resources.getInteger(R.integer.action_icon_size), context)
//                    disableReadAIcon()
                }
            }

            /*if (mReaderType == EBookType.FIXEDKITABOO) {
                setBottomActionbar(textAnnotation, R.id.text_annotation, "A", "ʧ", mBottomActionBarItemColor, Gravity.LEFT , 0);
            }*/
        } else {
            if (mReaderType == EBookType.FIXEDKITABOO) {
                if (context.resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
                    if (context.resources.getBoolean(R.bool.is_sparkCapital_client)) setBottomActionbar(actionItemHome, R.id.action_home, "A", CustomPlayerUIConstants.TOP_ACTION_HOME_TEXT, mBottomActionBarItemColor, Gravity.LEFT, context.resources.getInteger(R.integer.action_bar_bottom_pdf_margin), context.resources.getInteger(R.integer.action_icon_size), context)
                } else {
                    if (context.resources.getBoolean(R.bool.is_sparkCapital_client)) setBottomActionbar(actionItemHome, R.id.action_home, "A", CustomPlayerUIConstants.TOP_ACTION_HOME_TEXT, mBottomActionBarItemColor, Gravity.LEFT, context.resources.getInteger(R.integer.action_bar_bottom_pdf_land_margin), context.resources.getInteger(R.integer.action_icon_size), context)
                }
            } else {
                if (context.resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
                    if (mReaderType == EBookType.REFLOWEPUB) {
                        if (context.resources.getBoolean(R.bool.is_sparkCapital_client)) setBottomActionbar(actionItemHome, R.id.action_home, "A", CustomPlayerUIConstants.TOP_ACTION_HOME_TEXT, mBottomActionBarItemColor, Gravity.LEFT, context.resources.getInteger(R.integer.action_bar_bottom_epub_margin), context.resources.getInteger(R.integer.action_icon_size), context)
                    } else {
                        if (context.resources.getBoolean(R.bool.is_sparkCapital_client)) setBottomActionbar(actionItemHome, R.id.action_home, "A", CustomPlayerUIConstants.TOP_ACTION_HOME_TEXT, mBottomActionBarItemColor, Gravity.LEFT, context.resources.getInteger(R.integer.action_bar_bottom_epub_margin_fixed), context.resources.getInteger(R.integer.action_icon_size), context)
                    }
                } else {
                    if (context.resources.getBoolean(R.bool.is_sparkCapital_client)) setBottomActionbar(actionItemHome, R.id.action_home, "A", CustomPlayerUIConstants.TOP_ACTION_HOME_TEXT, mBottomActionBarItemColor, Gravity.LEFT, context.resources.getInteger(R.integer.action_bar_bottom_common_margin), context.resources.getInteger(R.integer.action_icon_size), context)
                }
            }
            /*if (!context.getResources().getBoolean(R.bool.is_sparkCapital_client)) {
                if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                    setBottomActionbar(actionItemHome, R.id.action_toc, "B", CustomPlayerUIConstants.ACTION_TOC_TEXT, mBottomActionBarItemColor, Gravity.LEFT, context.getResources().getInteger(R.integer.action_bar_bottom_pdf_margin));
                } else {
                    setBottomActionbar(actionItemHome, R.id.action_toc, "B", CustomPlayerUIConstants.ACTION_TOC_TEXT, mBottomActionBarItemColor, Gravity.LEFT, context.getResources().getInteger(R.integer.action_bar_bottom_pdf_land_margin));
                }
            }*/

            // setBottomActionbar(item2, R.id.action_my_data, "C", CustomPlayerUIConstants.ACTION_MYDATA_TEXT, mBottomActionBarItemColor, Gravity.LEFT, context.getResources().getInteger(R.integer.action_bar_bottom_common_margin));
            // setBottomActionbar(item3, R.id.action_search, "D", CustomPlayerUIConstants.ACTION_SEARCH_TEXT, mBottomActionBarItemColor, Gravity.LEFT, context.getResources().getInteger(R.integer.action_bar_bottom_common_margin));
            if (mReaderType == EBookType.FIXEDKITABOO) {
                if (!context.resources.getBoolean(R.bool.is_sparkCapital_client)) {
                    if (context.resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
                        setBottomActionbar(actionItemHome, R.id.action_toc, "B", CustomPlayerUIConstants.ACTION_TOC_TEXT, mBottomActionBarItemColor, Gravity.LEFT, context.resources.getInteger(R.integer.action_bar_bottom_common_margin), context.resources.getInteger(R.integer.action_icon_size), context)
                    } else {
                        setBottomActionbar(actionItemHome, R.id.action_toc, "B", CustomPlayerUIConstants.ACTION_TOC_TEXT, mBottomActionBarItemColor, Gravity.LEFT, context.resources.getInteger(R.integer.action_bar_bottom_pdf_land_margin), context.resources.getInteger(R.integer.action_icon_size), context)
                    }
                }
                setBottomActionbar(item2, R.id.action_my_data, "C", CustomPlayerUIConstants.ACTION_MYDATA_TEXT, mBottomActionBarItemColor, Gravity.LEFT, context.resources.getInteger(R.integer.action_bar_bottom_common_margin), context.resources.getInteger(R.integer.action_icon_size), context)
                setBottomActionbar(item3, R.id.action_search, "D", CustomPlayerUIConstants.ACTION_SEARCH_TEXT, mBottomActionBarItemColor, Gravity.LEFT, context.resources.getInteger(R.integer.action_bar_bottom_common_margin), context.resources.getInteger(R.integer.action_icon_size), context)
                setBottomActionbar(item4, R.id.action_pen, "E", CustomPlayerUIConstants.ACTION_PEN_TEXT, mBottomActionBarItemColor, Gravity.LEFT, context.resources.getInteger(R.integer.action_bar_bottom_common_margin), context.resources.getInteger(R.integer.action_icon_size), context)
                setBottomActionbar(item5, R.id.action_sticky_note, "B", CustomPlayerUIConstants.NOTE_ICON_TEXT, mBottomActionBarItemColor, Gravity.LEFT, context.resources.getInteger(R.integer.action_bar_bottom_common_margin), context.resources.getInteger(R.integer.action_icon_size), context)
                setBottomActionbar(item6, R.id.action_thumbnail, "9", CustomPlayerUIConstants.ACTION_THUMBNAIL_TEXT, mBottomActionBarItemColor, Gravity.LEFT, context.resources.getInteger(R.integer.action_bar_bottom_common_margin), context.resources.getInteger(R.integer.action_icon_size), context)
                // KitabooActionItemView item10 = new KitabooActionItemView(context );
                if (context.resources.getBoolean(R.bool.enable_TeacherReview_clearAll)) {
                    setBottomActionbar(item8, R.id.teacher_review_clear_all, "", CustomPlayerUIConstants.TEACHER_ACTIONBAR_CLEARALL, mPentoolToolbarItemColor, Gravity.LEFT, context.resources.getInteger(R.integer.mobile_bottombar_pdf_common_margin), context.resources.getInteger(R.integer.action_icon_size), context)
                }
                if (context.resources.getBoolean(R.bool.show_data_submit)) {
                    if (PlayerController.getInstance(context).isUgcShareEnabled) {
                        if (accountType == Constants.TEACHER) {
                            if (context.resources.getBoolean(R.bool.is_voyger_client)) {
                                if (IsClassAccociated) {
                                    setBottomActionbar(item7, R.id.topbar_review, "G", PlayerUIConstants.TB_TEACHER_IC_TEXT, mBottomActionBarItemColor, Gravity.LEFT, context.resources.getInteger(R.integer.mobile_bottombar_pdf_common_margin), context.resources.getInteger(R.integer.action_icon_size), context)
                                }
                            } else {
                                setBottomActionbar(item7, R.id.topbar_review, "G", PlayerUIConstants.TB_TEACHER_IC_TEXT, mBottomActionBarItemColor, Gravity.LEFT, context.resources.getInteger(R.integer.mobile_bottombar_pdf_common_margin), context.resources.getInteger(R.integer.action_icon_size), context)
                            }
                        } else {
                            setBottomActionbar(item7, R.id.topbar_review, "G", PlayerUIConstants.TB_STUDENT_IC_TEXT, mBottomActionBarItemColor, Gravity.LEFT, context.resources.getInteger(R.integer.mobile_bottombar_pdf_common_margin), context.resources.getInteger(R.integer.action_icon_size), context)
                        }
                    }
                }
                /*if (accountType.equals(Constants.TEACHER)) {
                    setBottomActionbar(item7, R.id.topbar_review, "G", PlayerUIConstants.TB_TEACHER_IC_TEXT, mBottomActionBarItemColor, Gravity.LEFT, context.getResources().getInteger(R.integer.action_bar_bottom_common_margin), context.getResources().getInteger(R.integer.action_icon_size),context);
                } else {
                    setBottomActionbar(item7, R.id.topbar_review, "G", PlayerUIConstants.TB_STUDENT_IC_TEXT, mBottomActionBarItemColor, Gravity.LEFT, context.getResources().getInteger(R.integer.action_bar_bottom_common_margin), context.getResources().getInteger(R.integer.action_icon_size),context);
                }*/if (protractorenable != null && protractorenable.equals("yes", ignoreCase = true)) {
                    setBottomActionbar(item10, R.id.text_protractor, "p", PlayerUIConstants.TB_PROTRACTOR_IC_TEXT, mBottomActionBarItemColor, Gravity.LEFT, context.resources.getInteger(R.integer.mobile_bottombar_pdf_common_margin), context.resources.getInteger(R.integer.action_icon_size), context)
                }
            } else {
                val displayMetrics = DisplayMetrics()
                (context as HelpScreenActivity).windowManager.defaultDisplay.getMetrics(displayMetrics)
                val height = displayMetrics.heightPixels
                val width = displayMetrics.widthPixels
                if (mReaderType == EBookType.REFLOWEPUB) {
                    if (SDKManager.getInstance().isMediaOverlay /*|| true*/) {
                        if (!context.getResources().getBoolean(R.bool.is_sparkCapital_client)) {
                            if (context.getResources().configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
                                setBottomActionbar(actionItemHome, R.id.action_toc, "B", CustomPlayerUIConstants.ACTION_TOC_TEXT, mBottomActionBarItemColor, Gravity.LEFT, width / 7, context.getResources().getInteger(R.integer.action_icon_size), context)
                            } else {
                                setBottomActionbar(actionItemHome, R.id.action_toc, "B", CustomPlayerUIConstants.ACTION_TOC_TEXT, mBottomActionBarItemColor, Gravity.LEFT, width / 5, context.getResources().getInteger(R.integer.action_icon_size), context)
                            }
                        }
                        setBottomActionbar(item2, R.id.action_my_data, "C", CustomPlayerUIConstants.ACTION_MYDATA_TEXT, mBottomActionBarItemColor, Gravity.LEFT, width / 13, context.getResources().getInteger(R.integer.action_icon_size), context)
                        setBottomActionbar(item3, R.id.action_search, "D", CustomPlayerUIConstants.ACTION_SEARCH_TEXT, mBottomActionBarItemColor, Gravity.LEFT, width / 13, context.getResources().getInteger(R.integer.action_icon_size), context)
                        setBottomActionbar(item4, R.id.read_aloud, "E", CustomPlayerUIConstants.READ_AUDIO_EPUB, mBottomActionBarItemColor, Gravity.LEFT, width / 13, context.getResources().getInteger(R.integer.action_icon_size), context)
//                        mReadAloudIcon = item4
//                        disableReadAloudIcon()
//                        setBottomActionbar(mEpubSettingPanel, R.id.action_font_settings, "F", CustomPlayerUIConstants.ACTION_FONT_SETTING, mBottomActionBarItemColor, Gravity.LEFT, width / 13, context.getResources().getInteger(R.integer.action_icon_size),context)
                    } else {
                        if (!context.getResources().getBoolean(R.bool.is_sparkCapital_client)) {
                            if (context.getResources().configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
                                setBottomActionbar(actionItemHome, R.id.action_toc, "B", CustomPlayerUIConstants.ACTION_TOC_TEXT, mBottomActionBarItemColor, Gravity.LEFT, width / 5, context.getResources().getInteger(R.integer.action_icon_size), context)
                            } else {
                                setBottomActionbar(actionItemHome, R.id.action_toc, "B", CustomPlayerUIConstants.ACTION_TOC_TEXT, mBottomActionBarItemColor, Gravity.LEFT, width / 5, context.getResources().getInteger(R.integer.action_icon_size), context)
                            }
                        }
                        setBottomActionbar(item2, R.id.action_my_data, "C", CustomPlayerUIConstants.ACTION_MYDATA_TEXT, mBottomActionBarItemColor, Gravity.LEFT, width / 10, context.getResources().getInteger(R.integer.action_icon_size), context)
                        setBottomActionbar(item3, R.id.action_search, "D", CustomPlayerUIConstants.ACTION_SEARCH_TEXT, mBottomActionBarItemColor, Gravity.LEFT, width / 10, context.getResources().getInteger(R.integer.action_icon_size), context)
                        setBottomActionbar(mEpubSettingPanel, R.id.action_font_settings, "C", CustomPlayerUIConstants.ACTION_FONT_SETTING, mBottomActionBarItemColor, Gravity.LEFT, width / 10, context.getResources().getInteger(R.integer.action_icon_size), context)
                    }
                }
            }
            if (mReaderType == EBookType.FIXEDEPUB) {
                val displayMetrics = DisplayMetrics()
                (context as HelpScreenActivity).windowManager.defaultDisplay.getMetrics(displayMetrics)
                val height = displayMetrics.heightPixels
                val width = displayMetrics.widthPixels
                if (mReaderType == EBookType.FIXEDEPUB) {
                    if (!context.getResources().getBoolean(R.bool.is_sparkCapital_client)) {
                        if (context.getResources().configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
                            setBottomActionbar(actionItemHome, R.id.action_toc, "B", CustomPlayerUIConstants.ACTION_TOC_TEXT, mBottomActionBarItemColor, Gravity.LEFT, width / 8, context.getResources().getInteger(R.integer.action_icon_size), context)
                        } else {
                            setBottomActionbar(actionItemHome, R.id.action_toc, "B", CustomPlayerUIConstants.ACTION_TOC_TEXT, mBottomActionBarItemColor, Gravity.LEFT, width / 5, context.getResources().getInteger(R.integer.action_icon_size), context)
                        }
                    }
                    val value = 20
                    setBottomActionbar(item2, R.id.action_my_data, "C", CustomPlayerUIConstants.ACTION_MYDATA_TEXT, mBottomActionBarItemColor, Gravity.LEFT, width / value, context.getResources().getInteger(R.integer.action_icon_size), context)
                    setBottomActionbar(item3, R.id.action_search, "D", CustomPlayerUIConstants.ACTION_SEARCH_TEXT, mBottomActionBarItemColor, Gravity.LEFT, width / value, context.getResources().getInteger(R.integer.action_icon_size), context)
                    setBottomActionbar(item4, R.id.action_pen, "E", CustomPlayerUIConstants.ACTION_PEN_TEXT, mBottomActionBarItemColor, Gravity.LEFT, width / value, context.getResources().getInteger(R.integer.action_icon_size), context)
                    setBottomActionbar(item5, R.id.action_sticky_note, "B", CustomPlayerUIConstants.NOTE_ICON_TEXT, mBottomActionBarItemColor, Gravity.LEFT, width / value, context.getResources().getInteger(R.integer.action_icon_size), context)
//                    mthumbnailIcon = item6
                    setBottomActionbar(item6, R.id.action_thumbnail, "9", CustomPlayerUIConstants.ACTION_THUMBNAIL_TEXT, mBottomActionBarItemColor, Gravity.LEFT, width / value, context.getResources().getInteger(R.integer.action_icon_size), context)
//                    disableReadAIcon()
                }
            }
            /*if (mReaderType == EBookType.FIXEDKITABOO) {

                setBottomActionbar(textAnnotation, R.id.text_annotation, "A", "ʧ", mBottomActionBarItemColor, Gravity.LEFT , context.getResources().getInteger(R.integer.action_bar_bottom_common_margin));
            }*/
        }
    }

    /**
     * Customise the bottom actionbar item with different icon , color
     *
     * @param item            : Actionbar item view
     * @param itemId          : id of Actionbar item view
     * @param itemName        : name of Actionbar item view
     * @param charManningChar : special character from ttf file
     * @param itemColor       : color of Actionbar item view
     * @param itemGravity     : left/right as required respective to actionbar
     */
    private fun setBottomActionbar(item: KitabooActionItemView, itemId: Int, itemName: String, charManningChar: String,
                                   itemColor: Int, itemGravity: Int, leftMargin: Int, textSize: Int, context: Context) {
        item.setId(itemId, item)
        item.uniqueName = itemName
        item.setCharatorManningChar(charManningChar)
        item.setTextColor(itemColor)
        item.gravity = Gravity.CENTER or Gravity.CENTER_VERTICAL
        item.typeface = if (itemId == R.id.teacher_review_done || itemId == R.id.action_page_zoom_percentage) null else this.topActionbar!!.defaultActionbarTypeface(context)
        item.isAllCaps = false
        item.textSize = textSize.toFloat()
        item.leftMargin = leftMargin
        if (itemId == R.id.action_page_zoom_percentage) {
//            item.text = charManningChar + String.format(getString(R.string.add_percentage))
//            mZoomText = charManningChar.toInt()
        }
        if (Utility.isDeviceTypeMobile(context)) {
            /*if (context.getResources().getBoolean(R.bool.is_it_worldbook)) {
                item.setMinimumWidth(160);
            } else {*/
            if (item.id == R.id.teacher_review_green || item.id == R.id.teacher_review_red) item.minimumWidth = context.resources.getInteger(R.integer.action_icon_min_width) else item.minimumWidth = context.resources.getInteger(R.integer.actionbar_menu_width_mobile)
            //}
        } else item.minimumWidth = 100
        /* else {
            //item.setMinimumWidth(100);
            //item.setMinimumWidth(context.getResources().getInteger(R.integer.actionbar_menu_width_tab));
        }*/


        //bottomActionbar.setBackgroundColor(context.getResources().getColor(R.color.white));
        bottomActionbar.setTheme(readerThemeSettingVo)
        bottomActionbar.addActionItem(item, itemGravity)
//        bottomActionbar.addEventCallback(context)
    }

    fun calculateBottomItemLineX(context: Context): Int {
        val numberOfBottomChiled = bottomActionbar.childCount
        val oneItemWidth = bottomActionbar.width / numberOfBottomChiled
        val itemWidthCenter = oneItemWidth / 2
        return itemWidthCenter
    }

    fun createActionBarFeatureText(vo: HelpVo,
                                   isBookShelf: Boolean,
                                   llBottomFeatureTextContainer: LinearLayout,
                                   rlTopFeatureTextContainer: RelativeLayout,
                                   context: Context,
                                   pagePosition: Int) {
        var featureText: String = ""
        var featureDescription: String = ""
        if (pagePosition == 1 && !extraPreviousWeightAdded) {
            addExtraPreviousWeight(context, llBottomFeatureTextContainer)
        }

        //View container for line and text
        val relativeLayout = RelativeLayout(context)
        val layoutParams: LinearLayout.LayoutParams =
                LinearLayout.LayoutParams(0,
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        1F)

        if (vo.bottomState) {
            bottomCount++

            when {
                context.resources.getString(vo.helpText).equals(context.resources.getString(R.string.search), ignoreCase = true) -> {
                    featureText = context.resources.getString(R.string.search)
                    featureDescription = context.resources.getString(R.string.search_) + "\n"
                    context.resources.getString(R.string.search_1)
                }
                context.resources.getString(vo.helpText).equals(context.resources.getString(R.string.toc), ignoreCase = true) -> {
                    featureText = context.resources.getString(R.string.toc)
                    featureDescription = context.resources.getString(R.string.table_Contents_)
                }
                context.resources.getString(vo.helpText).equals(context.resources.getString(R.string.my_data), ignoreCase = true) -> {
                    featureText = context.resources.getString(R.string.my_data)
                    featureDescription = context.resources.getString(R.string.my_data_1) + "\n" +
                            context.resources.getString(R.string.my_data_2)
                }
                context.resources.getString(vo.helpText).equals(context.resources.getString(R.string.pen), ignoreCase = true) -> {
                    featureText = context.resources.getString(R.string.pen)
                    featureDescription = context.resources.getString(R.string.pen_content1) + "\n" +
                            context.resources.getString(R.string.pen_content2)
                }
                context.resources.getString(vo.helpText).equals(context.resources.getString(R.string.font_setting), ignoreCase = true) -> {
                    featureText = context.resources.getString(R.string.font_setting)
                    featureDescription = context.resources.getString(R.string.font_setting_)
                }
                context.resources.getString(vo.helpText).equals(context.resources.getString(R.string.addnote), ignoreCase = true) -> {
                    featureText = context.resources.getString(R.string.addnote)
                    featureDescription = context.resources.getString(R.string.addnote_)
                }
                context.resources.getString(vo.helpText).equals(context.resources.getString(R.string.thumbnail), ignoreCase = true) -> {
                    featureText = context.resources.getString(R.string.thumbnail)
                    featureDescription = context.resources.getString(R.string.thumbnail_1) + "\n" +
                            context.resources.getString(R.string.thumbnail_2)
                }
                context.resources.getString(vo.helpText).equals(context.resources.getString(R.string.clearall), ignoreCase = true) -> {
                    featureText = context.resources.getString(R.string.clearall)
                    featureDescription = context.resources.getString(R.string.clearall_)
                }
                context.resources.getString(vo.helpText).equals(context.resources.getString(R.string.settings), ignoreCase = true) -> {
                    featureText = context.resources.getString(R.string.settings)
                    featureDescription = context.resources.getString(R.string.settings_)
                }
                context.resources.getString(vo.helpText).equals(context.resources.getString(R.string.marker_1), ignoreCase = true) -> {
                    featureText = context.resources.getString(R.string.marker_1)
                    featureDescription = context.resources.getString(R.string.marker1) + "\n" +
                            context.resources.getString(R.string.marker2)
                }
                context.resources.getString(vo.helpText).equals(context.resources.getString(R.string.marker_2), ignoreCase = true) -> {
                    featureText = context.resources.getString(R.string.marker_2)
                    featureDescription = context.resources.getString(R.string.marker1) + "\n" +
                            context.resources.getString(R.string.marker2)
                }
                context.resources.getString(vo.helpText).equals(context.resources.getString(R.string.eraser), ignoreCase = true) -> {
                    featureText = context.resources.getString(R.string.eraser)
                    featureDescription =context.resources.getString(R.string.eraser_)
                }
                context.resources.getString(vo.helpText).equals(context.resources.getString(R.string.arrow1), ignoreCase = true) -> {
                    featureText = context.resources.getString(R.string.arrow1)
                    featureDescription =context.resources.getString(R.string.arrow_1)+"\n"+context.resources.getString(R.string.arrow_1)
                }
                context.resources.getString(vo.helpText).equals(context.resources.getString(R.string.undo), ignoreCase = true) -> {
                    featureText = context.resources.getString(R.string.undo)+" And "+context.resources.getString(R.string.undo_1)
                    featureDescription =context.resources.getString(R.string.undo_)+"\n"+context.resources.getString(R.string.arrow_1)
                }
                context.resources.getString(vo.helpText).equals(context.resources.getString(R.string.clear_All), ignoreCase = true) -> {
                    featureText = context.resources.getString(R.string.clear_All)
                    featureDescription =context.resources.getString(R.string.clear_all)
                }
                context.resources.getString(vo.helpText).equals(context.resources.getString(R.string.done1), ignoreCase = true) -> {
                    featureText = context.resources.getString(R.string.done1)
                    featureDescription =context.resources.getString(R.string.done2)
                }
            }

            //set top margins according to orientation and help page index
            when (pagePosition) {
                0 -> {
                    if (bottomContainerHeight == 0) {
                        bottomContainerHeight = ((Utils.getDeviceHeight(context) * 60) / 100)
                    }
                    try {
                        this.marginTop = (bottomContainerHeight / pageTwoCount)
                        pageTwoCount--

                    } catch (e: Exception) {
                        this.marginTop = this.marginTop / 2
                    }

                    layoutParams.setMargins(0, this.marginTop, 0, bottomActionbar.height)

                }
                1 -> {
                    if (pageOneCount == 0) {
                        pageOneCount = 1
                    }
                    if (bottomContainerHeightNext == 0) {
                        bottomContainerHeightNext = (Utils.getDeviceHeight(context) * 60) / 100
                    }
                    this.marginTopNextPosition = bottomContainerHeightNext / pageOneCount
                    pageOneCount++
                    layoutParams.setMargins(0, this.marginTopNextPosition, 0, bottomActionbar.height)
                }
            }

            relativeLayout.layoutParams = layoutParams


            //Name and Description
            val textViewDescription = TextView(context)
            textViewDescription.textSize = 10f
            textDescriptionId += 1
            textViewDescription.id = textDescriptionId

            //creating Name and description span
            val spannableFeatureText = SpannableString(featureText)
            spannableFeatureText.setSpan(RelativeSizeSpan(1f), 0, featureText.length, 0)
            spannableFeatureText.setSpan(ForegroundColorSpan(Color.CYAN), 0, featureText.length, 0)

            textViewDescription.text = TextUtils.concat(spannableFeatureText, "\n" + featureDescription)
            textViewDescription.gravity = Gravity.CENTER_HORIZONTAL
            val textParams = RelativeLayout.LayoutParams(200,
                    RelativeLayout.LayoutParams.WRAP_CONTENT)
            textParams.addRule(RelativeLayout.CENTER_HORIZONTAL)
            textViewDescription.layoutParams = textParams


            //vertical line
            val lineView = View(context)
            lineView.id = vo.helpText
            lineView.setBackgroundColor(ContextCompat.getColor(context, R.color.white))
            val viewParams: RelativeLayout.LayoutParams = RelativeLayout.LayoutParams(4,
                    RelativeLayout.LayoutParams.WRAP_CONTENT)
            viewParams.addRule(RelativeLayout.CENTER_IN_PARENT)
            viewParams.addRule(RelativeLayout.BELOW, textViewDescription.id)
            lineView.layoutParams = viewParams

            val imageView = ImageView(context)
            imageView.setBackgroundResource(R.drawable.action_indicator)
            imageView.setBackgroundColor(mBottomActionBarItemColor)
            val imageParams: RelativeLayout.LayoutParams = RelativeLayout.LayoutParams(10,
                    10)
            imageParams.addRule(RelativeLayout.ABOVE, lineView.id)
            imageParams.addRule(RelativeLayout.CENTER_HORIZONTAL)
            imageView.layoutParams = imageParams


            relativeLayout.addView(textViewDescription)
            relativeLayout.addView(lineView)
//            relativeLayout.addView(imageView)


            //add to bottom linear layout
            llBottomFeatureTextContainer.addView(relativeLayout)

        } else {

            val relativeLayoutTopItem = RelativeLayout(context)
            val topItemLayoutParams = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.MATCH_PARENT)
            topItemLayoutParams.topMargin = getStatusbarHeight(context)
            if (!isBookShelf)
                topItemLayoutParams.topMargin = topActionbar!!.height

            //Name and Description
            val textViewDescription = TextView(context)
            textViewDescription.textSize = 10f
            textDescriptionId += 1
            textViewDescription.id = textDescriptionId

            //vertical line
            val lineView = View(context)
            lineView.id = vo.helpText
            lineView.setBackgroundColor(ContextCompat.getColor(context, R.color.white))
            when {
                context.resources.getString(vo.helpText).equals(context.resources.getString(R.string.profile), ignoreCase = true) -> {
                    if (isBookShelf) {
                        val topContainer = rlTopFeatureTextContainer.findViewById<RelativeLayout>(R.id.topContainer)
                        var tvProfileIcon = topContainer.findViewById<TextView>(R.id.profile_pic_icon)
                        tvProfileIcon.visibility = View.INVISIBLE
                        tvProfileIcon.typeface = Typefaces.get(context, context.resources.getString(R.string.kitaboo_bookshelf_font_file_name))
                        tvProfileIcon.isAllCaps = false
                        if (!com.hurix.kitaboo.constants.utils.Utils.isMobile(context)) {
                            tvProfileIcon.text = ShelfUIConstants.SHELF_DEFAULT_IC_TEXT
                        } else if (com.hurix.kitaboo.constants.utils.Utils.isMobile(context) && context.resources.getBoolean(R.bool.is_AAO)) {
                            tvProfileIcon.text = ShelfUIConstants.SHELF_MORE_OPTION_ICON
                        } else {
                            tvProfileIcon.text = ShelfUIConstants.SHELF_DEFAULT_IC_TEXT
                        }

                        featureText = context.resources.getString(R.string.profile)
                        featureDescription = context.resources.getString(R.string.more_setting_) + "\n" +
                                context.resources.getString(R.string.more_setting_1)

                        val tvProfileName = topContainer.findViewById<TextView>(R.id.tvProfileName)
                        val tvProfileDescription = topContainer.findViewById<TextView>(R.id.tvProfileDescription)
                        tvProfileName.text = featureText
                        tvProfileDescription.text = featureDescription
                    } else {

                        featureText = context.resources.getString(R.string.profile)
                        featureDescription = context.resources.getString(R.string.profile_)

                        topItemLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_END)


                        //line params
                        val viewParams: RelativeLayout.LayoutParams = RelativeLayout.LayoutParams(4,
                                RelativeLayout.LayoutParams.MATCH_PARENT)
                        viewParams.addRule(RelativeLayout.CENTER_HORIZONTAL)
                        viewParams.addRule(RelativeLayout.ABOVE, textViewDescription.id)
                        Log.d("TAG", "createActionBarFeatureText: profile :" + topActionbar!!.findViewById<ImageView>(R.id.action_profile_image).width +
                                ":" + (topActionbar!!.findViewById<ImageView>(R.id.action_profile_image).width / 2))

                        viewParams.setMargins(0,
                                0, (topActionbar!!.findViewById<ImageView>(R.id.action_profile_image).width / 2), 0)
                        lineView.layoutParams = viewParams

                        //Name and description params
                        val textParams = RelativeLayout.LayoutParams(200,
                                RelativeLayout.LayoutParams.WRAP_CONTENT)
                        textParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM)
                        textViewDescription.layoutParams = textParams
                        textViewDescription.gravity = Gravity.CENTER_HORIZONTAL
                    }

                }
                context.resources.getString(vo.helpText).equals(context.resources.getString(R.string.home), ignoreCase = true) -> {

                    val home = topActionbar!!.findViewById<TextView>(R.id.action_home)
                    home.visibility = View.INVISIBLE
                    Log.d("home", "createActionBarFeatureText: home" + home.width)
                    featureText = context.resources.getString(R.string.home)
                    featureDescription = context.resources.getString(R.string.home_)

                    topItemLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_START)

                    //line params
                    val viewParams: RelativeLayout.LayoutParams = RelativeLayout.LayoutParams(4,
                            RelativeLayout.LayoutParams.MATCH_PARENT)
                    viewParams.addRule(RelativeLayout.ABOVE, textViewDescription.id)
                    viewParams.setMargins(home.width / 2, 0, 0, 0)
                    lineView.layoutParams = viewParams

                    //Name and description params
                    val textParams = RelativeLayout.LayoutParams(200,
                            RelativeLayout.LayoutParams.WRAP_CONTENT)
                    textViewDescription.gravity = Gravity.CENTER_HORIZONTAL
                    textParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM)
                    textViewDescription.layoutParams = textParams
                }
                context.resources.getString(vo.helpText).equals(context.resources.getString(R.string.add_new), ignoreCase = true) -> {
                    if (isBookShelf) {

                        val topContainer = rlTopFeatureTextContainer.findViewById<RelativeLayout>(R.id.topContainer)
                        val tvAddNewIcon: TextView = topContainer.findViewById<TextView>(R.id.access_code)
                        tvAddNewIcon.visibility = View.INVISIBLE
                        tvAddNewIcon.typeface = Typefaces.get(context, context.resources.getString(R.string.kitaboo_bookshelf_font_file_name))
                        tvAddNewIcon.isAllCaps = false
                        tvAddNewIcon.text = ShelfUIConstants.SHELF_ACCESS_CODE_IC_ENABLE_TEXT

                        featureText = context.resources.getString(R.string.add_new)
                        featureDescription = context.resources.getString(R.string.add_new_content)

                        //Fetching description UI
                        val rlAccessCode = topContainer.findViewById<RelativeLayout>(R.id.rlAccessCode)
                        rlAccessCode.visibility = View.VISIBLE
                        val tvAccessCode = topContainer.findViewById<TextView>(R.id.tvAccessCode)
                        tvAccessCode.visibility = View.VISIBLE
                        val tvAccessCodeDescription = topContainer.findViewById<TextView>(R.id.tvAccessCodeDescription)
                        tvAccessCodeDescription.visibility = View.VISIBLE

                        tvAccessCode.text = featureText
                        tvAccessCodeDescription.text = featureDescription

                    }
                }
                context.resources.getString(vo.helpText).equals(context.resources.getString(R.string.search), ignoreCase = true) -> {
                    if (isBookShelf) {
                        val topContainer = rlTopFeatureTextContainer.findViewById<RelativeLayout>(R.id.topContainer)
                        val tvSearchIcon: TextView = topContainer.findViewById<TextView>(R.id.search_icon)
                        val tvSearchIconName: TextView = topContainer.findViewById<TextView>(R.id.tvSearchIcon)
                        val tvSearchIconDescription: TextView = topContainer.findViewById<TextView>(R.id.tvSearchIconDescription)

                        tvSearchIcon.visibility = View.INVISIBLE
                        tvSearchIcon.typeface = Typefaces.get(context, context.resources.getString(R.string.kitaboo_bookshelf_font_file_name))
                        tvSearchIcon.isAllCaps = false
                        tvSearchIcon.text = ShelfUIConstants.SHELF_ADD_SEARCH_IC_ENABLE_TEXT

                        featureText = context.resources.getString(R.string.search)
                        featureDescription = context.resources.getString(R.string.help_screen_search_text_new) + "\n" +
                                context.resources.getString(R.string.help_screen_search_text_new_)
                        tvSearchIconName.text = featureText
                        tvSearchIconDescription.text = featureDescription

                    }
                }
            }



            if (featureDescription.isNotEmpty() && !isBookShelf) { //checking for valid view
                //creating Name and description span
                val spannableFeatureText = SpannableString(featureText)
                spannableFeatureText.setSpan(RelativeSizeSpan(1f), 0, featureText.length, 0)
                spannableFeatureText.setSpan(ForegroundColorSpan(Color.CYAN), 0, featureText.length, 0)

                textViewDescription.text = TextUtils.concat(spannableFeatureText, "\n" + featureDescription)

                //Add views with params
                relativeLayoutTopItem.layoutParams = topItemLayoutParams
                relativeLayoutTopItem.addView(textViewDescription)
                relativeLayoutTopItem.addView(lineView)
                rlTopFeatureTextContainer.addView(relativeLayoutTopItem)
            }

        }
    }

    private fun getStatusbarHeight(context: Context): Int {
        var result: Int = 0
        val resourceId: Int = context.resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            result = context.resources.getDimensionPixelSize(resourceId)
        }
        return result
    }

    fun getSpannableDescription(featureText: String, featureDescription: String): String {
        //creating Name and description span
        val spannableFeatureText = SpannableString(featureText)
        spannableFeatureText.setSpan(RelativeSizeSpan(1f), 0, featureText.length, 0)
        spannableFeatureText.setSpan(ForegroundColorSpan(Color.CYAN), 0, featureText.length, 0)

        return TextUtils.concat(spannableFeatureText, "\n" + featureDescription).toString()
    }

    fun addExtraWeight(context: Context, llBottomFeatureTextContainer: LinearLayout) {
        if (bottomActionBarItems > bottomCount && !extraWeightAdded) {
            val extraWeight = bottomActionBarItems - bottomCount
            val dummyView = View(context)
            dummyView.layoutParams = LinearLayout.LayoutParams(0,
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    extraWeight.toFloat())

            llBottomFeatureTextContainer.addView(dummyView)
            extraWeightAdded = true
        }
    }

    fun addExtraPreviousWeight(context: Context, llBottomFeatureTextContainer: LinearLayout) {
        if (bottomActionBarItems >= bottomCount && !extraPreviousWeightAdded) {
            val extraWeight = bottomActionBarItems / 2
            val dummyView = View(context)
            dummyView.layoutParams = LinearLayout.LayoutParams(0,
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    extraWeight.toFloat())

            llBottomFeatureTextContainer.addView(dummyView)
            extraPreviousWeightAdded = true
        }
    }

    fun clearActionBarSpecs() {
        this.marginTop = 0
        this.bottomCount = 0
        this.extraWeightAdded = false
        this.bottomActionBarItems = 0
        this.extraPreviousWeightAdded = false
        this.bottomContainerHeight = 0
        this.pageOneCount = 2
        this.pageTwoCount = 2
        this.bottomContainerHeightNext = 0
        this.bottomContainerHeight = 0
    }


}