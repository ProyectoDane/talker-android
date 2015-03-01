package ar.uba.fi.talker.action.userlog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.InputType;
import android.widget.EditText;
import ar.uba.fi.talker.R;

public class TextDialogFragment extends DialogFragment {

	public interface TextDialogListener {
		public void onDialogPositiveClickTextDialogListener(
				DialogFragment dialog);

	}

	// Use this instance of the interface to deliver action events
	TextDialogListener listener;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			listener = (TextDialogListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement TextDialogListener");
		}
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// Use the Builder class for convenient dialog construction
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

		EditText input = new EditText(getActivity());
		input.setId(R.id.insert_text_input);
		input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);
		builder.setView(input)
				.setTitle(R.string.insert_text_title)
				.setPositiveButton(R.string.insert_text_accept,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								listener.onDialogPositiveClickTextDialogListener(TextDialogFragment.this);
								dialog.dismiss();
							}
						})
				.setNegativeButton(R.string.insert_text_cancel,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.dismiss();
							}
						});
		return builder.create();
	}

}