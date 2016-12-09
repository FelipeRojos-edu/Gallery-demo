package com.edu.rojos.felipe.gallerybase;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.GridView;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public final static String BASE_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/MemeticaMe";

    private GridView gridView;
    private GridViewAdapter gridAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gridView = (GridView) findViewById(R.id.gridView);
        gridAdapter = new GridViewAdapter(this, R.layout.grid_item_layout, getPictures());
        gridView.setAdapter(gridAdapter);
    }

    private ArrayList<Gallery> getPictures() {
        final ArrayList<Gallery> galleries = new ArrayList<>();
        String path = BASE_PATH;
        File directory = new File(path);
        if (!directory.exists()) {
            if (!directory.mkdirs()){
                Log.e("DIRECTORY","Problems with directory creation");
            }
            else
                Log.d("DIRECTORY","Creation done");

        }

        File[] files = directory.listFiles();
        for (int i = 0; i < files.length; i++)
        {
            if(files[i].isDirectory())
                continue;
            Log.d("Files", "FileName:" + files[i].getName());
            Log.d("Files", "Mime:" +getMimeType(files[i].getAbsolutePath()));
            Bitmap bb=null;
            String mime="";

            if (files[i].getName().contains("mma") )
                mime="fotoaudio";
            else
                mime=getMimeType(files[i].getAbsolutePath()).split("/")[0];
            if (mime.equals("application")){
                mime=getMimeType(files[i].getAbsolutePath());
            }
            final int THUMBSIZE = 100;
            if(mime.equals("image"))
            {
                BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                bb = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(files[i].getAbsolutePath(),bmOptions),
                        THUMBSIZE, THUMBSIZE);

            }

            else if(mime.equals("video")) {
                bb=ThumbnailUtils.createVideoThumbnail(Uri.fromFile(files[i]).getPath(), MediaStore.Images.Thumbnails.MICRO_KIND);
            }


            galleries.add(new Gallery(bb,files[i].getName(),mime,Uri.fromFile(files[i])));

        }

        return galleries;
    }

    public static String getMimeType(String url) {
        String type = null;
        String extension = MimeTypeMap.getFileExtensionFromUrl(url);
        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        }
        return type;
    }

}
