package com.ibrahim.soleeklabtask;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ahmadrosid.svgloader.SvgLoader;
import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter <RecyclerViewAdapter.MyViewHolder> {
    private static final String TAG = "RecyclerViewAdapter";

    ArrayList<Country> mCountryArrayList;
    Activity mContext;

//    public RecyclerViewAdapter(ArrayList<Country> countryArrayList , Context context) {
//        mCountryArrayList = countryArrayList;
//    }


    public RecyclerViewAdapter(ArrayList<Country> countryArrayList, Activity context) {
        mCountryArrayList = countryArrayList;
        mContext = context;
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView countryName ;
        TextView countryCapital ;
        TextView countryRegion ;
        ImageView mImageView ;
        public MyViewHolder( View itemView) {
            super(itemView);

            countryName = itemView.findViewById(R.id.countryNameTextView);
            countryCapital = itemView.findViewById(R.id.countryCapitalTextView);
            countryRegion = itemView.findViewById(R.id.countryRegionTextView);
            mImageView = itemView.findViewById(R.id.countryImageView);

        }
    }
    @NonNull
    @Override
    public RecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.country_view , viewGroup , false);

        MyViewHolder viewHolder = new MyViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int i) {
        Country currentCountry = mCountryArrayList.get(i);
        holder.countryName.setText( currentCountry.getCountryName());
        holder.countryCapital.setText("Capital : "+currentCountry.getCountryCapital());
        holder.countryRegion.setText("Region :\n "+currentCountry.getCountryRegion());

        Log.d(TAG, "onBindViewHolder: imageUrl"+currentCountry.getCountryImageUrl());

//        //set country image
//        Picasso.get()
//                .load("https://image.shutterstock.com/image-vector/football-arena-field-bright-stadium-260nw-760077748.jpg")
//                .into(holder.mImageView);

        //set country image
        //https://github.com/ar-android/AndroidSvgLoader
        SvgLoader.pluck()
                .with(mContext)
                .setPlaceHolder(R.mipmap.ic_launcher, R.mipmap.ic_launcher)
                .load(currentCountry.getCountryImageUrl(), holder.mImageView);

    }


    @Override
    public int getItemCount() {
        return mCountryArrayList.size();
    }
}
