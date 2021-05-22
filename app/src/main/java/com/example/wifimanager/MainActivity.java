package com.example.wifimanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private Button button;
    TextView textView;
    WifiInfo wifiInfo;
    WifiManager wifiManager;
    String display;
    TextView signalstr;
    TextView quality;
    int rssi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = (Button) findViewById(R.id.button1);
        textView = (TextView) findViewById(R.id.textView);
        signalstr = (TextView) findViewById(R.id.strength);
        quality = (TextView) findViewById(R.id.quality);

        Button nextButton = (Button) findViewById(R.id.buttonNext);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MainActivity2.class);
                startActivity(intent);
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wifiManager = (WifiManager)getSystemService(Context.WIFI_SERVICE);
                wifiInfo = wifiManager.getConnectionInfo();

                String ssid = wifiInfo.getSSID();
                int speed = wifiInfo.getLinkSpeed();

                double freq = wifiInfo.getFrequency()/1000.0;
                String mac = wifiInfo.getMacAddress();
                int ip = wifiInfo.getIpAddress() - 34533;
                rssi = wifiInfo.getRssi();

                display = "SSID: "+ssid + "\n" + "Speed: "+speed+" mbps\n"+"Frequency: "+freq+" GHz\n"+"Mac Address: "+mac+"\n"
                        +"Ip Address: "+ip;
                textView.setText(display);
                updateSignal(rssi);

                new Timer().scheduleAtFixedRate(new TimerTask() {
                    @Override
                    public void run() {

                        updateSignal(rssi);
                        wifiManager = (WifiManager)getSystemService(Context.WIFI_SERVICE);
                        wifiInfo = wifiManager.getConnectionInfo();
                        rssi = wifiInfo.getRssi();

                    }
                },0,1000);
            }
        });
    }

    public void updateSignal(int rss){
        if(rss < -70){
            quality.setText("Weak");
            quality.setTextColor(getColor(R.color.poor));
        }else if(rss < -60 && rss > -70){
            quality.setText("Fair");
            quality.setTextColor(getColor(R.color.fair));
        }else if(rss < -50 && rss > -60){
            quality.setText("Good");
            quality.setTextColor(getColor(R.color.good));
        }else if (rss > -50){
            quality.setText("Excellent");
            quality.setTextColor(getColor(R.color.excellent));
        }
        String display = rss + " dB";
        signalstr.setText(display);
    }

}