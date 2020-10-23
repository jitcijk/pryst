package com.BScProject.truffle.jsl.nodes.interop;

import com.BScProject.truffle.jsl.runtime.JSLContext;
import com.BScProject.truffle.jsl.runtime.JSLNull;
import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.interop.ForeignAccess;
import com.oracle.truffle.api.interop.Message;
import com.oracle.truffle.api.interop.TruffleObject;
import com.oracle.truffle.api.interop.UnsupportedMessageException;
import com.oracle.truffle.api.nodes.Node;

public abstract class JSLForeignToJSLTypeNode extends Node {
	
	public abstract Object executeConvert(VirtualFrame frame, Object value);
	
	@Specialization
	protected static Object fromObject(Number value) {
		return JSLContext.fromForeignValue(value);
	}
	
	@Specialization 
	protected static Object fromString(String value) {
		return value;
	}
	
	@Specialization
	protected static Object fromBoolean(boolean value) {
		return value;
	}
	
	@Specialization
	protected static Object fromChar(char value) {
		return String.valueOf(value);
	}
	
	//In case it is boxed
	@Specialization(guards = "isBoxedPrimitive(frame, value)")
	public Object unbox(VirtualFrame frame, TruffleObject value) {
		Object unboxed = doUnbox(frame, value);
		return JSLContext.fromForeignValue(unboxed);
	}
	
	@Specialization(guards = "!isBoxedPrimitive(frame, value)")
	public Object fromTruffleObject(VirtualFrame frame, TruffleObject value) {
		return value;
	}
	
	@Child private Node isBoxed;
	
	protected final boolean isBoxedPrimitive(VirtualFrame frame, TruffleObject value) {
		if(isBoxed == null) {
			CompilerDirectives.transferToInterpreterAndInvalidate();
			isBoxed = insert(Message.IS_BOXED.createNode());
		}
		return ForeignAccess.sendIsBoxed(isBoxed, frame, value);
	}
	
	@Child private Node unbox;
	
	protected final Object doUnbox(VirtualFrame frame, TruffleObject value) {
		if (unbox == null) {
			CompilerDirectives.transferToInterpreterAndInvalidate();
			unbox = insert(Message.UNBOX.createNode());
		}
		try {
			return ForeignAccess.sendUnbox(unbox, frame, value);
		} catch(UnsupportedMessageException ex) {
			return JSLNull.SINGLETON;
		}
	}
	
	
}