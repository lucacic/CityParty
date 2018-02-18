package luigi.casciaro.cityparty.adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import java.util.ArrayList;

import io.realm.Realm;
import luigi.casciaro.cityparty.AppController;
import luigi.casciaro.cityparty.R;
import luigi.casciaro.cityparty.adapter.viewholder.ViewHolderShopAdapter;
import luigi.casciaro.cityparty.model.Ad;
import luigi.casciaro.cityparty.view.AdDetailActivity;
import luigi.casciaro.cityparty.view.MainActivity;

public class AdAdapter extends RecyclerView.Adapter<ViewHolderShopAdapter> implements Filterable {

    public ArrayList<Ad> items, itemsBck;
    private Activity owner;
    private boolean manageFavourites;

    public AdAdapter(ArrayList<Ad> items, boolean manageFavourites, Activity activityParent) {
        this.items = items;
        this.itemsBck = items;
        this.owner = activityParent;
        this.manageFavourites = manageFavourites;
    }

    @Override
    public ViewHolderShopAdapter onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_ad, viewGroup, false);
        return new ViewHolderShopAdapter(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolderShopAdapter viewHolder, int position) {

        final boolean[] isFavourite = {MainActivity.userLogged.isInFavourites(items.get(position))};
        final boolean[] isLiked = {MainActivity.userLogged.isInLiked(items.get(position))};

        viewHolder.textViewName.setText(items.get(position).getFirstRow());
        viewHolder.textViewDate.setText(items.get(position).getDate_toString());
        viewHolder.textViewAddress.setText(items.get(position).getAddress());

        if(manageFavourites) {
            // show
            viewHolder.imageViewFavourite.setVisibility(View.VISIBLE);
            // check
            if (isFavourite[0])
                viewHolder.imageViewFavourite.setImageDrawable(ContextCompat.getDrawable(AppController.getInstance(), R.drawable.ic_favourite_active));
            else
                viewHolder.imageViewFavourite.setImageDrawable(ContextCompat.getDrawable(AppController.getInstance(), R.drawable.ic_favourite));

            // like check
            if (isLiked[0])
                viewHolder.textViewLike.setTypeface(null, Typeface.BOLD);
            else
                viewHolder.textViewLike.setTypeface(null, Typeface.NORMAL);

        }else{
            // hide
            viewHolder.imageViewFavourite.setVisibility(View.GONE);
        }

        viewHolder.imageViewAvatar.setBackground(ContextCompat.getDrawable(AppController.getInstance().getApplicationContext(), items.get(position).getImage()));

        // listener
        viewHolder.root.setOnClickListener(v -> {
            // current clicked
            AppController.getInstance().setCurrentAd(items.get(position));
            // go to
            owner.startActivity(new Intent(owner, AdDetailActivity.class));
        });
        // favourite
        viewHolder.imageViewFavourite.setOnClickListener(v -> {
            // favourite?
            if(isFavourite[0]){
                // yes
                viewHolder.imageViewFavourite.setImageDrawable(ContextCompat.getDrawable(AppController.getInstance(), R.drawable.ic_favourite));
                isFavourite[0] = false;
                // remove from favourite
                try(Realm r = Realm.getDefaultInstance()) {
                    r.executeTransaction((realm) -> {
                        // write on realm refreshed data
                        MainActivity.userLogged.removeFromFavourites(items.get(position));
                    });
                }
            } else{
                // no
                viewHolder.imageViewFavourite.setImageDrawable(ContextCompat.getDrawable(AppController.getInstance(), R.drawable.ic_favourite_active));
                isFavourite[0] = true;
                // add to favourite
                try(Realm r = Realm.getDefaultInstance()) {
                    r.executeTransaction((realm) -> {
                        // write on realm refreshed data
                        MainActivity.userLogged.getFavourites().add(items.get(position));
                    });
                }
            }
        });
        // favourite
        viewHolder.textViewLike.setOnClickListener(v -> {
            // favourite?
            if(isLiked[0]){
                // yes
                viewHolder.textViewLike.setTypeface(null, Typeface.NORMAL);
                isLiked[0] = false;
                // remove from liked
                try(Realm r = Realm.getDefaultInstance()) {
                    r.executeTransaction((realm) -> {
                        // write on realm refreshed data
                        MainActivity.userLogged.removeFromLiked(items.get(position));
                    });
                }
            } else{
                // no
                viewHolder.textViewLike.setTypeface(null, Typeface.BOLD);
                isLiked[0] = true;
                // add to favourite
                try(Realm r = Realm.getDefaultInstance()) {
                    r.executeTransaction((realm) -> {
                        // write on realm refreshed data
                        MainActivity.userLogged.getLiked().add(items.get(position));
                    });
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public Filter getFilter() {

        return new Filter() {

            private ArrayList<Ad> orig;

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {

                final FilterResults oReturn = new FilterResults();
                final ArrayList<Ad> results = new ArrayList();

                constraint = constraint.toString().toLowerCase().trim();

                if (orig == null) orig = items;

                if (constraint.length() > 0) {
                    for (Ad item : itemsBck)
                        if (item.getName().toLowerCase().contains(constraint.toString()) || item.getAddress().toLowerCase().contains(constraint.toString()))
                            results.add(item);

                    oReturn.values = results;
                } else oReturn.values = itemsBck;

                return oReturn;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                items = (ArrayList<Ad>) results.values;
                notifyDataSetChanged();
            }
        };
    }
}