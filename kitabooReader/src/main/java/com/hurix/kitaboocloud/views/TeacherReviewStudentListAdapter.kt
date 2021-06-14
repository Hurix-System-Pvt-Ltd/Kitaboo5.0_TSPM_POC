package com.hurix.kitaboocloud.views

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.hurix.commons.sdkDatamodel.SDKManager
import com.hurix.customui.interfaces.IUser
import com.hurix.kitaboo.iconify.Typefaces
import com.hurix.kitaboocloud.PlayerController
import com.hurix.kitaboocloud.R

import com.hurix.kitaboocloud.kitaboosdkrenderer.customviews.CustomPlayerUIConstants
import com.hurix.kitaboocloud.notifier.GlobalDataManager
import com.hurix.kitaboocloud.sdkreadertheme.InsightReaderThemeController
import com.hurix.kitaboocloud.sdkreadertheme.themePojo.ReaderThemeSettingVo
import java.util.*

class TeacherReviewStudentListAdapter(private val context: Context, private val mStudentList: ArrayList<IUser>, clickListener: ClickListener) : RecyclerView.Adapter<TeacherReviewStudentListAdapter.StudentListHolder>() {
    private val readerThemeSettingVo: ReaderThemeSettingVo
    private val mClickListener: ClickListener
    private var isSelectedAll = false
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentListHolder {
        val itemView: View
        itemView = if (viewType == 1) {
            LayoutInflater.from(parent.context).inflate(R.layout.select_student_layout_item_first, parent, false)
        } else {
            LayoutInflater.from(parent.context).inflate(R.layout.select_student_layout_item, parent, false)
        }
        return StudentListHolder(itemView)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: StudentListHolder, position: Int) {
        if (isSelectedAll) {
            holder.checkBox.isChecked = true
        } else {
            holder.checkBox.isChecked = false
        }
        if (position == 0) {
            holder.name.text = "All Students" + " " + "(" + mStudentList.size + ")"
        } else {
            val studentObj = mStudentList[position - 1]
            holder.name.text = studentObj.firstName + " " + studentObj.lastName
            val ugcDataArrayList = GlobalDataManager.getInstance().getUgcListPerUserIdMap("" + SDKManager.getInstance().classObj.id + studentObj.userID)
            if (ugcDataArrayList != null) {
                holder.tvUgcStatus.visibility = View.VISIBLE
                if (ugcDataArrayList.size > 0) {
                    if (ugcDataArrayList[0].pageID == -1) {
                        holder.tvUgcStatus.text = CustomPlayerUIConstants.TEACHER_ACTIONBAR_NOT_SELECTED
                        holder.tvUgcStatus.setTextColor(Color.parseColor("#d9d9d9"))
                    } else {
                        holder.tvUgcStatus.text = CustomPlayerUIConstants.PT_COLOR_IC_TEXT
                        holder.tvUgcStatus.setTextColor(Color.parseColor(readerThemeSettingVo.reader.dayMode.teacherStudentList.dataAddedColor))
                    }
                } else {
                    holder.tvUgcStatus.text = CustomPlayerUIConstants.PT_COLOR_IC_TEXT
                    holder.tvUgcStatus.setTextColor(Color.parseColor(readerThemeSettingVo.reader.dayMode.teacherStudentList.noDataAddedColor))
                }
            } else {
                holder.tvUgcStatus.visibility = View.GONE
            }
        }
    }

    override fun getItemCount(): Int {
        return mStudentList.size + 1
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) 1 else 2
    }

    inner class StudentListHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        var name: TextView
        var tvUgcStatus: TextView
        var typeFace: Typeface
        var checkBox: CheckBox

        override fun onClick(v: View) {
            val id = v.id
            if (id == R.id.check_box) {
                if (adapterPosition == 0) {
                    isSelectedAll = if (checkBox.isChecked) {
                        mClickListener.onAllItemClick(adapterPosition - 1, mStudentList, true)
                        true
                    } else {
                        mClickListener.onAllItemClick(adapterPosition - 1, mStudentList, false)
                        false
                    }
                    notifyDataSetChanged()
                } else {
                    if (checkBox.isChecked) {
                        //checkBox.setChecked(true);
                        mClickListener.onItemClick(adapterPosition - 1, mStudentList[adapterPosition - 1], true)
                    } else {
                        // checkBox.setChecked(false);
                        mClickListener.onItemClick(adapterPosition - 1, mStudentList[adapterPosition - 1], false)
                    }
                }
            }
        }

        init {
            itemView.setOnClickListener(this)
            name = itemView.findViewById<View>(R.id.name) as TextView
            checkBox = itemView.findViewById<View>(R.id.check_box) as CheckBox
            tvUgcStatus = itemView.findViewById<View>(R.id.tv_ugc_status) as TextView
            typeFace = Typefaces.get(context, context.resources.getString(R.string.kitaboo_bookshelf_font_file_name))
            name.setTextColor(Color.parseColor(readerThemeSettingVo.reader.dayMode.teacherStudentList.nameColor))
            tvUgcStatus.typeface = typeFace
            tvUgcStatus.isAllCaps = false
            tvUgcStatus.text = CustomPlayerUIConstants.PT_COLOR_IC_TEXT
            tvUgcStatus.setTextColor(Color.parseColor("#ececec"))
            checkBox.setOnClickListener(this)

            name.setTextSize(TypedValue.COMPLEX_UNIT_SP, PlayerController.getInstance(context).defaultPanelChapterFontSize.toInt().toFloat())
        }
    }

    interface ClickListener {
        fun onItemClick(position: Int, mStudentList: IUser, addOrRemove: Boolean)
        fun onAllItemClick(position: Int, mStudentList: ArrayList<IUser>, addOrRemove: Boolean)
    }

    init {
        readerThemeSettingVo = InsightReaderThemeController.getInstance(context.applicationContext).readerThemeUserSettingVo
        mClickListener = clickListener
    }
}