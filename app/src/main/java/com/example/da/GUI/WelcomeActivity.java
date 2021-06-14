package com.example.da.GUI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager2.widget.ViewPager2;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.da.BLL.OnboaringAdapter;
import com.example.da.BLL.OnboaringItem;
import com.example.da.R;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

public class WelcomeActivity extends AppCompatActivity {

    private OnboaringAdapter onboaringAdapter;
    private LinearLayout layoutOnboaringIndicators;
    private MaterialButton btnOnboardingAction;

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        sharedPreferences = getSharedPreferences("welcome", MODE_PRIVATE);

        layoutOnboaringIndicators = findViewById(R.id.layoutOnboaringIndicators);
        btnOnboardingAction = findViewById(R.id.btnOnboardingAction);

        setupOnboardingItems();

        final ViewPager2 onboaringViewPager = findViewById(R.id.onboaringViewPager);
        onboaringViewPager.setAdapter(onboaringAdapter);

        setupOnboardingIndicators();
        setCurrenOnboardingIndicator(0);

        onboaringViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                setCurrenOnboardingIndicator(position);
            }
        });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SharedPreferences preferences = getSharedPreferences("welcome", MODE_PRIVATE);
                if(preferences.getBoolean("isWelcome", false)) {
                        startActivity(new Intent(WelcomeActivity.this, LoginActivity.class));
                }
            }
        }, 0);

        btnOnboardingAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onboaringViewPager.getCurrentItem() + 1 < onboaringAdapter.getItemCount())
                    onboaringViewPager.setCurrentItem(onboaringViewPager.getCurrentItem() + 1);
                else{
                    checkSession();
                }
            }
        });
    }

    private void setupOnboardingItems() {

        List<OnboaringItem> onboaringItems = new ArrayList<>();

        OnboaringItem onboaringReadbook = new OnboaringItem();
        onboaringReadbook.setTilte("Read great stories!!");
        onboaringReadbook.setDescription("You can read many different types of stories: romantic love, funny humor, horror, detective, and many other stories.");
        onboaringReadbook.setImage(R.drawable.readbook);

        OnboaringItem onboaringWritebook = new OnboaringItem();
        onboaringWritebook.setTilte("Create a few stories for myself!!");
        onboaringWritebook.setDescription("You have a scary story you want to save for later telling to others!" +
                "You have just confessed success by heart and want to save as a souvenir." +
                "Or that day, you're sad, looking for a place to talk for comfort. Please write it here !!");
        onboaringWritebook.setImage(R.drawable.writebook);

        OnboaringItem onboaringLoveAndShare = new OnboaringItem();
        onboaringLoveAndShare.setTilte("Like and Share!!");
        onboaringLoveAndShare.setDescription("You have just read that story! You like it, do not hesitate without clicking like! Oh great," +
                " it would make the writer of that story so happy. If you have a funny story, share it with everyone, " +
                "it will be a meaningful gift! And if you're sad, don't be afraid to say it, someone will definitely share it with you!");
        onboaringLoveAndShare.setImage(R.drawable.lovebook);

        onboaringItems.add(onboaringReadbook);
        onboaringItems.add(onboaringWritebook);
        onboaringItems.add(onboaringLoveAndShare);

        onboaringAdapter = new OnboaringAdapter(onboaringItems);
    }

    private void setupOnboardingIndicators(){
        ImageView[] indicators = new ImageView[onboaringAdapter.getItemCount()];
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(8,0,8,0);
        for(int i = 0; i < indicators.length; i++){
            indicators[i] = new ImageView(getApplicationContext());
            indicators[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.onboaring_indicator_inactive));
            layoutOnboaringIndicators.addView(indicators[i]);
        }
    }

    @SuppressLint("SetTextI18n")
    private void setCurrenOnboardingIndicator(int index){
        int childCount = layoutOnboaringIndicators.getChildCount();
        for(int i = 0; i < childCount; i++) {
            ImageView imageView = (ImageView) layoutOnboaringIndicators.getChildAt(i);
            if(i == index)
                imageView.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.onboaring_indicator_active));
            else
                imageView.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.onboaring_indicator_inactive));
        }
        if(index == onboaringAdapter.getItemCount() - 1)
            btnOnboardingAction.setText("Start");
        else
            btnOnboardingAction.setText("Next");
    }

    private void checkSession(){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isWelcome", true);
        editor.commit();
        Intent introIntent = new Intent(WelcomeActivity.this, LoginActivity.class);
        startActivity(introIntent);
        finish();
    }
}