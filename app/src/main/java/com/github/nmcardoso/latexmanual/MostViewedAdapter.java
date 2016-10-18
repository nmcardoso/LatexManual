package com.github.nmcardoso.latexmanual;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;


public class MostViewedAdapter extends RecyclerView.Adapter<MostViewedAdapter.ViewHolder> {

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


    public MostViewedAdapter(Context context, List<History> historyList) {
        this.historyList = historyList;
        this.context = context;
    }


    @Override
    public MostViewedAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View historicView = inflater.inflate(R.layout.list_cardview, parent, false);

        MostViewedAdapter.ViewHolder viewHolder = new MostViewedAdapter.ViewHolder(historicView);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(MostViewedAdapter.ViewHolder holder, int position) {
        History history = historyList.get(position);

        TextView leftText = holder.leftText;
        leftText.setText(history.getDocumentation().getTitle());

        TextView rightText = holder.rightText;
        rightText.setText(String.valueOf(history.getViewCount()));
    }


    @Override
    public int getItemCount() {
        return historyList.size();
    }
}
