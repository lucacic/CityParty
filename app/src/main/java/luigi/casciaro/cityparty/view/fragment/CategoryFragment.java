package luigi.casciaro.cityparty.view.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import luigi.casciaro.cityparty.AppController;
import luigi.casciaro.cityparty.R;
import luigi.casciaro.cityparty.controller.RealmController;
import luigi.casciaro.cityparty.model.Category;
import luigi.casciaro.cityparty.util.MyUtil;
import luigi.casciaro.cityparty.view.MainActivity;

public class CategoryFragment extends Fragment {

    @BindView(R.id.root)
    RelativeLayout root;

    @BindView(R.id.progressBar)
    View progressBar;

    @BindView(R.id.linearLayoutCenter)
    LinearLayout linearLayoutCenter;

    public static MainActivity mainActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category, container, false);
        ButterKnife.bind(this, view);
        setHasOptionsMenu(false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        MyUtil.print("CATEGORIES -> " + RealmController.getCategories().size());

        for(Category category : RealmController.getCategories()){

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 400);
            params.setMargins(8, 8, 8, 0);

            Button button = new Button(getActivity());
            button.setText(category.getName());
            button.setTextSize(30);
            button.setTextColor(Color.WHITE);
            button.setGravity(Gravity.CENTER);
            button.setBackground(ContextCompat.getDrawable(getActivity(), category.getImage()));
            button.setLayoutParams(params);
            button.setOnClickListener(v -> {
                // set category
                AppController.getInstance().setFilterByCategory(category);
                // scroll to Home
                mainActivity.viewPager.setCurrentItem(0);
            });

            linearLayoutCenter.addView(button);
        }

        progressBar.setVisibility(View.GONE);
    }
}