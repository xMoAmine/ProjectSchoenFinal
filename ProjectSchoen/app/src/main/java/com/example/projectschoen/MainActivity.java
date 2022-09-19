package com.example.projectschoen;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.lifecycle.ViewModelProvider;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.ImageView;

import com.example.projectschoen.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    private ViewModel mViewModel;

    @BindView(R.id.insole_region_6) ImageView mInsoleRegion6;
    @BindView(R.id.insole_region_7) ImageView mInsoleRegion7;
    @BindView(R.id.insole_region_12) ImageView mInsoleRegion12;
    @BindView(R.id.insole_region_13) ImageView mInsoleRegion13;
    @BindView(R.id.insole_region_14) ImageView mInsoleRegion14;
    @BindView(R.id.insole_region_15) ImageView mInsoleRegion15;
    @BindView(R.id.insole_region_19) ImageView mInsoleRegion19;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mViewModel = new ViewModelProvider(this).get(ViewModel.class);

        mViewModel.getInsoleColor().observe(this, color -> {

            DrawableCompat.setTint(mInsoleRegion6.getDrawable(), Color.parseColor(color[5]));
            DrawableCompat.setTint(mInsoleRegion7.getDrawable(), Color.parseColor(color[6]));
            DrawableCompat.setTint(mInsoleRegion12.getDrawable(), Color.parseColor(color[11]));
            DrawableCompat.setTint(mInsoleRegion13.getDrawable(), Color.parseColor(color[12]));
            DrawableCompat.setTint(mInsoleRegion14.getDrawable(), Color.parseColor(color[13]));
            DrawableCompat.setTint(mInsoleRegion15.getDrawable(), Color.parseColor(color[14]));
            DrawableCompat.setTint(mInsoleRegion19.getDrawable(), Color.parseColor(color[18]));


        });

    }
}
