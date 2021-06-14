package com.hurix.kitaboocloud.views


import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.ContextWrapper
import android.text.Html
import android.view.View
import android.widget.TextView
import com.hurix.commons.KitabooSDKModel
import com.hurix.commons.sdkDatamodel.SDKManager
import com.hurix.commons.utils.ClearableEditText

import com.hurix.kitaboo.constants.dialog.DialogUtils
import com.hurix.kitaboocloud.PlayerController
import com.hurix.kitaboocloud.R
import com.hurix.kitaboocloud.kitaboosdkrenderer.ServiceHandler
import com.hurix.service.Interface.IServiceResponse
import com.hurix.service.Interface.IServiceResponseListener
import com.hurix.service.exception.ServiceException
import java.util.*


class GenerateReportDialog(context: Context, private val mSelectedChapterTitles: ArrayList<String>,
                           private val mSelectedUserIDs: ArrayList<Long>, private val mClickListener: ClickListener)
    : Dialog(context), View.OnClickListener, IServiceResponseListener {
    private val mDone: TextView
    private val content: TextView
    private val reportConformation: TextView
    private val mEmailEditText: ClearableEditText
    private val USER_NAME_CHARACTER_UPER_LIMIT = 255
    private var mServicehandler: ServiceHandler? = null

    init {
        setContentView(R.layout.generate_report_dialog)
        setCanceledOnTouchOutside(false)
        mDone = findViewById(R.id.done)
        content = findViewById(R.id.generate_report_content)
        reportConformation = findViewById(R.id.generate_report_receive_report)
        val userEmail = PlayerController.getInstance(context).geteMail()
        reportConformation.setText(context.resources.getString(R.string.generate_report_receive_report) + " " + userEmail)
        val firstPart = getColoredSpanned(context.resources.getString(R.string.generate_report_content), "#000000")
        val secondPart = getColoredSpanned(userEmail, "#6600719b")
        content.setText(Html.fromHtml(firstPart + " " + secondPart))
        mEmailEditText = findViewById(R.id.useridEditText)
        mDone.setOnClickListener(this)
        mServicehandler = ServiceHandler(context, KitabooSDKModel.getInstance().clientID,
                context.getResources().getString(R.string.server_pointing))
    }

    private fun getColoredSpanned(text: String, color: String): String? {
        // return "<font color=$color>$text</font>"
        return "<font color=" + color + ">" + text + "</font>"
    }

    override fun onClick(v: View?) {
        if (v == mDone) {
            if (mEmailEditText.text.toString().isNotEmpty()) {
                if (isDataValid(mEmailEditText.text.toString().trim())) {
                    mServicehandler!!.fetchStudentMarkupReport(KitabooSDKModel.getInstance().getUserToken(), mSelectedChapterTitles, mSelectedUserIDs, SDKManager.getInstance().classObj.id,
                            SDKManager.getInstance().getGetLocalBookData().getBookID(), mEmailEditText.text.toString().trim(), this)

                }
            } else {
                mServicehandler!!.fetchStudentMarkupReport(KitabooSDKModel.getInstance().getUserToken(), mSelectedChapterTitles, mSelectedUserIDs, SDKManager.getInstance().classObj.id,
                        SDKManager.getInstance().getGetLocalBookData().getBookID(), mEmailEditText.text.toString().trim(), this)
            }
            //dismiss();
        }
    }

    override fun requestErrorOccured(exeption: ServiceException?) {
        if (exeption != null && exeption.message.equals("Bucket name must be provided")) {
            DialogUtils.showOKAlert(View(scanForActivity(context)), 1, scanForActivity(context),
                    "Alert!", "We do not support this email ID to send a report") {
                dismiss()
                mClickListener.finishActivity()
            }
        } else {
            DialogUtils.showOKAlert(View(scanForActivity(context)), 1, scanForActivity(context), "", context.resources
                    .getString(R.string.generate_report_service_failure_content)) {
                dismiss()
                mClickListener.finishActivity()
            }
        }
    }

    override fun requestCompleted(response: IServiceResponse?) {
        DialogUtils.showOKAlert(View(scanForActivity(context)), 1, scanForActivity(context), context.resources
                .getString(R.string.generate_report_service_success_header), context.resources
                .getString(R.string.generate_report_service_success_content)) {
            dismiss()
            mClickListener.finishActivity()
        }
    }

    private fun scanForActivity(cont: Context?): Activity? {
        if (cont == null) return null
        else if (cont is Activity) return cont
        else if (cont is ContextWrapper) return scanForActivity(cont.baseContext)
        return null
    }

    private fun isDataValid(userName: String): Boolean {
        var isDataValid = true
        /*if (userName.length == 0) {
            mEmailEditText.setError(context.getResources().getString(R.string.alert_empty_mail_id))
            mEmailEditText.requestFocus()
            isDataValid = false
        } else*/ if (userName.length > USER_NAME_CHARACTER_UPER_LIMIT) {
            mEmailEditText.setError(kotlin.String.format(context.getResources().getString(R.string
                    .alert_emailid_character_limit), "" + USER_NAME_CHARACTER_UPER_LIMIT))
            mEmailEditText.requestFocus()
            isDataValid = false
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(userName).matches()) {
            mEmailEditText.setError(context.getResources().getString(R.string.generate_report_invalid_emailID))
            mEmailEditText.requestFocus()
            isDataValid = false
        }
        return isDataValid
    }

    interface ClickListener {
        fun finishActivity()
    }
}