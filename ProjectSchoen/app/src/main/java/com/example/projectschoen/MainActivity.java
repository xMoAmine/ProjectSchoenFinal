//@author: Mohamed Amine Sahla
//Applicatie voor Project Schoen(Project TI)

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

    @BindView(R.id.insole_region_1) ImageView mInsoleRegion1;
    @BindView(R.id.insole_region_2) ImageView mInsoleRegion2;
    @BindView(R.id.insole_region_3) ImageView mInsoleRegion3;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mViewModel = new ViewModelProvider(this).get(ViewModel.class);

        mViewModel.getInsoleColor().observe(this, color -> {

            DrawableCompat.setTint(mInsoleRegion1.getDrawable(), Color.parseColor(color[3]));
            DrawableCompat.setTint(mInsoleRegion2.getDrawable(), Color.parseColor(color[4]));
            DrawableCompat.setTint(mInsoleRegion3.getDrawable(), Color.parseColor(color[6]));


        });

    }
}
