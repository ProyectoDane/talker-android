package ar.uba.fi.talker.calculator;

import android.view.View;

public class CalculatorOpen extends CalculatorExpression {

	public CalculatorOpen(CalculatorState state) {
		super(state);
	}

	@Override
	public void onClick(View v) {
		if (getState().openEnabled()) {
			getState().setOpen();
			super.onClick(v);
		}
	}
}
