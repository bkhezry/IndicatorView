package com.github.bkhezry.demo.adapter;

import android.support.v4.app.Fragment;

import com.aspsine.fragmentnavigator.FragmentNavigatorAdapter;
import com.github.bkhezry.demo.BlankFragment;

public class FragmentAdapter implements FragmentNavigatorAdapter {
  private static final String TABS[] = {"basic", "loader", "listview", "about"};

  @Override
  public Fragment onCreateFragment(int position) {
    Fragment mFragment = null;
    switch (position) {
      case 0:
        mFragment = BlankFragment.newInstance("0", 0);
        break;
      case 1:
        mFragment = BlankFragment.newInstance("1", 1);
        break;
      case 2:
        mFragment = BlankFragment.newInstance("2", 2);
        break;
      case 3:
        mFragment = BlankFragment.newInstance("3", 3);
        break;
    }
    return mFragment;
  }

  @Override
  public String getTag(int position) {
    return TABS[position];
  }

  @Override
  public int getCount() {
    return TABS.length;
  }
}
