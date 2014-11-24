package com.example.gridimagesearch.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.gridimagesearch.R;
import com.example.gridimagesearch.models.ImageResult;
import com.example.gridimagesearch.widgets.TouchImageView;
import com.squareup.picasso.Picasso;

public class ImageDisplayActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_display);
        
        // Remove the action bar on the image display activity 
        getActionBar().hide();
        
        //String url = getIntent().getStringExtra("url");
        ImageResult result = (ImageResult) getIntent().getSerializableExtra("result");
        //ImageView ivImageResult = (ImageView) findViewById(R.id.ivImageResult);
        TouchImageView tivImageResult = (TouchImageView) findViewById(R.id.tivImageResult);
        Picasso.with(this).load(result.getFullUrl()).into(tivImageResult);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.image_display, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
