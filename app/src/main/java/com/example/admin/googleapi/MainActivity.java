package com.example.admin.googleapi;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.admin.googleapi.interfaces.API_interface;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements
        View.OnClickListener,
        GoogleApiClient.OnConnectionFailedListener {

    API_interface api_interface;
    Button signin, signout, revoke;
    LinearLayout llprofile;
    TextView name, email;
    ImageView profilepic;
    Context context;


    String token_id, personName, personEmail , photoURL;
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int RC_SIGN_IN = 007;

    private GoogleApiClient googleApiClient;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        signin = (Button) findViewById(R.id.signin);
        signout = (Button) findViewById(R.id.signout);
        llprofile = (LinearLayout) findViewById(R.id.llProfile);
        name = (TextView) findViewById(R.id.name);
        email = (TextView) findViewById(R.id.mail);
        profilepic = (ImageView) findViewById(R.id.profile_pic);

        signin.setOnClickListener(this);
        signout.setOnClickListener(this);

        GoogleSignInOptions gso = new GoogleSignInOptions.
                Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("102411201273-vsn179k59lep22vlcfoagr0jd2re425g.apps.googleusercontent.com").requestEmail().build();

        googleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(this, this).
                addApi(Auth.GOOGLE_SIGN_IN_API, gso).build();
    }

    public void signIn(){
        Intent signin_intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(signin_intent, RC_SIGN_IN);






    }

    public void signout(){
        Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {
                updateUI(false);
            }
        });
    }
    public void handleSignInResult(GoogleSignInResult result){
        //Log.d( Log.d(TAG, "handleSignInResult:" + result.isSuccess()));
        if (result.isSuccess()) {

        // Signed in successfully, show authenticated UI.

        GoogleSignInAccount signInAccount = result.getSignInAccount();

        personName = signInAccount.getDisplayName();
        personEmail = signInAccount.getEmail();
        photoURL = signInAccount.getPhotoUrl().toString();
        token_id = signInAccount.getIdToken();

        Log.e(TAG, "Name: "  +name+", email: " + personEmail
                + ", Image: " + result.getStatus()+ photoURL + "Token_id: "+ token_id);

            name.setText(personName);
            email.setText(personEmail);

            Glide.with(getApplicationContext())
                    .load(photoURL)
                    .thumbnail(0.5f)
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(profilepic);

            updateUI(true);
            insert();



        }else {
            // Signed out, show unauthenticated UI.
            updateUI(false);

        }




    }

    @Override
    public void onClick(View v) {

        int id = v.getId();

        switch (id){
            case R.id.signin:
                signIn();
                break;

            case R.id.signout:
                signout();
                break;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode==RC_SIGN_IN){
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(googleApiClient);
        if (opr.isDone()) {
            // If the user's cached credentials are valid, the OptionalPendingResult will be "done"
            // and the GoogleSignInResult will be available instantly.
            Log.d(TAG, "Got cached sign-in");
            GoogleSignInResult result = opr.get();
            handleSignInResult(result);
        }else {
            // If the user has not previously signed in on this device or the sign-in has expired,
            // this asynchronous branch will attempt to sign in the user silently.  Cross-device
            // single sign-on will occur in this branch.
            showProgressDialog();
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(GoogleSignInResult googleSignInResult) {
                    hideProgressDialog();
                    handleSignInResult(googleSignInResult);
                }
            });
        }


    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        Log.d(TAG, "onConnectionFailed:" + connectionResult);

    }

    private void showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Loading");
            progressDialog.setIndeterminate(true);
        }

        progressDialog.show();
    }

    private void hideProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.hide();
        }
    }

    private void updateUI(boolean isSignedIn) {
        if (isSignedIn) {
            signin.setVisibility(View.GONE);
            signout.setVisibility(View.VISIBLE);
            //revoke.setVisibility(View.VISIBLE);
            llprofile.setVisibility(View.VISIBLE);
        } else {
            signin.setVisibility(View.VISIBLE);
            signout.setVisibility(View.GONE);
            //revoke.setVisibility(View.GONE);
            llprofile.setVisibility(View.GONE);
        }
    }


    public void insert(){

        //insert userinfo in database

        api_interface = new APIclient().getClient().create(API_interface.class);

        final userinfo userinfo = new userinfo();

        userinfo.setToken_id(token_id);
        userinfo.setPersonName(personName);
        userinfo.setPersonEmail(personEmail);
        userinfo.setPhotoURL(photoURL);

        Call<userinfo> call =api_interface.insertInfo(userinfo.getToken_id(), userinfo.getPersonName(),
                userinfo.getPersonEmail()
                ,userinfo.getPhotoURL());

        call.enqueue(new Callback<userinfo>() {
            @Override
            public void onResponse(Call<userinfo> call, Response<userinfo> response) {
                Log.d("onResponse", "" + response.code() +
                        "  response body "  + response.body() +
                        " responseError " + response.errorBody() + " responseMessage " +
                        response.message());

                userinfo info = response.body();

                Log.d("info", info.getToken_id()+info.getPersonName()+info.getPersonEmail()+info.getPhotoURL());

               // Toast.makeText(context, "inserted", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onFailure(Call<userinfo> call, Throwable t) {
                Log.d("onFailure", t.toString());
            }


        });

    }

}
