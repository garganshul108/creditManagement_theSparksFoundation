package com.example.creditmanagement.Main;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.example.creditmanagement.Main.Users.MainActivity;
import com.example.creditmanagement.R;

public class FlashPage extends AppCompatActivity implements Runnable{

    LinearLayout flash_text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flash_page);
        flash_text = findViewById(R.id.flash_text);
        AnimationUtil.animateView( flash_text , true);
        Thread main = new Thread(this);
        main.start();

    }

    @Override
    public void run() {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        finish();
        startActivity(new Intent(FlashPage.this , MainActivity.class));
    }
}
