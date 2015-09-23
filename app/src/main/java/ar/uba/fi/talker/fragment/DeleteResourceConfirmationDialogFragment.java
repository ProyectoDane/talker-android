package ar.uba.fi.talker.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import ar.uba.fi.talker.R;
import ar.uba.fi.talker.dto.TalkerDTO;

public class DeleteResourceConfirmationDialogFragment extends ParentDialogFragment {

	private final Integer title;
	private final Integer message;

	public interface DeleteResourceDialogListener {
		public void onDialogPositiveClickDeleteResourceDialogListener(TalkerDTO scenarioView);
	}

	DeleteResourceDialogListener listener;
	private final TalkerDTO scenarioView;

	public DeleteResourceConfirmationDialogFragment(TalkerDTO scenarioView, Integer title, Integer message) {
		this.scenarioView = scenarioView;
		this.title = title;
		this.message = message;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			listener = (DeleteResourceDialogListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString() + " must implement DeleteResourceDialogListener");
		}
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// Use the Builder class for convenient dialog construction
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

		builder.setTitle(this.title).setMessage(this.message)
				.setPositiveButton(R.string.delete_scenario_accept, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int id) {
						listener.onDialogPositiveClickDeleteResourceDialogListener(scenarioView);
						dialog.dismiss();
					}
				}).setNegativeButton(R.string.delete_scenario_cancel, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int id) {
						dialog.dismiss();
					}
				});
		return builder.create();
	}

}
