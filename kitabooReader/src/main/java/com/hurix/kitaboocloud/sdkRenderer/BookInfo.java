package com.hurix.kitaboocloud.sdkRenderer;







import com.hurix.commons.sdkDatamodel.ClientBookInfoInterface;

import org.jetbrains.annotations.NotNull;


/**
 * Created by lopamudra.mohanty on 12/8/2017.
 */

/**
 * This class created by SDK user to pass the book
 * related data to SDK for rendering purpose
 */

public  class BookInfo implements ClientBookInfoInterface {
    String bookPath;
    long bookID;
    String bookIsbn;
    boolean isEncrypt;
    String encrType;



    public BookInfo(String bookPath, long bookID, String bookIsbn, String type) {
        this.bookPath = bookPath;
        this.bookID = bookID;
        this.bookIsbn = bookIsbn;
        this.encrType=type;
    }


    @Override
    public String getBookPath() throws IllegalArgumentException {
        return bookPath;
    }

    @Override
    public long getBookID() throws IllegalArgumentException {
        return bookID;
    }


    @Override
    public String getBookISBN() throws IllegalArgumentException {
        return bookIsbn;
    }

    public void setBookEncrypt(boolean isencrypt)
    {
        this.isEncrypt = isencrypt;
    }

    @Override
    public boolean isEncrypt() {
        return this.isEncrypt;
    }


    @NotNull
    @Override
    public String encryptType() {
        return encrType;
    }
}
