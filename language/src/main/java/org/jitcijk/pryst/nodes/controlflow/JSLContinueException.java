package com.BScProject.truffle.jsl.nodes.controlflow;

import com.oracle.truffle.api.nodes.ControlFlowException;

public final class JSLContinueException extends ControlFlowException {
	private final static long serialVersionUID = 1L;
	public final static JSLContinueException SINGLETON = new JSLContinueException();
	
	private JSLContinueException(){
	}
}