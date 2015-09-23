package ar.uba.fi.talker.adapter;

import java.util.List;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import ar.uba.fi.talker.fragment.GridFragment;

public class PagerAdapter extends FragmentStatePagerAdapter {
	
	private List<GridFragment> fragments;

	public PagerAdapter(FragmentManager fm, List<GridFragment> fragments) {
		super(fm);
		this.fragments = fragments;
	}

	@Override
	public GridFragment getItem(int position) {
		return this.fragments.get(position);
	}

	@Override
	public int getCount() {
		return this.fragments.size();
	}

}
