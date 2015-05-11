package fr.xgouchet.gitstorageprovider.core.apis;

import android.net.Uri;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import fr.xgouchet.gitstorageprovider.core.oauth.OAuthConfig;

/**
 * @author Xavier Gouchet
 */
public class GithubOAuthConfig extends OAuthConfig {


    private static final String OAUTH_LOGIN = "https://github.com/login/oauth/authorize";
    private static final String OAUTH_ACCESS = "https://github.com/login/oauth/access_token";


    private static final String CLIENT_ID = "ae6fa7487a90f106797f";
    private static final String SECRET_KEY = "4d581b0548e85fccc48fde56387efcca893b2d66";


    private static final String REDIRECT = "http://www.xgouchet.fr/gitstorageprovider/oauth/login_success";


    @Override
    public Uri getRequestTokenUri() {
        Uri.Builder builder = Uri.parse(OAUTH_LOGIN).buildUpon();


        builder.appendQueryParameter("client_id", CLIENT_ID);
        builder.appendQueryParameter("scope", "public_repo,repo");
        builder.appendQueryParameter("response_type", "code");
        builder.appendQueryParameter("redirect_uri", getRedirectUri());


        return builder.build();
    }

    @Override
    public URL getRequestAccessTokenUrl(String code) {
        return null;
    }


    @Override
    public String getRedirectUri() {
        return REDIRECT;
    }

    @Override
    public HttpPost getAccessTokenPost(String code) {
        HttpPost post = new HttpPost(OAUTH_ACCESS);


        post.setHeader("Content-Type", "application/x-www-form-urlencoded");
        post.setHeader("Accept", "application/json");


        List<NameValuePair> pairs = new ArrayList<NameValuePair>(8);


        pairs.add(new BasicNameValuePair("client_id", CLIENT_ID));
        pairs.add(new BasicNameValuePair("client_secret", SECRET_KEY));
        pairs.add(new BasicNameValuePair("code", code));


        try {
            post.setEntity(new UrlEncodedFormEntity(pairs));
        } catch (UnsupportedEncodingException e) {
            return null;
        }


        return post;
    }
}
