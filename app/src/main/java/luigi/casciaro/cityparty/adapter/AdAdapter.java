package luigi.casciaro.cityparty.adapter;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import java.util.ArrayList;

import luigi.casciaro.cityparty.AppController;
import luigi.casciaro.cityparty.R;
import luigi.casciaro.cityparty.adapter.viewholder.ViewHolderShopAdapter;
import luigi.casciaro.cityparty.contract.AdActionsContract;
import luigi.casciaro.cityparty.model.Ad;
import luigi.casciaro.cityparty.view.AdDetailActivity;
import luigi.casciaro.cityparty.view.MainActivity;

public class AdAdapter extends RecyclerView.Adapter<ViewHolderShopAdapter> implements Filterable {

    public ArrayList<Ad> items, itemsBck;
    private AdActionsContract owner;
    private boolean manageActions;

    public AdAdapter(ArrayList<Ad> items, boolean manageActions, AdActionsContract activityParent) {
        this.items = items;
        this.itemsBck = items;
        this.owner = activityParent;
        this.manageActions = manageActions;
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

        if(manageActions) {
            // show
            viewHolder.viewActions.setVisibility(View.VISIBLE);

            // check
            if (isLiked[0])
                viewHolder.imageViewSmile.setImageDrawable(ContextCompat.getDrawable(AppController.getInstance(), R.drawable.like_active_24dp));
            else
                viewHolder.imageViewSmile.setImageDrawable(ContextCompat.getDrawable(AppController.getInstance(), R.drawable.like_24dp));

        }else{
            // hide
            viewHolder.viewActions.setVisibility(View.GONE);
        }

        viewHolder.imageViewAvatar.setImageBitmap(items.get(position).getImage());

        // listener
        viewHolder.root.setOnClickListener(v -> {
            // current clicked
            AppController.getInstance().setCurrentAd(items.get(position));
            // go to
            AppController.getInstance().startActivity(new Intent(AppController.getInstance(), AdDetailActivity.class));
        });
        // smile
        viewHolder.imageViewSmile.setOnClickListener(v -> owner.onSmilePressed(items.get(position)));
        // call
        viewHolder.textViewCall.setOnClickListener(v -> owner.onCallPressed(items.get(position)));
        // share
        viewHolder.textViewShare.setOnClickListener(v -> owner.onSharePressed(items.get(position)));
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