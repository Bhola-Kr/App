package com.dreammedia.dreammedia.loginProcess;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.dreammedia.dreammedia.R;
import com.dreammedia.dreammedia.network.Constants;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class FacebookSignInActivity extends AppCompatActivity {

    private static final String TAG = FacebookSignInActivity.class.getSimpleName();
    private Context mContext;
    private CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Util.hideTitleBar(this);
        setContentView(R.layout.activity_facebook_sign_in);

        mContext = this;
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "email", "user_birthday"));
        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager, fbCallbacks);

    }

    private GraphRequest.GraphJSONObjectCallback graphJSONObjectCallback = new GraphRequest.GraphJSONObjectCallback() {
        @Override
        public void onCompleted(JSONObject object, GraphResponse response) {

            /*{
                "nameValuePairs":{
                "id":"1539291506255213","name":"Govind Saini","email":"govinds589@gmail.com", "picture":{
                    "nameValuePairs":{
                        "data":{
                            "nameValuePairs":{
                                "height":200, "is_silhouette":false,
                                        "url":"https://platform-lookaside.fbsbx.com/platform/profilepic/?asid\u003d1539291506255213\u0026height\u003d200\u0026width\u003d200\u0026ext\u003d1605853973\u0026hash\u003dAeR7cE6ZQs-uy4FFXQI",
                                        "width":200
                            }}}}}}*/
            try {

                Log.e("fbGraph", "onCompleted: " +new Gson().toJson(object));

                String name     = "";
                try {
                    name = object.getString("name");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                String email    = "";
                try {
                    email = object.getString("email");
                } catch (JSONException e) { e.printStackTrace(); }

                String socialId = "";
                try {
                    socialId = object.getString("id");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                 String profilePicUrl ="";//small | noraml | large
                try {
                    profilePicUrl = "https://graph.facebook.com/" + socialId + "/picture?type=large";
                } catch (Exception e) {
                    e.printStackTrace();
                }

                Map<String, String> map = new HashMap<>();
                map.put("socialId", socialId);
                map.put("name", name);
                map.put("email", email);
                map.put("picture", profilePicUrl);

                Intent intent = new Intent();
                intent.putExtra("account", object.toString());
                setResult(Constants.FB_SIGN_IN, intent);
                finish();

            } catch (Exception e) { callBack();e.printStackTrace(); }
        }
    };

    private FacebookCallback<LoginResult> fbCallbacks = new FacebookCallback<LoginResult>() {

        @Override
        public void onSuccess(LoginResult loginResult) {
            Log.d(TAG, "fb login success");

            GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), graphJSONObjectCallback);
            Bundle parameters = new Bundle();
            //parameters.putString("fields", "id,name,email,gender,link,picture.type(large)");
            parameters.putString("fields", "id,name,email,link,picture.type(large)");

            request.setParameters(parameters);
            request.executeAsync();
        }

        @Override
        public void onCancel() {
            Log.d("@", "fb login  cancel");
            callBack();
        }
        @Override
        public void onError(FacebookException error) {
            Log.d("@", "fb login  error = " + error.getMessage());
            callBack();
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void callBack() {
       // Utility.Companion.toast(mContext, getString(R.string.something_went_wrong));
        finish();
    }

}