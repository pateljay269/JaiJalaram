package patel.jay.jaijalaram.Login;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;

import patel.jay.jaijalaram.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment implements View.OnClickListener {

    Button btnSignIn, btnSignUp;
    SimpleDraweeView sdv;
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        Fresco.initialize(getContext());
        view = inflater.inflate(R.layout.fragment_main, container, false);

        btnSignIn = view.findViewById(R.id.btnSignIn);
        btnSignUp = view.findViewById(R.id.btnSignUp);

        sdv = view.findViewById(R.id.sdvImage);

        sdv.setImageURI("https://firebasestorage.googleapis.com/v0/b/jaijalaram-e003a.appspot.com/o/HOMEIMAGE%2FIMG-20180301-WA0044.jpg?alt=media&token=03e092b7-c754-43d9-a9fe-a48079ecbc00");
        btnSignIn.setOnClickListener(this);
        btnSignUp.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnSignUp:
                SignActivity.viewPager.setCurrentItem(0, true);
                break;

            case R.id.btnSignIn:
                SignActivity.viewPager.setCurrentItem(2, true);
                break;
        }
    }
}