package fr.xgouchet.gitstorageprovider.core.events;

import java.util.List;

import fr.xgouchet.gitsp.oauth.OAuthAccount;

/**
 * @author Xavier Gouchet
 */
public class AccountsChangedEvent {
    private final List<OAuthAccount> mAccounts;

    public AccountsChangedEvent(List<OAuthAccount> accounts) {
        mAccounts = accounts;
    }

    public List<OAuthAccount> getAccounts() {
        return mAccounts;
    }
}
