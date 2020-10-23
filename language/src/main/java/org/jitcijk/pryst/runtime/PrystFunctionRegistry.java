package org.jitcijk.pryst.runtime;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jitcijk.pryst.nodes.PrystRootNode;
import com.oracle.truffle.api.RootCallTarget;
import com.oracle.truffle.api.Truffle;

/**
 * Manages the mapping from function names to PrystFunction functions.
 */
public final class PrystFunctionRegistry  {
	private final HashMap<String, PrystFunction> functions = new HashMap<>();
	
	/**
	 * Returns the PrystFunction object for the given name. If the name does not exsist yet, it is created
	 */
	public PrystFunction lookup(String name, boolean createIfNotPresent) {
		PrystFunction result = functions.get(name);
		if (result == null && createIfNotPresent) {
			result = new PrystFunction(name);
			functions.put(name, result);
		}
		return result;
	}
	
	
	public PrystFunction register(String name, PrystRootNode rootNode) {
		PrystFunction function = lookup(name, true);
		RootCallTarget callTarget = Truffle.getRuntime().createCallTarget(rootNode);
		function.setCallTarget(callTarget);
		return function;
	}
	
	public void register(Map<String, PrystRootNode> newFunctions) {
		for(Map.Entry<String, PrystRootNode> entry : newFunctions.entrySet()) {
			register(entry.getKey(), entry.getValue());
		}
	}
	
	
	/**
	 * Printing purposes. List of all functions
	 */
	public List<PrystFunction> getAllFunctions() {
		ArrayList<PrystFunction> allFunctions = new ArrayList<>(functions.values());
		Collections.sort(allFunctions, new Comparator<PrystFunction>(){
			public int compare(PrystFunction f1, PrystFunction f2) {
                return f1.toString().compareTo(f2.toString());
            }
        });
		return allFunctions;
	}
	
	
}