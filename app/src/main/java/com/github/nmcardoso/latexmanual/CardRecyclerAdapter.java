package com.github.nmcardoso.latexmanual;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
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
        TextView txtViewMore;
        RecyclerView rvList;

        public ListViewHolder(View itemView) {
            super(itemView);
            txtTitle = (TextView) itemView.findViewById(R.id.txt_title);
            rvList = (RecyclerView) itemView.findViewById(R.id.rv_card_list);
            txtViewMore = (TextView) itemView.findViewById(R.id.txt_view_more);
        }
    }


    public class TextViewHolder extends ViewHolder {
        TextView txtTitle;
        TextView txtContent;

        public TextViewHolder(View itemView) {
            super(itemView);
            txtTitle = (TextView) itemView.findViewById(R.id.txt_title);
            txtContent = (TextView) itemView.findViewById(R.id.txt_content);
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
        ViewHolder viewHolder = null;

        switch (viewType) {
            case Card.LIST:
                View listView = inflater.inflate(R.layout.cardview_list, parent, false);
                viewHolder = new ListViewHolder(listView);
                break;
            case Card.TEXT:
                View textView = inflater.inflate(R.layout.cardview_text, parent, false);
                viewHolder = new TextViewHolder(textView);
                break;
        }
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(CardRecyclerAdapter.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case Card.LIST:
                ListViewHolder listViewHolder = (ListViewHolder) holder;
                configureListView(listViewHolder, position);
                break;
            case Card.TEXT:
                TextViewHolder textViewHolder = (TextViewHolder) holder;
                configureTextView(textViewHolder, position);
                break;
        }
    }


    @Override
    public int getItemCount() {
        return cardsList.size();
    }


    @Override
    public int getItemViewType(int position) {
        return cardsList.get(position).getContentType();
    }


    private void configureListView(ListViewHolder viewHolder, int position) {
        Card card = cardsList.get(position);

        viewHolder.txtTitle.setText(card.getTitle());
        viewHolder.txtTitle.setCompoundDrawablesWithIntrinsicBounds(card.getHeaderIcon(), 0, 0, 0);
        viewHolder.txtTitle.setBackgroundResource(card.getHeaderBackground());
        CardListAdapter listAdapter = new CardListAdapter(context,
                card.getContentList(), card.getListItemType());
        viewHolder.rvList.setAdapter(listAdapter);
        viewHolder.rvList.setLayoutManager(new LinearLayoutManager(context));
        viewHolder.txtViewMore.setVisibility(card.hasViewMoreButton() ? View.VISIBLE : View.GONE);
    }


    private void configureTextView(TextViewHolder viewHolder, int position) {
        Card card = cardsList.get(position);

        viewHolder.txtTitle.setText(card.getTitle());
        viewHolder.txtTitle.setCompoundDrawablesWithIntrinsicBounds(card.getHeaderIcon(), 0, 0, 0);
        viewHolder.txtTitle.setBackgroundResource(card.getHeaderBackground());
        viewHolder.txtContent.setText(card.getContentText());
    }
}
