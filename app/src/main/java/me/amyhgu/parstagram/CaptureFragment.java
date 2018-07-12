package me.amyhgu.parstagram;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import me.amyhgu.parstagram.model.Post;

public class CaptureFragment extends Fragment {

    private OnCameraSelectedListener listener;
    private EditText descriptionInput;
    private Button createButton;
    private Button cameraButton;
    private ImageView ivPreview;
    String imgPath = null;


    public interface OnCameraSelectedListener {
        void onCameraButtonSelected(View view);
        void onCreateButtonSelected();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnCameraSelectedListener) {
            listener = (OnCameraSelectedListener) context;
        } else {
            throw new ClassCastException(context.toString()
                    + " must implement CaptureFragment.OnCameraSelectedListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_capture, parent, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        descriptionInput = view.findViewById(R.id.etDescription);
        createButton = view.findViewById(R.id.btCreate);
        cameraButton = view.findViewById(R.id.btCamera);
        ivPreview = (ImageView) view.findViewById(R.id.ivPreview);

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (imgPath != null) {
                    grabPostInfo();
                } else {
                    Toast.makeText(getActivity(), "No picture taken", Toast.LENGTH_LONG).show();
                }
            }
        });

        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onCameraButtonSelected(view);
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

    private void grabPostInfo() {
        final String description = descriptionInput.getText().toString();
        final ParseUser user = ParseUser.getCurrentUser();

        final File file = new File(imgPath);
        final ParseFile parseFile = new ParseFile(file);
        parseFile.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    createPost(description, parseFile, user);
                } else {
                    e.printStackTrace();
                }
            }
        });
    }

    private void createPost(String description, ParseFile imageFile, ParseUser user) {
        final Post newPost = new Post();
        newPost.setDescription(description);
        newPost.setImage(imageFile);
        newPost.setUser(user);

        newPost.saveInBackground(new SaveCallback() {
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

        listener.onCreateButtonSelected();
        Toast.makeText(getContext(), "Posted to feed", Toast.LENGTH_SHORT).show();
    }

    public void setPreviewImage(Bitmap bitmap, String imagePath) {
        imgPath = imagePath;
        ivPreview.setImageBitmap(bitmap);
    }
}
