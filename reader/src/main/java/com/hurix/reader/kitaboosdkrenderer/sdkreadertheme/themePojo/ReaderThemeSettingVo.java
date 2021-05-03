
package com.hurix.reader.kitaboosdkrenderer.sdkreadertheme.themePojo;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ReaderThemeSettingVo implements Serializable {

    @SerializedName("Reader")
    @Expose
    private Reader reader;

    @SerializedName("Reflowable_reader")
    @Expose
    private ReflowableReader reflowableReader;

    @SerializedName("Markup_Glossary")
    @Expose
    private MarkupGlossary markupGlossary;

    @SerializedName("Help")
    @Expose
    private Help help;

    @SerializedName("Text_Annotation")
    @Expose
    private TextAnnotation textAnnotation;


    public Help getHelp() {
        return help;
    }

    public void setHelp(Help help) {
        this.help = help;
    }

    public MarkupGlossary getMarkupGlossary() {
        return markupGlossary;
    }

    public void setMarkupGlossary(MarkupGlossary markupGlossary) {
        this.markupGlossary = markupGlossary;
    }

    public Reader getReader() {
        return reader;
    }

    public void setReader(Reader reader) {
        this.reader = reader;
    }

    public ReflowableReader getReflowableReader() {
        return reflowableReader;
    }

    public void setReflowableReader(ReflowableReader reflowableReader) {
        this.reflowableReader = reflowableReader;
    }

    public TextAnnotation getTextAnnotation() {
        return textAnnotation;
    }

    public void setTextAnnotation(TextAnnotation textAnnotation) {
        this.textAnnotation = textAnnotation;
    }
}
