package com.BScProject.truffle.jsl.nodes.controlflow;

import com.oracle.truffle.api.nodes.ControlFlowException;

public final class JSLBreakException extends ControlFlowException {
	public static final JSLBreakException SINGLETON = new JSLBreakException();
	
	private static final long serialVersionUID = 1L;
	
	private JSLBreakException(){}
}