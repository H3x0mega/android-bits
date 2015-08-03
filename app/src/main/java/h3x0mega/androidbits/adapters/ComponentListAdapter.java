package h3x0mega.androidbits.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import h3x0mega.androidbits.R;
import h3x0mega.androidbits.fragments.LoaderViewFragment;

/**
 * Adapter for the different android-bits custom Views/components
 * Created by Francois on 03/08/2015.
 */
public class ComponentListAdapter extends RecyclerView.Adapter {

    private HashMap<String, Class> components;
    private List<String> keys;
    private View.OnClickListener listener;

    public ComponentListAdapter(View.OnClickListener listener) {
        components = new HashMap<String, Class>() {
            {
                put("LoaderView", LoaderViewFragment.class);
            }
        };
        this.keys = new ArrayList<>(components.keySet());
        this.listener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        TextView textView = (TextView) LayoutInflater.from(parent.getContext()).inflate(R.layout.row_component_list, parent, false);
        if (listener != null)
            textView.setOnClickListener(listener);

        return new RowViewHolder(textView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((RowViewHolder) holder).textView.setText(keys.get(position));
    }

    @Override
    public int getItemCount() {
        return components.keySet().size();
    }

    /**
     * ViewHolder for the adapter rows
     */
    public static class RowViewHolder extends RecyclerView.ViewHolder {

        private TextView textView;

        public RowViewHolder(View itemView) {
            super(itemView);
            this.textView = (TextView) itemView;
        }
    }
}
