package com.hyperdroidclient.auth;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.EditText;

import com.hyperdroidclient.R;
import com.hyperdroidclient.common.BaseActivity;
import com.hyperdroidclient.dashboard.MainActivity;
import com.hyperdroidclient.data.local.SharedPreferenceManager;
import com.hyperdroidclient.widgets.BaseButton;
import com.hyperdroidclient.widgets.BaseTextView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.GET_ACCOUNTS;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

/**
 * Created by Archish on 10/16/2017.
 */

public class LoginActivity extends BaseActivity {
    EditText etEmailID;
    EditText etPassword;
    BaseButton bLogin;
    BaseTextView tvRegister;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestPermission();
        if (new SharedPreferenceManager(getApplicationContext()).getMainPage() != 0) {
            Intent i = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(i);
            finish();
        }

        setContentView(R.layout.activity_login);
        initViews();
    }

    private void initViews() {
        etEmailID = (EditText) findViewById(R.id.etEmailId);
        etPassword = (EditText) findViewById(R.id.etPassword);
        tvRegister = (BaseTextView) findViewById(R.id.tvRegister);
        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(i);
                finish();
            }
        });
        bLogin = (BaseButton) findViewById(R.id.bLogin);
        bLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int check = validation();
                if (check == 0) {
                    new SharedPreferenceManager(getApplicationContext()).saveMainPage(1);
                    Intent i = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(i);
                    finish();
                    //TODO do Login
                } else if (check == 1) {
                    etEmailID.setError("Email ID cannot be empty");
                    etEmailID.setFocusable(true);
                } else if (check == 2) {
                    etPassword.setError("Password cannot be empty");
                    etPassword.setFocusable(true);
                } else if (check == 3) {
                    etEmailID.setError("Invalid EmailID");
                    etEmailID.setFocusable(true);
                }
            }
        });
    }

    private int validation() {
        String email = etEmailID.getText().toString();
        String password = etPassword.getText().toString();
        Pattern VALID_EMAIL_ADDRESS_REGEX =
                Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(email);

        if (email.isEmpty())
            return 1;
        else if (password.isEmpty())
            return 2;
        else if (!matcher.find())
            return 3;
        return 0;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void requestPermission() {

        ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION, CAMERA, READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE, GET_ACCOUNTS}, 1001);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1001:
                if (grantResults.length > 0) {


                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (shouldShowRequestPermissionRationale(ACCESS_FINE_LOCATION)) {
                            showMessageOKCancel("You need to allow access all the permissions",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                requestPermissions(new String[]{ACCESS_FINE_LOCATION, CAMERA, READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE, GET_ACCOUNTS},
                                                        1001);
                                            }
                                        }
                                    });
                            return;
                        }
                    }

                }
                break;
        }
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(LoginActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

}
