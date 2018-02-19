package luigi.casciaro.cityparty.adapter.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import luigi.casciaro.cityparty.R;

public class ViewHolderShopAdapter extends RecyclerView.ViewHolder {

    @BindView(R.id.root)
    public View root;

    @BindView(R.id.viewActions)
    public View viewActions;

    @BindView(R.id.imageViewAvatar)
    public ImageView imageViewAvatar;

    @BindView(R.id.imageViewFavourite)
    public ImageView imageViewFavourite;

    @BindView(R.id.textViewName)
    public TextView textViewName;

    @BindView(R.id.textViewAddress)
    public TextView textViewAddress;

    @BindView(R.id.textViewDate)
    public TextView textViewDate;

    @BindView(R.id.textViewLike)
    public TextView textViewLike;

    public ViewHolderShopAdapter(View v) {
        super(v);
        ButterKnife.bind(this, v);
    }
}