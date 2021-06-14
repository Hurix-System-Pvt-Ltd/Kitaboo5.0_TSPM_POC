package com.hurix.kitaboo.constants.utils;

import android.app.ActivityManager;
import android.content.ComponentCallbacks2;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import androidx.collection.LruCache;

import com.hurix.kitaboo.constants.Constants;
import com.hurix.kitaboo.constants.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by ravi.ranjan on 12/7/2015.
 */
public class LrImageLoader implements ComponentCallbacks2 {

    private static TCLruCache cache;
    private Context mContext;
    public static String extractFolder = Constants.ALLBOOKROOTPATH + File.separator + Constants.thumbsDir;
    private int sizeX, sizeY;
    Bitmap myBitmap = null;

    public LrImageLoader(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(
                Context.ACTIVITY_SERVICE);
        this.mContext = context;
        int maxKb = am.getMemoryClass() * 1024;
        int limitKb = maxKb / 8; // 1/8th of total ram
        cache = TCLruCache.getInstance(limitKb);
    }


    public void removeCache() {
        if (cache != null) {
            cache.evictAll();
        }

    }

    @Override
    public void onTrimMemory(int level) {
        if (level >= TRIM_MEMORY_MODERATE) {
            cache.evictAll();
        } else if (level >= TRIM_MEMORY_BACKGROUND) {
            cache.trimToSize(cache.size() / 2);
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {

    }

    @Override
    public void onLowMemory() {

    }

    private File isBookDifferent(String bookid, String filename) {
        File folder = new File(extractFolder);
        File[] listOfFiles = folder.listFiles();
        if (listOfFiles != null) {
            for (File file : listOfFiles) {
                if (file.isFile()) {
                    if (file.getName().contains(bookid) && !file.getName().equals(filename)) {
                        return file;
                    }
                }
            }
        }
        return null;
    }

    public void deletePreviousThumbnail(long bookID) {
        if (ImageStorage.checkifImageExists(bookID + "" + ".jpg")) {
            File folder = new File(extractFolder);
            File file = new File(folder.getAbsolutePath(), bookID + ".jpg");
            if (file.exists())
                file.delete();
            //store image
            // new SetImageTask(bookID,imageview).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, url);
        }
    }

    public synchronized void display(final long bookID, final String url, final ImageView imageview, final int defaultresource) {
        sizeX = imageview.getWidth();
        sizeY = imageview.getHeight();
        if (sizeX == 0) {
            ViewTreeObserver vto = imageview.getViewTreeObserver();
            vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                public boolean onPreDraw() {
                    imageview.getViewTreeObserver().removeOnPreDrawListener(this);
                    sizeX = imageview.getWidth() != 0 ? imageview.getWidth() : 400;
                    sizeY = imageview.getHeight() != 0 ? imageview.getHeight() : 400;
                    try {
                        Picasso.with(mContext).load(Uri.parse(url)).resize(sizeX, sizeY).centerInside().placeholder(defaultresource).into(imageview, new Callback() {
                            @Override
                            public void onSuccess() {
                                Bitmap bitmap = ((BitmapDrawable) imageview.getDrawable()).getBitmap();
                                if (bitmap.getHeight() > bitmap.getWidth()) {
                                    imageview.setScaleType(ImageView.ScaleType.FIT_XY);
                                }
                                //imageview.setScaleType(ImageView.ScaleType.FIT_XY);
                            }

                            @Override
                            public void onError() {
                                loadImageFromSdCardOrNetwork(bookID, defaultresource, url, imageview, sizeX, sizeY);
                            }
                        });
                    } catch (OutOfMemoryError e) {
                        e.printStackTrace();
                    }
                    return true;
                }
            });
        } else {
            try {
                Picasso.with(mContext).load(Uri.parse(url)).resize(sizeX, sizeY).centerInside().placeholder(defaultresource).into(imageview, new Callback() {
                    @Override
                    public void onSuccess() {
                        Bitmap bitmap = ((BitmapDrawable) imageview.getDrawable()).getBitmap();
                        if (bitmap.getHeight() > bitmap.getWidth()) {
                            imageview.setScaleType(ImageView.ScaleType.FIT_XY);
                        }
                    }

                    @Override
                    public void onError() {
                        loadImageFromSdCardOrNetwork(bookID, defaultresource, url, imageview, sizeX, sizeY);
                    }
                });
            } catch (OutOfMemoryError e) {
                e.printStackTrace();
            }
        }

    }

    private void loadImageFromSdCardOrNetwork(final long bookID, final int defaultresource, final String url, final ImageView imageView, int sizeX, int sizeY) {
        if (url == null) return;
        final String filename;
        if (url.contains("timestamp")) {
            String timestamp_name[] = url.split("timestamp=");
            filename = bookID + "-" + timestamp_name[1] + ".jpg";
            new Thread(new Runnable() {
                @Override
                public void run() {
                    File fileis = isBookDifferent(bookID + "-", filename);
                    if (fileis != null && fileis.exists()) {
                        fileis.delete();
                    }
                }
            }).start();

        } else {
            filename = bookID + "" + ".jpg";
        }
        final File file = ImageStorage.getImage(filename);

        if (!url.isEmpty()) {
            if (file != null && file.exists()) {
                Picasso.with(mContext).load(Uri.fromFile(file)).placeholder(defaultresource).into(imageView);
            } else {
                Target target = new CustomTarget(url, bookID);
                Picasso.with(mContext).load(url).placeholder(defaultresource).into(imageView);
                Picasso.with(mContext).load(url).into(target);

                // new SetImageTask(bookID, imageView).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, url);
            }
        }

    }
    private static class CustomTarget implements Target {
        //private ImageView imageView;
        private String url;
        private long bookId;
        public CustomTarget(String url, long bookID) {
            //this.imageView = imageView;
            this.url = url;
            this.bookId = bookID;
        }

        @Override
        public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
            //imageView.setImageBitmap(bitmap);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    if(!url.contains("timestamp")) {
                        ImageStorage.saveToSdCard(bitmap, bookId);
                    } else {
                        String timestamp_name[] = url.split("timestamp=");
                        ImageStorage.saveToSdCard(timestamp_name[1], bitmap, bookId);
                    }
                    if (bitmap != null) {
                        cache.put(url, bitmap);
                    }
                }
            }).start();

        }

        @Override
        public void onBitmapFailed(Drawable errorDrawable) {

        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {

        }
    }
    /**
     * Currently it's not in use: Use {@link CustomTarget} with picasso for books thumbnail downloading.
     */
    /*private class SetImageTask extends AsyncTask<String, Void, String> {
        private ImageView imageview;
        private String url;
        private long bookID;
        public SetImageTask(long bookID,ImageView imageview) {
            this.imageview = imageview;
            this.bookID = bookID;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... params) {
            url = params[0];
            Bitmap bmp;
            String filePath;
            try {
                bmp = getBitmapFromURL(url);
                //SaveBitmapPic(bookID,bmp);
                if(!url.contains("timestamp")) {
                    filePath = ImageStorage.saveToSdCard(bmp, bookID);
                }
                else{
                    String timestamp_name[] = url.split("timestamp=");
                    filePath = ImageStorage.saveToSdCard(timestamp_name[1],bmp, bookID);
                }
                if (bmp != null) {
                    cache.put(url, bmp);
                } else {
                    return null;
                }
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
            return filePath;
        }

        @Override
        protected void onPostExecute(String filePath) {
        *//*if (result != null && imageview.getTag() != null && imageview.getTag().toString().equals(url)) {
            imageview.setImageBitmap(result);
        }*//*
            super.onPostExecute(filePath);
        }

        private Bitmap getBitmapFromURL(String src) {
            HttpURLConnection connection = null;
            InputStream input = null;

            try {
                try {
                    InetAddress i = InetAddress.getByName(src);
                } catch (UnknownHostException e1) {
                    e1.printStackTrace();
                }

                URL url = new URL(src);
                connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                input = connection.getInputStream();
                if(myBitmap != null) myBitmap.recycle();
                */

    /**
     * Downscaling of downloaded bitmap for lower memory usage
     *//*
                myBitmap = ScalingUtilities.decodeStream(input, sizeX, sizeY, ScalingUtilities.ScalingLogic.FIT);
                return myBitmap;
            } catch (MalformedURLException e){
                e.printStackTrace();
                return null;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            } catch (OutOfMemoryError oomException) {
                if (myBitmap != null) {
                    myBitmap.recycle();
                }
                return null;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            } finally {
                if (connection != null)
                    connection.disconnect();
                if (input != null) {
                    input = null;
                }
            }
        }
    }*/
    public synchronized void loadImage(final long bookID, int resource, final ImageView imageview) {
        sizeX = imageview.getWidth();
        sizeY = imageview.getHeight();
        if (sizeX == 0) {
            ViewTreeObserver vto = imageview.getViewTreeObserver();
            vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                public boolean onPreDraw() {
                    imageview.getViewTreeObserver().removeOnPreDrawListener(this);
                    sizeX = imageview.getWidth() != 0 ? imageview.getWidth() : (int) mContext.getResources().getDimension(R.dimen.book_thumbnail_item_width);
                    sizeY = imageview.getHeight() != 0 ? imageview.getHeight() : (int) mContext.getResources().getDimension(R.dimen.book_thumbnail_item_height);
                    ;

                    try {
                        Picasso.with(mContext).load(resource).resize(sizeX, sizeY).into(imageview, new Callback() {
                            @Override
                            public void onSuccess() {
                                Bitmap bitmap = ((BitmapDrawable) imageview.getDrawable()).getBitmap();
                                if (bitmap.getHeight() > bitmap.getWidth()) {
                                    imageview.setScaleType(ImageView.ScaleType.FIT_XY);
                                }
                            }

                            @Override
                            public void onError() {

                            }
                        });
                    } catch (OutOfMemoryError e) {
                        e.printStackTrace();
                    }
                    return true;
                }
            });
        } else {
            try {
                Picasso.with(mContext).load(resource).resize(sizeX, sizeY).into(imageview, new Callback() {
                    @Override
                    public void onSuccess() {
                        Bitmap bitmap = ((BitmapDrawable) imageview.getDrawable()).getBitmap();
                        if (bitmap.getHeight() > bitmap.getWidth()) {
                            imageview.setScaleType(ImageView.ScaleType.FIT_XY);
                        }
                    }

                    @Override
                    public void onError() {

                    }
                });
            } catch (OutOfMemoryError e) {
                e.printStackTrace();
            }
        }

    }
}

class TCLruCache extends LruCache<String, Bitmap> {

    private static TCLruCache mTcLruCache;

    public TCLruCache(int maxSize) {
        super(maxSize);
    }

    public static TCLruCache getInstance(int maxSize) {
        if (mTcLruCache == null) {
            mTcLruCache = new TCLruCache(maxSize);
        }
        return mTcLruCache;
    }

    @Override
    protected int sizeOf(String key, Bitmap value) {
        int kbOfBitmap = value.getByteCount() / 1024;
        return kbOfBitmap;
    }


}

class ImageStorage {

    public static String extractFolder = Constants.ALLBOOKROOTPATH + File.separator + Constants.thumbsDir;

    public static String saveToSdCard(Bitmap bitmap, long filename) {
        if (bitmap == null) return "";
        String stored = null;
        File folder = new File(extractFolder);//the dot makes this directory hidden to the user
        folder.mkdirs();
        File file = new File(folder.getAbsolutePath(), filename + ".jpg");
        if (file.exists())
            return file.getAbsolutePath();

        try {
            FileOutputStream stream = new FileOutputStream(file);
            BufferedOutputStream bos = new BufferedOutputStream(stream);
            //choose another format if PNG doesn't suit you
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
            bos.flush();
            bos.close();
            stored = "success";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stored;
    }

    public static String saveToSdCard(String timeStamp, Bitmap bitmap, long filename) {
        if (bitmap == null) return "";
        String stored = null;
        File folder = new File(extractFolder);//the dot makes this directory hidden to the user
        folder.mkdirs();
        File file = new File(folder.getAbsolutePath(), filename + "-" + timeStamp + ".jpg");
        if (file.exists())
            return file.getAbsolutePath();

        try {
            FileOutputStream stream = new FileOutputStream(file);
            BufferedOutputStream bos = new BufferedOutputStream(stream);
            //choose another format if PNG doesn't suit you
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
            bos.flush();
            bos.close();
            stored = "success";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file.getAbsolutePath();
    }

    public static File getImage(String imagename) {

        File mediaImage = null;
        try {
            File myDir = new File(extractFolder);
            if (!myDir.exists())
                return null;
            mediaImage = new File(myDir.getPath() + "/" + imagename);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return mediaImage;
    }

    public static boolean checkifImageExists(String imagename) {
        Bitmap b = null;
        File file = ImageStorage.getImage(imagename);
        if (file == null || file.equals("")) {
            return false;
        }
        String path = file.getAbsolutePath();
        if (path != null)
            b = BitmapFactory.decodeFile(path);
        if (b == null || b.equals("")) {
            return false;
        }
        return true;
    }


}
