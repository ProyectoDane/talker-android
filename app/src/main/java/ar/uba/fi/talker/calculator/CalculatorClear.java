package ar.uba.fi.talker.calculator;

import android.view.View;

public class CalculatorClear extends CalculatorExpression {

	private static final int SIMBOL = 3;
	private static final int NUMBER = 1;

	public CalculatorClear(CalculatorState state) {
		super(state);
	}
	
	@Override
	public void onClick(View v) {
		if (!getState().isSolved()) {
			CharSequence text = this.getTextView().getText();
			if (text.length() > 0) {
				getState().stateBack();
				char lastChar = text.charAt(text.length()-1);
				int cut = (lastChar == ' ' ? SIMBOL : NUMBER);
				text = text.subSequence(0, text.length()-cut);
				
				this.changeText(text);
			}
		}
	}
}
