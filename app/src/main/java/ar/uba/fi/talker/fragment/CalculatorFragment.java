package ar.uba.fi.talker.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.TextView;
import ar.uba.fi.talker.R;
import ar.uba.fi.talker.calculator.CalculatorClear;
import ar.uba.fi.talker.calculator.CalculatorClearAll;
import ar.uba.fi.talker.calculator.CalculatorClose;
import ar.uba.fi.talker.calculator.CalculatorDot;
import ar.uba.fi.talker.calculator.CalculatorExpression;
import ar.uba.fi.talker.calculator.CalculatorOpen;
import ar.uba.fi.talker.calculator.CalculatorOperation;
import ar.uba.fi.talker.calculator.CalculatorResolve;
import ar.uba.fi.talker.calculator.CalculatorState;
import ar.uba.fi.talker.calculator.CalculatorValue;
import ar.uba.fi.talker.view.Scenario;

public class CalculatorFragment extends ParentDialogFragment implements OnClickListener {

	private SparseArray<CalculatorExpression> buttons;
	private TextView text;
	
	private View calculator;
		
	public CalculatorFragment() {
				
		buttons = new SparseArray<CalculatorExpression>();
		CalculatorState state =new CalculatorState();
		
		buttons.put(R.id.button1, new CalculatorValue(state));
		buttons.put(R.id.button2, new CalculatorValue(state));
		buttons.put(R.id.button3, new CalculatorValue(state));
		buttons.put(R.id.button4, new CalculatorValue(state));
		buttons.put(R.id.button5, new CalculatorValue(state));
		buttons.put(R.id.button6, new CalculatorValue(state));
		buttons.put(R.id.button7, new CalculatorValue(state));
		buttons.put(R.id.button8, new CalculatorValue(state));
		buttons.put(R.id.button9, new CalculatorValue(state));
		buttons.put(R.id.button0, new CalculatorValue(state));
		buttons.put(R.id.buttonPlus, new CalculatorOperation(state));
		buttons.put(R.id.buttonMinus, new CalculatorOperation(state));
		buttons.put(R.id.buttonMult, new CalculatorOperation(state));
		buttons.put(R.id.buttonDiv, new CalculatorOperation(state));
		buttons.put(R.id.buttonDot, new CalculatorDot(state));
		buttons.put(R.id.buttonClear, new CalculatorClear(state));
		buttons.put(R.id.buttonClearAll, new CalculatorClearAll(state));
		buttons.put(R.id.buttonOpen, new CalculatorOpen(state));
		buttons.put(R.id.buttonClose, new CalculatorClose(state));
		buttons.put(R.id.buttonResult, new CalculatorResolve(state));
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// Use the Builder class for convenient dialog construction
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		
		calculator = View.inflate(getActivity(), R.layout.calculator, null);

		text = (TextView) calculator.findViewById(R.id.calc_text);
		final HorizontalScrollView scroll = (HorizontalScrollView) calculator.findViewById(R.id.textScrollView);
		for (int i = 0; i < this.buttons.size(); i++) {
			Button button = (Button) calculator.findViewById(this.buttons.keyAt(i));
			
			CalculatorExpression expression = this.buttons.valueAt(i);
			expression.setTextView(text);
			expression.setScrollView(scroll);
			button.setOnClickListener(expression);
		}
		
		builder.setView(calculator).setNegativeButton(R.string.close_modal,
				new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				dialog.dismiss();
			}
		}).setPositiveButton(R.string.login_accept, null);

		return builder.create();
	}
	
	@Override
	public void onClick(View v) {
		Button button = (Button) calculator.findViewById(R.id.buttonResult);
		button.performClick();
		TextView inputText = (TextView) calculator.findViewById(R.id.calc_text);
		if (inputText != null && inputText.getEditableText() != null){
			Scenario scenario = (Scenario) getActivity().findViewById(R.id.gestureOverlayView1);
			scenario.setText(inputText.getEditableText());
			getDialog().dismiss();
		}
	}
	
	@Override
	public void onStart() {
		super.onStart(); 
		final AlertDialog dialog = (AlertDialog) getDialog();
		if (dialog != null) {
			final Button positiveButton = (Button) dialog.getButton(Dialog.BUTTON_POSITIVE);
			positiveButton.setOnClickListener(this);
		}
	}
}
