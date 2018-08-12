package com.github.bkhezry.demo;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.github.bkhezry.indoctorview.IndicatorView;
import com.github.bkhezry.indoctorview.MyViewPager;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
  private IndicatorView circleIndicator;
  private FragmentPagerAdapter mAdapter;
  private ArrayList<Fragment> mTabContents = new ArrayList<>();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    circleIndicator = findViewById(R.id.circleIndicator);
    mTabContents.add(BlankFragment.newInstance("0", 0));
    mTabContents.add(BlankFragment.newInstance("1", 1));
    mTabContents.add(BlankFragment.newInstance("2", 2));
    mTabContents.add(BlankFragment.newInstance("3", 3));
    mAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
      @Override
      public int getCount() {
        return mTabContents.size();
      }

      @Override
      public Fragment getItem(int position) {
        return mTabContents.get(position);
      }
    };

    Button next = findViewById(R.id.next);
    next.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        circleIndicator.setPositionWithAnim(circleIndicator.getCurrentPos() - 1);
      }
    });
    Button previous = findViewById(R.id.previous);
    previous.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        circleIndicator.setPositionWithAnim(circleIndicator.getCurrentPos() + 1);
      }
    });
  }
}
