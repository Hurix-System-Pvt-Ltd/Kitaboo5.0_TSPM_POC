package com.hurix.demoreader;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.hurix.commons.Constants.Constants;
import com.hurix.commons.datamodel.IDownloadable;
import com.hurix.commons.utils.DialogUtils;
import com.hurix.commons.utils.Utils;
import com.hurix.customui.datamodel.UserVO;
import com.hurix.database.datamodels.UserSettingVO;
import com.hurix.downloadbook.controller.SDKDownloadController;
import com.hurix.downloadbook.controller.UserController;
import com.hurix.downloadbook.listener.SDKDownloadCompleteListener;
import com.hurix.downloadbook.listener.SDKDownloadListener;
import com.hurix.epubreader.fixedepubreader.enums.BookState;
import com.hurix.reader.kitaboosdkrenderer.PlayerActivity;
import com.hurix.reader.kitaboosdkrenderer.R;
import com.hurix.service.Interface.IServiceResponse;
import com.hurix.service.Interface.IServiceResponseListener;
import com.hurix.service.adapter.KitabooServiceAdapter;
import com.hurix.service.exception.ServiceException;
import com.hurix.service.response.BookDownloadServiceResponse;
import com.hurix.service.response.LoginResponse;
import com.hurix.service.response.UserSetttingResponse;
import com.hurix.service.response.ValidateUserTokenResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import static android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION;
import static android.content.Intent.FLAG_GRANT_WRITE_URI_PERMISSION;
import static android.provider.ContactsContract.Directory.PACKAGE_NAME;

public class DemoReaderActivity extends AppCompatActivity {


    private com.hurix.customui.datamodel.UserVO mUserVO;
    private final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 2001;
    private String mClassID;
    private KitabooServiceAdapter kitabooServiceAdapter;
    private ProgressBar btnReflowProgressBar;
    private ProgressBar btnPdfPbProgressBar;
    private ProgressBar btnFixedEpubPbProgressBar;
    private Button btnReflow;
    private Button btnPdf;
    private Button btnFixedEpub;
    private ProgressBar downloadProgressBar;
    private Map<String, IDownloadable> mBookMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        getSupportActionBar().hide();
        if (getResources().getBoolean(R.bool.is_prepackageBookProvided)) {
            //Saving book package into storage from asset folder.
            saveBookPackage();
        }
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setContentView(R.layout.activity_demo_reader);


        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        btnReflow = findViewById(R.id.reflow_v3);
        btnPdf = findViewById(R.id.buttonpdf);
        btnFixedEpub = findViewById(R.id.fixed_v3);

        btnReflowProgressBar = findViewById(R.id.reflowProgressBar);
        btnPdfPbProgressBar = findViewById(R.id.pdfProgressBar);
        btnFixedEpubPbProgressBar = findViewById(R.id.fixedProgressBar);
        downloadProgressBar = findViewById(R.id.downloadProgressBar);
        kitabooServiceAdapter = new KitabooServiceAdapter(this, getString(R.string.clientid));
        prepareData();

        setBookNUserObject("pdf", false);
        setBookNUserObject("epub", false);

        if (mBookMap.containsKey("pdf") && new File(((BookVO) mBookMap.get("pdf")).getBookPath()).exists()) {
            btnPdf.setText("LAUNCH PDF");
        }

        /*if (mBookMap.containsKey("epub") && new File(((BookVO) mBookMap.get("epub")).getBookPath()).exists()) {
            btnReflow.setText("LAUNCH");
        }*/


        btnPdf.setOnClickListener(v -> {

            //Calling book reader activity
            callActivity("pdf");


            //callActivity();
        });

        btnReflow.setOnClickListener(v -> {



        });


        btnFixedEpub.setOnClickListener(v -> {

            //Calling book reader activity Downloading Error
            setBookNUserObject("epub_local", true);
            callActivity("epub_local");

        });

        if (!isStoragePermissionGranted()) {
            checkForPermission();
        }
    }


    private boolean isStoragePermissionGranted() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void checkForPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
            return;
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
                    DialogUtils.showOKAlert(new View(this), 1, this, "Request Permission",
                            "Permission required to Read/Write External Storage. \\n\\nEnable through Device Setting -> Apps -> Permission -> Enable Storage.", null);
                }
                break;
        }
    }

    private void saveBookPackage() {
        try {
            File bookFilepath = new File("/storage/emulated/0/Android/data/com.hurix.demoreader/files/.cloudreaderbooks/89047777");
            if (!bookFilepath.exists()) {
                File file = new File("/storage/emulated/0/Android/data/com.hurix.demoreader/files/.cloudreaderbooks/");
                if (!file.exists()) {
                    file.mkdirs();
                }
                String[] files = getAssets().list("BookPackage");

                for (int i = 0; i < files.length; i++) {
                    InputStream fis = getAssets().open("BookPackage/" + files[i]);

                    startExtract(fis, file, files[i]);

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /*
     *  inputStreamofInputFile : Input stream of input epub file
     *  outputFolder : Folder where your epub will extract
     *  fileName : name of your epub file (ex. 1245545.epub)
     * */
    private void startExtract(InputStream inputStreamofInputFile, File outputFolder, String fileName) {

        try {

            File bookPackZip = new File(outputFolder.getAbsolutePath() + File.separator + fileName);
            if (bookPackZip.exists()) {
                //  bookPackZip.delete();
            }

            String path = bookPackZip.getPath();
            if (path.contains(".zip")) {
                path = path.replace(".zip", "");
            } else if (path.contains(".epub")) {
                path = path.replace(".epub", "");
            }
            File bookFolder = new File(path);
            if (bookFolder.exists()) {
                bookFolder.delete();
            }

            FileOutputStream fos = new FileOutputStream(bookPackZip.getAbsolutePath());
            byte buf[] = new byte[1024];
            int len;
            while ((len = inputStreamofInputFile.read(buf)) > 0)
                fos.write(buf, 0, len);
            fos.close();
            inputStreamofInputFile.close();
            doUnzip(bookPackZip.getPath(), bookFolder.getAbsolutePath());

            if (bookPackZip.exists()) {
                bookPackZip.delete();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private static void doUnzip(String inputZip, String destinationDirectory) throws IOException {
        int BUFFER = 2048;
        File sourceZipFile = new File(inputZip);
        File unzipDestinationDirectory = new File(destinationDirectory);
        unzipDestinationDirectory.mkdir();
        ZipFile zipFile;
        zipFile = new ZipFile(sourceZipFile, ZipFile.OPEN_READ);
        Enumeration zipFileEntries = zipFile.entries();
        int val = 0;
        while (zipFileEntries.hasMoreElements()) {
            ZipEntry entry = (ZipEntry) zipFileEntries.nextElement();
            String currentEntry = entry.getName();
            val += entry.getSize();
            File destFile = new File(destinationDirectory, currentEntry);
            String canonicalPath = destFile.getCanonicalPath();
            if (!canonicalPath.startsWith(destinationDirectory)) {
                throw new IllegalArgumentException();
            } else {
                File destinationParent = destFile.getParentFile();
                destinationParent.mkdirs();
                try {
                    if (!entry.isDirectory()) {
                        BufferedInputStream is = new BufferedInputStream(zipFile.getInputStream(entry));
                        int currentByte;
                        byte data[] = new byte[BUFFER];
                        FileOutputStream fos = new FileOutputStream(destFile);
                        BufferedOutputStream dest = new BufferedOutputStream(fos, BUFFER);
                        while ((currentByte = is.read(data, 0, BUFFER)) != -1) {
                            dest.write(data, 0, currentByte);
                        }
                        dest.flush();
                        dest.close();
                        is.close();
                    }
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
            }
        }

        zipFile.close();
    }

    private void callActivity(String epub) {
        BookVO mBookVO = (BookVO) mBookMap.get(epub);
        Intent subActivity = new Intent(DemoReaderActivity.this, PlayerActivity.class);
        subActivity.putExtra("media_path", mBookVO.getBookPath());
        subActivity.putExtra("booktype", mBookVO.getBookType());
        subActivity.putExtra("bookDict", mBookVO.getBookDict());
        subActivity.putExtra("book_id", mBookVO.getBookID());
        subActivity.putExtra("asset_type", mBookVO.getBookAssetType() == null ? "DEFAULT" : mBookVO.getBookAssetType());
        subActivity.putExtra("token", mUserVO.getToken());
        subActivity.putExtra("reflow_print", "FALSE");
        subActivity.putExtra("isEncrypt", mBookVO.getIsBookEncrypt());
        subActivity.putExtra("encryptionType", mBookVO.getBookEncryptionType());
        subActivity.putExtra("ISBN", mBookVO.getISBN());
        subActivity.putExtra("UserID", mUserVO.getUserID());
        subActivity.putExtra("Rolename", mUserVO.getRoleName());
        subActivity.putExtra("classID", mClassID);
        subActivity.putExtra("lastPageSync", mBookVO.getLastPage());
        subActivity.putExtra("classAssociated", true);
        subActivity.putExtra("version", mBookVO.getBookVersion());
        subActivity.putExtra("searchQuery", mBookVO.getSearchQuery());
        subActivity.putExtra("cloudUserName", "demo.");
        subActivity.putExtra("cloudFirstName", "Demo");
        subActivity.putExtra("cloudLastName", "SDK");
        subActivity.putExtra("cloudProfilePic", "");
        startActivity(subActivity);
    }


    private void setBookNUserObject(String bookType, Boolean isLocalPage) {
        BookVO mBookVO = new BookVO();
        if (bookType.equalsIgnoreCase("pdf")) {

            mBookVO.setBookPath("/storage/emulated/0/Android/data/com.hurix.demoreader/files/.cloudreaderbooks/14113544/14113544");
            mBookVO.setBookType("BOOK");
            mBookVO.setBookAssetType("BOOK");
            mBookVO.setBookID((long) 14113544);
            mBookVO.setIsBookEncrypt(false);
            mBookVO.setBookEncryptionType("");
            mBookVO.setISBN("2006201800005");
            mBookVO.setLastPage("");
            mBookVO.setBookVersion("1.0");
            mBookVO.setSearchQuery("");
            mBookVO.setBookDict("");
            mClassID = "null";

        } else if (bookType.equalsIgnoreCase("epub_local1")) {
            mBookVO.setBookPath("/storage/emulated/0/Android/data/com.hurix.demoreader/files/.cloudreaderbooks/22387137/22387137");
            mBookVO.setBookType("FIXED_EPUB_IMAGE");
            mBookVO.setBookID((long) 22387137);
            mBookVO.setBookAssetType("FIXED_EPUB_IMAGE");
            mBookVO.setIsBookEncrypt(true);
            mBookVO.setBookEncryptionType("V3");
            mBookVO.setISBN("22387137");
            mBookVO.setLastPage("");
            mBookVO.setBookVersion("1.0");
            mBookVO.setSearchQuery("");
            mBookVO.setBookDict("");

            mClassID = "270";

        } else if ((bookType.equalsIgnoreCase("epub_local"))) {
            mBookVO.setBookPath("/storage/emulated/0/Android/data/com.hurix.demoreader/files/.cloudreaderbooks/1600956607_THEPOSSIBILITY/1600956607_THEPOSSIBILITY");
            mBookVO.setBookType("epub");
            mBookVO.setBookAssetType("epub");
            mBookVO.setBookID((long) 1600956607);
            mBookVO.setIsBookEncrypt(false);
            mBookVO.setBookEncryptionType("");
            mBookVO.setISBN("1600956607");
            mBookVO.setLastPage("");
            mBookVO.setBookVersion("1");
            mBookVO.setSearchQuery("");
            mBookVO.setBookDict("");

            mClassID = "null";
        }
        mBookMap.put(bookType, mBookVO);


    }

    private void prepareData(){
        mUserVO = new UserVO();
        mUserVO.setToken("");
        mUserVO.setUserID(011);
        mUserVO.setRoleName("LEARNER");
        mUserVO.setUserName("TestUser");
        mUserVO.setDisplayName("Test User");

        UserController.getInstance(getApplicationContext()).setUserVO(mUserVO);
    }

    /*private void validateUserToken(BookVO bookVO, boolean wasPaused) {

        if (mUserVO != null) {
            kitabooServiceAdapter.validateUserToken(this, mUserVO.getToken(), new IServiceResponseListener() {
                @Override
                public void requestCompleted(IServiceResponse iServiceResponse) {
                    ValidateUserTokenResponse loginResponse = (ValidateUserTokenResponse) iServiceResponse;

                    updateUserData(loginResponse);


                    downloadBook(wasPaused, bookVO, mUserVO.getUserID(), mUserVO.getToken());
                }

                @Override
                public void requestErrorOccured(ServiceException e) {
                    downloadProgressBar.setVisibility(View.GONE);
                    Toast.makeText(DemoReaderActivity.this, "Login error, please try again later", Toast.LENGTH_SHORT).show();
                    Log.e("TAG", "ServiceException" + e.getMessage());
                }
            });
        } else {
            kitabooServiceAdapter.login("demo.sdk@yopmail.com", "kitaboo@123", new IServiceResponseListener() {
                @Override
                public void requestCompleted(IServiceResponse iServiceResponse) {
                    LoginResponse loginResponse = (LoginResponse) iServiceResponse;

                    mUserVO = new com.hurix.customui.datamodel.UserVO();
                    mUserVO.setToken(loginResponse.getToken());
                    mUserVO.setUserID(loginResponse.getUserid());
                    mUserVO.setRoleName(loginResponse.getRoleName());
                    mUserVO.setUserName(loginResponse.getUserName());

                    UserController.getInstance(getApplicationContext()).setUserVO(loginResponse.getUserVoFromResponse());
                    kitabooServiceAdapter.getTheamOfUser(loginResponse.getToken(), new IServiceResponseListener() {
                        @Override
                        public void requestCompleted(IServiceResponse response) {
                            downloadProgressBar.setVisibility(View.GONE);
                            UserSetttingResponse responseObj = (UserSetttingResponse) response;


                            UserController.getInstance(getApplicationContext()).getUserSettings().updateSettingsFromService(responseObj
                                    .getSettingVO(), true);


                            downloadBook(wasPaused, bookVO, loginResponse.getUserid(), loginResponse.getToken());
                        }

                        @Override
                        public void requestErrorOccured(ServiceException exeption) {
                            downloadProgressBar.setVisibility(View.GONE);
                            Log.e("TAG", "exeption getTheamOfUser " + exeption.getMessage());
                        }
                    });


                }

                @Override
                public void requestErrorOccured(ServiceException e) {
                    downloadProgressBar.setVisibility(View.GONE);
                    Toast.makeText(DemoReaderActivity.this, "Login error, please try again later", Toast.LENGTH_SHORT).show();
                    Log.e("TAG", "ServiceException" + e.getMessage());
                }
            });
        }

    }

    private void updateUserData(ValidateUserTokenResponse loginResponse) {
        try {
            JSONObject jsonObject = loginResponse.getUserJson();
            if (jsonObject.has("userToken")) {
                mUserVO.setToken(jsonObject.getString("userToken"));
                UserController.getInstance(getApplicationContext()).getUserVO().setToken(mUserVO.getToken());
            }

            if (jsonObject.has("user")) {
                JSONObject user = jsonObject.getJSONObject("user");
                if (user.has("id")) {
                    mUserVO.setUserID(user.getLong("id"));
                }

                if (user.has("firstName")) {
                    mUserVO.setDisplayName(user.getString("firstName") + " " + user.getString("lastName"));
                }

                if (user.has("userName")) {
                    mUserVO.setUserName(user.getString("userName"));
                }

                if (user.has("roles")) {
                    JSONArray roles = user.getJSONArray("roles");
                    for (int i = 0; i < roles.length(); i++) {
                        JSONObject obj = (JSONObject) roles.get(i);

                        if (obj.has("name")) {
                            mUserVO.setRoleName(obj.getString("name"));
                        }
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void prepareData() {
        downloadProgressBar.setVisibility(View.VISIBLE);
        kitabooServiceAdapter.login("amit.sdk1@yopmail.com", "kitaboo@123", new IServiceResponseListener() {
            @Override
            public void requestCompleted(IServiceResponse iServiceResponse) {
                LoginResponse loginResponse = (LoginResponse) iServiceResponse;

                mUserVO = new UserVO();
                mUserVO.setToken(loginResponse.getToken());
                mUserVO.setUserID(loginResponse.getUserid());
                mUserVO.setRoleName(loginResponse.getRoleName());
                mUserVO.setUserName(loginResponse.getUserName());
                mUserVO.setDisplayName(loginResponse.getFirstName());

                UserController.getInstance(getApplicationContext()).setUserVO(loginResponse.getUserVoFromResponse());
                kitabooServiceAdapter.getTheamOfUser(loginResponse.getToken(), new IServiceResponseListener() {
                    @Override
                    public void requestCompleted(IServiceResponse response) {
                        downloadProgressBar.setVisibility(View.GONE);
                        UserSetttingResponse responseObj = (UserSetttingResponse) response;
                        UserSettingVO userSettingVO = responseObj.getSettingVO();
                        userSettingVO.setIsUgcShareEnabled(false);
                        responseObj.setSettingVO(userSettingVO);
                        UserController.getInstance(getApplicationContext()).getUserSettings().updateSettingsFromService(responseObj
                                .getSettingVO(), true);

                    }

                    @Override
                    public void requestErrorOccured(ServiceException exeption) {
                        downloadProgressBar.setVisibility(View.GONE);
                        Log.e("TAG", "exeption " + exeption.getMessage());
                    }
                });
                //  UserController.getInstance(DemoReaderActivity.this).fillUserVOFromResponse(mUserVO);
            }

            @Override
            public void requestErrorOccured(ServiceException e) {
                downloadProgressBar.setVisibility(View.GONE);
                Toast.makeText(DemoReaderActivity.this, "Login error, please try again later", Toast.LENGTH_SHORT).show();
                Log.e("TAG", "ServiceException" + e.getMessage());
            }
        });
    }


    private void downloadBook(boolean wasPaused, BookVO bookVO, Long userId, String token) {
        kitabooServiceAdapter.getUserBookDownload(bookVO.getBookID(), token,
                bookVO.getBookType().equalsIgnoreCase("DEFAULT") ? "ANDROID" : bookVO.getBookType(), new IServiceResponseListener() {
                    @Override
                    public void requestCompleted(IServiceResponse iServiceResponse) {

                        downloadProgressBar.setVisibility(View.GONE);
                        BookDownloadServiceResponse downloadBookServiceRequest = (BookDownloadServiceResponse) iServiceResponse;

                        String msg = String.format(getResources().getString(R.string.book_download_alert_wifi), wasPaused ?
                                new DecimalFormat("##.##").format((Double.parseDouble(downloadBookServiceRequest.getFileSize()) - Utils.getDownloadedZipFileSize(bookVO.getBookID() + "", bookVO.getBookISBN(),
                                        downloadBookServiceRequest.getBookURL().contains(Constants.FILE_TYPE_TAR) ? Constants.FILE_TYPE_TAR : Constants.FILE_TYPE_ZIP)))
                                : downloadBookServiceRequest.getFileSize());

                        String splitMsg[] = msg.split("\n");

                        AlertDialog.Builder builder1 = new AlertDialog.Builder(DemoReaderActivity.this);
                        builder1.setTitle(splitMsg[0].toString());
                        builder1.setMessage(splitMsg[1].toString());
                        builder1.setCancelable(true);

                        builder1.setPositiveButton(
                                "Yes",
                                (dialog, id) -> {
                                    dialog.cancel();
                                    bookVO.setDownloadURI(downloadBookServiceRequest.getBookURL());
                                    bookVO.setCurrentState(BookState.INITIALIZE);
                                    bookVO.setCurrentState(BookState.WAITING_TO_BE_IN_QUEUE);
                                    bookVO.setDownloadStateListener(DemoReaderActivity.this);
                                    SDKDownloadController.getInstance(DemoReaderActivity.this).getDownloadManager().addToQue(bookVO, userId, DemoReaderActivity.this);

                                });

                        builder1.setNegativeButton(
                                "No",
                                (dialog, id) -> dialog.cancel());

                        AlertDialog alert11 = builder1.create();
                        alert11.show();

                    }

                    @Override
                    public void requestErrorOccured(ServiceException e) {
                        downloadProgressBar.setVisibility(View.GONE);
                        Toast.makeText(DemoReaderActivity.this, "Error getting file Url", Toast.LENGTH_SHORT).show();
                        Log.e("TAG", "ServiceException" + e.getMessage());
                    }
                });
    }

    @Override
    public void stateUpdated(BookState bookState, IDownloadable iBook) {
        BookVO bookVO = (BookVO) iBook;

        Log.e("TAG", "State: " + bookState + " bookVO.getCurrSize(): " + bookVO.getCurrSize());
        switch (bookState) {
            case INITIALIZE:
                downloadStart(bookVO);
                break;
            case UNZIP_ERROR:
                unzipError();
                break;
            case COMPLETED:
                downloadCompleted(iBook, true);
                break;
            case DOWNLOADED:
                downloadInQueue();
                break;
            case DOWNLOADING:
                downloading(bookVO);
                break;
            case DOWNLOAD_ERROR:
                downloadError(bookVO);
                break;
            case IN_QUEUE:
                downloadInQueue();
                break;
            case STARTED:
                downloadStart(bookVO);
                break;
            case UNZIPPED:
                unzipCompleted(bookVO);
                break;
            case UNZIPPING:
                unzipInQueue();
                break;
            case WAITING_TO_BE_IN_QUEUE:
                waitingToBeInQueue();
                break;
            case NOT_STARTED:
                onDownloadingNotStarted();
                break;
            case PAUSED:
                onDownloadPaused(bookVO.getBookID());
                break;
            default:
                break;
        }
    }

    private void pausedDownloading(String bookType, int bookId) {
        onDownloadPaused(bookId);
        if (mBookMap.containsKey(bookType))
            SDKDownloadController.getInstance(this).getDownloadManager().stopDownload(mBookMap.get(bookType));
    }

    private void onDownloadPaused(long bookVOId) {
        switch ((int) bookVOId) {
            case 14120140:
                btnPdf.setText("RESUME");
                btnPdfPbProgressBar.setVisibility(View.GONE);
                break;
            case 14157513:
                btnReflow.setText("RESUME");
                btnReflowProgressBar.setVisibility(View.GONE);
                break;


        }
    }

    private void downloading(BookVO bookVO) {

        float val = ((bookVO.getCurrSize() / bookVO.getTotalSize()) * 100f);
        switch ((int) bookVO.getBookID()) {
            case 14120140:
                btnPdf.setText("");
                btnPdfPbProgressBar.setVisibility(View.VISIBLE);
                btnPdfPbProgressBar.setProgress((int) val);
                break;
            case 14157513:
                btnReflow.setText("");
                btnReflowProgressBar.setVisibility(View.VISIBLE);
                btnReflowProgressBar.setProgress((int) val);
                break;


        }
        Log.e("TAG", "bookVO Id: " + bookVO.getBookID() + " & downloading: " + val);
    }

    private void downloadCompletedd() {

    }

    private void onDownloadingNotStarted() {

    }

    private void waitingToBeInQueue() {

    }

    private void unzipInQueue() {

    }

    private void unzipCompleted(BookVO bookVO) {
        switch ((int) bookVO.getBookID()) {
            case 14120140:
                btnPdfPbProgressBar.setVisibility(View.GONE);
                btnPdf.setText("LAUNCH");

                break;
            case 14157513:
                btnReflowProgressBar.setVisibility(View.GONE);
                btnReflow.setText("LAUNCH");
                break;

        }

        Log.e("TAG", "downloadCompleted: ");
    }

    private void downloadStart(BookVO bookVO) {
        switch ((int) bookVO.getBookID()) {
            case 14120140:
                btnPdf.setText("Downloading...");
                break;
            case 14157513:
                btnReflow.setText("Downloading...");
                break;


        }
    }

    private void downloadError(BookVO bookVO) {
        switch ((int) bookVO.getBookID()) {
            case 14120140:
                btnPdfPbProgressBar.setVisibility(View.GONE);
                btnPdf.setText("Downloading Error");

                break;
            case 14157513:
                btnReflowProgressBar.setVisibility(View.GONE);
                btnReflow.setText("Downloading Error");

                break;

        }
    }

    private void downloadInQueue() {

    }

    private void unzipError() {

    }

    @Override
    public void downloadCompleted(IDownloadable iDownloadable, boolean b) {
        Log.e("TAG", "downloadCompleted: ");
    }

    @Override
    public void downloadIntrrupted(IDownloadable iDownloadable) {

    }

*/

}
