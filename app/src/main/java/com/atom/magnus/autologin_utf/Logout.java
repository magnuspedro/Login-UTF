package com.atom.magnus.autologin_utf;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * Created by magnus on 3/11/16.
 */
class Logout extends AsyncTask<String, Void, Void> {
    private static Context context;
    private static Activity activity;

    Logout(Context context, Activity activity){
        this.context = context;
        this.activity = activity;
    }



    @Override
    protected Void doInBackground(String... params) {
        try {
            TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return null;
                        }

                        public void checkClientTrusted(
                                java.security.cert.X509Certificate[] certs, String authType) {
                        }

                        public void checkServerTrusted(
                                java.security.cert.X509Certificate[] certs, String authType) {
                        }
                    }
            };

            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());



            URL url = new URL("https://1.1.1.1/logout.html");
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            String urlParameters = "userStatus=1&err_flag=0&err_msg=";
            connection.setRequestMethod("POST");
            connection.setRequestProperty("USER-AGENT", "Mozilla/5.0");
            connection.setRequestProperty("ACCEPT-LANGUAGE", "en-US,en;0.5");
            connection.setDoOutput(true);
            DataOutputStream dStream = new DataOutputStream(connection.getOutputStream());
            dStream.writeBytes(urlParameters);
            dStream.flush();
            dStream.close();
            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line = "";
            final StringBuilder responseOutput = new StringBuilder();
            while ((line = br.readLine()) != null) {
                responseOutput.append(line);
            }
            br.close();

            activity.runOnUiThread(new Runnable() {

                @Override

                //Mudar esse modo de verificar conexão
                public void run() {

                    if (responseOutput.toString() != null)
                        Toast.makeText(context, "Logout Successful", Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(context, "Logout Fail", Toast.LENGTH_SHORT).show();
                }

            });


        } catch (MalformedURLException e) {

            Log.e("ERROR", e.toString());

        } catch (IOException e)
        {
            Log.e("ERROR", e.toString());
        } catch (Exception e) {
        }

        return null;
    }
}