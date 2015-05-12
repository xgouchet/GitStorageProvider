package fr.xgouchet.gitstorageprovider.core.events;

import java.util.List;

import fr.xgouchet.gitstorageprovider.core.account.Account;

/**
 * @author Xavier Gouchet
 */
public class AccountsChangedEvent {
    private final List<Account> mAccounts;

    public AccountsChangedEvent(List<Account> accounts) {
        mAccounts = accounts;
    }

    public List<Account> getAccounts() {
        return mAccounts;
    }
}
