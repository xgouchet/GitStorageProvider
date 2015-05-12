package fr.xgouchet.gitstorageprovider.core.account;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import fr.xgouchet.gitstorageprovider.core.oauth.OAuthConfigFactory;

/**
 * @author Xavier Gouchet
 */
public class Account implements Parcelable {

    @OAuthConfigFactory.ServiceId
    private final int mServiceId;

    private final String mUserName;
    private final String mAccessToken;

    public Account(final @OAuthConfigFactory.ServiceId int serviceId,
                   final @NonNull String userName,
                   final @NonNull String accessToken) {
        mServiceId = serviceId;
        mUserName = userName;
        mAccessToken = accessToken;
    }

    private Account(final @NonNull Parcel parcel) {
        //noinspection ResourceType
        mServiceId = parcel.readInt();
        mUserName = parcel.readString();
        mAccessToken = parcel.readString();
    }

    @NonNull
    public String getUserName() {
        return mUserName;
    }

    @OAuthConfigFactory.ServiceId
    public int getServiceId() {
        return mServiceId;
    }

    @NonNull
    public String getAccessToken() {
        return mAccessToken;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Account account = (Account) o;

        return mServiceId == account.mServiceId;

    }

    @Override
    public int hashCode() {
        return mServiceId;
    }

    @Override
    public String toString() {
        return "Account{" + mUserName + " @" + mServiceId + " [" + mAccessToken + "]}";
    }

    @Override
    public int describeContents() {
        return 0;
    }


    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeInt(mServiceId);
        parcel.writeString(mUserName);
        parcel.writeString(mAccessToken);
    }

    public static final Parcelable.Creator<Account> CREATOR = new Parcelable.Creator<Account>() {
        public Account createFromParcel(Parcel in) {
            return new Account(in);
        }

        public Account[] newArray(int size) {
            return new Account[size];
        }
    };
}
