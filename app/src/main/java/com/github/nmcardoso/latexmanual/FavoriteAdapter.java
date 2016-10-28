package com.github.nmcardoso.latexmanual;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.ViewHolder> {
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView leftText;
        public Button btnRemove;

        public ViewHolder(View itemView) {
            super(itemView);

            leftText = (TextView) itemView.findViewById(R.id.txt_left);
            btnRemove = (Button) itemView.findViewById(R.id.btn_remove);
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

        View favoriteView = inflater.inflate(R.layout.list_favorites, parent, false);

        ViewHolder viewHolder = new ViewHolder(favoriteView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(FavoriteAdapter.ViewHolder holder, final int position) {
        final Favorite favorite = favList.get(position);

        TextView leftText = holder.leftText;
        leftText.setText(favorite.getDocumentation().getTitle());

        Button btnRemove = holder.btnRemove;
        btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (i == DialogInterface.BUTTON_POSITIVE) {
                            DatabaseHelper dbHelper = new DatabaseHelper(context);
                            dbHelper.deleteFavorite(favorite.getDocumentation().getId());
                            Toast.makeText(context, context.getResources().getString(R.string.fav_removed),
                                    Toast.LENGTH_SHORT).show();
                            dialogInterface.dismiss();
                            removeAt(position);
                        } else {
                            dialogInterface.dismiss();
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle(context.getString(R.string.confirm_deletion))
                        .setMessage(Html.fromHtml(String.format(context.getString(
                                R.string.remove_doc_from_favorites),
                                favorite.getDocumentation().getTitle())))
                        .setPositiveButton(context.getString(R.string.yes), dialogClickListener)
                        .setNegativeButton(context.getString(R.string.no), dialogClickListener)
                        .show();
            }
        });

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

    @Override
    public int getItemCount() {
        return favList.size();
    }

    private void removeAt(int position) {
        favList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, favList.size());
    }

    public void swap(List<Favorite> favList) {
        this.favList.clear();
        this.favList.addAll(favList);
        notifyDataSetChanged();
    }
}
