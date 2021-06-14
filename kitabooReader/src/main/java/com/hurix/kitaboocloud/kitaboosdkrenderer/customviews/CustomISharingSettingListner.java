package com.hurix.kitaboocloud.kitaboosdkrenderer.customviews;

import com.hurix.customui.datamodel.HighlightVO;
import com.hurix.customui.datamodel.UserClassVO;
import com.hurix.customui.interfaces.IClass;
import com.hurix.service.Interface.IServiceResponseListener;

import java.util.ArrayList;

/**
 * Created by amitraj.sharma on 3/6/2018.
 */

public interface CustomISharingSettingListner {
    void saveMydataHiglightedNotetoDatabase(HighlightVO mSharingData, UserClassVO mCurrentSelectedClass, CustomMyDataFragment mUgcHolder);

    void syncUserForHighlightSettingService(ArrayList<IClass> arrayList, IServiceResponseListener iServiceResponseListener);

    void saveMydataHiglightedNotetoDatabase(HighlightVO mSharingData, UserClassVO mCurrentSelectedClass, CustomMyDataTabFragment mUgcHolder);
}
