package me.amyhgu.parstagram;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import org.parceler.Parcels;

import me.amyhgu.parstagram.model.Post;

public class DetailsActivity extends AppCompatActivity {

    TextView tvUsername;
    TextView tvDescription;
    TextView tvTimestamp;
    ImageView ivPicture;
    ImageView ivPropic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        tvUsername = (TextView) findViewById(R.id.tvUsername);
        tvDescription = (TextView) findViewById(R.id.tvDescription);
        tvTimestamp = (TextView) findViewById(R.id.tvTimestamp);
        ivPicture = (ImageView) findViewById(R.id.ivPicture);
        ivPropic = (ImageView) findViewById(R.id.ivProfilePic);

        tvUsername.setText(getIntent().getStringExtra("username"));
        tvDescription.setText(getIntent().getStringExtra("description"));
        tvTimestamp.setText(getIntent().getStringExtra("timestamp"));

        Glide.with(DetailsActivity.this)
                .load(getIntent().getStringExtra("image"))
                .into(ivPicture);

        if (getIntent().getStringExtra("propic") != null) {
            Glide.with(DetailsActivity.this)
                    .load(getIntent().getStringExtra("propic"))
                    .apply(RequestOptions.circleCropTransform())
                    .into(ivPropic);
        } else {
            ivPropic.setImageResource(R.drawable.ic_user_filled);
        }
    }
}
