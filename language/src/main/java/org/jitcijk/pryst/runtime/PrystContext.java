package org.jitcijk.pryst.runtime;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.math.BigInteger;

import org.jitcijk.pryst.PrystLanguage;
import org.jitcijk.pryst.builtins.PrystBuiltinNode;
import org.jitcijk.pryst.builtins.PrystPrintlnBuiltinFactory;
import org.jitcijk.pryst.builtins.PrystTimeBuiltinFactory;
import org.jitcijk.pryst.nodes.PrystExpressionNode;
import org.jitcijk.pryst.nodes.PrystRootNode;
import org.jitcijk.pryst.nodes.local.PrystReadArgumentNode;
import com.oracle.truffle.api.CallTarget;
import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.CompilerDirectives.TruffleBoundary;
import com.oracle.truffle.api.ExecutionContext;
import com.oracle.truffle.api.TruffleLanguage;
import com.oracle.truffle.api.dsl.NodeFactory;
import com.oracle.truffle.api.frame.FrameDescriptor;
import com.oracle.truffle.api.interop.TruffleObject;
import com.oracle.truffle.api.nodes.NodeInfo;
import com.oracle.truffle.api.source.Source;
import com.oracle.truffle.api.source.SourceSection;

public final class PrystContext extends ExecutionContext {
	//Only used for the builtins
	private static final Source SOURCE_BUILTIN = Source.newBuilder("").name("Pryst Builtin").mimeType(PrystLanguage.MIME_TYPE).build();
	
	private final BufferedReader input;
	private final PrintWriter output;
	private final PrystFunctionRegistry functionRegistry;
	private final TruffleLanguage.Env env;
	
	public PrystContext(TruffleLanguage.Env env, BufferedReader input, PrintWriter output){
		this.input = input;
		this.output = output;
		this.functionRegistry = new PrystFunctionRegistry();
		this.env = env;
		installBuiltins();
	}
	
	public BufferedReader getInput() {
		return input;
	}
	
	public PrintWriter getOutput() {
		return output;
	}
	
	public PrystFunctionRegistry getFunctionRegistry(){
		return functionRegistry;
	}
	
	private void installBuiltins() {
		installBuiltin(PrystPrintlnBuiltinFactory.getInstance());
		installBuiltin(PrystTimeBuiltinFactory.getInstance());
	}
	
	private void installBuiltin(NodeFactory<? extends PrystBuiltinNode> factory) {
		int argumentCount = factory.getExecutionSignature().size();
		PrystExpressionNode[] argumentNodes = new PrystExpressionNode[argumentCount];
		
		for (int i = 0; i < argumentCount; i++) {
			argumentNodes[i] = new PrystReadArgumentNode(i);
		}
				
		PrystBuiltinNode builtinBodyNode = factory.createNode(argumentNodes, this);
		builtinBodyNode.addRootTag();
		
		String name = lookupNodeInfo(builtinBodyNode.getClass()).shortName();
		// SourceSection.createUnavailable("Pryst builtin", name);
		final SourceSection srcSection = SOURCE_BUILTIN.createUnavailableSection();
		builtinBodyNode.setSourceSection(srcSection);
		
		PrystRootNode rootNode = new PrystRootNode(new FrameDescriptor(), builtinBodyNode, srcSection, name);
		getFunctionRegistry().register(name, rootNode);
	}
	
	
	
	public static NodeInfo lookupNodeInfo(Class<?> clazz) {
		if (clazz == null) {
			return null;
		}
		NodeInfo info = clazz.getAnnotation(NodeInfo.class);
		if (info != null) {
			return info;
		} else {
			return lookupNodeInfo(clazz.getSuperclass());
		}
	}
	
	
	
	/**
	 * Methods for language interoperability (100% Copied from SL language)
	 */
	
    public static Object fromForeignValue(Object a) {
        if (a instanceof Long || a instanceof BigInteger || a instanceof String) {
            return a;
        } else if (a instanceof Number) {
            return fromForeignNumber(a);
        } else if (a instanceof TruffleObject) {
            return a;
        } else if (a instanceof PrystContext) {
            return a;
        }
        CompilerDirectives.transferToInterpreter();
        throw new IllegalStateException(a + " is not a Truffle value");
    }

    @TruffleBoundary
    private static long fromForeignNumber(Object a) {
        return ((Number) a).longValue();
    }

    public CallTarget parse(Source source) throws Exception {
        return env.parse(source);
    }

    /**
     * Goes through the other registered languages to find an exported global symbol of the
     * specified name. The expected return type is either <code>TruffleObject</code>, or one of
     * wrappers of Java primitive types ({@link Integer}, {@link Double}).
     *
     * @param name the name of the symbol to search for
     * @return object representing the symbol or <code>null</code>
     */
    @TruffleBoundary
    public Object importSymbol(String name) {
        Object object = env.importSymbol(name);
        Object slValue = fromForeignValue(object);
        return slValue;
    }

}