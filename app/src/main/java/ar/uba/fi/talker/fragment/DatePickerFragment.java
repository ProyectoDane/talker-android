package ar.uba.fi.talker.fragment;

import java.util.Calendar;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import ar.uba.fi.talker.R;

public class DatePickerFragment extends ParentDialogFragment {
	
	public interface DatePickerDialogListener {
		public void onDialogPositiveClickDatePickerDialogListener(
				DatePickerFragment datePickerFragment);

	}
	DatePickerDialogListener listener;
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			listener = (DatePickerDialogListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement DatePickerDialogListener");
		}
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// Use the current date as the default date in the picker
		final Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH);
		int day = c.get(Calendar.DAY_OF_MONTH);

		DatePickerDialog dialog =  new DatePickerDialog(getActivity(), (OnDateSetListener) getActivity(), year, month, day);
		dialog.setButton(DialogInterface.BUTTON_NEGATIVE, getString(R.string.close_modal), new DialogInterface.OnClickListener() {
		    public void onClick(DialogInterface dialog, int which) {
		       if (which == DialogInterface.BUTTON_NEGATIVE) {
		    	   dialog.dismiss();
		       }
		    }
		});
		dialog.setButton(DialogInterface.BUTTON_POSITIVE, getString(R.string.login_accept), new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						listener.onDialogPositiveClickDatePickerDialogListener(DatePickerFragment.this);
						dialog.dismiss();
					}
				});
		return dialog;
	}
	
}