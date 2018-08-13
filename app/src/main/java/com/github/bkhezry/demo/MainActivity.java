package com.github.bkhezry.demo;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.aspsine.fragmentnavigator.FragmentNavigator;
import com.github.bkhezry.demo.adapter.FragmentAdapter;
import com.github.bkhezry.indicatorview.IndicatorView;


public class MainActivity extends AppCompatActivity {
  private IndicatorView circleIndicator;
  private int index = 3;
  private FragmentNavigator fragmentNavigator;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    circleIndicator = findViewById(R.id.circleIndicator);
    fragmentNavigator = new FragmentNavigator(getSupportFragmentManager(), new FragmentAdapter(), R.id.contentFrameLayout);
    fragmentNavigator.showFragment(index);
    Button next = findViewById(R.id.next);
    next.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if (index != 0) {
          index--;
          fragmentNavigator.showFragment(index);
          Handler handler = new Handler();
          handler.postDelayed(new Runnable() {
            @Override
            public void run() {
              circleIndicator.setPositionWithAnim(circleIndicator.getCurrentPos() - 1);
            }
          }, 300);

        }
      }
    });
    Button previous = findViewById(R.id.previous);
    previous.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if (index != 3) {
          index++;
          fragmentNavigator.showFragment(index);
          Handler handler = new Handler();
          handler.postDelayed(new Runnable() {
            @Override
            public void run() {
              circleIndicator.setPositionWithAnim(circleIndicator.getCurrentPos() + 1);
            }
          }, 300);
        }
      }
    });
  }
}
