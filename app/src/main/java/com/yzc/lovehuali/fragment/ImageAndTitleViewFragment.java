package com.yzc.lovehuali.fragment;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Gallery.LayoutParams;

import com.yzc.lovehuali.NewsDetailsActivity;
import com.yzc.lovehuali.R;
import com.yzc.lovehuali.tool.ACache;

import android.widget.ViewSwitcher;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * A simple {@link Fragment} subclass.
 */
public class ImageAndTitleViewFragment extends Fragment {

    private String uri;
    private String title;
    private ImageSwitcher ivNewsPic;
    private TextView tvNewsName;
    private Bitmap PicBitmap;
    private Intent intent;

    private ACache mCache;

    public ImageAndTitleViewFragment(String uri,String title,Intent intent) {
        this.uri = uri;
        this.title = title;
        this.intent =intent;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_image_and_title_view, container, false);
        ivNewsPic = (ImageSwitcher) rootView.findViewById(R.id.ivNewsPic);
        ivNewsPic.setFactory(new ViewSwitcher.ViewFactory() {

            @Override
            public View makeView() {
                ImageView imageView = new ImageView(getActivity());
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView.setLayoutParams(new ImageSwitcher.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
                return imageView;
            }
        });
        ivNewsPic.setImageResource(R.drawable.image_loading_pic);


        tvNewsName = (TextView) rootView.findViewById(R.id.tvNewsName);
        tvNewsName.setText(title);

        mCache = ACache.get(getActivity());

        if(mCache.getAsBitmap(uri)!=null){
            Drawable dw = new BitmapDrawable(mCache.getAsBitmap(uri));
            ivNewsPic.setImageDrawable(dw);
        }else {
            getImageByUri(uri);
        }

        rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.setClass(getActivity(), NewsDetailsActivity.class);
                startActivity(intent);
            }
        });

        return rootView;
    }
    public void getImageByUri(String Url){
        new AsyncTask<String, Void, Void>() {

            @Override
            protected Void doInBackground(String... params) {

                URL PicUrl  = null;
                try {
                    PicUrl = new URL(params[0]);
                    InputStream Picis = PicUrl.openConnection().getInputStream();
                    BufferedInputStream Picbis = new BufferedInputStream(Picis);
                    PicBitmap = BitmapFactory.decodeStream(Picbis);
                    Picis.close();
                    Picis.close();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }


                return null;
            }
            protected void onPostExecute(Void result) {
                Drawable dw = new BitmapDrawable(PicBitmap);
                ivNewsPic.setImageDrawable(dw);
                mCache.put(uri,PicBitmap,ACache.TIME_DAY*20);

            }

        }.execute(Url);

    }


}
