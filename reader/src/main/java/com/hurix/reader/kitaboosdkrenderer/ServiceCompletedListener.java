package com.hurix.reader.kitaboosdkrenderer;
import com.hurix.service.datamodel.UGCFetchResponseObject;

import java.util.ArrayList;

interface ServiceCompletedListener {

    void fetchUGCRequestCompleted(ArrayList<UGCFetchResponseObject> arrayOfUGCIDs);
}
