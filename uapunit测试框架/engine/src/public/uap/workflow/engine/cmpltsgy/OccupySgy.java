package uap.workflow.engine.cmpltsgy;
import uap.workflow.engine.core.IActivityInstance;
public class OccupySgy extends AbstractSgyService {
	public boolean isComplete(IActivityInstance actIns, Integer count) {
		if (count == null) {
			count = Integer.parseInt((String) actIns.getVariableLocal("nrOfCompletedInstances"));
			if (count == 0) {
				return true;
			} else {
				return false;
			}
		} else {
			if (count == 1) {
				return true;
			} else {
				return false;
			}
		}
	}
}
