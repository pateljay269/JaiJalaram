package patel.jay.jaijalaram.Item;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;

import patel.jay.jaijalaram.Activity.DashActivity;
import patel.jay.jaijalaram.Adapter.ItemAdapter;
import patel.jay.jaijalaram.ConstClass.Animations;
import patel.jay.jaijalaram.ConstClass.MyConst;
import patel.jay.jaijalaram.Fragments.CartFragment;
import patel.jay.jaijalaram.ModelClass.Items;
import patel.jay.jaijalaram.R;

public class ItemDscActivity extends AppCompatActivity implements View.OnClickListener {

    Activity activity = ItemDscActivity.this;
    TextView tvName, tvPrice, tvFav, tvAdd;
    SimpleDraweeView sdvImage;
    ImageButton imgFav;
    EditText etDsc;

    private String temp = "https://earthobservatory.nasa.gov/NaturalHazards/view.php?id=78685";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(activity);
        setContentView(R.layout.activity_item_dsc);

        tvAdd = findViewById(R.id.tvAddCart);
        etDsc = findViewById(R.id.etDsc);

        sdvImage = findViewById(R.id.sdvImage);
        imgFav = findViewById(R.id.imgFav);
        tvFav = findViewById(R.id.tvFav);
        tvName = findViewById(R.id.tvName);
        tvPrice = findViewById(R.id.tvPrice);

        tvFav.setOnClickListener(this);
        tvAdd.setOnClickListener(this);

        imgFav.setOnClickListener(this);
        sdvImage.setOnClickListener(this);

        sdvImage.setImageURI(Uri.parse(temp));
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onStart() {
        super.onStart();
        try {
            sdvImage.setImageURI(Uri.parse(ItemAdapter.ITEMS.getImgSrc()));
            tvName.setText(ItemAdapter.ITEMS.getName());
            tvPrice.setText(getString(R.string.rs) + " " + ItemAdapter.ITEMS.getPrice());

            Animations.Alpha(findViewById(R.id.layout), 1000);
            Animations.Alpha(sdvImage, 1000);

        } catch (Exception e) {
            e.printStackTrace();
            MyConst.toast(activity, e.getMessage());
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

            case R.id.sdvImage:
                sdvImage.setImageURI(Uri.parse(temp));
                break;

            case R.id.tvAddCart:
                for (Items items : CartFragment.cartItems) {
                    if (items.getiId() == ItemAdapter.ITEMS.getiId()) {
                        MyConst.toast(activity, "Already Added");
                        return;
                    }
                }

                CartFragment.cartDsc.add(etDsc.getText().toString());
                CartFragment.cartItems.add(ItemAdapter.ITEMS);
                DashActivity.setupBadge();
                finish();
                break;
        }
    }
}
