package ar.uba.fi.talker.calculator;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class CalculatorOperation extends CalculatorExpression {

	public CalculatorOperation(CalculatorState state) {
		super(state);
	}
	
	@Override
	public void onClick(View v) {
		if (getState().operationEnabled()) {
			Button button = (Button) v;
			CharSequence text = button.getText();
			if (getState().isSolved()) {
				this.setNewText();
			}
			this.getState().setOperation();
			this.appendText(" " + text + " ");
		} 
	}

	private void setNewText() {
		TextView textView = this.getTextView();
		CharSequence result = textView.getText();
		int equal = result.toString().indexOf("=");
		CharSequence subSequence = result.subSequence(equal+2, result.length());
		this.changeText(subSequence);
	}
}
