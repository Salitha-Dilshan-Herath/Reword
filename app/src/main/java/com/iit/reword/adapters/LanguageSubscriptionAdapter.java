package com.iit.reword.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.iit.reword.R;
import com.iit.reword.model.LanguageDisplay;

import java.util.List;

public class LanguageSubscriptionAdapter extends  RecyclerView.Adapter<LanguageSubscriptionAdapter.LanguageSubscriptionAdapterViewHolder>{

    private List<LanguageDisplay> languageDisplayList;

    public LanguageSubscriptionAdapter(List<LanguageDisplay> languageDisplayList) {
        this.languageDisplayList = languageDisplayList;
    }

    @NonNull
    @Override
    public LanguageSubscriptionAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.languagesubscription_row, parent, false);

        // Return a new holder instance
        LanguageSubscriptionAdapter.LanguageSubscriptionAdapterViewHolder viewHolder = new LanguageSubscriptionAdapter.LanguageSubscriptionAdapterViewHolder(contactView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull LanguageSubscriptionAdapterViewHolder holder, int position) {

        final LanguageDisplay languageDisplay  = languageDisplayList.get(position);
        TextView textView = holder.name;
        textView.setText(languageDisplay.getName());
        holder.cbSubscription.setTag(position);

        holder.cbSubscription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CheckBox cb = (CheckBox)view;
                int clickedPos = (Integer) cb.getTag();

                if(cb.isChecked())
                {
                    languageDisplayList.get(clickedPos).setSubscribe(false);
                }
                else {
                    languageDisplayList.get(clickedPos).setSubscribe(true);
                }

            }
        });
    }

    @Override
    public int getItemCount() {

        if(languageDisplayList != null)
            return languageDisplayList.size();
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

    public class LanguageSubscriptionAdapterViewHolder extends RecyclerView.ViewHolder {

        public View view;
        public TextView name;
        public CheckBox cbSubscription;



        public LanguageSubscriptionAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            this.view      = itemView;
            name           = view.findViewById(R.id.txtLanguage);
            cbSubscription = view.findViewById(R.id.checkBox);
        }
    }
}
