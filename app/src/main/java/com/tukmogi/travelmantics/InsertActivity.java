package com.tukmogi.travelmantics;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.android.material.textfield.TextInputEditText;

public class InsertActivity extends AppCompatActivity {

    private TextInputEditText txtTitle, txtDescription, txtPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert);

        txtTitle = findViewById(R.id.edTitle);
        txtDescription = findViewById(R.id.edDescription);
        txtPrice = findViewById(R.id.edPrice);
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

    private void saveTravelDeal() {
        String title;
        String description;
        String price;

        title = txtTitle.getText().toString();
        description = txtDescription.getText().toString();
        price = txtPrice.getText().toString();

        TravelDeal travelDeal = new TravelDeal(title, description,price,"");
        FirebaseUtil.myRef.push().setValue(travelDeal);
    }

    private void clean() {
        txtTitle.setText("");
        txtDescription.setText("");
        txtPrice.setText("");
        txtTitle.requestFocus();
    }
}
