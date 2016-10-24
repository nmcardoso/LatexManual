package com.github.nmcardoso.latexmanual;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class AutoCompleteAdapter extends RecyclerView.Adapter<AutoCompleteAdapter.ViewHolder> {
    private List<Documentation> docList;
    private Context context;
    private String query;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtTitle;

        public ViewHolder(View itemView) {
            super(itemView);

            txtTitle = (TextView) itemView.findViewById(R.id.txt_title);
        }
    }

    public AutoCompleteAdapter(Context context, List<Documentation> docList, String query) {
        this.docList = docList;
        this.context = context;
        this.query = query;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.list_auto_complete, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Documentation doc = docList.get(position);

        holder.txtTitle.setText(TextUtil.highlight(doc.getTitle(), query));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DocViewerActivity.class);
                intent.putExtra(DatabaseHelper.DOCUMENTATIONS_FILE_NAME, doc.getFileName());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return docList.size();
    }

    public void swap(List<Documentation> newData, String newQuery) {
        docList.clear();
        docList.addAll(newData);
        query = newQuery;
        notifyDataSetChanged();
    }
}
