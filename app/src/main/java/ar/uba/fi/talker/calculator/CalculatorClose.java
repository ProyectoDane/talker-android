package ar.uba.fi.talker.calculator;

import android.view.View;

public class CalculatorClose extends CalculatorExpression {

	public CalculatorClose(CalculatorState state) {
		super(state);
	}

	String s = new String();

	@Override
	public void onClick(View v) {
		if (getState().closeEnabled()) {
			getState().setClose();
			super.onClick(v);
		}
	}
}
