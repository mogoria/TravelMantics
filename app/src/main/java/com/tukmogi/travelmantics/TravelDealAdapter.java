package com.tukmogi.travelmantics;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

public class TravelDealAdapter extends RecyclerView.Adapter<TravelDealAdapter.ViewHolder> {

    private ArrayList<TravelDeal> deals;
    private static final String TAG = "TravelDealAdapter";

    TravelDealAdapter(Context context) {
        deals = new ArrayList<>();
        FirebaseUtil.openFbReference((Activity) context, "traveldeals");
        DatabaseReference mDatabaseRef = FirebaseUtil.myRef;
        ChildEventListener childListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Log.d(TAG, "onChildAdded: called");
                TravelDeal deal = dataSnapshot.getValue(TravelDeal.class);
                deal.setId(dataSnapshot.getKey());
                deals.add(deal);
                notifyItemChanged(deals.size()-1);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Log.d(TAG, "onChildChanged: called: dataSnapShot: " + dataSnapshot);

                for (int i = 0; i < deals.size(); i++) {
                    TravelDeal deal = deals.get(i);
                    if(deal.getId().equals(dataSnapshot.getKey())) {
                        TravelDeal updatedDeal = dataSnapshot.getValue(TravelDeal.class);
                        updatedDeal.setId(dataSnapshot.getKey());
                        deals.set(i, updatedDeal);
                        notifyItemChanged(i);
                        break;
                    }
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        mDatabaseRef.addChildEventListener(childListener);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvDescription, tvPrice;
        ImageView imgDeals;

        ViewHolder(View view) {
            super(view);
            tvTitle = view.findViewById(R.id.tvTitle);
            tvDescription = view.findViewById(R.id.tvDescription);
            tvPrice = view.findViewById(R.id.tvPrice);
            imgDeals = view.findViewById(R.id.deals_image);
        }

        void bind(TravelDeal deal) {
            tvTitle.setText(deal.getTitle());
            tvDescription.setText(deal.getDescription());
            tvPrice.setText(deal.getPrice());
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view = LayoutInflater.from(context)
                .inflate(R.layout.deals_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TravelDeal deal = deals.get(position);
        holder.bind(deal);
    }

    @Override
    public int getItemCount() {
        return deals.size();
    }

}
