package com.hurix.kitaboocloud.views

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hurix.commons.sdkDatamodel.SDKManager
import com.hurix.customui.interfaces.IUser
import com.hurix.kitaboocloud.R
import com.hurix.kitaboocloud.notifier.GlobalDataManager
import java.util.*

class SelectStudentFragment(private val mContext: Context, private val mClickListener: ClickListener) : Fragment(),
        TeacherReviewStudentListAdapter.ClickListener, View.OnClickListener, GenerateReportDialog.ClickListener {
    private var rvStudentListView: RecyclerView? = null
    private var mStudentListAdapter: TeacherReviewStudentListAdapter? = null
    private var _next: TextView? = null
    private var _cancel: TextView? = null
    private var mSelectedUserIDs: ArrayList<Long>? = ArrayList()
    private var mSelectedChapterTitles: ArrayList<String>? = ArrayList()

    //Overriden method onCreateView
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        //Returning the layout file after inflating
        val view = inflater.inflate(R.layout.select_student_layout, container, false)
        rvStudentListView = view.findViewById<View>(R.id.rv_student_list) as RecyclerView
        _next = view.findViewById<View>(R.id.next) as TextView
        _cancel = view.findViewById<View>(R.id.cancel) as TextView
        _next!!.setOnClickListener(this)
        _cancel!!.setOnClickListener(this)
        _next!!.setTextColor(resources.getColor(R.color.blue_dark_disabled))
        _next!!.isClickable = false
        if (SDKManager.getInstance().selectedClassStudentList != null) {
            mStudentListAdapter = TeacherReviewStudentListAdapter(mContext, SDKManager.getInstance().selectedClassStudentList, this)
            rvStudentListView!!.adapter = mStudentListAdapter
        }
        rvStudentListView!!.layoutManager = LinearLayoutManager(context)
        return view
    }

    init {
        //mSelectedClassStudentList = SDKManager.getInstance().selectedClassStudentList
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onItemClick(position: Int, mStudentList: IUser, addOrRemove: Boolean) {
        mClickListener.onItemClick(position, mStudentList, addOrRemove)
        onStudentClick(position, mStudentList, addOrRemove)
    }

    private fun onStudentClick(position: Int, mStudentList: IUser, addOrRemove: Boolean) {
        mSelectedChapterTitles = GlobalDataManager.mInstance.getSelectedChapterTitles()
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
            _next!!.setTextColor(resources.getColor(R.color.blue_dark))
            _next!!.isClickable = true
        } else {
            _next!!.setTextColor(resources.getColor(R.color.blue_dark_disabled))
            _next!!.isClickable = false
        }
    }

    override fun onAllItemClick(position: Int, mStudentList: ArrayList<IUser>, addOrRemove: Boolean) {
        mClickListener.onAllItemClick(position, mStudentList, addOrRemove)
        onAllStudentClick(position, mStudentList, addOrRemove)
    }

    private fun onAllStudentClick(position: Int, mStudentList: ArrayList<IUser>, addOrRemove: Boolean) {
        mSelectedChapterTitles = GlobalDataManager.mInstance.getSelectedChapterTitles()
        if (addOrRemove) {
            mSelectedUserIDs = ArrayList()
            for (i in mStudentList.indices) {
                mSelectedUserIDs!!.add(mStudentList[i].userID)
            }
        } else {
            mSelectedUserIDs = ArrayList()
        }
        if (mSelectedUserIDs!!.size > 0 && mSelectedChapterTitles!!.size > 0) {
             _next!!.setTextColor(resources.getColor(R.color.blue_dark))
             _next!!.isClickable = true
        } else {
             _next!!.setTextColor(resources.getColor(R.color.blue_dark_disabled))
             _next!!.isClickable = false
        }
        mSelectedUserIDs = ArrayList()
    }

    interface ClickListener {
        fun onItemClick(position: Int, mStudentList: IUser, addOrRemove: Boolean)
        fun onAllItemClick(position: Int, mStudentList: ArrayList<IUser>, addOrRemove: Boolean)
        fun finishActivity()
    }

    override fun onClick(v: View?) {
        if (v === _next) {
            val dialog = GenerateReportDialog(mContext, mSelectedChapterTitles!!, mSelectedUserIDs!!, this)
            if (!dialog.isShowing) dialog.show()
        } else if (v === _cancel) {
            mClickListener.finishActivity();
        }
    }

    override fun finishActivity() {
        mClickListener.finishActivity()
    }
}