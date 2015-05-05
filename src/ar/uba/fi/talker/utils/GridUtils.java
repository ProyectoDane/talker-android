package ar.uba.fi.talker.utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import ar.uba.fi.talker.R;
import ar.uba.fi.talker.dao.ConversationDAO;
import ar.uba.fi.talker.fragment.GridFragment;
import ar.uba.fi.talker.fragment.ScenesGridFragment;

public final class GridUtils {

	public static List<GridFragment> setGridFragments(ActionBarActivity activity, ArrayList<ConversationDAO> conversationsViews) {

		Iterator<ConversationDAO> it = conversationsViews.iterator();
		int maxImages = calculateImagesPerPage(activity);
		List<GridFragment> gridFragments = new ArrayList<GridFragment>();

		while (it.hasNext()) {
			ArrayList<GridConversationItems> itmLst = new ArrayList<GridConversationItems>();
			for (int i = 0; i < maxImages && it.hasNext(); i++) {
				GridConversationItems itm = new GridConversationItems(i, it.next());
				itmLst.add(itm);
			}
			gridFragments.add(new GridFragment(itmLst, activity));
		}
		return gridFragments;
	}
	
	public static List<ScenesGridFragment> setScenesGridFragments(ActionBarActivity activity, ArrayList<ScenarioView> scenarioViews) {

		Iterator<ScenarioView> it = scenarioViews.iterator();
		int maxImages = calculateImagesPerPage(activity);
		List<ScenesGridFragment> gridFragments = new ArrayList<ScenesGridFragment>();
		int pos = 0;
		while (it.hasNext()) {
			ArrayList<GridItems> itmLst = new ArrayList<GridItems>();
			for (int i = 0; i < maxImages && it.hasNext(); i++) {
				GridItems itm = new GridItems(pos++, it.next());
				itmLst.add(itm);
			}
			gridFragments.add(new ScenesGridFragment(itmLst, activity));
		}
		return gridFragments;
	}

	private static int calculateImagesPerPage(Activity activity) {
		
		float scenarioWidth = activity.getResources().getDimension(R.dimen.scenarioWidth);
		float scenarioHeight = activity.getResources().getDimension(R.dimen.scenarioHeight);
		
		DisplayMetrics displayMetrics = activity.getResources().getDisplayMetrics();

        float dpHeight = displayMetrics.heightPixels;
        float dpWidth = displayMetrics.widthPixels;
		
		float maxImagesFloat =  Math.round(dpHeight/scenarioHeight) *  Math.round(dpWidth /scenarioWidth);
		int maxImages = Math.round(maxImagesFloat);
		
		if (maxImages < 1 ){
			maxImages = 1;
		}
		Log.d(maxImagesFloat+":"+dpHeight+":"+dpWidth+ " width "+ scenarioWidth +" heig " +scenarioHeight, "Densidad");
		return maxImages;
	}
}
