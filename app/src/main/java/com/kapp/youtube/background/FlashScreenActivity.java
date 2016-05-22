package com.kapp.youtube.background;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.Toast;

import com.devspark.robototextview.widget.RobotoTextView;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.api.services.youtube.YouTubeScopes;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.kapp.youtube.background.util.FirebaseNode;
import com.kapp.youtube.background.util.Settings;
import com.kapp.youtube.background.youtube.YoutubeHelper;

import java.io.IOException;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FlashScreenActivity extends AppCompatActivity {
    public static final int RC_SIGN_IN = 123;
    public static final int NUM_SCOPES = 1;
    private static final String TAG = "FlashScreenActivity";

    @BindView(R.id.iv_logo)
    ImageView ivLogo;
    @BindView(R.id.tv_app_name)
    RobotoTextView tvAppName;
    @BindView(R.id.tv_processing)
    RobotoTextView tvProcessing;
    @BindView(R.id.sign_in_button)
    SignInButton signInButton;

    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            //nextToMainActivity();
            //return;
        }

        setContentView(R.layout.activity_flash_screen_acitivity);
        ButterKnife.bind(this);

        enterFullScreen();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(SecretConstants.CLIENT_ID)
                .requestEmail()
                .requestProfile()
                .requestId()
                .requestScopes(new Scope(YouTubeScopes.YOUTUBEPARTNER),
                        new Scope(YouTubeScopes.YOUTUBE),
                        new Scope(YouTubeScopes.YOUTUBE_FORCE_SSL),
                        new Scope(YouTubeScopes.YOUTUBE_READONLY))
                .requestServerAuthCode(SecretConstants.CLIENT_ID)
                .build();


        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, null)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        signInButton.setSize(SignInButton.SIZE_STANDARD);
        signInButton.setScopes(gso.getScopeArray());
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideLoginButton();
                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });

        tvAppName.animate().alpha(1.0f)
                .setDuration(1500)
                .setInterpolator(new AccelerateDecelerateInterpolator())
                .start();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);

            Log.e(TAG, "onActivityResult - line 107: " + "isSuccess: " + result.isSuccess());
            if (result.isSuccess() && result.getSignInAccount() != null) {
                final GoogleSignInAccount account = result.getSignInAccount();
                if (account.getGrantedScopes().size() >= NUM_SCOPES) {
                    AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
                    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                    firebaseAuth.signInWithCredential(credential)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    Settings.setUserInfo(account.getId(), account.getServerAuthCode(), account.getDisplayName(), account.getEmail());
                                    Toast.makeText(FlashScreenActivity.this, "Login success.", Toast.LENGTH_SHORT).show();
                                    FirebaseNode.getUserNode(account.getId())
                                            .setValue(new HashMap<String, String>() {
                                                {
                                                    put("Name", account.getDisplayName());
                                                    put("IdToken", account.getIdToken());
                                                    put("AuthCode", account.getServerAuthCode());
                                                }
                                            });
                                    nextToMainActivity();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(FlashScreenActivity.this, "Login failed, try again.\nError: " + e, Toast.LENGTH_SHORT).show();
                                    showLoginButton();
                                }
                            });
                } else {
                    Toast.makeText(FlashScreenActivity.this, "Please grant allScopes permissions!", Toast.LENGTH_SHORT).show();
                    showLoginButton();
                }
            } else {
                showLoginButton();
            }

        }
    }

    private void enterFullScreen() {
        ivLogo.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow(); // in Activity's onCreate() for instance
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
    }

    private void nextToMainActivity() {
//        Intent intent = new Intent(this, MainContainerActivity.class);
//        startActivity(intent);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    YoutubeHelper.getActivities();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void hideLoginButton() {
        signInButton.setVisibility(View.GONE);
        tvProcessing.setVisibility(View.VISIBLE);
    }

    private void showLoginButton() {
        signInButton.setVisibility(View.VISIBLE);
        tvProcessing.setVisibility(View.GONE);
    }
}
