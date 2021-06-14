package com.hurix.kitaboocloud.views

import android.animation.LayoutTransition
import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.hurix.commons.sdkDatamodel.SDKManager
import com.hurix.customui.toc.TableOfContentVO
import com.hurix.database.datamodels.UserSettingVO
import com.hurix.kitaboo.constants.PlayerUIConstants
import com.hurix.kitaboo.iconify.Typefaces
import com.hurix.kitaboocloud.PlayerController
import com.hurix.kitaboocloud.R



import com.hurix.kitaboocloud.notifier.GlobalDataManager

class SelectChapterFragment(var mContext: Context, private val mClickListener: ChapterListClickListener, private var viewPager: ViewPager) : Fragment(), View.OnClickListener {
    private val kitabooTypeFace: Typeface
    private var mTOCListLayout: LinearLayout? = null
    private var _next: TextView? = null
    private var _cancel: TextView? = null
    private var mSelectedUserIDs: ArrayList<Long>? = ArrayList()
    private var mSelectedChapterTitles: ArrayList<String>? = ArrayList()

    //Overriden method onCreateView
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View
        view = inflater.inflate(R.layout.select_chapter_layout, container, false)
        mTOCListLayout = view.findViewById<View>(R.id.expandListContainer) as LinearLayout
        _next = view.findViewById<View>(R.id.next) as TextView
        _cancel = view.findViewById<View>(R.id.cancel) as TextView
        _next!!.setOnClickListener(this)
        _cancel!!.setOnClickListener(this)
        _next!!.setTextColor(resources.getColor(R.color.blue_dark_disabled))
        _next!!.isClickable = false

        /*val groupParent = ExpandingViewGroup(context, 0)
        groupParent.setSelectAll("Select All")
        mTOCListLayout!!.addView(groupParent)*/
        if (SDKManager.getInstance().tocdata.size != 0) {
            for (i in SDKManager.getInstance().tocdata.indices) {
                val groupParent = ExpandingViewGroup(context, 0)
                if (i == 0) {
                    groupParent.setData(SDKManager.getInstance().tocdata[i], i, true, false)
                    mTOCListLayout!!.addView(groupParent)
                }
                groupParent.setData(SDKManager.getInstance().tocdata[i], i + 1, false, false)
                //mTOCListLayout!!.removeAllViews()
                try {
                    mTOCListLayout!!.addView(groupParent)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
        return view
    }

    inner class ExpandingViewGroup(context: Context?, private val _depth: Int) : LinearLayout(context), View.OnClickListener {
        private val _isExpanded = false
        private val _canExpand = false
        private var _data: TableOfContentVO? = null
        private val _container: LinearLayout
        private val _childContainer: LinearLayout
        private val mTransitioner = LayoutTransition()
        fun setData(data: TableOfContentVO?, index: Int, isSelectAll: Boolean, selectAllCheckBoxes: Boolean) {
            if (index != 0)
                _data = data
            val view = getView(data, index, null, isSelectAll, selectAllCheckBoxes) as LinearLayout?
            val holder = view!!.tag as TOCElementViewHolder
            holder.frameNext!!.setOnClickListener(this)
            val mainTocRootLayout = view.findViewById<View>(R.id.mainTocRootLayout)
            mainTocRootLayout.setPadding(_depth * 50, 0, 0, 0)
            val params = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
            params.bottomMargin = 1
            view.layoutParams = params
            _container.addView(view)
            if (holder.checkBox != null) {
                holder.checkBox!!.setOnClickListener {
                    onChapterItemClick(_data!!, holder.checkBox!!.isChecked)
                    mClickListener.onChapterItemClick(_data!!, holder.checkBox!!.isChecked)

                    _childContainer.removeAllViews()
                    for (i in _data!!.children.indices) {
                        val groupParent = ExpandingViewGroup(context, _depth + 1)
                        groupParent.setData(_data!!.children[i], i + 1, false, holder.checkBox!!.isChecked)
                        try {
                            _childContainer.addView(groupParent)
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                }
            }
            if (holder.AllcheckBox != null) {
                holder.AllcheckBox!!.setOnClickListener {
                    onAllChapterItemClick(SDKManager.getInstance().tocdata, holder.AllcheckBox!!.isChecked)
                    mClickListener.onAllChapterItemClick(SDKManager.getInstance().tocdata, holder.AllcheckBox!!.isChecked)
                    if (SDKManager.getInstance().tocdata.size != 0) {
                        mTOCListLayout!!.removeAllViews()
                        for (i in SDKManager.getInstance().tocdata.indices) {
                            val groupParent = ExpandingViewGroup(context, 0)
                            if (i == 0) {
                                groupParent.setData(SDKManager.getInstance().tocdata[i], i, true, holder.AllcheckBox!!.isChecked)
                                try {
                                    mTOCListLayout!!.addView(groupParent)
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }
                            }
                            groupParent.setData(SDKManager.getInstance().tocdata[i], i + 1, false, holder.AllcheckBox!!.isChecked)
                            try {
                                mTOCListLayout!!.addView(groupParent)
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }
                    }
                }
            }

        }

        override fun onClick(v: View) {
            if (v.id == this.id) {
                var pageID = _data!!.pageid
                if (pageID < 1) {
                    if (_data!!.children.size > 0) {
                        pageID = _data!!.children[0].pageid
                    }
                }
            } else if (v.id == R.id.wrapParent) {
                if (_data!!.children.size > 0) {
                    val holder = returnHolderForView(_container)
                    if (_childContainer.childCount == 0) {
                        holder.textViewTocNext!!.text = PlayerUIConstants.UP_ARROW_IC_TEXT
                        for (i in _data!!.children.indices) {
                            val groupParent = ExpandingViewGroup(context, _depth + 1)
                            groupParent.setData(_data!!.children[i], i + 1, false, holder.checkBox!!.isChecked)
                            _childContainer.addView(groupParent)
                        }
                    } else {
                        _childContainer.removeAllViews()
                        holder.textViewTocNext!!.text = PlayerUIConstants.DOWN_ARROW_IC_TEXT
                    }
                }
            }
        }

        init {
            this.orientation = VERTICAL
            setOnClickListener(this)
            _container = LinearLayout(getContext())
            _container.orientation = VERTICAL
            _childContainer = LinearLayout(getContext())
            _childContainer.orientation = VERTICAL
            this.addView(_container)
            this.addView(_childContainer)
            _childContainer.layoutTransition = mTransitioner
            // setupCustomAnimations();
        }
    }

    internal inner class TOCElementViewHolder {
        var txtViewTitle: TextView? = null
        var frameNext: RelativeLayout? = null
        var textViewTocNext: TextView? = null
        var checkBox: CheckBox? = null
        var AllcheckBox: CheckBox? = null
    }

    fun getView(currTocVO: TableOfContentVO?, position: Int, convertView: View?, isSelectAll: Boolean, selectAllCheckBoxes: Boolean): View? {
        var convertView = convertView
        var holder: TOCElementViewHolder? = null
        if (currTocVO != null) {
            if (convertView != null && convertView.tag == null) {
                convertView = null
            }
            if (position == 0) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.teacher_revire_toc_list_item, null, false)
                holder = returnHolderForView(convertView)
                convertView.tag = holder
            } else {
                if (convertView == null) {
                    convertView = LayoutInflater.from(mContext).inflate(R.layout.teacher_revire_toc_list_first_item, null, false)
                    holder = returnHolderForView(convertView)
                    convertView.tag = holder
                } else {
                    holder = convertView.tag as TOCElementViewHolder
                }
            }
            if (currTocVO.parentId == 0) {
                val displayNum: String? = null
                if (currTocVO.children.size > 0) {
                    holder!!.txtViewTitle!!.setTextColor(Color.parseColor(PlayerController.getInstance(mContext)
                            .sideMenuHeaderTitleColor))
                }
            } else {
                holder!!.txtViewTitle!!.setTextColor(Color.parseColor(PlayerController.getInstance(mContext)
                        .defaultChapterNameColor))
            }
            holder!!.txtViewTitle!!.text = currTocVO.title.trim { it <= ' ' }
            convertView!!.setBackgroundColor(Color.WHITE)
        } else {
            convertView = TextView(mContext)
            convertView.setTag(null)
        }
        if (currTocVO!!.children.size == 0) {
            holder!!.frameNext!!.visibility = View.INVISIBLE
        } else {
            holder!!.frameNext!!.visibility = View.VISIBLE
        }
        if (isSelectAll) {
            holder!!.frameNext!!.visibility = View.INVISIBLE
            holder!!.txtViewTitle!!.text = "Select All"
        }
        if (selectAllCheckBoxes) {
            if (holder.checkBox != null) {
                holder.checkBox!!.isChecked = selectAllCheckBoxes
            }
            if (holder.AllcheckBox != null) {
                holder.AllcheckBox!!.isChecked = selectAllCheckBoxes
            }
        }
        return convertView
    }

    private fun returnHolderForView(convertView: View?): TOCElementViewHolder {
        val holder: TOCElementViewHolder
        holder = TOCElementViewHolder()
        holder.txtViewTitle = convertView!!.findViewById<View>(R.id.txtViewTitle) as TextView
        holder.textViewTocNext = convertView.findViewById<View>(R.id.txtnexttoc) as TextView
        holder.frameNext = convertView.findViewById<View>(R.id.wrapParent) as RelativeLayout
        if (convertView.findViewById<View>(R.id.check_box) != null)
            holder.checkBox = convertView.findViewById<View>(R.id.check_box) as CheckBox
        if (convertView.findViewById<View>(R.id.check_box_all) != null)
            holder.AllcheckBox = convertView.findViewById<View>(R.id.check_box_all) as CheckBox
        holder.txtViewTitle!!.setTextColor(Color.parseColor(PlayerController.getInstance(mContext)
                .defaultChapterNameColor))
        holder.txtViewTitle!!.setTextSize(TypedValue.COMPLEX_UNIT_SP, PlayerController.getInstance(context).defaultPanelChapterFontSize.toInt().toFloat())
        if (!PlayerController.getInstance(context).loginHeaderFontFace.isEmpty()) {
            holder.txtViewTitle!!.typeface = kitabooTypeFace
        }
        holder.textViewTocNext!!.typeface = kitabooTypeFace
        holder.textViewTocNext!!.isAllCaps = false
        holder.textViewTocNext!!.text = PlayerUIConstants.DOWN_ARROW_IC_TEXT
        holder.textViewTocNext!!.setTextColor(Color.parseColor(PlayerController.getInstance(mContext)
                .defaultChapterNameColor))
        return holder
    }

    interface ChapterListClickListener {
        fun onChapterItemClick(_data: TableOfContentVO, addOrRemove: Boolean)
        fun onAllChapterItemClick(tocdata: ArrayList<TableOfContentVO>?, checked: Boolean)
        fun finishActivity()
    }

    init {

        kitabooTypeFace = Typefaces.get(mContext, mContext.resources
                .getString(R.string.kitaboo_font_file_name))
    }

    override fun onClick(v: View?) {
        if (v === _next) {
            viewPager.setCurrentItem(1, true)
           GlobalDataManager.mInstance.setSelectedChapterTitles(mSelectedChapterTitles)
        } else if (v === _cancel) {
            mClickListener.finishActivity();
        }
    }

    fun onChapterItemClick(_data: TableOfContentVO, addOrRemove: Boolean) {
        if (addOrRemove) {
            mSelectedChapterTitles!!.add(_data.tocPosition)
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
        if (mSelectedChapterTitles!!.size > 0) {
            _next!!.setTextColor(resources.getColor(R.color.blue_dark))
            _next!!.isClickable = true
        } else {
            _next!!.setTextColor(resources.getColor(R.color.blue_dark_disabled))
            _next!!.isClickable = false
        }
    }

    fun onAllChapterItemClick(tocdata: ArrayList<TableOfContentVO>?, addOrRemove: Boolean) {

        if (addOrRemove) {
            mSelectedChapterTitles = ArrayList()
            for (i in tocdata!!.indices) {
                mSelectedChapterTitles!!.add(tocdata[i].tocPosition)
            }
        } else {
            mSelectedChapterTitles = ArrayList()
        }
        if (mSelectedChapterTitles!!.size > 0) {
             _next!!.setTextColor(resources.getColor(R.color.blue_dark))
             _next!!.isClickable = true
        } else {
             _next!!.setTextColor(resources.getColor(R.color.blue_dark_disabled))
             _next!!.isClickable = false
        }
    }
}