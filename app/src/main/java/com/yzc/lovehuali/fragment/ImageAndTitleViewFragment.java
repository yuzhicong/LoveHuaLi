package com.yzc.lovehuali.fragment;


import android.annotation.SuppressLint;
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

    private ImageSwitcher ivNewsPic;
    private TextView tvNewsName;
    private Bitmap PicBitmap;
    private View rootView;

    private ACache mCache;


    public static Fragment newInstance(String uri,String title,String context,String publishDate){
        ImageAndTitleViewFragment fragment = new ImageAndTitleViewFragment();
        Bundle bundle = new Bundle();
        bundle.putString("uri", uri);
        bundle.putString("title",title);
        bundle.putString("context", context);
        bundle.putString("publishDate",publishDate);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_image_and_title_view, container, false);
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
        tvNewsName.setText(getArguments().getString("title"));

        mCache = ACache.get(getActivity());

        if(mCache.getAsBitmap(getArguments().getString("uri"))!=null){
            Drawable dw = new BitmapDrawable(mCache.getAsBitmap(getArguments().getString("uri")));
            ivNewsPic.setImageDrawable(dw);
        }else {
            getImageByUri(getArguments().getString("uri"));
        }

        rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("title",getArguments().getString("title"));
                intent.putExtra("context",getArguments().getString("context"));
                intent.putExtra("publishDate",getArguments().getString("publishDate"));
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
                mCache.put(getArguments().getString("uri"),PicBitmap,ACache.TIME_DAY*20);

            }

        }.execute(Url);

    }


}
