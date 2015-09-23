package ar.uba.fi.talker.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import ar.uba.fi.talker.R;

public class ExitConversationConfirmationDialogFragment extends ParentDialogFragment {

	public interface ExitConversationConfirmationDialogListener {
		public void onDialogPositiveClickExitConversationConfirmationListener(
				DialogFragment dialog);
	}

	ExitConversationConfirmationDialogListener listener;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			listener = (ExitConversationConfirmationDialogListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement ExitConversationConfirmationDialogListener");
		}
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// Use the Builder class for convenient dialog construction
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

		builder.setTitle(R.string.exit_canvas_title)
				.setMessage(R.string.exit_canvas_message)
				.setPositiveButton(R.string.exit_canvas_accept,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int id) {
								listener.onDialogPositiveClickExitConversationConfirmationListener(ExitConversationConfirmationDialogFragment.this);
								dialog.dismiss();
							}
						})
				.setNegativeButton(R.string.exit_canvas_cancel,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int id) {
								dialog.dismiss();
							}
						});
		return builder.create();
	}

}
