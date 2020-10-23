package com.BScProject.truffle.jsl.nodes;

import com.BScProject.truffle.jsl.JSLLanguage;
import com.oracle.truffle.api.CompilerDirectives.CompilationFinal;
import com.oracle.truffle.api.frame.FrameDescriptor;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.NodeInfo;
import com.oracle.truffle.api.nodes.RootNode;
import com.oracle.truffle.api.source.SourceSection;

/**
 * Root of the tree. Truffle needs to have this Node extend the 
 * rootNode class.
 */ 
@NodeInfo(language = "JSL", description = "The root node of all JSL trees")
public class JSLRootNode extends RootNode {
	@Child private JSLExpressionNode bodyNode;
	
	//For printing purposes;
	private final String name;
	
	@CompilationFinal private boolean isCloningAllowed;
	
	public JSLRootNode(FrameDescriptor frameDescriptor, JSLExpressionNode bodyNode, SourceSection sourceSection, String name) {
		super(JSLLanguage.class, sourceSection, frameDescriptor);
		this.bodyNode = bodyNode;
		this.name = name;
	}
	
	@Override
	public Object execute(VirtualFrame frame) {
		assert JSLLanguage.INSTANCE.findContext() != null;
		return bodyNode.executeGeneric(frame);
	}
	
	public JSLExpressionNode getBodyNode() {
		return bodyNode;
	}
	
	@Override
	public String getName() {
		return name;
	}
	
	public void setCloningAllowed(boolean isCloningAllowed) {
		this.isCloningAllowed = isCloningAllowed;
	}
	
	@Override
	public boolean isCloningAllowed() {
		return isCloningAllowed;
	}
	
	@Override
	public String toString() {
		return "root " + name;
	}
}