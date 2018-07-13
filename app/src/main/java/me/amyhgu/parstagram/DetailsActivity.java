package me.amyhgu.parstagram;

import android.media.Image;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.parse.ParseFile;

import org.parceler.Parcels;

import me.amyhgu.parstagram.model.Post;

public class DetailsActivity extends AppCompatActivity {

    PostHelper helper;
    TextView tvUsername;
    TextView tvDescription;
    TextView tvTimestamp;
    TextView tvCommentName;
    TextView tvNumLikes;
    ImageView ivPicture;
    ImageView ivPropic;
    ImageView ivFavorite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        helper = new PostHelper();

        tvUsername = (TextView) findViewById(R.id.tvUsername);
        tvDescription = (TextView) findViewById(R.id.tvDescription);
        tvTimestamp = (TextView) findViewById(R.id.tvTimestamp);
        tvCommentName = (TextView) findViewById(R.id.tvCommentName);
        tvNumLikes = (TextView) findViewById(R.id.tvNumLikes);
        ivPicture = (ImageView) findViewById(R.id.ivPicture);
        ivPropic = (ImageView) findViewById(R.id.ivProfilePic);
        ivFavorite = (ImageView) findViewById(R.id.ivFavorite);

        final Post post = Parcels.unwrap(getIntent().getParcelableExtra(Post.class.getSimpleName()));

        tvUsername.setText(post.getUser().getUsername());
        tvDescription.setText(post.getDescription());
        tvTimestamp.setText(post.getRelativeTimestamp());
        tvCommentName.setText(post.getUser().getUsername());
        tvNumLikes.setText(helper.getLikesString(post));

        Glide.with(DetailsActivity.this)
                .load(post.getImage().getUrl())
                .into(ivPicture);

        ParseFile propic = post.getUser().getParseFile("propic");
        if (propic != null) {
            Glide.with(DetailsActivity.this)
                    .load(propic.getUrl())
                    .apply(RequestOptions.circleCropTransform())
                    .into(ivPropic);
        } else {
            ivPropic.setImageResource(R.drawable.ic_user_filled);
        }

        helper.setHeartImage(post, ivFavorite);
        ivFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                helper.handleFaves(post, ivFavorite, tvNumLikes);
            }
        });
    }
}
