package com.kapp.youtube.background.youtube;

import android.content.Context;
import android.util.Log;

import com.google.api.client.auth.oauth2.BearerToken;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleRefreshTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTubeScopes;
import com.kapp.youtube.background.SecretConstants;
import com.kapp.youtube.background.util.Settings;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * Shared class used by every sample. Contains methods for authorizing a user and caching credentials.
 */
public class Auth {

    public static final String SC_YOUTUBE_PARTNER = YouTubeScopes.YOUTUBEPARTNER;
    public static final String SC_YOUTUBE_FORCE_SSL = YouTubeScopes.YOUTUBE_FORCE_SSL;
    public static final String YOUTUBE_READONLY = YouTubeScopes.YOUTUBE_READONLY;
    public static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
    public static final JsonFactory JSON_FACTORY = new JacksonFactory();
    private static final String CREDENTIAL_DATA_STORE = "credential_data_store";
    private static final String CLIENT_SECRETS_ASSET = "client_secrets.json";
    private static final String CREDENTIALS_DIRECTORY = "oauth-credentials";
    private static final String TAG = "Auth";
    private static Context context;

    public static Set<String> allScopes() {
        return new HashSet<String>() {
            {
                add(SC_YOUTUBE_PARTNER);
                add(SC_YOUTUBE_FORCE_SSL);
                add(YOUTUBE_READONLY);
            }
        };
    }

    public static void initialize(Context context) {
        Auth.context = context;
    }

    public static synchronized Credential authorize() throws IOException {
//        Reader clientSecretReader = new InputStreamReader(context.getAssets().open(CLIENT_SECRETS_ASSET));
//        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, clientSecretReader);
//
//        FileDataStoreFactory fileDataStoreFactory = new FileDataStoreFactory(context.getFileStreamPath(CREDENTIALS_DIRECTORY));
//        DataStore<StoredCredential> datastore = fileDataStoreFactory.getDataStore(CREDENTIAL_DATA_STORE);
//        Log.e("TAG", "authorize - line 76: " + Settings.getAuthCode());
//        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
//                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, allScopes())
//                .setAccessType("offline")
//                .setApprovalPrompt("auto")
//                .setClientId(SecretConstants.CLIENT_ID)
//                .build();
//        //Credential credential = flow.loadCredential(Settings.getUserId());
//        //if (credential != null)
//            //return credential;
//        GoogleTokenResponse tokenResponse = flow.newTokenRequest(Settings.getAuthCode()).execute();
        GoogleTokenResponse tokenResponse;
        Settings settings = Settings.getInstance();
        if (settings.getRefreshToken() != null)
            tokenResponse = new GoogleRefreshTokenRequest(
                    HTTP_TRANSPORT, JSON_FACTORY, settings.getRefreshToken(), SecretConstants.CLIENT_ID, SecretConstants.CLIENT_SECRET
            ).execute();
        else {
            tokenResponse = new GoogleAuthorizationCodeTokenRequest(
                    HTTP_TRANSPORT, JSON_FACTORY, SecretConstants.CLIENT_ID,
                    SecretConstants.CLIENT_SECRET, settings.getAuthCode(), ""
            ).execute();
            settings.setRefreshToken(tokenResponse.getRefreshToken());
            Log.e(TAG, "authorize - line 81: " + settings.getRefreshToken());
        }
        return newCredential(tokenResponse);
    }

    private static Credential newCredential(TokenResponse tokenResponse) {
        Credential.Builder builder = new Credential.Builder(BearerToken.authorizationHeaderAccessMethod())
                .setTransport(HTTP_TRANSPORT)
                .setJsonFactory(JSON_FACTORY);
        return builder.build().setFromTokenResponse(tokenResponse);
    }
}
