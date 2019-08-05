package com.tukmogi.travelmantics;

import android.app.Activity;

import androidx.annotation.NonNull;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.List;

public class FirebaseUtil {

    static final int RC_SIGN_IN = 254;
    private static FirebaseUtil firebaseUtil;
    static FirebaseAuth mFirebaseAuth;
    private static FirebaseAuth.AuthStateListener mAuthListener;
    private static Activity caller;

    private FirebaseUtil() {}

    static void openFbReference(Activity callerActivity) {
        caller = callerActivity;
        if(firebaseUtil == null) {
            firebaseUtil = new FirebaseUtil();
            FirebaseApp.initializeApp(caller);
            mFirebaseAuth = FirebaseAuth.getInstance();
            mAuthListener = new FirebaseAuth.AuthStateListener() {
                @Override
                public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                    FirebaseUser user = firebaseAuth.getCurrentUser();
                    if (user==null) {
                        FirebaseUtil.signIn();
                    }
                }
            };
        }
    }

    private static void signIn() {
        // Choose authentication providers
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build());

        // Create and launch sign-in intent
        caller.startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .build(),
                RC_SIGN_IN);
    }


    static void attachListener() {
        mFirebaseAuth.addAuthStateListener(mAuthListener);
    }

    static void dettachListener() {
        mFirebaseAuth.removeAuthStateListener(mAuthListener);
    }
}
