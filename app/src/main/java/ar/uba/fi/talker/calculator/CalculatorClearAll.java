package ar.uba.fi.talker.calculator;

import android.view.View;

public class CalculatorClearAll extends CalculatorExpression {


	public CalculatorClearAll(CalculatorState state) {
		super(state);
	}
	
	@Override
	public void onClick(View v) {
		getState().clearStates();
		this.changeText(null);
	}
}
