package com.dreammedia.dreammedia.loginProcess;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.dreammedia.dreammedia.R;
import com.dreammedia.dreammedia.config.App;
import com.dreammedia.dreammedia.network.Constants;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Objects;

import static com.google.android.gms.auth.api.signin.GoogleSignInStatusCodes.getStatusCodeString;

public class GoogleSignInActivity extends AppCompatActivity {

    private static final String TAG = GoogleSignInActivity.class.getSimpleName();
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Util.hideTitleBar(this);

        setContentView(R.layout.activity_google_sign_in);
        mContext = this;
        Log.v(TAG, getApplicationContext().getPackageName());

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                //.requestIdToken(GoogleSignInActivity.this.getResources().getString(R.string.server_client_id))
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
           GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
          App.getInstance().setGoogleSignInClient(mGoogleSignInClient);

          Intent signInIntent = mGoogleSignInClient.getSignInIntent();
          startActivityForResult(signInIntent, Constants.RC_SIGN_IN);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == Constants.RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {

        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            // Signed in successfully, show authenticated UI.
            // updateUI(account);
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("id", Objects.requireNonNull(account).getId());
            hashMap.put("name", account.getDisplayName());
            hashMap.put("email", account.getEmail());

            Intent intent = new Intent();
            intent.putExtra("account", new JSONObject(hashMap).toString());
            setResult(Constants.RC_SIGN_IN, intent);
            finish();

        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.e("jsonObjecterror", "onActivityResult: " + e.getMessage());
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            Log.w(TAG, "signInResult:failed code=" + e.getMessage());
            Log.w(TAG, "signInResult:failed code=" + getStatusCodeString(e.getStatusCode()));
            e.printStackTrace();
            // updateUI(null);
            callBack(getStatusCodeString(e.getStatusCode()));
        }
    }

    private void callBack(String message) {
       // Utility.Companion.toast(mContext, message);
        Toast.makeText(mContext, message+"", Toast.LENGTH_SHORT).show();
        finish();

    }

}
