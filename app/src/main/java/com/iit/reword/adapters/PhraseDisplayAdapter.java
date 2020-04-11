package com.iit.reword.adapters;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.iit.reword.R;
import com.iit.reword.roomdb.model.Phrase;
import com.iit.reword.utility.interfaces.AdapterClickListener;

import java.util.ArrayList;
import java.util.List;

public class PhraseDisplayAdapter extends RecyclerView.Adapter<PhraseDisplayAdapter.PhraseDisplayViewHolder>  {

    private List<Phrase> phraseArrayList;
    private AdapterClickListener adapterClickListener;
    private int selectedIndex = -1;
    private List<View> viewList = new ArrayList<>();


    public PhraseDisplayAdapter(List<Phrase> phrases, AdapterClickListener listener) {
        this.adapterClickListener = listener;
        this.phraseArrayList = phrases;

    }

    @NonNull
    @Override
    public PhraseDisplayViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.displaypharase_row, parent, false);

        // Return a new holder instance
        PhraseDisplayViewHolder viewHolder = new PhraseDisplayViewHolder(contactView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull PhraseDisplayViewHolder holder, final int position) {

        final Phrase phrase = phraseArrayList.get(position);

        // Set item views based on your views and data model
        TextView textView = holder.name;
        textView.setText(phrase.getPhrase());

        if (!viewList.contains(holder.view)) {
            viewList.add(holder.view);
        }

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                selectedIndex = position;
                if (adapterClickListener!= null){
                    adapterClickListener.onCellClick(phraseArrayList.get(position), position);

                    //All view color is set to colorDefault
                    for(View viw : viewList){
                        viw.setBackground(new ColorDrawable(0xffffffff));
                    }

                    // set color to selected view
                    view.setBackground(new ColorDrawable(0xffcdcdcd));
                }

            }
        });

    }


    @Override
    public int getItemCount() {

        if(phraseArrayList != null)
           return phraseArrayList.size();
        return 0;
    }

    public class PhraseDisplayViewHolder extends RecyclerView.ViewHolder {
        public View view;
        public TextView name;
        public LinearLayout viwBackground;



        public PhraseDisplayViewHolder(@NonNull View itemView) {
            super(itemView);
            this.view = itemView;
            name = view.findViewById(R.id.txtPhrase);
            viwBackground = view.findViewById(R.id.viwBackground);

        }
    }
}




