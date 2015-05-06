package fr.xgouchet.gitstorageprovider.ui.activities;

import android.database.Cursor;
import android.database.MatrixCursor;
import android.os.CancellationSignal;
import android.os.ParcelFileDescriptor;
import android.provider.DocumentsContract;
import android.provider.DocumentsProvider;
import android.support.annotation.Nullable;

import java.io.FileNotFoundException;

import fr.xgouchet.gitstorageprovider.BuildConfig;
import fr.xgouchet.gitstorageprovider.R;

/**
 * @author Xavier Gouchet
 */
public class GitProvider extends DocumentsProvider {

    public static final String AUTHORITY = BuildConfig.APPLICATION_ID + ".provider.documents";

    private static final String[] DEFAULT_ROOT_PROJECTION = new String[]{
            DocumentsContract.Root.COLUMN_ROOT_ID,
            DocumentsContract.Root.COLUMN_MIME_TYPES,
            DocumentsContract.Root.COLUMN_FLAGS,
            DocumentsContract.Root.COLUMN_ICON,
            DocumentsContract.Root.COLUMN_TITLE,
            DocumentsContract.Root.COLUMN_SUMMARY,
            DocumentsContract.Root.COLUMN_DOCUMENT_ID,
            DocumentsContract.Root.COLUMN_AVAILABLE_BYTES,
    };

    private static final String[] DEFAULT_DOCUMENT_PROJECTION = new String[]{
            DocumentsContract.Document.COLUMN_DOCUMENT_ID,
            DocumentsContract.Document.COLUMN_MIME_TYPE,
            DocumentsContract.Document.COLUMN_DISPLAY_NAME,
            DocumentsContract.Document.COLUMN_LAST_MODIFIED,
            DocumentsContract.Document.COLUMN_FLAGS,
            DocumentsContract.Document.COLUMN_SIZE,
    };

    @Override
    public Cursor queryRoots(final @Nullable String[] projection) throws FileNotFoundException {
        // Create a cursor with either the requested fields, or the default
        // projection if "projection" is null.
        final MatrixCursor result = new MatrixCursor(resolveRootProjection(projection));

        // TODO If user is not logged in, return an empty root cursor.
//        if (!isUserLoggedIn()) {
//            return result;
//        }

        // TODO add one root per local repository
        final MatrixCursor.RowBuilder row = result.newRow();
        row.add(DocumentsContract.Root.COLUMN_ROOT_ID, "Foo");
        row.add(DocumentsContract.Root.COLUMN_DOCUMENT_ID, "gita://Foo/");
        row.add(DocumentsContract.Root.COLUMN_TITLE, getContext().getString(R.string.root_title));
        row.add(DocumentsContract.Root.COLUMN_SUMMARY, "Foo △");

        // Flags to describe when this root should be presented
        row.add(DocumentsContract.Root.COLUMN_FLAGS,
                DocumentsContract.Root.FLAG_SUPPORTS_CREATE |
                DocumentsContract.Root.FLAG_SUPPORTS_RECENTS |
                DocumentsContract.Root.FLAG_SUPPORTS_SEARCH);

        // assume we support all types
        row.add(DocumentsContract.Root.COLUMN_MIME_TYPES, null);

        row.add(DocumentsContract.Root.COLUMN_AVAILABLE_BYTES, null);
        row.add(DocumentsContract.Root.COLUMN_ICON, R.mipmap.ic_launcher);

        return result;
    }

    private String[] resolveRootProjection(final @Nullable String[] projection) {
        // TODO create an intersect of projection and default root projection
        return DEFAULT_ROOT_PROJECTION;
    }

    @Override
    public Cursor queryDocument(String s, String[] strings) throws FileNotFoundException {
        return null;
    }

    @Override
    public Cursor queryChildDocuments(String s, String[] strings, String s1) throws FileNotFoundException {
        return null;
    }

    @Override
    public ParcelFileDescriptor openDocument(String s, String s1, CancellationSignal cancellationSignal) throws FileNotFoundException {
        return null;
    }

    @Override
    public boolean onCreate() {
        return false;
    }
}
