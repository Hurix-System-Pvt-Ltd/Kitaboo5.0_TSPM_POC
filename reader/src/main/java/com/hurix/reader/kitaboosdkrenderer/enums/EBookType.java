package com.hurix.reader.kitaboosdkrenderer.enums;

public enum EBookType 
{
	EPUB("EPUB"),DEFAULT("default"),LANDSCAPE_ONE_PAGE("landscape_one_page")
	,LANDSCAPE_TWO_PAGE("landscape_two_page"),PORTRAIT("portrait"),VIDEO("VIDEO"), AUDIO("AUDIO")
	,FIXED_EPUB_IMAGE("FIXED_EPUB_IMAGE"),REFLOW_EPUB3("REFLOW_EPUB3"),ANDROID("ANDROID"),Author_REFLOW_EPUB3("Author_REFLOW_EPUB3");
	
	EBookType(final String text)
	{
		this.text = text;
	}

	private String text;

	@Override
	public String toString() 
	{
		return text;
	}
}
