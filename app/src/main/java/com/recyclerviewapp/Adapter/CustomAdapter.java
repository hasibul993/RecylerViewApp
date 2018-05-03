package com.recyclerviewapp.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.recyclerviewapp.Model.Model;
import com.recyclerviewapp.R;

import java.util.List;


public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {


    Context context;
    List<Model> modelArrayList;


    public CustomAdapter(Context context, List<Model> modelArrayList) {
        this.context = context;
        this.modelArrayList = modelArrayList;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_row, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView nameTV, artistTV, albumTV;

        public ViewHolder(View itemView) {
            super(itemView);

            nameTV = (TextView) itemView.findViewById(R.id.nameTV);
            artistTV = (TextView) itemView.findViewById(R.id.artistTV);
            albumTV = (TextView) itemView.findViewById(R.id.albumTV);

        }
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        try {

            Model drugModel = modelArrayList.get(position);
            holder.nameTV.setText(drugModel.getSongName());
            holder.artistTV.setText(drugModel.getArtist());
            holder.albumTV.setText(drugModel.getAlbum());

        } catch (Exception ex) {
            ex.printStackTrace();
        }


    }


    @Override
    public int getItemCount() {
        return modelArrayList != null ? modelArrayList.size() : 0;
    }

    public Model getItem(int position) {
        // TODO Auto-generated method stub
        return modelArrayList.get(position);
    }


    public void UpdateList(List<Model> newList) {
        try {
            modelArrayList = newList;
            notifyDataSetChanged();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


}