package ar.uba.fi.talker;

import java.io.File;
import java.util.List;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.widget.Toast;
import ar.uba.fi.talker.adapter.PagerScenesAdapter;
import ar.uba.fi.talker.dao.ConversationDAO;
import ar.uba.fi.talker.dataSource.ConversationTalkerDataSource;
import ar.uba.fi.talker.dto.TalkerDTO;
import ar.uba.fi.talker.fragment.DeleteResourceConfirmationDialogFragment.DeleteResourceDialogListener;
import ar.uba.fi.talker.fragment.ScenesGridFragment;
import ar.uba.fi.talker.utils.GridUtils;

import com.viewpagerindicator.PageIndicator;

public class HistoricalActivity extends ActionBarActivity implements DeleteResourceDialogListener {

    private ConversationTalkerDataSource datasource;
	public PageIndicator pageIndicator;
	private ViewPager viewPager;
	private PagerScenesAdapter pagerAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_hist_conversations);
		conversationPagerSetting();
	}

	private void conversationPagerSetting() {
		viewPager = (ViewPager) this.findViewById(R.id.pager);
		pageIndicator = (PageIndicator) this.findViewById(R.id.pagerIndicator);

		datasource = new ConversationTalkerDataSource(this);
		List<TalkerDTO> allImages = datasource.getAll();
		List<ScenesGridFragment> gridFragments = GridUtils.setScenesGridFragments(this, allImages, datasource);

		pagerAdapter = new PagerScenesAdapter(getSupportFragmentManager(), gridFragments);
		viewPager.setAdapter(pagerAdapter);
		pageIndicator.setViewPager(viewPager);
	}

	@Override
	public void onDialogPositiveClickDeleteResourceDialogListener(
			TalkerDTO scenarioView) {
		
		boolean deleted = true;
		if (scenarioView.getPath().contains("/")) {
			File file = new File(scenarioView.getPath());
			deleted = file.delete();
		}
		if (deleted){
			ConversationDAO conversation = new ConversationDAO();
			conversation.setId(scenarioView.getId()); 
			datasource.delete(conversation.getId());
		} else {
			Toast.makeText(this, "Ocurrio un error con la imagen.",	Toast.LENGTH_SHORT).show();
			Log.e("NewScene", "Unexpected error deleting imagen.");
		}
		conversationPagerSetting();
	}
}
