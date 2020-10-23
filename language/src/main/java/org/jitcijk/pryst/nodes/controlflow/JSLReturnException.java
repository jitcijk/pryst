package com.BScProject.truffle.jsl.nodes.controlflow;

import com.oracle.truffle.api.nodes.ControlFlowException;

public final class JSLReturnException extends ControlFlowException {
	private static final long serialVersionUID = 1L;
	
	private final Object result;
	
	public JSLReturnException(Object result) {
		this.result = result;
	}
	
	public Object getResult() {
		return result;
	}
}