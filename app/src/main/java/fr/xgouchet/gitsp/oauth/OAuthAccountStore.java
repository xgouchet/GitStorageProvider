package fr.xgouchet.gitsp.oauth;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import fr.xgouchet.gitsp.oauth.config.OAuthConfig;
import fr.xgouchet.gitsp.oauth.config.OAuthConfigFactory;
import rx.Subscriber;

/**
 * @author Xavier Gouchet
 */
public class OAuthAccountStore {


    private static final String PREFERENCE_ACCOUNTS = "oauth";

    private static final String KEY_ACCESS_TOKEN = "access_token_%d_%s";

    private static final Pattern KEY_PARSER = Pattern.compile("access_token_([0-9]+)_(.*)");

    /**
     * @param account persists the account
     */
    public static void persistAccount(final @NonNull Context context, final @NonNull OAuthAccount account) {
        SharedPreferences preferences = getSharedPreferences(context);

        SharedPreferences.Editor editor = preferences.edit();
        String key = String.format(Locale.US, KEY_ACCESS_TOKEN, account.getServiceId(), account.getUserName());
        String value = account.getAccessToken();
        editor.putString(key, value);
        editor.apply();
    }


    private static SharedPreferences getSharedPreferences(@NonNull Context context) {
        return context.getSharedPreferences(PREFERENCE_ACCOUNTS, Context.MODE_PRIVATE);
    }

    public static class Observable implements rx.Observable.OnSubscribe<OAuthAccount> {


        private Context context;

        public Observable(@NonNull Context context) {
            this.context = context;
        }

        @Override
        public void call(Subscriber<? super OAuthAccount> subscriber) {

            SharedPreferences preferences = getSharedPreferences(context);

            Map<String, ?> entries = preferences.getAll();
            for (Map.Entry<String, ?> entry : entries.entrySet()) {
                Matcher matcher = KEY_PARSER.matcher(entry.getKey());
                if (matcher.matches()) {
                    int serviceId = Integer.valueOf(matcher.group(1));
                    String userName = matcher.group(2);
                    String accessToken = String.valueOf(entry.getValue());

                    //noinspection WrongConstant
                    subscriber.onNext(new OAuthAccount(serviceId, userName, accessToken));
                }
            }
            subscriber.onCompleted();
        }
    }

}
