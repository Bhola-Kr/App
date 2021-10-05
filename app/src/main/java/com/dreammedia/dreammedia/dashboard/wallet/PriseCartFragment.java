package com.dreammedia.dreammedia.dashboard.wallet;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.fragment.app.Fragment;

import com.dreammedia.dreammedia.R;
import com.dreammedia.dreammedia.model.WalletResponse;
import com.zyp.cardview.YcCardView;

public final class PriseCartFragment extends Fragment {

    private TextView title;
    private RelativeLayout card;
    int col[] = {Color.BLUE, Color.GREEN, Color.RED, Color.YELLOW, Color.CYAN};
    private String mContent = "???";
    WalletResponse.Responce list ;

    public static PriseCartFragment newInstance(WalletResponse.Responce list, Context c) {
        PriseCartFragment fragment = new PriseCartFragment();
        fragment.list = list;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup Rootview = (ViewGroup) inflater.inflate(R.layout.fragment_prise_card, container, false);

        TextView tvTitle  = Rootview.findViewById(R.id.tvTitle);
        TextView tvPrise  = Rootview.findViewById(R.id.tvPrise);
        TextView tvYr     = Rootview.findViewById(R.id.tvYr);
        YcCardView lyMain = Rootview.findViewById(R.id.lyMain);

        TextView tvLiblityPrise          = Rootview.findViewById(R.id.tvLiblityPrise);
        TextView tvPersonalPropertyPrise = Rootview.findViewById(R.id.tvPersonalPropertyPrise);
        TextView tvMedicalPrise = Rootview.findViewById(R.id.tvMedicalPrise);

        tvTitle.setText(list.getTitle());
        tvPrise.setText(list.getPrice());

        tvLiblityPrise.setText(list.getMonth()+" Month");
        tvPersonalPropertyPrise.setText(list.getPrice()+" Rs");
        tvMedicalPrise.setText(list.getFeature());

        if (list.getTitle().equals("Sliver")){
            lyMain.setCardBackgroundColor(Color.parseColor("#2eb9fc"));
        }else if (list.getTitle().equals("Platinum")){
            lyMain.setCardBackgroundColor(Color.parseColor("#ff4faf"));
        }else {
            lyMain.setCardBackgroundColor(Color.parseColor("#4cbf48"));
        }

        return Rootview;
    }

}
