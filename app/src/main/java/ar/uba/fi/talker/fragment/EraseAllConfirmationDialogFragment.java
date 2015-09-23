package ar.uba.fi.talker.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import ar.uba.fi.talker.R;

public class EraseAllConfirmationDialogFragment extends ParentDialogFragment {

	public interface EraseAllConfirmationDialogListener {
		public void onDialogPositiveClickEraseAllConfirmationListener(
				DialogFragment dialog);
	}

	EraseAllConfirmationDialogListener listener;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			listener = (EraseAllConfirmationDialogListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement EraseAllConfirmationDialogListener");
		}
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// Use the Builder class for convenient dialog construction
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

		builder.setTitle(R.string.erase_all_title)
				.setMessage(R.string.erase_all_message)
				.setPositiveButton(R.string.erase_all_accept,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								listener.onDialogPositiveClickEraseAllConfirmationListener(EraseAllConfirmationDialogFragment.this);
								dialog.dismiss();
							}
						})
				.setNegativeButton(R.string.erase_all_cancel,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.dismiss();
							}
						});
		return builder.create();
	}

}
