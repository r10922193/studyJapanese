package com.example.studyjapanese.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.studyjapanese.R;
import com.example.studyjapanese.WordActivity;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.categoryViewHolder> {
    private String[] chineseCategory;
    private String[] englishCategory;
    private Context mContext;

    public class categoryViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public View view;
        public TextView chineseTextView;
        public TextView englishTextView;
        public categoryViewHolder(View v) {
            super(v);
            view = v;
            chineseTextView = (TextView) v.findViewById(R.id.chineseTextView);
            englishTextView = (TextView) v.findViewById(R.id.englishTextView);
        }
    }

    public CategoryAdapter(Context context, String[] chineseArray, String[] englishArray) {
        chineseCategory = chineseArray;
        englishCategory = englishArray;
        mContext = context;
    }

    @Override
    public CategoryAdapter.categoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = (View) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.category_content, parent, false);
        CategoryAdapter.categoryViewHolder vh = new CategoryAdapter.categoryViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(CategoryAdapter.categoryViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.chineseTextView.setText(chineseCategory[position]);
        holder.englishTextView.setText(englishCategory[position]);
        final String mCategory=englishCategory[position];
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent wordIntent = new Intent(mContext, WordActivity.class);
                wordIntent.putExtra("Category", mCategory);
                mContext.startActivity(wordIntent);
            }
        });

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return chineseCategory.length;
    }



}
