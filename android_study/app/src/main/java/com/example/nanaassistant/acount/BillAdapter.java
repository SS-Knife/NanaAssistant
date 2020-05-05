package com.example.nanaassistant.acount;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nanaassistant.R;
import com.kongzue.dialog.v2.CustomDialog;

import java.util.List;

import static com.example.nanaassistant.MainActivity.billdb;


public class BillAdapter extends RecyclerView.Adapter<com.example.nanaassistant.acount.BillAdapter.BillViewHolder> {
    private List<Bill> bills;
    private View lastView;
    private Button lastbutton;
    private Context context;

    static class BillViewHolder extends RecyclerView.ViewHolder {
        View billView;
        TextView title;
        TextView money;
        TextView time;
        Button button;

        public BillViewHolder(View view) {
            super(view);
            billView = view;
            money = view.findViewById(R.id.money);
            title = view.findViewById(R.id.title);
            time = view.findViewById(R.id.acounttime);
            button = view.findViewById(R.id.delete);
        }

    }

    @NonNull
    @Override
    public com.example.nanaassistant.acount.BillAdapter.BillViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_acount_item, parent, false);
        final com.example.nanaassistant.acount.BillAdapter.BillViewHolder holder = new com.example.nanaassistant.acount.BillAdapter.BillViewHolder(view);
        holder.billView.setOnLongClickListener(new View.OnLongClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public boolean onLongClick(View view) {
                // TODO: 2020/5/2 消除焦点
                if(lastView!=null){
                    lastView.setBackgroundColor(Color.parseColor("#FFFFFF"));
                    lastbutton.setVisibility(View.INVISIBLE);
                    lastbutton=null;
                    lastView=null;
                }
                holder.billView.setBackgroundColor(Color.parseColor("#CCCCCC"));
                lastbutton=holder.button;
                holder.button.setVisibility(View.VISIBLE);
                lastView=holder.billView;
                return true;
            }
        });
        holder.billView.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View view) {
                if(lastView!=null){
                    lastView.setBackgroundColor(Color.parseColor("#FFFFFF"));
                    lastbutton.setVisibility(View.INVISIBLE);
                    lastbutton=null;
                    lastView=null;
                }else {
                    int position = holder.getAdapterPosition();
                    Bill bill = bills.get(position);
                    commonDialog(bill.getTime(),bill.getTitle()+" "+bill.getAnt(),bill.getDetail(),bill.getIo()+bill.getMoney());
                }
            }
        });
        return holder;
    }


    @Override
    public void onBindViewHolder(@NonNull com.example.nanaassistant.acount.BillAdapter.BillViewHolder holder, final int position) {
        final Bill bill = bills.get(position);
        holder.title.setText(bill.getTitle()+" "+bill.getAnt());
        holder.time.setText(bill.getTime());
        holder.money.setText(bill.getIo()+bill.getMoney());
        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                billdb.delete("bill","title=? and time=?",new String[]{bill.getTitle(),bill.getTime()});
                bills.remove(position);
                notifyDataSetChanged();
            }
        });
    }


    @Override
    public int getItemCount() {
        return bills.size();
    }
    public BillAdapter(List<Bill> bills,Context context) {
        this.bills = bills;
        this.context=context;
    }

    public void setContext(){

    }
    private void commonDialog(final String time, final String title, final String detail,final String money) {
        CustomDialog.show(context, R.layout.dialog_bill, new CustomDialog.BindView() {
            @Override
            public void onBind(CustomDialog dialog, View rootView) {
                //绑定布局
                TextView moneytext = rootView.findViewById(R.id.money);
                TextView titletext = rootView.findViewById(R.id.title);
                TextView timetext=rootView.findViewById(R.id.time);
                TextView detailtext = rootView.findViewById(R.id.detail);
                moneytext.setText(money);
                titletext.setText(title);
                timetext.setText(time);
                detailtext.setMovementMethod(ScrollingMovementMethod.getInstance());
                detailtext.setText(detail);
            }
        }).setCanCancel(true);

    }
}
