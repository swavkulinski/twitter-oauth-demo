package com.swavdev.tc;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.swavdev.tc.model.Entry;

import java.util.ArrayList;


/**
 * Array adapter which takes ArrayList of Entry objects
 */
public class SimpleTwitterAdapter extends ArrayAdapter<Entry> {
    private final Context sContext;
    private final ArrayList<Entry> sObjects;
    private static DrawableManager dm;

    /**
     * Constructor
     * @param context activity context
     * @param objects content to be displayed
     */
    public SimpleTwitterAdapter(Context context, ArrayList<Entry> objects) {
        super(context, R.layout.activity_list_item, objects);
        sContext = context;
        sObjects = objects;
        if (dm == null)
            dm = new DrawableManager();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) sContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.activity_list_item, parent, false);
        TextView name = (TextView) view.findViewById(R.id.name);
        TextView lastTweet = (TextView) view.findViewById(R.id.last_tweet);
        name.setText(sObjects.get(position).getRealName() + " @" + sObjects.get(position).getScreenName());
        lastTweet.setText(sObjects.get(position).getLastTweet());
        ImageView avatar = (ImageView) view.findViewById(R.id.avatar);

        dm.fetchDrawableOnThread(sObjects.get(position).getAvatarUrl(), avatar);
        return view;
    }


    public void addAll(ArrayList<Entry> entries) {
        for (Entry e : entries) {
            add(e);
        }
    }

}
