package ar.uba.fi.talker.calculator;

import android.view.View;

public class CalculatorValue extends CalculatorExpression {

	public CalculatorValue(CalculatorState state) {
		super(state);
	}

	@Override
	public void onClick(View v) {
		if (!getState().isSolved()) {
			getState().setValue();
			super.onClick(v);
		}
	}
}
