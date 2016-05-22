package com.kapp.youtube.background.youtube;

import android.content.Context;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.StoredCredential;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.DataStore;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.youtube.YouTubeScopes;
import com.kapp.youtube.background.util.Settings;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
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

    public static Credential authorize() throws IOException {
        Reader clientSecretReader = new InputStreamReader(context.getAssets().open(CLIENT_SECRETS_ASSET));
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, clientSecretReader);

        FileDataStoreFactory fileDataStoreFactory = new FileDataStoreFactory(context.getFileStreamPath(CREDENTIALS_DIRECTORY));
        DataStore<StoredCredential> datastore = fileDataStoreFactory.getDataStore(CREDENTIAL_DATA_STORE);

        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, allScopes()).setCredentialDataStore(datastore)
                .setAccessType("offline")
                .setApprovalPrompt("auto")
                .build();
        Credential credential = flow.loadCredential(Settings.getUserId());
        if (credential != null)
            return credential;
        GoogleTokenResponse tokenResponse = flow.newTokenRequest(Settings.getAuthCode()).execute();
        return flow.createAndStoreCredential(tokenResponse, Settings.getUserId());
    }
}
