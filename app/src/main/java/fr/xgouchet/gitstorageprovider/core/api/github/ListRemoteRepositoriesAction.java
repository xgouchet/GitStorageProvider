package fr.xgouchet.gitstorageprovider.core.api.github;

import android.support.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import fr.xgouchet.gitstorageprovider.core.account.Account;
import fr.xgouchet.gitstorageprovider.core.git.RemoteRepository;
import fr.xgouchet.gitstorageprovider.utils.actions.AsyncRequestAction;

/**
 * @author Xavier Gouchet
 */
public class ListRemoteRepositoriesAction
        extends AsyncRequestAction<ListRemoteRepositoriesAction.Input, List<RemoteRepository>> {

    public static class Input {
        final Account mAccount;

        public Input(final @NonNull Account account) {
            mAccount = account;
        }
    }

    @NonNull
    @Override
    protected URL getRequestUrl(final @NonNull Input accessToken) throws Exception {
        // TODO add filter : type=[all|owner|private|public|member]
        return new URL(GithubOAuthConfig.ENDPOINT + "/users/repo?access_token=" + accessToken);
    }

    @Override
    protected void prepareUrlConnection(final @NonNull HttpURLConnection urlConnection,
                                        final @NonNull Input input) throws Exception {
    }

    @Override
    protected List<RemoteRepository> handleResponse(final @NonNull String response,
                                                    final @NonNull Input input) throws Exception {
        JSONArray array =  new JSONArray(response);
        int count = array.length();
        List<RemoteRepository> output = new ArrayList<>(count);

        for (int i = 0; i < count ; ++i){
            JSONObject repository = array.getJSONObject(i);
            // {
            //   ...
            //   "name" : "Foo",
            //   "clone_url" : "https://github.com/owner/Foo.git",
            //   "git_url" : "git://github.com/owner/Foo.git",
            //   "ssh_url" : "https://github.com/owner/Foo.git",
            //   ...
            // }
        }

        return output;
    }
}
