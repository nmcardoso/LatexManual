package com.github.nmcardoso.latexmanual;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.util.Pair;
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
        TextView txtLeft;
        TextView txtRight;

        public ViewHolder(View itemView) {
            super(itemView);
            txtLeft = (TextView) itemView.findViewById(R.id.txt_left);
            txtRight = (TextView) itemView.findViewById(R.id.txt_right);
        }
    }

    private Context context;
    private List<?> data;
    private int itemType;

    public static final int MOST_VIEWED = 0;
    public static final int FAVORITE = 1;
    public static final int HISTORY = 2;
    public static final int STATISTICS = 3;

    public CardListAdapter(Context context, List<?> data, int itemType) {
        this.context = context;
        this.data = data;
        this.itemType = itemType;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.list_cardview, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case CardListAdapter.FAVORITE:
                configureFavoriteView(holder, position);
                break;
            case CardListAdapter.HISTORY:
                configureHistoryView(holder, position);
                break;
            case CardListAdapter.MOST_VIEWED:
                configureMostViewedView(holder, position);
                break;
            case CardListAdapter.STATISTICS:
                configureStatisticsView(holder, position);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public int getItemViewType(int position) {
        return itemType;
    }

    private void configureStatisticsView(ViewHolder holder, int position) {
        final Pair<String, Integer> stat = (Pair<String, Integer>) data.get(position);

        holder.txtLeft.setText(stat.first);
        holder.txtRight.setText(String.valueOf(stat.second));
    }

    private void configureMostViewedView(ViewHolder holder, int position) {
        final Pair<Documentation, Integer> mvPair = (Pair<Documentation, Integer>) data.get(position);

        holder.txtLeft.setText(mvPair.first.getTitle());
        holder.txtRight.setText(String.valueOf(mvPair.second));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DocViewerActivity.class);
                intent.putExtra(DatabaseHelper.DOCUMENTATIONS_FILE_NAME,
                        mvPair.first.getFileName());
                context.startActivity(intent);
            }
        });
    }

    private void configureHistoryView(ViewHolder holder, int position) {
        final History history = (History) data.get(position);

        SimpleDateFormat dateFormat = new SimpleDateFormat(
                DatabaseHelper.DATE_FORMAT, Locale.getDefault());
        String dateStr = "";
        try {
            Date date = dateFormat.parse(history.getCreatedAt());
            long time = date.getTime();
            dateStr = (String) DateUtils.getRelativeTimeSpanString(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        holder.txtLeft.setText(history.getDocumentation().getTitle());
        holder.txtRight.setText(dateStr);

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

    private void configureFavoriteView(ViewHolder holder, int position) {
        final Favorite favorite = (Favorite) data.get(position);

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

        holder.txtLeft.setText(favorite.getDocumentation().getTitle());
        holder.txtRight.setText(dateStr);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DocViewerActivity.class);
                intent.putExtra(DatabaseHelper.DOCUMENTATIONS_FILE_NAME,
                        favorite.getDocumentation().getFileName());
                context.startActivity(intent);
            }
        });
    }
}
