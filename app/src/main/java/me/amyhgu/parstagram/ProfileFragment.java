package me.amyhgu.parstagram;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.parceler.Parcels;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import me.amyhgu.parstagram.model.ClickListener;
import me.amyhgu.parstagram.model.GridAdapter;
import me.amyhgu.parstagram.model.Post;

public class ProfileFragment extends Fragment {

    private OnItemSelectedListener listener;
    private Button logoutButton;
    private ImageView ivProfilePic;
    private RecyclerView rvPostGrid;
    static ArrayList<Post> posts;
    private GridAdapter gridAdapter;
    private ParseUser user;

    public interface OnItemSelectedListener {
        // This can be any number of events to be sent to the activity
        public void onLogoutButtonSelected();
        void onProfilePictureSelected();
    }

    public static ProfileFragment newInstance(ParseUser user) {
        ProfileFragment profileFragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putParcelable(ParseUser.class.getSimpleName(), Parcels.wrap(user));
        profileFragment.setArguments(args);
        return profileFragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnItemSelectedListener) {
            listener = (OnItemSelectedListener) context;
        } else {
            throw new ClassCastException(context.toString()
                    + " must implement ProfileFragment.OnLogoutSelectedListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Get back arguments
        user = Parcels.unwrap(getArguments().getParcelable(ParseUser.class.getSimpleName()));
        posts = new ArrayList<>();
        gridAdapter = new GridAdapter(posts);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, parent, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        logoutButton = view.findViewById(R.id.btLogout);
        ivProfilePic = view.findViewById(R.id.ivProfilePic);
        rvPostGrid = view.findViewById(R.id.rvPostGrid);

        ParseFile propic = user.getParseFile("propic");
        if (propic != null) {
            Glide.with(getContext())
                    .load(propic.getUrl())
                    .apply(RequestOptions.circleCropTransform())
                    .into(ivProfilePic);
        } else {
            ivProfilePic.setImageResource(R.drawable.ic_user_filled);
        }

        rvPostGrid = (RecyclerView) view.findViewById(R.id.rvPostGrid);
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 3);
        rvPostGrid.setLayoutManager(layoutManager);
        rvPostGrid.setAdapter(gridAdapter);
        loadUserPosts();

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logoutUser();
            }
        });
        ivProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (user.equals(ParseUser.getCurrentUser())) {
                    listener.onProfilePictureSelected();
                } else {
                    Toast.makeText(getActivity().getApplicationContext(),
                            "Can only update your own profile picture", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public void onDetach() {
        super.onDetach();
        this.listener = null;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private void logoutUser() {
        ParseUser.logOut();
        ParseUser currentUser = ParseUser.getCurrentUser(); // this will now be null
        Log.d("HomeActivity", "Logout successful");

        listener.onLogoutButtonSelected();
    }

    public void setProfilePicture(Bitmap bitmap, String imagePath) {
        ivProfilePic.setImageBitmap(bitmap);

        final ParseUser user = ParseUser.getCurrentUser();
        final File file = new File(imagePath);
        final ParseFile parseFile = new ParseFile(file);
        parseFile.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    saveProfilePicture(user, parseFile);
                } else {
                    e.printStackTrace();
                }
            }
        });

        Toast.makeText(getContext(), "Profile picture changed", Toast.LENGTH_SHORT).show();
    }

    public void saveProfilePicture(ParseUser user, ParseFile parseFile) {
        user.put("propic", parseFile);

        user.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Log.d("HomeActivity", "Create post success");
                } else {
                    Log.e("HomeActivity", "Create post failure");
                    e.printStackTrace();
                }
            }
        });
    }

    private void loadUserPosts() {
        final Post.Query postsQuery = new Post.Query();
        postsQuery.getTop().withUser().whereEqualTo("user", user);

        postsQuery.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> objects, ParseException e) {
                if (e == null) {
                    for (int i = 0; i < objects.size(); i++) {
                        Log.d("ProfileFragment", "Post[" + i + "] = "
                                + objects.get(i).getDescription()
                                + "\nusername = " + objects.get(i).getUser().getUsername());
                        Post post = new Post();
                        post.setUser(objects.get(i).getUser());
                        post.setImage(objects.get(i).getImage());
                        post.setDescription(objects.get(i).getDescription());
                        posts.add(0, post);
                        gridAdapter.notifyItemInserted(posts.size() - 1);
                    }
                } else {
                    e.printStackTrace();
                }
            }
        });
    }
}
