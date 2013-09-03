package uap.workflow.engine.cmpltsgy;
import uap.workflow.engine.core.IActivityInstance;
public class ByCountSgy extends AbstractSgyService {
	public boolean isComplete(IActivityInstance actIns, Integer count) {
		int all = Integer.parseInt((String) actIns.getVariableLocal("nrOfInstances"));
		if (count == null) {
			count = Integer.parseInt((String) actIns.getVariableLocal("nrOfCompletedInstances"));
			if (count + 1 == all) {
				return true;
			} else {
				return false;
			}
		} else {
			if (count == all) {
				return true;
			} else {
				return false;
			}
		}
	}
}
