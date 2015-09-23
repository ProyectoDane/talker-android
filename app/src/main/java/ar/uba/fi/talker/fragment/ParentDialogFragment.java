package ar.uba.fi.talker.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.widget.Button;
import ar.uba.fi.talker.R;

public class ParentDialogFragment extends DialogFragment {

	@Override
	public void onStart() {
		super.onStart();
		// Buttons
		Button positiveButton = ((AlertDialog) getDialog()).getButton(DialogInterface.BUTTON_POSITIVE);
		Drawable drawablePositive = getActivity().getResources().getDrawable(R.drawable.accept);
		positiveButton.setText("");
		positiveButton.setTextSize(0);
		positiveButton.setGravity(Gravity.CENTER);
		drawablePositive.setBounds(0, 0, drawablePositive.getIntrinsicWidth(), drawablePositive.getIntrinsicHeight());
		positiveButton.setCompoundDrawables(null, drawablePositive, null, null);
		positiveButton.setBackgroundColor(getResources().getColor(R.color.greenok));

		Button negativeButton = ((AlertDialog) getDialog()).getButton(DialogInterface.BUTTON_NEGATIVE);
		Drawable drawableNegative = getActivity().getResources().getDrawable(R.drawable.cancel_back);
		negativeButton.setText("");
		negativeButton.setTextSize(0);
		negativeButton.setGravity(Gravity.CENTER);
		drawableNegative.setBounds(0, 0, drawableNegative.getIntrinsicWidth(), drawableNegative.getIntrinsicHeight());
		negativeButton.setCompoundDrawables(null, drawableNegative, null, null);
		negativeButton.setBackgroundColor(getResources().getColor(R.color.redcancel));

	}

}
