package com.tukmogi.travelmantics;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class DealsActivity extends AppCompatActivity {

    private TextInputEditText txtTitle, txtDescription, txtPrice;
    private TravelDeal deal;
    private ImageView imageView;
    private static final String TAG = "DealsActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deals);

        deal = new TravelDeal();

        txtTitle = findViewById(R.id.edTitle);
        txtDescription = findViewById(R.id.edDescription);
        txtPrice = findViewById(R.id.edPrice);
        Button btnPic = findViewById(R.id.btnSelectImage);
        imageView = findViewById(R.id.imgInsertDeal);

        btnPic.setOnClickListener(onClickListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.insert_activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_menu:
                saveTravelDeal();
                clean();
                return true;
            case R.id.logout_menu:
                FirebaseUtil.logout();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==254 && resultCode==RESULT_OK) {
            Uri imageUri = data.getData();
            final StorageReference ref = FirebaseUtil.mStorageRef.child(imageUri.getLastPathSegment());
            ref.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Log.d(TAG, "onSuccess: uri: " + uri);
                            deal.setImageUrl(uri.toString());
                            showImage(uri);
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e(TAG, "onFailure: ", e);
                }
            });


        }
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            getImage();
        }
    };

    private void saveTravelDeal() {
        String title;
        String description;
        String price;

        title = txtTitle.getText().toString();
        description = txtDescription.getText().toString();
        price = txtPrice.getText().toString();

        // deal = new TravelDeal(title, description,price,"");
        deal.setTitle(title);
        deal.setDescription(description);
        deal.setPrice(price);
        FirebaseUtil.myRef.push().setValue(deal);
    }

    private void clean() {
        txtTitle.setText("");
        txtDescription.setText("");
        txtPrice.setText("");
        txtTitle.requestFocus();
    }

    private void getImage() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/jpeg");
        intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
        startActivityForResult(intent.createChooser(intent,
                "Insert picture"), 254);
    }

    public void showImage(Uri imageUri) {
        Picasso.get()
                .load(imageUri)
                .into(imageView);
    }
}
