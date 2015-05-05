package ar.uba.fi.talker.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import ar.uba.fi.talker.R;

public class DeleteScenarioConfirmationDialogFragment extends TalkerDialogFragment {

	public interface DeleteScenarioDialogListener {
		void onDialogPositiveClickDeleteScenarioDialogListener(
				DialogFragment dialog, int position);
	}

	DeleteScenarioDialogListener listener;
	private int position;

	public DeleteScenarioConfirmationDialogFragment(int position) {
		this.position = position;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			listener = (DeleteScenarioDialogListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement DeleteScenarioDialogListener");
		}
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// Use the Builder class for convenient dialog construction
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

		builder.setTitle(R.string.delete_scenario_title)
				.setMessage(R.string.delete_scenario_message)
				.setPositiveButton(R.string.delete_scenario_accept,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								listener.onDialogPositiveClickDeleteScenarioDialogListener(DeleteScenarioConfirmationDialogFragment.this, position);
								dialog.dismiss();
							}
						})
				.setNegativeButton(R.string.delete_scenario_cancel,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.dismiss();
							}
						});
		return builder.create();
	}

}
