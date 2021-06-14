package com.hurix.demoreader;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.hurix.commons.datamodel.IDownloadable;
import com.hurix.customui.datamodel.UserVO;
import com.hurix.downloadbook.controller.UserController;
import com.hurix.kitaboo.constants.dialog.DialogUtils;
import com.hurix.kitaboocloud.kitaboosdkrenderer.PlayerActivity;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class DemoReaderActivity extends AppCompatActivity {


    private com.hurix.customui.datamodel.UserVO mUserVO;
    private final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 2001;
    private String mClassID;

    private Button btnPdf;
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

        btnPdf = findViewById(R.id.buttonpdf);
        prepareData();

        setBookNUserObject("pdf", false);

        btnPdf.setOnClickListener(v -> {

            //Calling book reader activity
            callActivity("pdf");
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
        }
        mBookMap.put(bookType, mBookVO);


    }

    private void prepareData() {
        mUserVO = new UserVO();
        mUserVO.setToken("");
        mUserVO.setUserID(011);
        mUserVO.setRoleName("LEARNER");
        mUserVO.setUserName("TestUser");
        mUserVO.setDisplayName("Test User");

        UserController.getInstance(getApplicationContext()).setUserVO(mUserVO);
    }

}
