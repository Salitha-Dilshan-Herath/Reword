package com.iit.reword.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.iit.reword.R;
import com.iit.reword.roomdb.model.Phrase;
import com.iit.reword.utility.interfaces.EditPhraseRadioClickListener;

import java.util.List;

public class PhraseEditAdapter extends RecyclerView.Adapter<PhraseEditAdapter.PhraseEditViewHolder>   {

    private List<Phrase> phraseArrayList;

    public static RadioButton lastCheck = null;
    public static int lastCheckIndex    = -1;
    private EditPhraseRadioClickListener phraseRadioClickListener;

    public PhraseEditAdapter(List<Phrase> phrases, EditPhraseRadioClickListener editPhraseRadioClickListener) {
        this.phraseArrayList = phrases;
        this.phraseRadioClickListener = editPhraseRadioClickListener;
    }

    @NonNull
    @Override
    public PhraseEditAdapter.PhraseEditViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.editphrase_row, parent, false);

        // Return a new holder instance
        PhraseEditAdapter.PhraseEditViewHolder viewHolder = new PhraseEditAdapter.PhraseEditViewHolder(contactView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull PhraseEditAdapter.PhraseEditViewHolder holder, int position) {

        final Phrase phrase = phraseArrayList.get(position);

        TextView textView = holder.name;
        textView.setText(phrase.getPhrase());
        holder.isChecked.setTag(position);

        holder.isChecked.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                RadioButton rb = (RadioButton)v;
                int clickedPos = (Integer) rb.getTag();

                if(rb.isChecked())
                {
                    if(lastCheck != null)
                    {
                        lastCheck.setChecked(false);
                        phraseArrayList.get(lastCheckIndex).setSelected(false);
                    }

                    lastCheck = rb;
                    lastCheckIndex = clickedPos;
                }
                else
                    lastCheck = null;

                phraseArrayList.get(clickedPos).setSelected(rb.isChecked());
                phraseRadioClickListener.onRadioClick(phraseArrayList.get(clickedPos), clickedPos);
            }
        });
    }

    @Override
    public int getItemCount() {

        if(phraseArrayList != null)
            return phraseArrayList.size();
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

    public class PhraseEditViewHolder extends RecyclerView.ViewHolder {
        public View view;
        public TextView name;
        public RadioButton isChecked;


        public PhraseEditViewHolder(@NonNull View itemView) {
            super(itemView);
            this.view = itemView;
            name = view.findViewById(R.id.txtPhraseEdit);
            isChecked = view.findViewById(R.id.radioIsCheck);
        }
    }
}
