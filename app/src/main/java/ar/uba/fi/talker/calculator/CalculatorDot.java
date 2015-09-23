package ar.uba.fi.talker.calculator;

import android.view.View;

public class CalculatorDot extends CalculatorExpression {

	public CalculatorDot(CalculatorState state) {
		super(state);
	}

	@Override
	public void onClick(View v) {
		if (getState().dotEnabled()) {
			getState().setDotted();
			CharSequence text = this.getTextView().getText();
			char lastChar = text.charAt(text.length()-1);
			if (lastChar == '(') {
				this.getTextView().append("0");
			}
			super.onClick(v);
		}
	}
}
