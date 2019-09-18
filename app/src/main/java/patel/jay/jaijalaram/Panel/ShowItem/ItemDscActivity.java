package patel.jay.jaijalaram.Panel.ShowItem;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.HashMap;

import patel.jay.jaijalaram.Constants.Animations;
import patel.jay.jaijalaram.Constants.MYSQL;
import patel.jay.jaijalaram.Constants.MyConst;
import patel.jay.jaijalaram.Constants.PrefConst;
import patel.jay.jaijalaram.Constants.ServerCall;
import patel.jay.jaijalaram.Models.Items;
import patel.jay.jaijalaram.Panel.Customer.CustDashActivity;
import patel.jay.jaijalaram.Panel.Customer.Fragments.CartFragment;
import patel.jay.jaijalaram.R;

import static patel.jay.jaijalaram.Constants.MyConst.titleSet;
import static patel.jay.jaijalaram.Constants.MyConst.toast;

public class ItemDscActivity extends AppCompatActivity implements View.OnClickListener,
        RatingBar.OnRatingBarChangeListener, ServerCall.OnAsyncResponse {

    public static Items ITEMS;

    Activity activity = ItemDscActivity.this;
    TextView tvName, tvPrice, tvFav, tvAdd, tvRate, tvReview;
    SimpleDraweeView sdvImage;
    ImageButton imgFav;
    EditText etDsc;
    RatingBar rate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(activity);
        setContentView(R.layout.activity_item_dsc);
        titleSet(this, "Item Details");

        tvAdd = findViewById(R.id.tvAddCart);
        etDsc = findViewById(R.id.etDsc);

        rate = findViewById(R.id.ratingBar);

        sdvImage = findViewById(R.id.sdvImage);
        imgFav = findViewById(R.id.imgFav);
        tvFav = findViewById(R.id.tvFav);
        tvName = findViewById(R.id.tvName);
        tvPrice = findViewById(R.id.tvPrice);
        tvRate = findViewById(R.id.tvRate);
        tvReview = findViewById(R.id.tvReview);

        tvFav.setOnClickListener(this);
        tvAdd.setOnClickListener(this);

        imgFav.setOnClickListener(this);

        rate.setOnRatingBarChangeListener(this);

        sdvImage.setImageURI(Uri.parse(ITEMS.getImgSrc()));
    }

    @Override
    protected void onStart() {
        super.onStart();
        try {

            setRating();

            Animations.Alpha(findViewById(R.id.layout), 1000);
            Animations.Alpha(sdvImage, 1000);

        } catch (Exception e) {
            e.printStackTrace();
            toast(activity, e.getMessage());
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgFav:
            case R.id.tvFav:
                if (imgFav.getDrawable().getConstantState() == getResources().getDrawable(R.drawable.favfill).getConstantState()) {
                    imgFav.setImageResource(R.color.transparent);
                    imgFav.setImageDrawable(getResources().getDrawable(R.drawable.fav));
                } else {
                    imgFav.setImageResource(R.color.transparent);
                    imgFav.setImageDrawable(getResources().getDrawable(R.drawable.favfill));
                }
                break;

            case R.id.tvAddCart:
                for (Items items : CartFragment.cartItems) {
                    if (items.getiId() == ITEMS.getiId()) {
                        toast(activity, "Already Added");
                        return;
                    }
                }

                CartFragment.cartDsc.add(etDsc.getText().toString());
                CartFragment.cartItems.add(ITEMS);
                CustDashActivity.setupBadge();
                finish();
                break;
        }
    }

    @Override
    public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
        String url = ServerCall.BASE_URL + ServerCall.ITEMS + "updateRate";
        HashMap<String, String> hm = new HashMap<>();
        hm.put(MYSQL.Items.IID, ITEMS.getiId() + "");
        hm.put(MYSQL.Items.RATE, (v * 2) + "");

        new ServerCall(activity, url, hm, MyConst.UPDATE).execute();

        ratingBar.setEnabled(false);
    }

    @Override
    public void getResponse(String response, int flag) {

        if (response.length() == 0) {
            return;
        }

        switch (flag) {
            case MyConst.UPDATE:
                if (response.equals("1")) {
                    toast(activity, "Rating Saved...");

                    String url = ServerCall.BASE_URL + ServerCall.ITEMS + "allItem";
                    new ServerCall(activity, url, new HashMap<String, String>(), MyConst.SELECT).execute();
                } else {
                    toast(activity, "Error: " + response);
                    etDsc.setText(response);
                }
                break;

            case MyConst.SELECT:
                MyConst.putIntoPref(activity, PrefConst.ITEM_S, response);
                setRating();
                break;
        }
    }

    @SuppressLint("DefaultLocale,SetTextI18n")
    private void setRating() {
        ITEMS = Items.getItem(activity, ITEMS.getiId());
        float total = ITEMS.getTotal();
        float rating = ITEMS.getRate() / total;
        String savedRate = String.format("%.1f", rating);
        tvRate.setText(savedRate);
        tvReview.setText(total + " Review");

        tvName.setText(ITEMS.getName());
        tvPrice.setText(getString(R.string.rs) + " " + ITEMS.getPrice());
    }
}
