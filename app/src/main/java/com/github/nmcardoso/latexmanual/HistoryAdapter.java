package com.github.nmcardoso.latexmanual;


import android.content.Context;
import android.content.Intent;
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


public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView leftText;
        public TextView rightText;

        public ViewHolder (View itemView) {
            super(itemView);

            leftText = (TextView) itemView.findViewById(R.id.txt_left);
            rightText = (TextView) itemView.findViewById(R.id.txt_right);
        }
    }

    private List<History> historyList;
    private Context context;

    public HistoryAdapter(Context context, List<History> historyList) {
        this.historyList = historyList;
        this.context = context;
    }

    @Override
    public HistoryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View historicView = inflater.inflate(R.layout.list_history, parent, false);

        ViewHolder viewHolder = new ViewHolder(historicView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(HistoryAdapter.ViewHolder holder, int position) {
        final History history = historyList.get(position);

        TextView leftText = holder.leftText;
        leftText.setText(history.getDocumentation().getTitle());

        TextView rightText = holder.rightText;
        SimpleDateFormat dateFormat = new SimpleDateFormat(DatabaseHelper.DATE_FORMAT, Locale.getDefault());
        String dateStr = "";

        try {
            Date date = dateFormat.parse(history.getCreatedAt());
            long time = date.getTime();
            dateStr = (String) DateUtils.getRelativeTimeSpanString(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        rightText.setText(dateStr);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DocViewerActivity.class);
                intent.putExtra(DatabaseHelper.DOCUMENTATIONS_FILE_NAME,
                        history.getDocumentation().getFileName());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return historyList.size();
    }

    public void swap(List<History> newData) {
        historyList.clear();
        historyList.addAll(newData);
        notifyDataSetChanged();
    }
}
