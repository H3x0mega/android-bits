package h3x0mega.androidbits.activities;

import android.app.Activity;
import android.os.Bundle;

import h3x0mega.androidbits.R;
import h3x0mega.androidbits.fragments.LoaderViewFragment;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getFragmentManager().beginTransaction().add(R.id.component_container, new LoaderViewFragment()).commit();
    }

}
