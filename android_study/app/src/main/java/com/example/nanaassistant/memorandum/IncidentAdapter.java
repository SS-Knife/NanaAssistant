package com.example.nanaassistant.memorandum;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.media.Image;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nanaassistant.R;
import com.kongzue.dialog.v2.CustomDialog;

import java.util.List;

import static com.example.nanaassistant.MainActivity.incidentdb;

public class IncidentAdapter extends RecyclerView.Adapter<IncidentAdapter.IncidentViewHolder> {
    private List<Incident> incidents;
    private View lastView;
    private ImageButton lastbutton;
    private Context context;

    static class IncidentViewHolder extends RecyclerView.ViewHolder {
        View incidentView;
        TextView title;
        TextView remindtime;
        ImageButton button;

        public IncidentViewHolder(View view) {
            super(view);
            incidentView = view;
            title = view.findViewById(R.id.title);
            remindtime = view.findViewById(R.id.remindtime);
            button = view.findViewById(R.id.delete);
        }

    }

    @NonNull
    @Override
    public IncidentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_memorandum_item, parent, false);
        final IncidentViewHolder holder = new IncidentViewHolder(view);
        holder.incidentView.setOnLongClickListener(new View.OnLongClickListener() {
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
                holder.incidentView.setBackgroundColor(Color.parseColor("#CCCCCC"));
                lastbutton=holder.button;
                holder.button.setVisibility(View.VISIBLE);
                lastView=holder.incidentView;
                return true;
            }
        });
        holder.incidentView.setOnClickListener(new View.OnClickListener() {
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
                    Incident incident = incidents.get(position);
                    // TODO: 2020/3/15 incident detail
                    commonDialog(incident.getRemindtime(),incident.getTitle(),incident.getDetail());
                }
            }
        });
        return holder;
    }


    @Override
    public void onBindViewHolder(@NonNull IncidentViewHolder holder, final int position) {
        final Incident incident = incidents.get(position);
        holder.title.setText(incident.getTitle());
        holder.remindtime.setText(incident.getRemindtime());
        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                incidentdb.delete("incident","title=? and time=?",new String[]{incident.getTitle(),incident.getRemindtime()});
                incidents.remove(position);
                notifyDataSetChanged();
            }
        });
    }


    @Override
    public int getItemCount() {
        return incidents.size();
    }
    public IncidentAdapter(List<Incident> incidents,Context context) {
        this.incidents = incidents;
        this.context=context;
    }

    public void setContext(){

    }
    private void commonDialog(final String time, final String title, final String detail) {
        CustomDialog.show(context, R.layout.dialog_incident, new CustomDialog.BindView() {
            @Override
            public void onBind(CustomDialog dialog, View rootView) {
                //绑定布局
                TextView titletext = rootView.findViewById(R.id.title);
                TextView timetext=rootView.findViewById(R.id.time);
                TextView detailtext = rootView.findViewById(R.id.detail);
                titletext.setText(title);
                timetext.setText(time);
                detailtext.setMovementMethod(ScrollingMovementMethod.getInstance());
                detailtext.setText(detail);
            }
        }).setCanCancel(true);

    }
}
