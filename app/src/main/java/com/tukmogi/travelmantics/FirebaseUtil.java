package com.tukmogi.travelmantics;

import android.app.Activity;

import androidx.annotation.NonNull;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FirebaseUtil {

    private static final int RC_SIGN_IN = 254;
    private static FirebaseUtil firebaseUtil;
    public static FirebaseAuth mFirebaseAuth;
    private static FirebaseAuth.AuthStateListener mAuthListener;
    private static Activity caller;
    public static FirebaseDatabase database;
    public static DatabaseReference myRef;
    public static FirebaseStorage mStorage;
    public static StorageReference mStorageRef;
    public static ArrayList<TravelDeal> travelDeals;

    private FirebaseUtil() {}

    public static void openFbReference(Activity callerActivity, String pathString) {
        caller = callerActivity;
        if(firebaseUtil == null) {
            firebaseUtil = new FirebaseUtil();
            FirebaseApp.initializeApp(caller);
        }
        travelDeals = new ArrayList<>();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference().child(pathString);
        mStorage = FirebaseStorage.getInstance();
        mStorageRef = mStorage.getReference().child("deals_pictures");
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


    public static void attachListener() {
        mFirebaseAuth.addAuthStateListener(mAuthListener);
    }

    public static void dettachListener() {
        mFirebaseAuth.removeAuthStateListener(mAuthListener);
    }

    public static void logout() {
        AuthUI.getInstance().signOut(caller).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                FirebaseUtil.attachListener();
            }
        });
        FirebaseUtil.dettachListener();
    }
}
