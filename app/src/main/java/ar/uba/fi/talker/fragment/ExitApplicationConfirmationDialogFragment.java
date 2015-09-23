package ar.uba.fi.talker.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import ar.uba.fi.talker.R;

public class ExitApplicationConfirmationDialogFragment extends ParentDialogFragment {

	public interface ExitAplicationDialogListener {
		public void onDialogPositiveClickExitApplicationDialogListener(
				DialogFragment dialog);
	}

	ExitAplicationDialogListener listener;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			listener = (ExitAplicationDialogListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement ExitAplicationDialogListener");
		}
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

		builder.setMessage(R.string.exit_app_message)
				.setPositiveButton(R.string.exit_app_accept,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								listener.onDialogPositiveClickExitApplicationDialogListener(ExitApplicationConfirmationDialogFragment.this);
								dialog.dismiss();
							}
						})
				.setNegativeButton(R.string.exit_app_cancel,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.dismiss();
							}
						});
		return builder.create();
	}

}
