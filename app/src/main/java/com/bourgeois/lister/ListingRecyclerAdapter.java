package com.bourgeois.lister;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class ListingRecyclerAdapter extends FirestoreRecyclerAdapter<Listing, ListingRecyclerAdapter.ListingViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    private final SimpleDateFormat format = new SimpleDateFormat("MM-dd-yy", Locale.US);
    private final OnItemClickListener listener;

    ListingRecyclerAdapter(FirestoreRecyclerOptions<Listing> options, OnItemClickListener listener) {
        super(options);
        this.listener = listener;
    }

    class ListingViewHolder extends RecyclerView.ViewHolder {
        final CardView view;
        final TextView title;
        final TextView price;
        final TextView desc;
        final TextView posted;

        ListingViewHolder(CardView v) {
            super(v);
            view = v;
            title = v.findViewById(R.id.item_title);
            price = v.findViewById(R.id.item_price);
            desc = v.findViewById(R.id.item_desc);
            posted = v.findViewById(R.id.item_posted);
        }
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ListingViewHolder holder, @NonNull int position, @NonNull final Listing list) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        //holder.userId.setText(person.getUserId());
        holder.title.setText(list.getTitle());
        holder.price.setText(Integer.toString(list.getPrice()));
        holder.desc.setText(list.getDesc());
        holder.posted.setText(holder.view.getContext()
                .getString(R.string.created_on, format.format(list.getPosted())));
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(holder.getAdapterPosition());
            }
        });
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ListingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        CardView v = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.listing_item, parent, false);

        return new ListingViewHolder(v);
    }
}



