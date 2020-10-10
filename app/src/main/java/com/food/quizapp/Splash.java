package com.food.quizapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

public class Splash extends AppCompatActivity {

    InterstitialAd mInterstitialAd;
    int c = 1
            ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
      //  System.out.println("sdas");

        Button button = findViewById(R.id.button);
        ImageView imageView = findViewById(R.id.appicon);
        imageView.animate().rotationBy(360).setDuration(1000).start();

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getString(R.string.inter_id));
        mInterstitialAd.loadAd(new AdRequest.Builder().build());


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(c == 0){
                    if (mInterstitialAd.isLoaded()) {
                        mInterstitialAd.show();
                        c++;

                    }
                }
                else {
                    startActivity(new Intent(getApplicationContext(),MainActivity.class));

                }

            }
        });
    }

}
