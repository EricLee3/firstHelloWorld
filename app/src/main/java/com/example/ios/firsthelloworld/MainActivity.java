package com.example.ios.firsthelloworld;


import android.content.Context;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;
import android.os.AsyncTask;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.ProtocolException;
import java.io.IOException;
import java.io.InputStream;


import static android.provider.ContactsContract.CommonDataKinds.Website.URL;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Switch toggle = (Switch) findViewById(R.id.wifi_switch);
        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    toggleWiFi(true);
                    //    Toast.makeText(getApplicationContext(), R.string.wifi_start, Toast.LENGTH_LONG).show();
                } else {
                    toggleWiFi(false);
                    //    Toast.makeText(getApplicationContext(), R.string.wifi_end, Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public class NetworkTask extends AsyncTask {
        String strUrl = null;
        String strCookie = null;
        String result = null;

        URL Url = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            strUrl = "http://192.168.30.35:9080/delegateAndroidDao/testSelect.jsp?command=LoadData";
        }

        @Override
        protected Object doInBackground(Object[] params) {
            try {
                Url = new URL(strUrl);
                HttpURLConnection conn = (HttpURLConnection) Url.openConnection();
                conn.setRequestMethod("GET");
                conn.setDoOutput(true);
                conn.setDoInput(true);
                conn.setUseCaches(false);
                conn.setDefaultUseCaches(false);
                strCookie = conn.getHeaderField("Set-Cookie");
                InputStream is = conn.getInputStream();

                StringBuilder builder = new StringBuilder();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                String line;

                while ((line = reader.readLine()) != null) {
                    builder.append(line + "\n");
                }
                result = builder.toString();
                Log.d("builder is: ", result);

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException io) {
                io.printStackTrace();
            }
            return result;
        }


        protected void onPostExecute(String s) {
            Log.d("HTTP_RESULT", s);
        }
    }

    public void btnStart(View v) {
        NetworkTask networkTask = new NetworkTask();
        networkTask.execute("");
    }

    public void toggleWiFi(boolean status) {
        WifiManager wifiManager = (WifiManager) this.getSystemService(Context.WIFI_SERVICE);
        if (status && !wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(true);
        } else if (!status && wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(false);
        }
    }

}
