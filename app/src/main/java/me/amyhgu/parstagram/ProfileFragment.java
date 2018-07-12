package me.amyhgu.parstagram;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.File;

import me.amyhgu.parstagram.model.Post;

public class ProfileFragment extends Fragment {

    private OnItemSelectedListener listener;
    private Button logoutButton;
    private ImageView ivProfilePic;

    public interface OnItemSelectedListener {
        // This can be any number of events to be sent to the activity
        public void onLogoutButtonSelected();
        void onProfilePictureSelected();
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

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logoutUser();
            }
        });
        ivProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onProfilePictureSelected();
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
}
