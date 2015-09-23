package ar.uba.fi.talker.calculator;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.TextView;

public abstract class CalculatorExpression implements OnClickListener {
	
	private TextView textView;
	private CalculatorState state;
	private HorizontalScrollView scroll;

	public CalculatorExpression(CalculatorState state) {
		this.state = state;
	}
	
	@Override
	public void onClick(View v) {
		Button button = (Button) v;
		CharSequence text = button.getText();
		this.appendText(text);
	}

	protected void appendText(CharSequence text) {
		this.appendText(text.toString());
	}
	
	protected void appendText(String text) {
		this.textView.append(text);
		scroll.fullScroll(HorizontalScrollView.FOCUS_RIGHT);
	}
	
	protected void changeText(CharSequence text) {
		this.textView.setText(text);
		scroll.fullScroll(HorizontalScrollView.FOCUS_RIGHT);
	}
	
	public void setTextView(TextView text) {
		this.textView = text;
	}
	
	protected TextView getTextView() {
		return textView;
	}
	
	protected CalculatorState getState() {
		return state;
	}

	public void setScrollView(HorizontalScrollView scroll) {
		this.scroll = scroll;
	}
}
