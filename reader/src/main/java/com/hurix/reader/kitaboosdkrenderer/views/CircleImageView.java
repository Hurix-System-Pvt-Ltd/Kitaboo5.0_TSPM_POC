package com.hurix.reader.kitaboosdkrenderer.views;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.AttributeSet;


import com.hurix.reader.kitaboosdkrenderer.R;
import com.hurix.reader.kitaboosdkrenderer.constants.Constants;
import com.hurix.reader.kitaboosdkrenderer.utils.ImageUtils;
import com.hurix.reader.kitaboosdkrenderer.utils.Utils;
import com.squareup.picasso.Picasso.LoadedFrom;
import com.squareup.picasso.Target;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import androidx.appcompat.widget.AppCompatImageView;

public class CircleImageView extends AppCompatImageView {

    private static final ScaleType SCALE_TYPE = ScaleType.CENTER_CROP;

    private static final Bitmap.Config BITMAP_CONFIG = Bitmap.Config.ARGB_8888;
    private static final int COLORDRAWABLE_DIMENSION = 1;

    private static final int DEFAULT_BORDER_WIDTH = 0;
    private static final int DEFAULT_BORDER_COLOR = Color.BLACK;

    private final RectF mDrawableRect = new RectF();
    private final RectF mBorderRect = new RectF();

    private final Matrix mShaderMatrix = new Matrix();
    private final Paint mBitmapPaint = new Paint();
    private final Paint mBorderPaint = new Paint();

    private int mBorderColor = DEFAULT_BORDER_COLOR;
    private int mBorderWidth = DEFAULT_BORDER_WIDTH;

    private Bitmap mBitmap;
    private BitmapShader mBitmapShader;
    private int mBitmapWidth;
    private int mBitmapHeight;

    private float mDrawableRadius;
    private float mBorderRadius;

    private boolean mReady;
    private boolean mSetupPending;

    private String mFileExtension, mFilePath;

    public CircleImageView(Context context) {
        super(context);

        init();
    }

    public CircleImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CircleImageView, defStyle, 0);

        mBorderWidth = a.getDimensionPixelSize(R.styleable.CircleImageView_border_width, DEFAULT_BORDER_WIDTH);
        mBorderColor = a.getColor(R.styleable.CircleImageView_border_color, DEFAULT_BORDER_COLOR);

        a.recycle();

        init();
    }

    private void init() {
        super.setScaleType(SCALE_TYPE);
        mReady = true;

        if (mSetupPending) {
            setup();
            mSetupPending = false;
        }
    }

    @Override
    public ScaleType getScaleType() {
        return SCALE_TYPE;
    }

    @Override
    public void setScaleType(ScaleType scaleType) {
        if (scaleType != SCALE_TYPE) {
            throw new IllegalArgumentException(String.format("ScaleType %s not supported.", scaleType));
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (getDrawable() == null) {
            return;
        }

        canvas.drawCircle(getWidth() / 2, getHeight() / 2, mDrawableRadius, mBitmapPaint);
        if (mBorderWidth != 0) {
            canvas.drawCircle(getWidth() / 2, getHeight() / 2, mBorderRadius, mBorderPaint);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        setup();
    }

    public int getBorderColor() {
        return mBorderColor;
    }

    public void setBorderColor(int borderColor) {
        if (borderColor == mBorderColor) {
            return;
        }

        mBorderColor = borderColor;
        mBorderPaint.setColor(mBorderColor);
        invalidate();
    }

    public int getBorderWidth() {
        return mBorderWidth;
    }

    public void setBorderWidth(int borderWidth) {
        if (borderWidth == mBorderWidth) {
            return;
        }

        mBorderWidth = borderWidth;
        setup();
    }

    @Override
    public void setImageBitmap(Bitmap bm) {
        super.setImageBitmap(bm);
        mBitmap = bm;
        setup();
    }

    @Override
    public void setImageDrawable(Drawable drawable) {
        super.setImageDrawable(drawable);
        mBitmap = getBitmapFromDrawable(drawable);
        setup();
    }

    @Override
    public void setImageResource(int resId) {
        super.setImageResource(resId);
        mBitmap = getBitmapFromDrawable(getDrawable());
        setup();
    }

    @Override
    public void setImageURI(Uri uri) {
        super.setImageURI(uri);
        mBitmap = getBitmapFromDrawable(getDrawable());
        setup();
    }

    private Bitmap getBitmapFromDrawable(Drawable drawable) {
        if (drawable == null) {
            return null;
        }

        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }

        try {
            Bitmap bitmap;

            if (drawable instanceof ColorDrawable) {
                bitmap = Bitmap.createBitmap(COLORDRAWABLE_DIMENSION, COLORDRAWABLE_DIMENSION, BITMAP_CONFIG);
            } else {
                bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), BITMAP_CONFIG);
            }

            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);
            return bitmap;
        } catch (OutOfMemoryError e) {
            return null;
        }
    }

    private void setup() {
        if (!mReady) {
            mSetupPending = true;
            return;
        }

        if (mBitmap == null) {
            return;
        }

        mBitmapShader = new BitmapShader(mBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);

        mBitmapPaint.setAntiAlias(true);
        mBitmapPaint.setShader(mBitmapShader);

        mBorderPaint.setStyle(Paint.Style.STROKE);
        mBorderPaint.setAntiAlias(true);
        mBorderPaint.setColor(mBorderColor);
        mBorderPaint.setStrokeWidth(mBorderWidth);

        mBitmapHeight = mBitmap.getHeight();
        mBitmapWidth = mBitmap.getWidth();

        mBorderRect.set(0, 0, getWidth(), getHeight());
        mBorderRadius = Math.min((mBorderRect.height() - mBorderWidth) / 2, (mBorderRect.width() - mBorderWidth) / 2);

        mDrawableRect.set(mBorderWidth, mBorderWidth, mBorderRect.width() - mBorderWidth, mBorderRect.height() - mBorderWidth);
        mDrawableRadius = Math.min(mDrawableRect.height() / 2, mDrawableRect.width() / 2);

        updateShaderMatrix();
        invalidate();
    }

    private void updateShaderMatrix() {
        float scale;
        float dx = 0;
        float dy = 0;

        mShaderMatrix.set(null);

        if (mBitmapWidth * mDrawableRect.height() > mDrawableRect.width() * mBitmapHeight) {
            scale = mDrawableRect.height() / (float) mBitmapHeight;
            dx = (mDrawableRect.width() - mBitmapWidth * scale) * 0.5f;
        } else {
            scale = mDrawableRect.width() / (float) mBitmapWidth;
            dy = (mDrawableRect.height() - mBitmapHeight * scale) * 0.5f;
        }

        mShaderMatrix.setScale(scale, scale);
        mShaderMatrix.postTranslate((int) (dx + 0.5f) + mBorderWidth, (int) (dy + 0.5f) + mBorderWidth);

        mBitmapShader.setLocalMatrix(mShaderMatrix);
    }

    public void setImage(String strUrl, String strUserID, boolean isFromService) {
        if (TextUtils.isEmpty(strUrl)) {
            mBitmap = BitmapFactory.decodeResource(this.getResources(),
                    R.drawable.default_icon);
            setup();
        }

        File sourceFile = getFileIfExists(strUserID);
        if (sourceFile != null) {
            //mBitmap = BitmapFactory.decodeFile(sourceFile.getAbsolutePath());
            try {
                mBitmap =  ImageUtils.handleSamplingAndRotationBitmap(getContext(),Uri.fromFile(sourceFile));
            } catch (IOException e) {
                e.printStackTrace();
            }
            setup();
        }

        if (!TextUtils.isEmpty(strUrl) && strUrl.contains(".")) {
            mFileExtension = strUrl.substring(strUrl.lastIndexOf("."));
            if(mFileExtension.equals(Constants.PROFILE_IMAGE_EXTENSION_OLD)){
                mFileExtension = Constants.PROFILE_IMAGE_EXTENSION;
            }
            mFilePath = Utils.getProfilePicFolderPathCompat() + File.separator + strUserID + mFileExtension;

            if (isFromService) {

               // Picasso.with(getContext()).load(strUrl).into(mTarget);
                new Downloadimage().execute(strUrl);
            }
        }
    }

    class Downloadimage extends AsyncTask<String,Void,Boolean> {
        Bitmap bitmap = null;
        @Override
        protected Boolean doInBackground(String... strings) {
            try {
                // Download Image from URL
                InputStream input = new java.net.URL(strings[0]).openStream();
                // Decode Bitmap
                bitmap = BitmapFactory.decodeStream(input);
                if(bitmap == null){
                  return false;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            saveBitmap(bitmap);
            return true;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            if(aBoolean) {
                mBitmap = bitmap;
                setup();
            }
        }
    }


    private void saveBitmap(Bitmap inputBitmap) {
        try {
            //Convert bitmap to byte array
            ByteArrayOutputStream bos = new ByteArrayOutputStream();

            if (mFileExtension.equalsIgnoreCase("jpg") || mFileExtension.equalsIgnoreCase("jpeg")) {
                inputBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            } else {
                inputBitmap.compress(Bitmap.CompressFormat.PNG, 0, bos);
            }

            byte[] byteArray = bos.toByteArray();

            //create a file to write bitmap data
            File file = new File(mFilePath);
            file.delete();

            file.createNewFile();

            //write the bytes in file
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(byteArray);
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private File getFileIfExists(String strFileName) {
        File returnFile = null;
        try {
            File directory = new File(Utils.getProfilePicFolderPathCompat());
            File[] fileList = directory.listFiles();

            if (fileList.length > 0) {
                for (File file : fileList) {
                    if (file.isFile()) {
                        int dotIndex = file.getName().lastIndexOf(".");
                        String fileName = file.getName().substring(0, dotIndex);
                        if (fileName.equalsIgnoreCase(strFileName)) {
                            returnFile = file;
                            break;
                        }
                    }
                }
            }
        } catch (Exception ex) {
        }
        return returnFile;
    }

    private Target mTarget = new Target() {

        @Override
        public void onPrepareLoad(Drawable arg0) {
            // TODO Auto-generated method stub
        }

        @Override
        public void onBitmapLoaded(Bitmap arg0, LoadedFrom arg1) {
            // TODO Auto-generated method stub
            saveBitmap(arg0);
            mBitmap = arg0;
            setup();
        }

        private void saveBitmap(Bitmap inputBitmap) {
            try {
                //Convert bitmap to byte array
                ByteArrayOutputStream bos = new ByteArrayOutputStream();

                if (mFileExtension.equalsIgnoreCase("jpg") || mFileExtension.equalsIgnoreCase("jpeg")) {
                    inputBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                } else {
                    inputBitmap.compress(Bitmap.CompressFormat.PNG, 0, bos);
                }

                byte[] byteArray = bos.toByteArray();

                //create a file to write bitmap data
                File file = new File(mFilePath);
                file.createNewFile();

                //write the bytes in file
                FileOutputStream fos = new FileOutputStream(file);
                fos.write(byteArray);
                fos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onBitmapFailed(Drawable arg0) {
            // TODO Auto-generated method stub

        }

    };



    private Bitmap getRotateAngle(Bitmap bitmap, String url){
        ExifInterface ei = null;
        try {
            ei = new ExifInterface(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_UNDEFINED);

        Bitmap rotatedBitmap = null;
        switch(orientation) {

            case ExifInterface.ORIENTATION_ROTATE_90:
                rotatedBitmap = rotateImage(bitmap, 90);
                break;

            case ExifInterface.ORIENTATION_ROTATE_180:
                rotatedBitmap = rotateImage(bitmap, 180);
                break;

            case ExifInterface.ORIENTATION_ROTATE_270:
                rotatedBitmap = rotateImage(bitmap, 270);
                break;

            case ExifInterface.ORIENTATION_NORMAL:
            default:
                rotatedBitmap = bitmap;
        }
        return rotatedBitmap;
    }

    public static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
    }
}
