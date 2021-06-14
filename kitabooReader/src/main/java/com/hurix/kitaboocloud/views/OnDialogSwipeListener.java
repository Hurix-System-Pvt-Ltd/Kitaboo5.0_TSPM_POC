package com.hurix.kitaboocloud.views;

import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;


/**
 * Created by Dhananjay.Talekar on 22/8/2019.
 */

public class OnDialogSwipeListener implements View.OnTouchListener {

    public final GestureDetector gestureDetector;

    public OnDialogSwipeListener(Context ctx){
        gestureDetector = new GestureDetector(ctx, new GestureListener());
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return false;
    }

    private final class GestureListener extends GestureDetector.SimpleOnGestureListener {

        private static final int SWIPE_DISTANCE_THRESHOLD = 100;
        private static final int SWIPE_VELOCITY_THRESHOLD = 100;
        private static final long VELOCITY_THRESHOLD = 3000;

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }


      @Override
      public boolean onFling(final MotionEvent e1, final MotionEvent e2,
                             final float velocityX,
                             final float velocityY){

          if(Math.abs(velocityX) < VELOCITY_THRESHOLD
                  && Math.abs(velocityY) < VELOCITY_THRESHOLD){
              return false;//if the fling is not fast enough then it's just like drag
          }

          //if velocity in X direction is higher than velocity in Y direction,
          //then the fling is horizontal, else->vertical
          if(Math.abs(velocityX) > Math.abs(velocityY)){
              if(velocityX >= 0){
                  //Log.i("TAG", "swipe right");
                  onSwipeRight();
              }else{//if velocityX is negative, then it's towards left
                 // Log.i("TAG", "swipe left");
                  onSwipeLeft();
              }
          }else{
              if(velocityY >= 0){
                //  Log.i("TAG", "swipe down");
                  onSwipeBottom();
                  return false;
              }else{
                 // Log.i("TAG", "swipe up");
                  onSwipeTop();
                  return false;
              }
          }

          return true;
      }

    }

    public void onSwipeRight() {
    }

    public void onSwipeLeft() {
    }
    public void onSwipeTop() {
    }

    public void onSwipeBottom() {
    }
}
