package com.hurix.kitaboocloud.views

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.content.res.Configuration
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.hurix.customui.interfaces.IUser
import com.hurix.customui.toc.TableOfContentVO
import com.hurix.kitaboocloud.R
import com.hurix.kitaboocloud.notifier.GlobalDataManager
import com.hurix.kitaboocloud.views.SelectChapterFragment.ChapterListClickListener


@SuppressLint("ValidFragment")
class TeacherReviewGenerateReport : FragmentActivity(), TabLayout.OnTabSelectedListener,
        SelectStudentFragment.ClickListener, ChapterListClickListener, View.OnClickListener {
    private var tabLayout: TabLayout? = null
    private var viewPager: CustomViewPager? = null
    /*private var _next: TextView? = null
    private var _cancel: TextView? = null*/
    private var mSelectedUserIDs: ArrayList<Long>? = ArrayList()
    private var mSelectedChapterTitles: ArrayList<String>? = ArrayList()
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.generate_report_layout)
        tabLayout = findViewById<View>(R.id.tabLayout) as TabLayout
        viewPager = findViewById<View>(R.id.pager) as CustomViewPager
       /* _next = findViewById<View>(R.id.next) as TextView
        _cancel = findViewById<View>(R.id.cancel) as TextView
        _next!!.setOnClickListener(this)
        _cancel!!.setOnClickListener(this)
        _next!!.setTextColor(resources.getColor(R.color.blue_dark_disabled))
        _next!!.isClickable = false*/
        val adapter = TeacherReviewViewPageAdapter(supportFragmentManager, 1, this)
        adapter.addFragment(SelectChapterFragment(this, this, viewPager!!), "Select Chapter")
        adapter.addFragment(SelectStudentFragment(this, this), "Select Student")
        //tabLayout!!.getTabAt(0).text.setTabTextColors(ColorStateList.valueOf(getResources().getColor(R.color.black)));
        viewPager!!.adapter = adapter
        viewPager!!.offscreenPageLimit = 1
        tabLayout!!.setupWithViewPager(viewPager)
        tabLayout!!.setOnTabSelectedListener(this)
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        val height = displayMetrics.heightPixels
        val width = displayMetrics.widthPixels
        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            window.setLayout(height, width / 2)
        } else
            window.setLayout(height / 2, width)
        tabLayout!!.getTabAt(0)!!.setIcon(R.drawable.grey);
        tabLayout!!.getTabAt(1)!!.setIcon(R.drawable.grey_2);
        tabLayout!!.tabTextColors = ColorStateList.valueOf(resources.getColor(R.color.grey))
        defaultSelectedTab(0)
        viewPager!!.setPagingEnabled(false)
    }

    private fun defaultSelectedTab(tabPosition: Int) {
        val tab = tabLayout!!.getTabAt(tabPosition)
        val tabLayout1 = (tabLayout!!.getChildAt(0) as ViewGroup).getChildAt(tab!!.position) as LinearLayout
        val tabTextView = tabLayout1.getChildAt(1) as TextView
        tabTextView.setTextColor(resources.getColor(R.color.black))
    }

    override fun onTabSelected(selectedTab: TabLayout.Tab?) {
        viewPager!!.currentItem = selectedTab!!.position
        val tabLayout1 = (tabLayout!!.getChildAt(0) as ViewGroup).getChildAt(selectedTab.getPosition()) as LinearLayout
        val tabTextView = tabLayout1.getChildAt(1) as TextView
        tabTextView.setTextColor(resources.getColor(R.color.black))
        if (selectedTab.position == 0) {
            if (mSelectedChapterTitles!!.size > 0) {
                tabLayout!!.getTabAt(selectedTab.position)!!.setIcon(R.drawable.blue)
                defaultSelectedTab(0)
            }
        } else {
            GlobalDataManager.mInstance.setSelectedChapterTitles(mSelectedChapterTitles)
            if (mSelectedUserIDs!!.size > 0) {
                tabLayout!!.getTabAt(selectedTab.position)!!.setIcon(R.drawable.blue_2)
                defaultSelectedTab(1)
            }
        }
    }

    override fun onTabUnselected(selectedTab: TabLayout.Tab?) {
        val tabLayout1 = (tabLayout!!.getChildAt(0) as ViewGroup).getChildAt(selectedTab!!.getPosition()) as LinearLayout
        val tabTextView = tabLayout1.getChildAt(1) as TextView
        tabTextView.setTextColor(resources.getColor(R.color.grey))
        //tabLayout!!.getTabAt(tab!!.position)!!.setColorFilter(resources.getColor(R.color.grey), PorterDuff.Mode.SRC_IN)
        if (selectedTab.position == 0) {
            if (mSelectedChapterTitles!!.size > 0) {
                tabLayout!!.getTabAt(selectedTab.position)!!.setIcon(R.drawable.green)
            }
        } else {
            if (mSelectedUserIDs!!.size > 0) {
                tabLayout!!.getTabAt(selectedTab.position)!!.setIcon(R.drawable.green_2)
            }
        }
    }

    override fun onTabReselected(tab: TabLayout.Tab?) {}
    override fun onItemClick(position: Int, mStudentList: IUser, addOrRemove: Boolean) {
        if (addOrRemove) {
            mSelectedUserIDs!!.add(mStudentList.userID)
        } else {
            if (mSelectedUserIDs != null && !mSelectedUserIDs!!.isEmpty()) {
                for (i in mSelectedUserIDs!!.indices) {
                    if (mSelectedUserIDs!![i] == mStudentList.userID) {
                        mSelectedUserIDs!!.remove(mStudentList.userID)
                        break
                    }
                }
            }
        }
        if (mSelectedUserIDs!!.size > 0 && mSelectedChapterTitles!!.size > 0) {
            /*_next!!.setTextColor(resources.getColor(R.color.blue_dark))
            _next!!.isClickable = true*/
        } else {
            /*_next!!.setTextColor(resources.getColor(R.color.blue_dark_disabled))
            _next!!.isClickable = false*/
        }
        if (mSelectedUserIDs!!.size > 0) {
            tabLayout!!.getTabAt(1)!!.setIcon(R.drawable.blue_2);
        } else {
            tabLayout!!.getTabAt(1)!!.setIcon(R.drawable.grey_2);
        }
        defaultSelectedTab(1)
    }

    override fun onAllItemClick(position: Int, mStudentList: ArrayList<IUser>, addOrRemove: Boolean) {
        if (addOrRemove) {
            mSelectedUserIDs = ArrayList()
            for (i in mStudentList.indices) {
                mSelectedUserIDs!!.add(mStudentList[i].userID)
            }
        } else {
            mSelectedUserIDs = ArrayList()
        }

        if (mSelectedUserIDs!!.size > 0) {
            tabLayout!!.getTabAt(1)!!.setIcon(R.drawable.blue_2);
        } else {
            tabLayout!!.getTabAt(1)!!.setIcon(R.drawable.grey_2);
        }
        defaultSelectedTab(1)

        mSelectedUserIDs = ArrayList()
    }

    override fun onChapterItemClick(_data: TableOfContentVO, addOrRemove: Boolean) {
        if (addOrRemove) {
            mSelectedChapterTitles!!.add(_data.title)
        } else {
            if (mSelectedChapterTitles != null && !mSelectedChapterTitles!!.isEmpty()) {
                for (i in mSelectedChapterTitles!!.indices) {
                    if (mSelectedChapterTitles!![i].equals(_data.tocPosition)) {
                        mSelectedChapterTitles!!.remove(_data.tocPosition)
                        break
                    }
                }
            }
        }
       /* if (mSelectedChapterTitles!!.size > 0 && mSelectedUserIDs!!.size > 0) {
            _next!!.setTextColor(resources.getColor(R.color.blue_dark))
            _next!!.isClickable = true
        } else {
            _next!!.setTextColor(resources.getColor(R.color.blue_dark_disabled))
            _next!!.isClickable = false
        }*/
        if (mSelectedChapterTitles!!.size > 0) {
            tabLayout!!.getTabAt(0)!!.setIcon(R.drawable.blue);
        } else {
            tabLayout!!.getTabAt(0)!!.setIcon(R.drawable.grey);
        }
        defaultSelectedTab(0)
    }

    override fun onAllChapterItemClick(tocdata: ArrayList<TableOfContentVO>?, addOrRemove: Boolean) {

        if (addOrRemove) {
            mSelectedChapterTitles = ArrayList()
            for (i in tocdata!!.indices) {
                mSelectedChapterTitles!!.add(tocdata[i].tocPosition)
            }
        } else {
            mSelectedChapterTitles = ArrayList()
        }
        if (mSelectedChapterTitles!!.size > 0 && mSelectedUserIDs!!.size > 0) {
           /* _next!!.setTextColor(resources.getColor(R.color.blue_dark))
            _next!!.isClickable = true*/
        } else {
           /* _next!!.setTextColor(resources.getColor(R.color.blue_dark_disabled))
            _next!!.isClickable = false*/
        }
        if (mSelectedChapterTitles!!.size > 0) {
            tabLayout!!.getTabAt(0)!!.setIcon(R.drawable.blue);
        } else {
            tabLayout!!.getTabAt(0)!!.setIcon(R.drawable.grey);
        }
        defaultSelectedTab(0)
    }

    override fun finishActivity() {
        finish()
    }

    override fun onClick(v: View) {
       /* if (v === _next) {
            val dialog = GenerateReportDialog(this, mSelectedChapterTitles!!, mSelectedUserIDs!!)
            if (!dialog.isShowing) dialog.show()
        } else if (v === _cancel) {
            finish()
        }*/
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        val height = displayMetrics.heightPixels
        val width = displayMetrics.widthPixels
        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            window.setLayout(height, width / 2)
        } else
            window.setLayout(height / 2, width)
    }
}