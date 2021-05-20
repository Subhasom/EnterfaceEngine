import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BackwardsChaining extends Method {
	private String _query;
	private List<String> _knownRules;

	public BackwardsChaining(List<String> kb, String query) {
		super(kb);
		_knownRules = new ArrayList<String>();
		_query = query;
		InitTrueVals();
	}
	
	@Override
	public void solve(NLTranslator NLT) {
		boolean result = false;
		//if the query is known to be true
		if (_trueValues.contains(_query)) {
			System.out.println(_query + " is True Fact");
			_knownRules.add(_query);
			result = true;
		} else {
			//otherwise perform DFS to discover all unknown variables related to the query.
			result = Search(_query);
		}
		PrintResult(result, NLT);
	}
	
	private boolean Search(String current) {
		boolean result = false;
		// for every rule in the knowledge base
		NextRule:
		for (String rule : _kb) {
			String[] parts = rule.split("=>");
			if(parts.length == 2){
				if (current.equals(parts[1])) {
					//repetitive checking
					if (_knownRules.contains(rule))
						continue;
					else
						_knownRules.add(rule);
					//for each variable on the left side of the implication sign
					for (String var : parts[0].split("&")) {
						if (!_trueValues.contains(var)) {
							//if at least one var on the left side is false then
							//this rule is false try another rule.
							if (!Search(var)) {
								result = false;
								continue NextRule;
							}
							//add var to known true variables
							_trueValues.add(var);
						} else {
							_knownRules.add(var);
						}
					}
					//if all variables pass as true then return true.
					result = true;
				}
			}
		}
		//Can't find any matching rule for this var.
		return result;
	}
	
	private void PrintResult(boolean result, NLTranslator NLT) {
		if (result) {
			System.out.print("YES:");
			Set<String> visitedSet = new HashSet<>();
			for (String rule : _knownRules) {
				String[] parts = rule.split("=>");
				if (parts.length == 1) {
					visitedSet.add(parts[0]);
				} else {
					visitedSet.add(parts[1]);
				}
			}
			for (String element : visitedSet) {
				if (NLT == null)
					System.out.print(" " + element);
				else
					System.out.print(" " + NLT.GetNLfromVar(element) + ".");
			}
		} else
			System.out.println("NO:");

	}
}
