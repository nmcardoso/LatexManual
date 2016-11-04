package com.github.nmcardoso.latexmanual;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class FullIndexAdapter extends RecyclerView.Adapter<FullIndexAdapter.ViewHolder> {
    private Context context;
    private List<Documentation> docList;
    private String firstChar = "";

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtSection;
        TextView txtTitle;

        public ViewHolder(View itemView) {
            super(itemView);

            txtSection = (TextView) itemView.findViewById(R.id.txt_section);
            txtTitle = (TextView) itemView.findViewById(R.id.txt_title);
        }
    }

    public FullIndexAdapter(Context context, List<Documentation> dataSet) {
        docList = dataSet;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.list_full_index, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Documentation doc = docList.get(position);

        // showing section if first char its changed
        String firstChar = doc.getTitle().substring(0, 1);
        if (!this.firstChar.equalsIgnoreCase(firstChar)) {
            holder.txtSection.setText(firstChar);
            holder.txtSection.setVisibility(View.VISIBLE);
            this.firstChar = firstChar;
        }

        holder.txtTitle.setText(doc.getTitle());
    }

    @Override
    public int getItemCount() {
        return docList.size();
    }

    public void swap(List<Documentation> newDataSet) {
        docList.clear();
        docList.addAll(newDataSet);
        notifyDataSetChanged();

        // Refresh first char for section
        firstChar = "";
    }
}
