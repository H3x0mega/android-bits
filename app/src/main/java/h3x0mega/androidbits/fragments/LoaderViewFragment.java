package h3x0mega.androidbits.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import h3x0mega.androidbits.R;
import h3x0mega.loaderview.views.LoaderView;

/**
 * Fragment that shows the behavior of the LoaderView
 * Created by Francois on 03/08/2015.
 */
public class LoaderViewFragment extends Fragment implements View.OnClickListener {

    private LoaderView loaderView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_loaderview, null);
        this.loaderView = (LoaderView) root.findViewById(R.id.loader);
        root.findViewById(R.id.show_error).setOnClickListener(this);
        root.findViewById(R.id.show_loading).setOnClickListener(this);
        root.findViewById(R.id.hide).setOnClickListener(this);
        root.findViewById(R.id.reset).setOnClickListener(this);

        return root;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.show_error:
                loaderView.showError();
                break;

            case R.id.show_loading:
                loaderView.hideError();
                break;

            case R.id.hide:
                loaderView.finish();
                break;

            case R.id.reset:
                loaderView.reset();
                break;
        }
    }
}
