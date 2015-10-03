package ar.uba.fi.talker;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import ar.uba.fi.talker.CommonImageSettingsActiviy;
import ar.uba.fi.talker.R;
import ar.uba.fi.talker.adapter.PagerScenesAdapter;
import ar.uba.fi.talker.component.indicator.PageIndicator;
import ar.uba.fi.talker.dao.CategoryDAO;
import ar.uba.fi.talker.dao.ImageDAO;
import ar.uba.fi.talker.dataSource.CategoryTalkerDataSource;
import ar.uba.fi.talker.dataSource.ImageTalkerDataSource;
import ar.uba.fi.talker.dto.TalkerDTO;
import ar.uba.fi.talker.fragment.ScenesGridFragment;
import ar.uba.fi.talker.fragment.TextDialogFragment;
import ar.uba.fi.talker.utils.GridUtils;
import ar.uba.fi.talker.utils.ImageUtils;
import ar.uba.fi.talker.utils.ResultConstant;

public class NewCategoryImageActivity extends CommonImageSettingsActiviy {

    private final CategoryTalkerDataSource categoryDatasource;
    private final ImageTalkerDataSource imageDatasource;

    public NewCategoryImageActivity() {
        super();
        categoryDatasource = new CategoryTalkerDataSource(this);
        imageDatasource = new ImageTalkerDataSource(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.layout_images);

        categoriesPagerSetting();
        View createCategoryBttn = this.findViewById(R.id.new_image_button);
        createCategoryBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextDialogFragment newFragment = new TextDialogFragment(R.string.insert_new_categ_name_title);
                newFragment.show(getSupportFragmentManager(), "insert_text");
                categoriesPagerSetting();
            }
        });
    }

    private void categoriesPagerSetting() {
        ViewPager viewPager = (ViewPager) this.findViewById(R.id.pager);
        PageIndicator pageIndicator = (PageIndicator) this.findViewById(R.id.pagerIndicator);

        List<CategoryDAO> allImages = categoryDatasource.getImageCategories();
        for (CategoryDAO categoryDAO : allImages) {
            if (categoryDAO.getImage() != null) {
                categoryDAO.setPath(categoryDAO.getImage().getPath());
            } else {
                categoryDAO.setPath(String.valueOf(R.drawable.history_panel));
            }
        }
        List<ScenesGridFragment> gridFragments = GridUtils.setScenesGridFragments(this, allImages, categoryDatasource);

        PagerScenesAdapter pagerAdapter = new PagerScenesAdapter(this.getSupportFragmentManager(), gridFragments);
        viewPager.setAdapter(pagerAdapter);
        pageIndicator.setViewPager(viewPager);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ResultConstant.RESULT_LOAD_IMAGE && null != data) {
            Uri imageUri = data.getData();
            String categoryName = imageUri.getLastPathSegment();
            Bitmap bitmap = null;
            try {/*Entra al if cuando se elige una foto de google +*/
                if (imageUri != null && imageUri.getHost().contains("com.google.android.apps.photos.content")) {
                    InputStream is = getContentResolver().openInputStream(imageUri);
                    bitmap = BitmapFactory.decodeStream(is);
                } else {
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                }
                categoryName = "IMAGE_" + String.valueOf(imageDatasource.getLastImageID() + 1);
                Context ctx = this.getApplicationContext();
                ImageUtils.saveFileInternalStorage(categoryName, bitmap, ctx, 0);
                categoriesPagerSetting();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onDialogPositiveClickDeleteResourceDialogListener(TalkerDTO categoryView) {
        boolean deleted = true;
        List<ImageDAO> innnerImages = imageDatasource.getImagesForCategory(categoryView.getId());
        for (ImageDAO imageDAO : innnerImages) {
            if (imageDAO.getPath().contains("/")) {
                File file = new File(imageDAO.getPath());
                deleted = file.delete();
            }
        }
        if (deleted) {
            CategoryDAO categoryDAO = new CategoryDAO();
            categoryDAO.setId(categoryView.getId());
            categoryDatasource.delete(categoryDAO.getId());
        } else {
            Toast.makeText(this, "Ocurrio un error con la imagen.", Toast.LENGTH_SHORT).show();
            Log.e("NewScene", "Unexpected error deleting imagen.");
        }
        categoriesPagerSetting();
    }

    @Override
    public void onDialogPositiveClickTextDialogListener(DialogFragment dialog) {
        Dialog dialogView = dialog.getDialog();
        EditText inputText = (EditText) dialogView
                .findViewById(R.id.insert_text_input);

        categoryDatasource.createCategory(inputText.getText().toString(), 0);
        categoriesPagerSetting();
    }

}
