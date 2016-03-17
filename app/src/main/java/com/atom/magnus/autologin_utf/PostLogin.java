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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * Created by magnus on 3/13/16.
 */
class PostLogin extends AsyncTask<String, Void, Void> {
    public static Context  context;
    private static Activity activity;
    private String _RA;
    private String _Senha;

    PostLogin(String RA, String Senha, Context context, Activity activity){
        this._RA = RA;
        this._Senha = Senha;
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


            URL url = new URL("https://1.1.1.1/login.html");
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            String urlParameters = "buttonClicked=4&err_flag=0&err_msg=&info_flag=0&info_msg=&redirect_url=&username=" + _RA + "&password=" + _Senha;
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
                public void run() {

                    Matcher m = Pattern.compile("Successful").matcher(responseOutput);
                    while (m.find()) {
                        if (m.group() != null)
                            Toast.makeText(context, "Login Successful", Toast.LENGTH_SHORT).show();
                    }
                }
            });


        } catch (MalformedURLException e) {
            Log.e("Error",e.toString());
        } catch (IOException e) {
            Log.e("Error", e.toString());
        }catch (Exception e) {
        }
        return null;
    }

}
