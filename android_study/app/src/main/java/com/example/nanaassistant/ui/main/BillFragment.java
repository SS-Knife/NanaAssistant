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
import com.example.nanaassistant.acount.Bill;
import com.example.nanaassistant.acount.BillAdapter;
import com.example.nanaassistant.memorandum.Incident;
import com.example.nanaassistant.memorandum.IncidentAdapter;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;
import static com.example.nanaassistant.MainActivity.billdb;


public class BillFragment extends Fragment {
    private static final String ARG_SECTION_NUMBER = "section_number";
    private RecyclerView recyclerView;
    private BillAdapter adapter;
    private static List<Bill> bills = new ArrayList<>();
    public static BillFragment newInstance(int index) {
        BillFragment fragment = new BillFragment();
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

        View root = inflater.inflate(R.layout.fragment_main, container, false);
        Cursor c = billdb.rawQuery("select * from bill ORDER BY time", null);

        if(c !=null)
        {
            int i=0;
            c.moveToFirst();
            while(!c.isAfterLast())
            {

                Bill bill =new Bill();
                i++;
                Log.d(TAG, "hhhhhhhhhhhhhhhhhhhhhhhhhhhhhhh "+i);
                bill.setTitle(c.getString(c.getColumnIndex("title")));
                bill.setTime(c.getString(c.getColumnIndex("time")));
                bill.setMoney(Double.parseDouble(c.getString(c.getColumnIndex("money"))));
                bill.setDetail(c.getString(c.getColumnIndex("detail")));
                bill.setAnt(c.getString(c.getColumnIndex("ant")));
                bill.setIo(c.getString(c.getColumnIndex("io")));
                bills.add(bill);
                c.moveToNext();
            }
        }

        recyclerView = root.findViewById(R.id.recyclerview1);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new BillAdapter(bills,getContext());
        recyclerView.setAdapter(adapter);
        return root;
    }

    public void addBill(Bill bill) {
        bills.add(bill);
        adapter.notifyDataSetChanged();
    }


}
