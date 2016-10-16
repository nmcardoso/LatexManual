package com.github.nmcardoso.latexmanual;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class HistoricAdapter extends RecyclerView.Adapter<HistoricAdapter.ViewHolder> {
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView leftText;
        public TextView rightText;

        public ViewHolder (View itemView) {
            super(itemView);

            leftText = (TextView) itemView.findViewById(R.id.txt_left);
            rightText = (TextView) itemView.findViewById(R.id.txt_right);
        }
    }


    private List<Historic> historicList;
    private Context context;


    public HistoricAdapter(Context context, List<Historic> historicList) {
        this.historicList = historicList;
        this.context = context;
    }


    @Override
    public HistoricAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View historicView = inflater.inflate(R.layout.list_2_columns, parent, false);

        ViewHolder viewHolder = new ViewHolder(historicView);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(HistoricAdapter.ViewHolder holder, int position) {
        Historic historic = historicList.get(position);

        TextView leftText = holder.leftText;
        leftText.setText(historic.getDocumentation().getTitle());

        TextView rightText = holder.rightText;
        SimpleDateFormat dateFormat = new SimpleDateFormat(DatabaseHelper.DATE_FORMAT, Locale.getDefault());
        String dateStr = "";

        try {
            Date date = dateFormat.parse(historic.getCreatedAt());
            long time = date.getTime();
            dateStr = (String) DateUtils.getRelativeTimeSpanString(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        rightText.setText(dateStr);
    }


    @Override
    public int getItemCount() {
        return historicList.size();
    }


    public void swap(List<Historic> newData) {
        historicList.clear();
        historicList.addAll(newData);
        notifyDataSetChanged();
    }
}
