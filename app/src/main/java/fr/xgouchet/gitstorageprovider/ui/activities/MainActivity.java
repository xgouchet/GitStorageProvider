package fr.xgouchet.gitstorageprovider.ui.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.InjectView;
import fr.xgouchet.gitstorageprovider.R;
import fr.xgouchet.gitstorageprovider.ui.fragments.CredentialsFragment;
import fr.xgouchet.gitstorageprovider.ui.fragments.LocalReposFragment;
import fr.xgouchet.gitstorageprovider.ui.views.SlidingTabLayout;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
    }

    private static final int PAGE_LOCAL_REPOS = 0;
    private static final int PAGE_CREDENTIALS = 1;

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
            return 2;
        }

        @Override
        public Fragment getItem(int position) {

            switch (position) {
                case PAGE_LOCAL_REPOS:
                    return new LocalReposFragment();
                case PAGE_CREDENTIALS:
                    return new CredentialsFragment();
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
                default:
                    return null;
            }
        }
    }
}
