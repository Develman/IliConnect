/**
 *
 */
package com.android.iliConnect;

import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;


public class PagerAdapter extends FragmentPagerAdapter {
	
	
	private List<Fragment> fragments;
	
	public PagerAdapter(FragmentManager fm, List<Fragment> fragments) {
		super(fm);
		this.fragments = fragments;
	}
	/*c c
	 * @see android.support.v4.app.FragmentPagerAdapter#getItem(int)
	 */
	@Override
	public Fragment getItem(int position) {
		return this.fragments.get(position);
	}

	/* 
	 * @see android.support.v4.view.PagerAdapter#getCount()
	 */
	@Override
	public int getCount() {
		return this.fragments.size();
	}
	
	public void updateFragment(int position){
		fragments.set(position,Fragment.instantiate(MainActivity.instance, fragments.get(position).getClass().getName())); 
		
	}
}
