package com.hurix.reader.kitaboosdkrenderer.sdkreadertheme.themePojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Comments implements Serializable {

    @SerializedName("back_icon-color")
    @Expose
    private String backIconColor;
    @SerializedName("tab_text-color")
    @Expose
    private String tabTextColor;
    @SerializedName("divider-color")
    @Expose
    private String dividerColor;
    @SerializedName("other_message")
    @Expose
    private  OtherMessage otherMessage;
    @SerializedName("my_message")
    @Expose
    private MyMessage myMessage ;
    @SerializedName("bottom_panel")
    @Expose
    private BottomPanel bottomPanel ;

    public String getBackIconColor() {
        return backIconColor;
    }

    public void setBackIconColor(String backIconColor) {
        this.backIconColor = backIconColor;
    }

    public String getTabTextColor() {
        return tabTextColor;
    }

    public void setTabTextColor(String tabTextColor) {
        this.tabTextColor = tabTextColor;
    }

    public String getDividerColor() {
        return dividerColor;
    }

    public void setDividerColor(String dividerColor) {
        this.dividerColor = dividerColor;
    }

    public OtherMessage getOtherMessage() {
        return otherMessage;
    }

    public void setOtherMessage(OtherMessage otherMessage) {
        this.otherMessage = otherMessage;
    }

    public MyMessage getMyMessage() {
        return myMessage;
    }

    public void setMyMessage(MyMessage myMessage) {
        this.myMessage = myMessage;
    }

    public BottomPanel getBottomPanel() {
        return bottomPanel;
    }

    public void setBottomPanel(BottomPanel bottomPanel) {
        this.bottomPanel = bottomPanel;
    }
}
