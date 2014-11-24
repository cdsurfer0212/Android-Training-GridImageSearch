package com.example.gridimagesearch.widgets;

import java.util.ArrayList;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.gridimagesearch.R;
import com.example.gridimagesearch.helpers.ImageLoader;
import com.example.gridimagesearch.models.ImageResult;

public class ImageResultsAdapter extends ArrayAdapter<ImageResult> {

    private ImageLoader mLoader;
    
    public ImageResultsAdapter(Context context, ArrayList<ImageResult> images) {
        super(context, R.layout.item_image_result, images);
        mLoader = new ImageLoader(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageResult imageInfo = getItem(position); 
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_image_result, parent, false);
        }
        
        ImageView ivImage = (ImageView) convertView.findViewById(R.id.ivImage);
        // Clear out image from last time
        ivImage.setImageResource(0);
        //Picasso.with(getContext()).load(imageInfo.getThumbUrl()).into(ivImage);
        
        TextView tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
        //tvTitle.setText(imageInfo.getTitle());
        tvTitle.setText(Html.fromHtml(imageInfo.getTitle()));
        
        mLoader.DisplayImage(imageInfo.getThumbUrl(), ivImage);
        
        return convertView;
    }
    
    static class ViewHolder {
        ScaleImageView imageView;
    }

}
