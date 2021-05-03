package com.hurix.reader.kitaboosdkrenderer.notifier;

import com.hurix.commons.datamodel.IBook;


/**
 * Created by consultant.ravi on 1/12/2017.
 */
public class GlobalBookPositionManager {

    private int lastSelectedBookPosition = -1;
    private int lastSelectedCollectionPosition = -1;
    private int lastExpandbleViewParentPosition = -1;
    private IBook lastSelectedBook;
    private static GlobalBookPositionManager globalDataManager;


    public static GlobalBookPositionManager getInstance() {
        if(globalDataManager == null) {
            globalDataManager = new GlobalBookPositionManager();
        }
        return globalDataManager;
    }

    public int getLastSelectedBookPosition() {
        return lastSelectedBookPosition;
    }

    public void setLastSelectedBookPosition(int lastSelectedBookPosition) {
        this.lastSelectedBookPosition = lastSelectedBookPosition;
    }

    public int getLastSelectedCollectionPosition() {
        return lastSelectedCollectionPosition;
    }

    public void setLastSelectedCollectionPosition(int lastSelectedCollectionPosition) {
        this.lastSelectedCollectionPosition = lastSelectedCollectionPosition;
    }
    public IBook getLastSelectedBook() {
        return lastSelectedBook;
    }

    public void setLastSelectedBook(IBook lastSelectedBook) {
        this.lastSelectedBook = lastSelectedBook;
    }

    public int getLastExpandbleViewParentPosition() {
        return lastExpandbleViewParentPosition;
    }

    public void setLastExpandbleViewParentPosition(int lastExpandbleViewParentPosition) {
        this.lastExpandbleViewParentPosition = lastExpandbleViewParentPosition;
    }

    public void clear() {
        globalDataManager = null;
    }



}
