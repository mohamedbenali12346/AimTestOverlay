package com.example.aimtestoverlay;

import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.widget.*;

public class MainActivity extends Activity {
    @Override public void onCreate(Bundle b) {
        super.onCreate(b);
        LinearLayout l = new LinearLayout(this);
        l.setOrientation(LinearLayout.VERTICAL);
        l.setPadding(24,32,24,24);
        TextView title = new TextView(this); title.setText("AimTest Overlay"); title.setTextSize(26);
        l.addView(title);
        TextView info = new TextView(this);
        info.setText("زر عائم تجريبي يعرض حركة بصرية فقط: 150px خلال 0.2 ثانية. لا يرسل لمساً ولا يتحكم في أي لعبة.");
        info.setTextSize(16); info.setPadding(0,24,0,24); l.addView(info);
        Button permission = new Button(this); permission.setText("السماح بالظهور فوق التطبيقات");
        l.addView(permission);
        Button start = new Button(this); start.setText("تشغيل الزر العائم"); l.addView(start);
        Button stop = new Button(this); stop.setText("إيقاف الزر العائم"); l.addView(stop);
        permission.setOnClickListener(v -> {
            if (!Settings.canDrawOverlays(this)) {
                Intent i = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:"+getPackageName()));
                startActivity(i);
            } else Toast.makeText(this,"الإذن مفعّل",Toast.LENGTH_SHORT).show();
        });
        start.setOnClickListener(v -> {
            if (!Settings.canDrawOverlays(this)) {
                Toast.makeText(this,"فعّل إذن الظهور فوق التطبيقات أولاً",Toast.LENGTH_LONG).show();
                return;
            }
            startService(new Intent(this, OverlayService.class));
            Toast.makeText(this,"الزر العائم يعمل",Toast.LENGTH_SHORT).show();
        });
        stop.setOnClickListener(v -> stopService(new Intent(this, OverlayService.class)));
        setContentView(l);
    }
}
