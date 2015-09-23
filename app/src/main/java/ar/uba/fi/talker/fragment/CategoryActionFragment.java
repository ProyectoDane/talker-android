package ar.uba.fi.talker.fragment;

import android.app.ActionBar.LayoutParams;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.BaseAdapter;
import ar.uba.fi.talker.CanvasActivity;
import ar.uba.fi.talker.R;
import ar.uba.fi.talker.dataSource.CategoryTalkerDataSource;
import ar.uba.fi.talker.dataSource.TalkerDataSource;
import ar.uba.fi.talker.dto.TalkerDTO;
import ar.uba.fi.talker.utils.GridItems;

public class CategoryActionFragment extends DialogFragment implements OnClickListener {

	private final GridItems gridItem;
	private final View view;
	private final BaseAdapter adapter;

	public CategoryActionFragment(GridItems gridItems, View view, BaseAdapter adapter) {
		this.gridItem = gridItems;
		this.view = view;
		this.adapter = adapter;
	}
	
	@Override
	public void onStart() {
		super.onStart();
		Window window = getDialog().getWindow();
		window.setLayout(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
	}
	
	@Override
	public void dismiss() {
		this.view.setBackgroundColor(Color.WHITE);
		super.dismiss();
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {		
		View actions = View.inflate(getActivity(), R.layout.scenario_actions, null);

		View backBttn = actions.findViewById(R.id.sceneActionBack);
		backBttn.setOnClickListener(this);
		View editNameScenarioBttn = actions.findViewById(R.id.new_scene_edit_scenario_name);
		editNameScenarioBttn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ChangeNameDialogFragment newFragment = new ChangeNameDialogFragment();
				TalkerDataSource dao = new CategoryTalkerDataSource(getActivity());
				newFragment.init(gridItem.getElementGridView(), adapter, dao);
				newFragment.onAttach(getActivity());
				newFragment.show(getActivity().getSupportFragmentManager(), "insert_text");
				CategoryActionFragment.this.dismiss();
			}
		});

		View startScenarioBttn = actions.findViewById(R.id.new_scene_start);
		startScenarioBttn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				
				Bundle extras = new Bundle();
				TalkerDTO scenarioView = gridItem.getElementGridView();
				if (scenarioView.getPath() != null && scenarioView.getPath().contains("/")){
					extras.putString("path", scenarioView.getPath());
				} else {
					int idCode = Integer.valueOf(scenarioView.getPath());
					extras.putInt("code", idCode);
				}
				Intent intent = new Intent(getActivity().getApplicationContext(), CanvasActivity.class);
				intent.putExtras(extras);
				startActivity(intent);
				CategoryActionFragment.this.dismiss();
			}
		});
		
		View deleteResourceBttn = actions.findViewById(R.id.new_scene_delete_scenario_name);
		deleteResourceBttn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				DialogFragment newFragment = new DeleteResourceConfirmationDialogFragment
						(gridItem.getElementGridView(), R.string.delete_resource_title, R.string.delete_resource_message);
				newFragment.onAttach(getActivity());
				newFragment.show(getActivity().getSupportFragmentManager(), "delete_scenario");
				CategoryActionFragment.this.dismiss();
			}
		});

		// Use the Builder class for convenient dialog construction
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setView(actions);
		return builder.create();
	}
	
	@Override
	public void onClick(View v) {
		this.dismiss();
	}
	
}
