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


public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.ViewHolder> {
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView leftText;
        public TextView rightText;

        public ViewHolder(View itemView) {
            super(itemView);

            leftText = (TextView) itemView.findViewById(R.id.txt_left);
            rightText = (TextView) itemView.findViewById(R.id.txt_right);
        }
    }


    private List<Favorite> favList;
    private Context context;


    public FavoriteAdapter(Context context, List<Favorite> favList) {
        this.favList = favList;
        this.context = context;
    }


    @Override
    public FavoriteAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View favoriteView = inflater.inflate(R.layout.list_2_columns, parent, false);

        ViewHolder viewHolder = new ViewHolder(favoriteView);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(FavoriteAdapter.ViewHolder holder, int position) {
        final Favorite favorite = favList.get(position);

        TextView leftText = holder.leftText;
        leftText.setText(favorite.getDocumentation().getTitle());

        TextView rightText = holder.rightText;
        SimpleDateFormat dateFormat = new SimpleDateFormat(DatabaseHelper.DATE_FORMAT, Locale.getDefault());
        String dateStr = "";

        try {
            Date date = dateFormat.parse(favorite.getCreatedAt());
            long time = date.getTime();
            dateStr = (String) DateUtils.getRelativeTimeSpanString(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        rightText.setText(dateStr);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Intent intent = new Intent(context, DocViewerActivity.class);
                intent.putExtra(DatabaseHelper.DOCUMENTATIONS_FILE_NAME,
                        favorite.getDocumentation().getFileName());
                context.startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount() {
        return favList.size();
    }


    public void swap(List<Favorite> favList) {
        this.favList.clear();
        this.favList.addAll(favList);
        notifyDataSetChanged();
    }
}
