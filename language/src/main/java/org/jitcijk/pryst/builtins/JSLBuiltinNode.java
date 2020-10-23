package com.BScProject.truffle.jsl.builtins;

import com.BScProject.truffle.jsl.nodes.JSLExpressionNode;
import com.BScProject.truffle.jsl.runtime.JSLContext;
import com.oracle.truffle.api.dsl.GenerateNodeFactory;
import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.NodeField;

@NodeChild(value = "arguments", type = JSLExpressionNode[].class)
@NodeField(name = "context", type = JSLContext.class)
@GenerateNodeFactory
public abstract class JSLBuiltinNode extends JSLExpressionNode {
	public abstract JSLContext getContext();
	
}