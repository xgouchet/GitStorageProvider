package fr.xgouchet.gitstorageprovider.ui.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import fr.xgouchet.gitstorageprovider.R;

/**
 * @author Xavier Gouchet
 */
public class TestActivity extends FragmentActivity {

    private static final int READ_REQUEST_CODE = 0x42;

    @InjectView(R.id.text_document_info)
    public TextView mTextDocumentInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_test);
        ButterKnife.inject(this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent resultData) {

        // The ACTION_OPEN_DOCUMENT intent was sent with the request code
        // READ_REQUEST_CODE. If the request code seen here doesn't match, it's the
        // response to some other intent, and the code below shouldn't run at all.

        if (requestCode == READ_REQUEST_CODE) {
            displayOpenResult(resultCode, resultData);
        }
    }

    private void displayOpenResult(final int resultCode, final @Nullable Intent resultData) {

        String info = "OPEN\n";
        switch (resultCode) {
            case Activity.RESULT_OK:
                info += "Request successful\n";
                if (resultData != null) {
                    info += "Document URI : " + resultData.getData();
                }
                break;
            case Activity.RESULT_CANCELED:
                info += "Request canceled\n";
                break;
            default:
                info += "Unexpected result code : " + resultCode;
                break;
        }

        mTextDocumentInfo.setText(info);
    }

    @OnClick(R.id.button_open)
    public void onOpenDocument(View view) {

        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("text/*");

        startActivityForResult(intent, READ_REQUEST_CODE);
    }
}
