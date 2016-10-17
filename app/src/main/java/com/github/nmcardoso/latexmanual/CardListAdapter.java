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


public class CardListAdapter extends RecyclerView.Adapter<CardListAdapter.ViewHolder> {
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView leftText;
        public TextView rightText;

        public ViewHolder(View itemView) {
            super(itemView);

            leftText = (TextView) itemView.findViewById(R.id.txt_left);
            rightText = (TextView) itemView.findViewById(R.id.txt_right);
        }
    }

    private Context context;
    private List<?> data;
    private int flag;

    public static final int MOST_VIEWED = 0;
    public static final int FAVORITE = 1;
    public static final int HISTORIC = 2;

    public CardListAdapter(Context context, List<?> data, int flag) {
        this.context = context;
        this.data = data;
        this.flag = flag;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.list_card_view, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Object itemObject = data.get(position);
        TextView txtLeft = holder.leftText;
        TextView txtRight = holder.rightText;

        if (flag == HISTORIC) {
            Historic historic = (Historic) itemObject;

            SimpleDateFormat dateFormat = new SimpleDateFormat(
                    DatabaseHelper.DATE_FORMAT, Locale.getDefault());
            String dateStr = "";
            try {
                Date date = dateFormat.parse(historic.getCreatedAt());
                long time = date.getTime();
                dateStr = (String) DateUtils.getRelativeTimeSpanString(time);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            txtLeft.setText(historic.getDocumentation().getTitle());
            txtRight.setText(dateStr);
        } else if (flag == FAVORITE) {
            Favorite favorite = (Favorite) itemObject;

            SimpleDateFormat dateFormat = new SimpleDateFormat(
                    DatabaseHelper.DATE_FORMAT, Locale.getDefault());
            String dateStr = "";
            try {
                Date date = dateFormat.parse(favorite.getCreatedAt());
                long time = date.getTime();
                dateStr = (String) DateUtils.getRelativeTimeSpanString(time);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            txtLeft.setText(favorite.getDocumentation().getTitle());
            txtRight.setText(dateStr);
        } else if (flag == MOST_VIEWED) {
            Historic historic = (Historic) itemObject;

            txtLeft.setText(historic.getDocumentation().getTitle());
            txtRight.setText(String.valueOf(historic.getViewCount()));
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
