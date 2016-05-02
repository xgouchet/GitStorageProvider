package fr.xgouchet.gitsp.oauth;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

/**
 * @author Xavier Gouchet
 */
public class OAuthAccount implements Parcelable {

    @OAuthConfigFactory.ServiceId
    private final int mServiceId;

    private final String mUserName;
    private final String mAccessToken;

    public OAuthAccount(final @OAuthConfigFactory.ServiceId int serviceId,
                        final @NonNull String userName,
                        final @NonNull String accessToken) {
        mServiceId = serviceId;
        mUserName = userName;
        mAccessToken = accessToken;
    }

    private OAuthAccount(final @NonNull Parcel parcel) {
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

        OAuthAccount account = (OAuthAccount) o;

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

    public static final Parcelable.Creator<OAuthAccount> CREATOR = new Parcelable.Creator<OAuthAccount>() {
        public OAuthAccount createFromParcel(Parcel in) {
            return new OAuthAccount(in);
        }

        public OAuthAccount[] newArray(int size) {
            return new OAuthAccount[size];
        }
    };
}
