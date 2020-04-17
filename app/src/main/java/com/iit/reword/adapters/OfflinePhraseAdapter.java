package com.iit.reword.adapters;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.iit.reword.R;
import com.iit.reword.model.OfflineTranslate;
import com.iit.reword.model.TranslateModel;
import com.iit.reword.utility.interfaces.AdapterClickListener;
import com.iit.reword.utility.interfaces.OfflineAdapterCellListener;

import java.util.ArrayList;
import java.util.List;

public class OfflinePhraseAdapter extends  RecyclerView.Adapter<OfflinePhraseAdapter.OfflinePhraseAdapterViewHolder> {

    private List<TranslateModel> offlineTranslateList;
    private OfflineAdapterCellListener adapterClickListener;
    private int selectedIndex = -1;
    private List<View> viewList = new ArrayList<>();

    public OfflinePhraseAdapter(List<TranslateModel> offlineTranslateList, OfflineAdapterCellListener listener) {
        this.offlineTranslateList = offlineTranslateList;
        this.adapterClickListener = listener;
    }

    @NonNull
    @Override
    public OfflinePhraseAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.phrase_offline_row, parent, false);

        // Return a new holder instance
        OfflinePhraseAdapter.OfflinePhraseAdapterViewHolder viewHolder = new OfflinePhraseAdapter.OfflinePhraseAdapterViewHolder(contactView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull OfflinePhraseAdapterViewHolder holder, int position) {
        final TranslateModel offlineTranslate  = offlineTranslateList.get(position);
        holder.txtEnglish.setText(offlineTranslate.getWord());

        if (offlineTranslate.getTranslatedWord() == null){
            holder.txtTranslate.setText("Not Available");
        }else {
            holder.txtTranslate.setText(offlineTranslate.getTranslatedWord());
        }

        if (!viewList.contains(holder.view)) {
            viewList.add(holder.view);
        }

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                selectedIndex = position;
                if (adapterClickListener!= null){
                    adapterClickListener.onCellClick(offlineTranslateList.get(position), position);

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
        if(offlineTranslateList != null)
            return offlineTranslateList.size();
        return 0;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return  position;
    }

    public class OfflinePhraseAdapterViewHolder extends RecyclerView.ViewHolder {

        public View view;
        public TextView txtEnglish;
        public TextView txtTranslate;



        public OfflinePhraseAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            this.view      = itemView;
            txtEnglish     = view.findViewById(R.id.txtOriginalPhrase);
            txtTranslate   = view.findViewById(R.id.txtTranslatePhrase);
        }
    }

}
