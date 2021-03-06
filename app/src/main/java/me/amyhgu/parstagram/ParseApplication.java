package me.amyhgu.parstagram;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseObject;

import me.amyhgu.parstagram.model.Post;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

public class ParseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        ParseObject.registerSubclass(Post.class);

        // Use for troubleshooting -- remove this line for production
        Parse.setLogLevel(Parse.LOG_LEVEL_DEBUG);

        // Use for monitoring Parse OkHttp traffic
        // Can be Level.BASIC, Level.HEADERS, or Level.BODY
        // See http://square.github.io/okhttp/3.x/logging-interceptor/ to see the options.
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        builder.networkInterceptors().add(httpLoggingInterceptor);

        final Parse.Configuration config = new Parse.Configuration.Builder(this)
                .applicationId(getString(R.string.app_id)) // should correspond to APP_ID env variable
                .clientKey(getString(R.string.master_key))  // set explicitly unless clientKey is explicitly configured on Parse server
                .clientBuilder(builder)
                .server(getString(R.string.server_url))
                .build();

        // set applicationId, and server server based on the values in the Heroku settings.
        // clientKey is not needed unless explicitly configured
        // any network interceptors must be added with the Configuration Builder given this syntax
        Parse.initialize(config);
    }
}
