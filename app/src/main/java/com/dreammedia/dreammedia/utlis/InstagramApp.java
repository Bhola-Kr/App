package com.dreammedia.dreammedia.utlis;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.dreammedia.dreammedia.network.ApiInterface;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.HashMap;
import java.util.Map;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class InstagramApp {

    private InstagramSession mSession;
    private InstagramDialog mDialog;
    private OAuthAuthenticationListener mListener;
    private ProgressDialog mProgress;
    private HashMap<String, String> userInfo = new HashMap<String, String>();
    private String mAuthUrl;
    private String mTokenUrl;
    private String mAccessToken;
    private String userId;
    private Context mCtx;

    private String mClientId;
    private String mClientSecret;

    static int WHAT_FINALIZE = 0;
    static int WHAT_ERROR = 1;
    private static int WHAT_FETCH_INFO = 2;

    /**
     * Callback url, as set in 'Manage OAuth Costumers' page
     * (https://developer.github.com/)
     */
    public static String mCallbackUrl     = "https://dreammedia.in/outh";
    private static final String AUTH_URL  = "https://api.instagram.com/oauth/authorize/";
    private static final String TOKEN_URL = "https://api.instagram.com/oauth/access_token";
    private static final String API_URL   = "https://api.instagram.com/v1";

    private static final String TAG = "InstagramAPI";

    public static final String TAG_DATA = "data";
    public static final String TAG_ID = "id";
    public static final String TAG_PROFILE_PICTURE = "profile_picture";
    public static final String TAG_USERNAME = "username";
    public static final String TAG_BIO = "bio";
    public static final String TAG_WEBSITE = "website";
    public static final String TAG_COUNTS = "counts";
    public static final String TAG_FOLLOWS = "follows";
    public static final String TAG_FOLLOWED_BY = "followed_by";
    public static final String TAG_MEDIA = "media";
    public static final String TAG_FULL_NAME = "full_name";
    public static final String TAG_META = "meta";
    public static final String TAG_CODE = "code";

    private AuthenticationListener listener;


    public void myInstaListner(AuthenticationListener mlistener){
        this.listener =mlistener ;
    }

    public InstagramApp(Context context, String clientId, String clientSecret, String callbackUrl , AuthenticationListener mlistener) {

        mClientId = clientId;
        mClientSecret = clientSecret;
        mCtx = context;
        listener = mlistener;

        mSession = new InstagramSession(context);
        mAccessToken = mSession.getAccessToken();

        mCallbackUrl = callbackUrl;

        mTokenUrl = TOKEN_URL + "?client_id=" + clientId + "&client_secret="
                + clientSecret + "&redirect_uri=" + mCallbackUrl + "&grant_type=authorization_code";

        mAuthUrl = AUTH_URL + "?client_id=" + clientId + "&redirect_uri="
                + mCallbackUrl + "&scope=user_profile,user_media&response_type=code";

        InstagramDialog.OAuthDialogListener listener = new InstagramDialog.OAuthDialogListener() {
            @Override
            public void onComplete(String code) {

                String result = code.replaceAll("#_", "");
                Log.e("getAccessToken", "onComplete: " +code );
                Log.e("getAccessToken", "onComplete: " +result );

             //  getAccessToken(result);
              getAccessToken_(result);

            }
            @Override
            public void onError(String error) {
                mListener.onFail("Authorization failed");
            }
        };

        mDialog = new InstagramDialog(context, mAuthUrl, listener);
        mProgress = new ProgressDialog(context);
        mProgress.setCancelable(false);

    }

    public void getAccessToken_(String code ){

        Gson gson = new GsonBuilder().setLenient().create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.instagram.com/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        ApiInterface r = retrofit.create(ApiInterface.class);

        Log.e(TAG, "getAccessToken_: " +mClientId );
        Log.e(TAG, "getAccessToken_: " +mClientSecret );
        Log.e(TAG, "getAccessToken_: " +mCallbackUrl );
        Log.e(TAG, "getAccessToken_: " +code );


        Call<InstaAccessToken> call = r.getAccessToken(mClientId, mClientSecret, "authorization_code", mCallbackUrl, code);
        call.enqueue(new Callback<InstaAccessToken>() {
            @Override
            public void onResponse(Call<InstaAccessToken> call, Response<InstaAccessToken> response) {
                // Log.i("WORKING", response.body().access_token + " " + response.body().user.getUsername());
                Log.e("InstaAccessToken", "onResponse: " + response.body().getAccess_token());

                try {

                  // listener.onTokenReceived(response.body().getAccess_token());
                     mAccessToken = response.body().getAccess_token();
                     userId       = response.body().getUser_id();

                     listener.onTokenReceived(mAccessToken);

                } catch (Exception e) { e.printStackTrace(); }

            }@Override
            public void onFailure(Call<InstaAccessToken> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private Map<String, String> mParameter;

    private void getUserNameId(String toke){

        Gson gson = new GsonBuilder().setLenient().create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.instagram.com/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        ApiInterface r = retrofit.create(ApiInterface.class);

        Call<GetProfileInstaModel> call = r.getINstaInfo("id,username", toke);
        call.enqueue(new Callback<GetProfileInstaModel>() {
            @Override
            public void onResponse(Call<GetProfileInstaModel> call, Response<GetProfileInstaModel> response) {
                Log.e("InstaAccessToken", "onResponse: " + response.body().getUsername());

            try {


                } catch (Exception e) { e.printStackTrace(); Log.e("InstaAccessToken", "onResponse: " + e.getMessage());  }

            }@Override
            public void onFailure(Call<GetProfileInstaModel> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }


    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == WHAT_ERROR) {
                mProgress.dismiss();
                if (msg.arg1 == 1) {
                    mListener.onFail("Failed to get access token");
                } else if (msg.arg1 == 2) {
                    mListener.onFail("Failed to get user information");
                }
            } else if (msg.what == WHAT_FETCH_INFO) {
                // fetchUserName();
                mProgress.dismiss();
                mListener.onSuccess();
            }
        }
    };

    public HashMap<String, String> getUserInfo() {
        return userInfo;
    }

    public boolean hasAccessToken() {
        return (mAccessToken == null) ? false : true;
    }

    public void setListener(OAuthAuthenticationListener listener) {
        mListener = listener;
    }

    public String getUserName() {
        return mSession.getUsername();
    }

    public String getId() {
        return mSession.getId();
    }

    public String getName() {
        return mSession.getName();
    }
    public String getTOken() {
        return mSession.getAccessToken();
    }
    public void authorize() {
        // Intent webAuthIntent = new Intent(Intent.ACTION_VIEW);
        // webAuthIntent.setData(Uri.parse(AUTH_URL));
        // mCtx.startActivity(webAuthIntent);
        mDialog.show();
    }

    public void resetAccessToken() {
        if (mAccessToken != null) {
            mSession.resetAccessToken();
            mAccessToken = null;
        }
    }

    public interface OAuthAuthenticationListener {
        public abstract void onSuccess();

        public abstract void onFail(String error);
    }


}