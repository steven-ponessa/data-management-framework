package com.ibm.wfm.utils;

import java.util.List;

import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

public class ExpressionEvaluator extends SpelExpressionParser {
	
	public ExpressionEvaluator() {
		super();
	}

	public static void main(String[] args) {

	}
	
	public <T> Object getAttribute(T object, String attributeName) {
		Expression exp = this.parseExpression(attributeName);
		return  exp.getValue(object);
	}
	
	public <T> boolean evaluateContext(T object, String expression) {
		Expression exp = this.parseExpression(expression);
		EvaluationContext context = new StandardEvaluationContext(object);
		boolean result = exp.getValue(context, Boolean.class);  // evaluates to true
		return result;
	}
	
	public <T> Object findInListByAttribute(List<T> list, Object value, String attributeName) {
		T foundObject = list
				.stream()
				.filter(obj -> value.equals(getAttribute(obj,attributeName))) //cicDomCntr.getCtryCd().trim()))
				.findFirst()
				.orElse(null);
		return foundObject;
	}
	

}
