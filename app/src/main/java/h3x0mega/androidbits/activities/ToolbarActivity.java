package h3x0mega.androidbits.activities;

import android.app.Activity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;

import h3x0mega.androidbits.R;

/**
 * Activity that adds a toolbar on top. This class should be extended by other Activities in order
 * for the Toolbar to appear
 * Created by Francois on 8/4/2015.
 */
public class ToolbarActivity extends Activity {

    protected Toolbar toolbar;
    protected FrameLayout content;

    @Override
    public void setContentView(int layoutResID) {
        View layout = getLayoutInflater().inflate(R.layout.activity_toolbar, null);
        content = (FrameLayout) layout.findViewById(R.id.content);
        toolbar = (Toolbar) layout.findViewById(R.id.toolbar);

        getLayoutInflater().inflate(layoutResID, content, true);
        super.setContentView(layout);
    }

    public void setToolbarTitle(String title) {
        toolbar.setTitle(title);
    }

}
