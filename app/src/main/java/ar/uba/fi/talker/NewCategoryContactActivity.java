package ar.uba.fi.talker;

import java.io.File;
import java.util.List;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import ar.uba.fi.talker.adapter.PagerScenesAdapter;
import ar.uba.fi.talker.dao.CategoryDAO;
import ar.uba.fi.talker.dao.ImageDAO;
import ar.uba.fi.talker.dataSource.CategoryTalkerDataSource;
import ar.uba.fi.talker.dataSource.ImageTalkerDataSource;
import ar.uba.fi.talker.dto.TalkerDTO;
import ar.uba.fi.talker.fragment.ScenesGridFragment;
import ar.uba.fi.talker.fragment.TextDialogFragment;
import ar.uba.fi.talker.utils.GridUtils;

import com.viewpagerindicator.PageIndicator;

public class NewCategoryContactActivity extends CommonImageSettingsActiviy {

	 	private final CategoryTalkerDataSource categoryDatasource;
		private final ImageTalkerDataSource imageDatasource;
		// private final ContactTalkerDataSource contactDatasource;
		
		public PageIndicator pageIndicator;
		private ViewPager viewPager;
		private PagerScenesAdapter pagerAdapter;
		
		public NewCategoryContactActivity() {
			super();
			categoryDatasource = new CategoryTalkerDataSource(this);
			imageDatasource = new ImageTalkerDataSource(this);
			// contactDatasource = new ContactTalkerDataSource(this);
		}
		
		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.layout_categories);
			categoriesPagerSetting();
			ImageButton createCategoryBttn = (ImageButton) this.findViewById(R.id.add_image);
			createCategoryBttn.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					TextDialogFragment newFragment = new TextDialogFragment(R.string.insert_new_contact_group_name_title);
					newFragment.show(getSupportFragmentManager(), "insert_text");
					categoriesPagerSetting();
				}
			});
		}

		private void categoriesPagerSetting() {
			viewPager = (ViewPager) this.findViewById(R.id.pager);
			pageIndicator = (PageIndicator) this.findViewById(R.id.pagerIndicator);
			
			List<CategoryDAO> allImages = categoryDatasource.getAllContacts();
			for (CategoryDAO categoryDAO : allImages) {
				if (categoryDAO.getImage() != null) {
					categoryDAO.setPath(categoryDAO.getImage().getPath());
				} else {
					categoryDAO.setPath(String.valueOf(R.drawable.history_panel));
				}
			}
			List<ScenesGridFragment> gridFragments = GridUtils.setScenesGridFragments(this, allImages, categoryDatasource);

			pagerAdapter = new PagerScenesAdapter(getSupportFragmentManager(), gridFragments);
			viewPager.setAdapter(pagerAdapter);
			pageIndicator.setViewPager(viewPager);
		}

		@Override
		public void onDialogPositiveClickDeleteResourceDialogListener(
				TalkerDTO categoryView) {
			boolean deleted = true;
			List<ImageDAO> innnerImages = imageDatasource.getImagesForCategory(categoryView.getId());
			for (ImageDAO imageDAO : innnerImages) {
				if (imageDAO.getPath().contains("/")) {
					File file = new File(imageDAO.getPath());
					deleted = file.delete();
				}
			}
			if (deleted){
				CategoryDAO categoryDAO = new CategoryDAO();
				categoryDAO.setId(categoryView.getId());
				categoryDatasource.delete(categoryDAO.getId());
			} else {
				Toast.makeText(this, "Ocurrio un error con la imagen.",	Toast.LENGTH_SHORT).show();
				Log.e("NewScene", "Unexpected error deleting imagen.");
			}
			categoriesPagerSetting();
		}
		
		@Override
		public void onDialogPositiveClickTextDialogListener(DialogFragment dialog) {
			Dialog dialogView = dialog.getDialog();
			EditText inputText = (EditText) dialogView
					.findViewById(R.id.insert_text_input);
			categoryDatasource.createCategory(inputText.getText().toString(), 1);
			categoriesPagerSetting();
		}
}
