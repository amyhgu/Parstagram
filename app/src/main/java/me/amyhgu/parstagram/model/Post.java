package me.amyhgu.parstagram.model;

import android.text.format.DateUtils;
import android.util.Log;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.json.JSONArray;
import org.json.JSONObject;
import org.ocpsoft.prettytime.PrettyTime;

import java.text.DateFormat;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@ParseClassName("Post")
public class Post extends ParseObject {
    private static final String KEY_DESCRIPTION = "description";
    private static final String KEY_IMAGE = "image";
    private static final String KEY_USER = "user";
    private static final String KEY_FAVES = "userFaves";

    public String getDescription() {
        return getString(KEY_DESCRIPTION);
    }

    public void setDescription(String description) {
        put(KEY_DESCRIPTION, description);
    }

    public ParseFile getImage() {
        return getParseFile(KEY_IMAGE);
    }

    public void setImage(ParseFile image) {
        put(KEY_IMAGE, image);
    }

    public ParseUser getUser() {
        return getParseUser(KEY_USER);
    }

    public void setUser(ParseUser user) {
        put(KEY_USER, user);
    }

    public List<ParseUser> getUserFaves() {
        return getList(KEY_FAVES);
    }

    public void addUserFave(ParseUser user) {
        List userFaves = getUserFaves();
        userFaves.add(user);
        put(KEY_FAVES, userFaves);
    }

//    public Boolean getIsFave() { return getBoolean(KEY_IS_FAVE); }
//
//    public void setIsFave() {
//        if (getIsFave()) {
//            put(KEY_IS_FAVE, false);
//        } else {
//            put(KEY_IS_FAVE, true);
//        }
//    }
//
//    public int getNumFaves() { return getInt(KEY_NUM_FAVES); };
//
//    public void setNumFaves() {
//        int n = getNumFaves();
//        if (getIsFave()) {
//            put(KEY_NUM_FAVES, n + 1);
//        } else {
//            put(KEY_NUM_FAVES, n - 1);
//        }
//    }

    public String getRelativeTimestamp() {
        PrettyTime prettyTime = new PrettyTime();
//        Date createdAt = getCreatedAt();
//        Date date = new Date(0);

//        Format formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
//        String s = formatter.format(createdAt);
//        Log.d("Post created at", s);

//        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
//        try {
//            date = dateFormat.parse(createdAt.toString());//You will get date object relative to server/client timezone wherever it is parsed
//        } catch (ParseException e) {
//            Log.e("Date", "Date parsing failed");
//            e.printStackTrace();
//        }

//        Log.d("Pretty Time", prettyTime.format(getCreationTime()));

        return prettyTime.format(getCreatedAt());
    }

    public static class Query extends ParseQuery {
        public Query() {
            super (Post.class);
        }

        public Query getTop() {
            setLimit(20);
            return this;
        }

        public Query withUser() {
            include("user");
            return this;
        }
    }
}
