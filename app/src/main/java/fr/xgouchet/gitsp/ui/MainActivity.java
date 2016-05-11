package fr.xgouchet.gitsp.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import butterknife.BindView;
import fr.xgouchet.gitsp.R;
import fr.xgouchet.gitsp.ui.fragments.AccountsFragment;
import fr.xgouchet.gitsp.ui.fragments.LocalReposFragment;

import static butterknife.ButterKnife.bind;

/**
 * @author Xavier Gouchet
 */
public class MainActivity extends AppCompatActivity {

    @BindView(R.id.main_view_pager)
    ViewPager viewPager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        bind(this);

        viewPager.setAdapter(new MainPagerAdapter(getSupportFragmentManager()));
    }

    private class MainPagerAdapter extends FragmentPagerAdapter {

        public static final int PAGE_LOCAL_REPOS = 0;
        public static final int PAGE_ACCOUNTS = 1;
        public static final int PAGE_SETTINGS = 2;

        public static final int PAGES_COUNT = 3;

        public MainPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return PAGES_COUNT;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case PAGE_LOCAL_REPOS:
                    return getString(R.string.title_page_local_repos);
                case PAGE_ACCOUNTS:
                    return getString(R.string.title_page_accounts);
                case PAGE_SETTINGS:
                    return getString(R.string.title_page_settings);
                default:
                    return "???";
            }
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case PAGE_LOCAL_REPOS:
                    return new LocalReposFragment();
                case PAGE_ACCOUNTS:
                    return new AccountsFragment();
                default:
                    return new Fragment();
            }
        }


    }
}

