package com.hurix.kitaboocloud.interfaces;

import com.hurix.commons.datamodel.KitabooFixedBook;

/**
 * Created by Ravi Ranjan on 4/27/2015.
 */
public interface ReadAloudController {
    void letMeReadClicked(KitabooFixedBook.ReadAloudType letmeread);
    void autoPlayClicked(KitabooFixedBook.ReadAloudType autoplay);
    void readToMeClicked(KitabooFixedBook.ReadAloudType readtome);
    void readDialogDismiss();
}
