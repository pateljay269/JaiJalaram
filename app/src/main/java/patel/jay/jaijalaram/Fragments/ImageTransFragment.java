package patel.jay.jaijalaram.Fragments;


import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;

import patel.jay.jaijalaram.ConstClass.MyConst;
import patel.jay.jaijalaram.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ImageTransFragment extends Fragment {

    View view;
    SimpleDraweeView sdvImage;

    private String url;

    public ImageTransFragment() {
        url = "";
    }

    @SuppressLint("ValidFragment")
    public ImageTransFragment(String url) {
        this();
        this.url = url;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Fresco.initialize(getContext());
        view = inflater.inflate(R.layout.sdv_image, container, false);
        sdvImage = view.findViewById(R.id.sdvImage);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        try {
            sdvImage.setImageURI(Uri.parse(url));

        } catch (Exception e) {
            MyConst.toast(getActivity(), e.getMessage());
        }
    }
}