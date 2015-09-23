package ar.uba.fi.talker.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import ar.uba.fi.talker.R;

public class SaveAllConfirmationDialogFragment extends ParentDialogFragment {

	public interface SaveAllConfirmationDialogListener {
		public void onDialogPositiveClickSaveAllConfirmationListener(
				DialogFragment dialog);
	}

	SaveAllConfirmationDialogListener listener;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			listener = (SaveAllConfirmationDialogListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement SaveAllConfirmationDialogListener");
		}
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// Use the Builder class for convenient dialog construction
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

		builder.setTitle(R.string.save_all_title)
				.setMessage(R.string.save_all_message)
				.setPositiveButton(R.string.save_all_accept,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								listener.onDialogPositiveClickSaveAllConfirmationListener(SaveAllConfirmationDialogFragment.this);
								dialog.dismiss();
							}
						})
				.setNegativeButton(R.string.save_all_cancel,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.dismiss();
							}
						});
		return builder.create();
	}
}
