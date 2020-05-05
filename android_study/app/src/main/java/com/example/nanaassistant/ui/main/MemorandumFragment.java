package com.example.nanaassistant.ui.main;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nanaassistant.R;
import com.example.nanaassistant.memorandum.Incident;
import com.example.nanaassistant.memorandum.IncidentAdapter;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;
import static com.example.nanaassistant.MainActivity.incidentdb;

/**
 * A placeholder fragment containing a simple view.
 */
public class MemorandumFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";
    private RecyclerView recyclerView;
    private IncidentAdapter adapter;
    private static List<Incident> incidents = new ArrayList<>();

    public static MemorandumFragment newInstance(int index) {
        MemorandumFragment fragment = new MemorandumFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_memorandum, container, false);
        Cursor c = incidentdb.rawQuery("select * from incident ORDER BY time", null);

        if(c !=null)
        {
            int i=0;
            c.moveToFirst();
            while(!c.isAfterLast())
            {

                Incident incident =new Incident();
                i++;
                Log.d(TAG, "hhhhhhhhhhhhhhhhhhhhhhhhhhhhhhh "+i);
                incident.setTitle(c.getString(c.getColumnIndex("title")));
                incident.setRemindtime(c.getString(c.getColumnIndex("time")));
                incident.setDetail(c.getString(c.getColumnIndex("detail")));
                incidents.add(incident);
                c.moveToNext();
            }

        }


        recyclerView = root.findViewById(R.id.recyclerview1);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new IncidentAdapter(incidents,getContext());
        recyclerView.setAdapter(adapter);

        return root;
    }

    public void addIncident(Incident incident) {
        incidents.add(incident);
        adapter.notifyDataSetChanged();
    }

}