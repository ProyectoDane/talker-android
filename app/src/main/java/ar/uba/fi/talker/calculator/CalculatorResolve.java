package ar.uba.fi.talker.calculator;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Stack;

import android.util.Log;
import android.view.View;

public class CalculatorResolve extends CalculatorExpression {

	// Associativity constants for operators
	private static final int LEFT_ASSOC = 0;
	private static final int RIGHT_ASSOC = 1;

	// Supported operators
	private static final Map<String, int[]> OPERATORS = new HashMap<String, int[]>();
	static {
		// Map<"token", []{precendence, associativity}>
		OPERATORS.put("+", new int[] { 0, LEFT_ASSOC });
		OPERATORS.put("-", new int[] { 0, LEFT_ASSOC });
		OPERATORS.put("*", new int[] { 5, LEFT_ASSOC });
		OPERATORS.put("/", new int[] { 5, LEFT_ASSOC });
	}

	public CalculatorResolve(CalculatorState state) {
		super(state);
	}

	/**
	 * Test if a certain is an operator .
	 * 
	 * @param token
	 *            The token to be tested .
	 * @return True if token is an operator . Otherwise False .
	 */
	private static boolean isOperator(String token) {
		return OPERATORS.containsKey(token);
	}

	/**
	 * Test the associativity of a certain operator token .
	 * 
	 * @param token
	 *            The token to be tested (needs to operator).
	 * @param type
	 *            LEFT_ASSOC or RIGHT_ASSOC
	 * @return True if the tokenType equals the input parameter type .
	 */
	private static boolean isAssociative(String token, int type) {
		if (!isOperator(token)) {
			throw new IllegalArgumentException("Invalid token: " + token);
		}
		if (OPERATORS.get(token)[1] == type) {
			return true;
		}
		return false;
	}

	/**
	 * Compare precendece of two operators.
	 * 
	 * @param token1
	 *            The first operator .
	 * @param token2
	 *            The second operator .
	 * @return A negative number if token1 has a smaller precedence than token2,
	 *         0 if the precendences of the two tokens are equal, a positive
	 *         number otherwise.
	 */
	private static final int cmpPrecedence(String token1, String token2) {
		if (!isOperator(token1) || !isOperator(token2)) {
			throw new IllegalArgumentException("Invalied tokens: " + token1
					+ " " + token2);
		}
		return OPERATORS.get(token1)[0] - OPERATORS.get(token2)[0];
	}

	public static Queue<String> infixToRPN(String[] inputTokens) {
		Queue<String> out = new LinkedList<String>();
		Stack<String> stack = new Stack<String>();
		// For all the input tokens [S1] read the next token [S2]
		for (String token : inputTokens) {
			if (isOperator(token)) {
				// If token is an operator (x) [S3]
				while (!stack.empty() && isOperator(stack.peek())) {
					// [S4]
					if ((isAssociative(token, LEFT_ASSOC) && cmpPrecedence(
							token, stack.peek()) <= 0)
							|| (isAssociative(token, RIGHT_ASSOC) && cmpPrecedence(
									token, stack.peek()) < 0)) {
						out.add(stack.pop()); // [S5] [S6]
						continue;
					}
					break;
				}
				// Push the new operator on the stack [S7]
				stack.push(token);
			} else if (token.equals("(")) {
				stack.push(token); // [S8]
			} else if (token.equals(")")) {
				// [S9]
				while (!stack.empty() && !stack.peek().equals("(")) {
					out.add(stack.pop()); // [S10]
				}
				stack.pop(); // [S11]
			} else {
				out.add(token); // [S12]
			}
		}
		while (!stack.empty()) {
			out.add(stack.pop()); // [S13]
		}
		return out;
	}

	private boolean isDouble(String str) {
		try {
			Double.parseDouble(str);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}
	
	private double resuelve(Queue<String> postfix) {
		Stack<Double> stack = new Stack<Double>();
		double a, b;
		while (postfix.peek() != null) {
			String dato = postfix.poll();
			if (this.isDouble(dato)) {
				stack.push(Double.valueOf(dato));
			} else {
				b = stack.pop();
				a = stack.pop();

				if ("+".equals(dato))
					stack.push(a + b);
				else if ("-".equals(dato))
					stack.push(a - b);
				else if ("*".equals(dato))
					stack.push(a * b);
				else if ("/".equals(dato))
					stack.push(a / b);
			}
		}
		
		getState().setSolved();
		return stack.pop();
	}

	private String expand(String string) {
		StringBuilder expanded = new StringBuilder();
		
		for (int i = 0; i < string.length(); i++) {
			char c = string.charAt(i);
			if (c == ')') {
				expanded.append(' ');
			}
			expanded.append(c);
			if (c == '(') {
				expanded.append(' ');
			}
		}
		
		return expanded.toString();
	}
	
	@Override
	public void onClick(View v) {
		
		if (getState().isSolved()) return;
		String expression = getTextView().getText().toString();
		
		if (!expression.isEmpty()) {
			String[] input = this.expand(expression).split(" ");
			Queue<String> output = infixToRPN(input);
			try {
				double result = resuelve(output);
				DecimalFormat df = new DecimalFormat("#.##");
				String aString = df.format(result);
				this.appendText(" = " + aString);
			} catch (Exception e) {
				Log.e("CALCULATOR", "Ocurrió un error en el calculo", e);
			}
		}
	}

}
