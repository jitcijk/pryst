package com.BScProject.truffle.jsl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Map;

import com.BScProject.truffle.jsl.nodes.JSLEvalRootNode;
import com.BScProject.truffle.jsl.nodes.JSLRootNode;
import com.BScProject.truffle.jsl.parser.JSLParseException;
import com.BScProject.truffle.jsl.parser.JSLParser;
import com.BScProject.truffle.jsl.runtime.JSLContext;
import com.BScProject.truffle.jsl.runtime.JSLFunction;
import com.oracle.truffle.api.CallTarget;
import com.oracle.truffle.api.CompilerAsserts;
import com.oracle.truffle.api.Truffle;
import com.oracle.truffle.api.TruffleLanguage;
import com.oracle.truffle.api.debug.DebuggerTags;
import com.oracle.truffle.api.frame.MaterializedFrame;
import com.oracle.truffle.api.instrumentation.ProvidedTags;
import com.oracle.truffle.api.instrumentation.StandardTags;
import com.oracle.truffle.api.nodes.Node;
import com.oracle.truffle.api.source.Source;

@TruffleLanguage.Registration(name = "JSL", version = "0.1", mimeType = JSLLanguage.MIME_TYPE)
@ProvidedTags({StandardTags.CallTag.class, StandardTags.StatementTag.class, StandardTags.RootTag.class, DebuggerTags.AlwaysHalt.class})
public final class JSLLanguage extends TruffleLanguage<JSLContext> {
	public static final String MIME_TYPE = "application/x-jsl";
	
	public static final JSLLanguage INSTANCE = new JSLLanguage();
	
	/**
	 * No instances allowed except the INSTANCE one.
	 */
	private JSLLanguage(){
	}

	@Override
	protected JSLContext createContext(Env env) {
		BufferedReader in = new BufferedReader(new InputStreamReader(env.in()));
		PrintWriter out = new PrintWriter(env.out(), true);
		return new JSLContext(env, in, out);
	}
	
	@Override
	protected CallTarget parse(ParsingRequest request) throws Exception {
		JSLParser parser = new JSLParser(request.getSource());
		Map<String, JSLRootNode> functions = parser.parse();
		if (functions != null){
			JSLRootNode root = functions.get("main");			
			JSLRootNode evalMain = new JSLEvalRootNode(root.getFrameDescriptor(), root.getBodyNode(), root.getSourceSection(), root.getName(), functions);
			return Truffle.getRuntime().createCallTarget(evalMain);
		} else {
			throw new JSLParseException("Parsing went wrong");
		}
	}

	@Override
	protected Object findExportedSymbol(JSLContext context, String globalName, boolean onlyExplicit) {
		return context.getFunctionRegistry().lookup(globalName, false);
	}

	@Override
	protected Object getLanguageGlobal(JSLContext context) {
		return context;
	}

	@Override
	protected boolean isObjectOfLanguage(Object object) {
		return object instanceof JSLFunction;
	}
	
	@Override
	protected Object evalInContext(Source source, Node node, MaterializedFrame mFrame) throws IOException {
		throw new IllegalStateException("evalInContext not supported");
	}
	
	public JSLContext findContext() {
		CompilerAsserts.neverPartOfCompilation();
		return super.findContext(super.createFindContextNode());
	}
	
	
	
}