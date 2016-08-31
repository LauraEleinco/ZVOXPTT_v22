package com.diex.communityhelp;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import android.content.pm.Signature;


public class Login extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;
    private LoginButton loginButton;

    private CallbackManager callbackManager;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //initialize facebook sdk
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.id);
        //initialize callback manager
        callbackManager = CallbackManager.Factory.create();

        //loginButton.setReadPermissions("user_friends");
        loginButton = (LoginButton)findViewById(R.id.connectWithFbButton);
        printKeyHash();

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                String result = "\"User ID: \"\n" +
                        "    + loginResult.getAccessToken().getUserId() \n" +
                        "  + \"\\n\" +\n" +
                        "    \"Auth Token: \"\n" +
                        "    + loginResult.getAccessToken().getToken()\n";
            }

            @Override
            public void onCancel() {
                String result ="Login attempt canceled.";
            }

            @Override
            public void onError(FacebookException e) {
                String result = "Login attempt failed.";
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }


    private void printKeyHash(){
        // Add code to print out the key hash
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.diex.communityhelp",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.d("KeyHash:", e.toString());
        } catch (NoSuchAlgorithmException e) {
            Log.d("KeyHash:", e.toString());
        }
    }
}
