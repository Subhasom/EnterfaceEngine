import java.util.*;

public class ForwardChaining extends Method{
	private String _query;
	public ForwardChaining(List<String> kb, String query) {
		super(kb);
		_query = query;
		InitTrueVals();
	}
	
	private void PrintResult(boolean result, NLTranslator NLT) {
		if (result) {
			System.out.print("YES:");
			for (String element : _trueValues) {
				if (NLT == null)
					System.out.print(" " + element);
				else
					System.out.print(" " + NLT.GetNLfromVar(element)+".");
			}
		} else
			System.out.println("NO:");
	}
	
	@Override
	public void solve(NLTranslator NLT) {
		boolean result = false;
		boolean validRule = true;
		//is the query is known to be true
		for(String true_var : _trueValues)
			if(true_var.equals(_query))
				result = true;
		//loop until you find a solution
		while (!result) {
			boolean newAdded = false;
			//for every rule in the KB check if there are new rules proven true.
			for(String rule : _kb) {
				String[] parts = rule.split("=>");
				//its a rule
				if(parts.length == 2){
					validRule = true;
					//get the variables on the left side of the implementation.
					for(String depVar : parts[0].split("&")) {
						//if at least 1 variable is unknown then this rule can't be proven so go to the next rule.
						if(!_trueValues.contains(depVar)) {
							validRule = false;
							break;
						}
					}
					//if rule is proven true add, add the newly proven variable to the _trueVariables list.
					if(validRule) {
						if(!_trueValues.contains(parts[1])) {
							newAdded = true;
							_trueValues.add(parts[1]);
						}
						//goal check - check if the newly proven variable is the same as the query.
						if(parts[1].equals(_query)) {
							result = true;
							break;
						}
					}
				}
			}
			//if you can't prove any more rules break the search loop. 
			if(!newAdded)
				break;
		}
		PrintResult(result,NLT);
	}
}
