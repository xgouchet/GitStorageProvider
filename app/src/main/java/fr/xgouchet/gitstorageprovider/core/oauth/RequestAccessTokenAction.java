package fr.xgouchet.gitstorageprovider.core.oauth;

import android.support.annotation.Nullable;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import fr.xgouchet.gitstorageprovider.utils.actions.AsyncAction;

/**
 * @author Xavier Gouchet
 */
public class RequestAccessTokenAction implements AsyncAction<RequestAccessTokenAction.Input, String> {


    public static class Input {
        final OAuthConfig mConfig;
        final String mCode;

        /**
         * @param config the OAuth service configuration
         * @param code the OAuth code
         */
        public Input(OAuthConfig config, String code) {
            mConfig = config;
            mCode = code;
        }
    }

    @Nullable
    @Override
    public String performAction(@Nullable Input input) throws Exception {

        // TODO use URL.openConnection instead
        HttpPost post = input.mConfig.getAccessTokenPost(input.mCode);


        HttpClient client = new DefaultHttpClient();


        HttpResponse response = client.execute(post);


        if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            HttpEntity entity = response.getEntity();
            JSONObject json = new JSONObject(EntityUtils.toString(entity));


            return json.getString("access_token");
        } else {
            return null;
        }
    }
}
