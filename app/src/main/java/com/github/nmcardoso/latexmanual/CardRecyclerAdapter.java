package com.github.nmcardoso.latexmanual;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class CardRecyclerAdapter extends RecyclerView.Adapter<CardRecyclerAdapter.ViewHolder> {
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder (View itemView) {
            super(itemView);
        }
    }


    public class ListViewHolder extends ViewHolder {
        TextView txtTitle;
        TextView txtReadMore;
        RecyclerView rvList;

        public ListViewHolder(View itemView) {
            super(itemView);
            txtTitle = (TextView) itemView.findViewById(R.id.txt_title);
            rvList = (RecyclerView) itemView.findViewById(R.id.rv_card_list);
            txtReadMore = (TextView) itemView.findViewById(R.id.txt_read_more);
        }
    }


    private Context context;
    private List<Card> cardsList;


    public CardRecyclerAdapter(Context context, List<Card> cardsList) {
        this.context = context;
        this.cardsList = cardsList;
    }


    @Override
    public CardRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View cardView = inflater.inflate(R.layout.list_2_columns, parent, false); ////////

        CardRecyclerAdapter.ViewHolder viewHolder = new CardRecyclerAdapter.ViewHolder(cardView);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(CardRecyclerAdapter.ViewHolder holder, int position) {
    }


    @Override
    public int getItemCount() {
        return cardsList.size();
    }


    @Override
    public int getItemViewType(int position) {
        return cardsList.get(position).getContentType();
    }
}
