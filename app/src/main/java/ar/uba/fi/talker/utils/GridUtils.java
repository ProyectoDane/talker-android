package ar.uba.fi.talker.utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.app.Activity;
import android.util.DisplayMetrics;
import android.util.Log;
import ar.uba.fi.talker.R;
import ar.uba.fi.talker.dataSource.TalkerDataSource;
import ar.uba.fi.talker.dto.TalkerDTO;
import ar.uba.fi.talker.fragment.ScenesGridFragment;

public final class GridUtils {
	
	public static List<ScenesGridFragment> setScenesGridFragments(
			Activity activity, 
			List<? extends TalkerDTO> gridElementViews, 
			TalkerDataSource dao) {

		Iterator<? extends TalkerDTO> it = gridElementViews.iterator();
		int maxImages = calculateImagesPerPage(activity);
		List<ScenesGridFragment> gridFragments = new ArrayList<ScenesGridFragment>();
		while (it.hasNext()) {
			List<GridItems> itmLst = new ArrayList<GridItems>();
			for (int i = 0; i < maxImages && it.hasNext(); i++) {
				itmLst.add(new GridItems(i, it.next()));
			}
			ScenesGridFragment fragment = new ScenesGridFragment();
			fragment.init(itmLst, dao);
			gridFragments.add(fragment);
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
