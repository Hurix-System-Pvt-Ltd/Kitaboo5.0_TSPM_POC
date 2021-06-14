
package com.hurix.kitaboocloud.sdkreadertheme.themePojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class DayMode implements Serializable {

    @SerializedName("main")
    @Expose
    private Main main;
    @SerializedName("tableofcontents")
    @Expose
    private Tableofcontents tableofcontents;
    @SerializedName("MyData")
    @Expose
    private MyData myData;
    @SerializedName("Bookmark")
    @Expose
    private Bookmark bookmark;
    @SerializedName("Search")
    @Expose
    private Search search;
    @SerializedName("Pentool")
    @Expose
    private Pentool pentool;
    @SerializedName("Thumbnail_Slider")
    @Expose
    private ThumbnailSlider thumbnailSlider;
    @SerializedName("Note")
    @Expose
    private Note note;
    @SerializedName("FontSettings")
    @Expose
    private FontSettings fontSettings;
    @SerializedName("Teacher_Settings")
    @Expose
    private TeacherSettings teacherSettings;
    @SerializedName("Comments")
    @Expose
    private Comments comments;
    @SerializedName("zoom")
    @Expose
    private Zoom zoom;
    @SerializedName("Teacher_studentlist")
    @Expose
    private TeacherStudentList teacherStudentList;
    @SerializedName("Highlight_on_screen")
    @Expose
    private HighlightOnScreen highlightOnScreen;
    @SerializedName("Share")
    @Expose
    private Share share;


    public Highlight getHighlight() {
        return highlight;
    }

    public void setHighlight(Highlight highlight) {
        this.highlight = highlight;
    }

    @SerializedName("Highlight")
    @Expose
    private Highlight highlight;


    public Main getMain() {
        return main;
    }

    public void setMain(Main main) {
        this.main = main;
    }

    public Tableofcontents getTableofcontents() {
        return tableofcontents;
    }

    public void setTableofcontents(Tableofcontents tableofcontents) {
        this.tableofcontents = tableofcontents;
    }

    public MyData getMyData() {
        return myData;
    }

    public void setMyData(MyData myData) {
        this.myData = myData;
    }

    public Bookmark getBookmark() {
        return bookmark;
    }

    public void setBookmark(Bookmark bookmark) {
        this.bookmark = bookmark;
    }

    public Search getSearch() {
        return search;
    }

    public void setSearch(Search search) {
        this.search = search;
    }

    public Pentool getPentool() {
        return pentool;
    }

    public void setPentool(Pentool pentool) {
        this.pentool = pentool;
    }

    public ThumbnailSlider getThumbnailSlider() {
        return thumbnailSlider;
    }

    public void setThumbnailSlider(ThumbnailSlider thumbnailSlider) {
        this.thumbnailSlider = thumbnailSlider;
    }

    public Note getNote() {
        return note;
    }

    public void setNote(Note note) {
        this.note = note;
    }

    public FontSettings getFontSettings() {
        return fontSettings;
    }

    public void setFontSettings(FontSettings fontSettings) {
        this.fontSettings = fontSettings;
    }

    public TeacherSettings getTeacherSettings() {
        return teacherSettings;
    }

    public void setTeacherSettings(TeacherSettings teacherSettings) {
        this.teacherSettings = teacherSettings;
    }

    public Comments getComments() {
        return comments;
    }

    public void setComments(Comments comments) {
        this.comments = comments;
    }

    public Zoom getZoom() {
        return zoom;
    }

    public void setZoom(Zoom zoom) {
        this.zoom = zoom;
    }

    public TeacherStudentList getTeacherStudentList() {
        return teacherStudentList;
    }

    public void setTeacherStudentList(TeacherStudentList teacherStudentList) {
        this.teacherStudentList = teacherStudentList;
    }

    public HighlightOnScreen getHighlightOnScreen() {
        return highlightOnScreen;
    }

    public void setHighlightOnScreen(HighlightOnScreen highlightOnScreen) {
        this.highlightOnScreen = highlightOnScreen;
    }

    public Share getShare() {
        return share;
    }

    public void setShare(Share share) {
        this.share = share;
    }



}
