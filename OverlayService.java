package com.example.aimtestoverlay;

import android.app.Service;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.view.*;
import android.view.animation.LinearInterpolator;
import android.widget.*;

public class OverlayService extends Service {
    private WindowManager wm;
    private View bubble;
    private TextView marker;
    private WindowManager.LayoutParams bubbleParams, markerParams;

    @Override public void onCreate() {
        super.onCreate();
        wm=(WindowManager)getSystemService(WINDOW_SERVICE);
        int type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        bubble = new TextView(this);
        ((TextView)bubble).setText("↑");
        ((TextView)bubble).setTextSize(24);
        ((TextView)bubble).setTextColor(Color.WHITE);
        ((TextView)bubble).setGravity(Gravity.CENTER);
        bubble.setBackgroundColor(Color.rgb(155,95,55));
        bubbleParams = new WindowManager.LayoutParams(58,58,type,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE|WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
            PixelFormat.TRANSLUCENT);
        bubbleParams.gravity=Gravity.TOP|Gravity.END;
        bubbleParams.x=24; bubbleParams.y=260;
        wm.addView(bubble,bubbleParams);

        bubble.setOnTouchListener(new View.OnTouchListener() {
            float downX,downY; int startX,startY; long downTime;
            public boolean onTouch(View v, MotionEvent e) {
                if(e.getAction()==MotionEvent.ACTION_DOWN) {
                    downX=e.getRawX(); downY=e.getRawY(); startX=bubbleParams.x; startY=bubbleParams.y; downTime=System.currentTimeMillis(); return true;
                }
                if(e.getAction()==MotionEvent.ACTION_MOVE) {
                    bubbleParams.x=Math.max(0,startX+(int)(downX-e.getRawX()));
                    bubbleParams.y=Math.max(0,startY+(int)(e.getRawY()-downY));
                    wm.updateViewLayout(bubble,bubbleParams); return true;
                }
                if(e.getAction()==MotionEvent.ACTION_UP) {
                    if(System.currentTimeMillis()-downTime<250 && Math.abs(e.getRawX()-downX)<12 && Math.abs(e.getRawY()-downY)<12) animateMarker();
                    return true;
                }
                return true;
            }
        });
    }

    private void animateMarker() {
        if(marker!=null) { try { wm.removeView(marker); } catch(Exception ignored) {} }
        marker=new TextView(this);
        marker.setText("＋"); marker.setTextSize(30); marker.setTextColor(Color.WHITE); marker.setGravity(Gravity.CENTER);
        marker.setBackgroundColor(Color.rgb(40,135,85));
        markerParams=new WindowManager.LayoutParams(54,54,WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE|WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE|WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
            PixelFormat.TRANSLUCENT);
        markerParams.gravity=Gravity.TOP|Gravity.START;
        markerParams.x= bubbleParams.x + 40;
        markerParams.y= bubbleParams.y + 260;
        wm.addView(marker,markerParams);
        marker.animate().translationY(-150).setDuration(200).setInterpolator(new LinearInterpolator())
            .withEndAction(() -> marker.postDelayed(() -> {
                if(marker!=null) { try { wm.removeView(marker); } catch(Exception ignored) {} marker=null; }
            },500)).start();
    }

    @Override public int onStartCommand(Intent i,int flags,int id) { return START_NOT_STICKY; }
    @Override public void onDestroy() {
        if(marker!=null) try { wm.removeView(marker); } catch(Exception ignored) {}
        if(bubble!=null) try { wm.removeView(bubble); } catch(Exception ignored) {}
        marker=null; bubble=null; super.onDestroy();
    }
    @Override public IBinder onBind(Intent i) { return null; }
}
