package com.wwyl;

import java.io.Serializable;

import com.wwyl.entity.security.Operator;

/**
 * @author fyunli
 */
public class ThreadLocalHolder implements Serializable {

	private static ThreadLocal<Operator> threadLocalOperator = new ThreadLocal<Operator>();

	public static Operator getCurrentOperator() {
		return (Operator) threadLocalOperator.get();
	}

	public static void setCurrentOperator(Operator operator) {
		threadLocalOperator.set(operator);
	}

}
