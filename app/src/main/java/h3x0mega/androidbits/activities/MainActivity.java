package h3x0mega.androidbits.activities;

import android.os.Bundle;

import h3x0mega.androidbits.R;
import h3x0mega.androidbits.fragments.LoaderViewFragment;

public class MainActivity extends ToolbarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setToolbarTitle(getString(R.string.app_name));

        getFragmentManager().beginTransaction().add(R.id.component_container, new LoaderViewFragment()).commit();
    }

}
