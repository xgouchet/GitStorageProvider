package fr.xgouchet.gitstorageprovider.ui.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import butterknife.ButterKnife;
import butterknife.InjectView;
import fr.xgouchet.gitstorageprovider.GitApplication;
import fr.xgouchet.gitstorageprovider.R;
import fr.xgouchet.gitstorageprovider.core.account.Account;
import fr.xgouchet.gitstorageprovider.core.events.UserLoggedEvent;
import fr.xgouchet.gitstorageprovider.ui.fragments.AccountsFragment;
import fr.xgouchet.gitstorageprovider.ui.fragments.CredentialsFragment;
import fr.xgouchet.gitstorageprovider.ui.fragments.LocalRepositoriesFragment;
import fr.xgouchet.gitstorageprovider.ui.views.SlidingTabLayout;
import fr.xgouchet.gitstorageprovider.utils.DoubleDeckerBus;

/**
 * @author Xavier Gouchet
 */
public class MainActivity extends FragmentActivity implements Toolbar.OnMenuItemClickListener {


    @InjectView(R.id.sliding_tabs)
    public SlidingTabLayout mSlidingTabLayout;
    @InjectView(R.id.view_pager)
    public ViewPager mViewPager;
    @InjectView(R.id.toolbar)
    public Toolbar mToolbar;


    private DoubleDeckerBus mBus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get the common event bus
        mBus = ((GitApplication) getApplication()).getBus();

        // set content
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);

        // prepare toolbar
        mToolbar.setOnMenuItemClickListener(this);
        mToolbar.setTitleTextColor(Color.WHITE);
        mToolbar.setTitle(R.string.title_activity_main);

        // prepare view pager and sliding layout
        mViewPager.setAdapter(new SamplePagerAdapter(getSupportFragmentManager()));
        mSlidingTabLayout.setViewPager(mViewPager);
        mSlidingTabLayout.setSelectedIndicatorColors(getResources().getColor(R.color.primary_dark));
    }

    private static final int PAGE_LOCAL_REPOS = 0;
    private static final int PAGE_CREDENTIALS = 1;
    private static final int PAGE_ACCOUNTS = 2;

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        return false;
    }

    class SamplePagerAdapter extends FragmentPagerAdapter {

        public SamplePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public Fragment getItem(int position) {

            switch (position) {
                case PAGE_LOCAL_REPOS:
                    return new LocalRepositoriesFragment();
                case PAGE_CREDENTIALS:
                    return new CredentialsFragment();
                case PAGE_ACCOUNTS:
                    return new AccountsFragment();
                default:
                    return null;
            }
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case PAGE_LOCAL_REPOS:
                    return getString(R.string.title_fragment_local_repos);
                case PAGE_CREDENTIALS:
                    return getString(R.string.title_fragment_credentials);
                case PAGE_ACCOUNTS:
                    return getString(R.string.title_fragment_accounts);
                default:
                    return null;
            }
        }
    }
}
