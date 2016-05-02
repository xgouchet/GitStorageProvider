package fr.xgouchet.gitstorageprovider.core.events;

/**
 * @author Xavier Gouchet
 */
public class NavigationEvent {

    public static final int NAV_ACCOUNT = 1;
    public static final int NAV_CREDENTIALS = 2;

    private final int nav;

    public NavigationEvent(int nav) {
        this.nav = nav;
    }

    public int getNav() {
        return nav;
    }
}
