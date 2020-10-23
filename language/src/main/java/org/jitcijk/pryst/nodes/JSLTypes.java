package com.BScProject.truffle.jsl.nodes;

import com.BScProject.truffle.jsl.runtime.JSLFunction;
import com.BScProject.truffle.jsl.runtime.JSLNull;
import com.oracle.truffle.api.dsl.ImplicitCast;
import com.oracle.truffle.api.dsl.TypeCast;
import com.oracle.truffle.api.dsl.TypeCheck;
import com.oracle.truffle.api.dsl.TypeSystem;
import com.oracle.truffle.api.dsl.internal.DSLOptions;

@TypeSystem({long.class, double.class, boolean.class, String.class, JSLFunction.class, JSLNull.class})
@DSLOptions
public abstract class JSLTypes {
	
	@TypeCheck(JSLNull.class)
	public static boolean isJSLNull(Object value) {
		return value == JSLNull.SINGLETON;
	}
	
	@TypeCast(JSLNull.class)
	public static JSLNull asJSLNull(Object value) {
		assert isJSLNull(value);
		return JSLNull.SINGLETON;
	}
	
	@ImplicitCast
	public static double castDouble(long value) {
		return value;
	}
	
	@ImplicitCast
	public static boolean castBoolean(long value) {
		return value != 0;
	}
}