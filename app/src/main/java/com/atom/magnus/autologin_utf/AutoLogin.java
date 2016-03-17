package com.atom.magnus.autologin_utf;

import android.app.Activity;
import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class AutoLogin extends AppCompatActivity {

    int _ID;
    String _RA;
    String _Senha;
    EditText RA;
    EditText Senha;
    Button Login;
    private static final int SWIPE_MIN_DISTANCE = 120;
    private static final int SWIPE_THRESHOLD_VELOCITY = 200;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auto_login);


        final ImageView imageView = (ImageView) findViewById(R.id.Img);
        final GestureDetector gdt = new GestureDetector(new GestureListener());

        imageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(final View view, final MotionEvent event) {
                gdt.onTouchEvent(event);
                return true;
            }
        });

        RA = (EditText) findViewById(R.id.RA);
        Senha = (EditText) findViewById(R.id.Senha);
        Login = (Button) findViewById(R.id.Login);

        final DatabaseHandler db = new DatabaseHandler(this);
        setValues();

        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WifiManager wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
                if (wifi.isWifiEnabled()) {
                    if(RA.getText().toString().equals("") || Senha.getText().toString().equals("")){
                        Toast.makeText(getApplicationContext(), "Campo Vazio", Toast.LENGTH_SHORT).show();
                    }else{
                        if(getDbCount() <= 0) {
                            db.AddUser(new User(RA.getText().toString(), Senha.getText().toString()));
                            setValues();
                        }

                        if(!(_RA.equals(RA.getText().toString()) || _Senha.equals(Senha.getText().toString()))){
                            _RA = RA.getText().toString();
                            _Senha = Senha.getText().toString();
                            db.updateUser(new User(_ID,_RA,_Senha));
                        }


                        PostLogin postLogin = new PostLogin(_RA,_Senha,AutoLogin.this,AutoLogin.this);
                        postLogin.execute();
                        Log.e("DBC",getDbCount()+"");
                    }
                }
            }

        });

    }



    private class GestureListener extends GestureDetector.SimpleOnGestureListener {
        int ct = 0;

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {


            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    ct = 0;
                }
            }, 3000);



            if(e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                return false; // Right to left
            }  else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                if(ct <=0) {
                    Toast.makeText(getApplicationContext(), "Swipe Again to Logout", Toast.LENGTH_SHORT).show();
                    ct++;
                }else {
                    Logout logout = new Logout(AutoLogin.this, AutoLogin.this);
                    logout.execute();
                    ct = 0;
                }
            }

            if(e1.getY() - e2.getY() > SWIPE_MIN_DISTANCE && Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY) {
                return false; // Bottom to top
            }  else if (e2.getY() - e1.getY() > SWIPE_MIN_DISTANCE && Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY) {
                return false; // Top to bottom
            }
            return false;
        }
    }





    private int getDbCount(){
        DatabaseHandler db = new DatabaseHandler(this);
        return db.getUserCount();
    }

    private void setValues(){
        DatabaseHandler db = new DatabaseHandler(this);
        List<User> users = db.getLastUser();
        if(getDbCount() > 0){
            for (User us : users){
                _ID = us.getID();
                _RA = us.getRA();
                _Senha = us.getSenha();
            }
            RA.setText(_RA);
            Senha.setText(_Senha);
            //PostLogin postLogin = new PostLogin(_RA,_Senha,AutoLogin.this,AutoLogin.this);
            //postLogin.execute();
        }
    }
}

