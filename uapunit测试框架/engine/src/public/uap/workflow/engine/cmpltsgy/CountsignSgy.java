package uap.workflow.engine.cmpltsgy;
import uap.workflow.engine.core.IActivityInstance;
public class CountsignSgy extends AbstractSgyService {
	public boolean isComplete(IActivityInstance actIns, Integer count) {
		int all = Integer.parseInt(String.valueOf( actIns.getVariableLocal("nrOfInstances")));
		if (count == null) {
			if( actIns.getVariableLocal("nrOfCompletedInstances")==null)
				actIns.setVariableLocal("nrOfCompletedInstances", 0);
			count = Integer.parseInt(String.valueOf( actIns.getVariableLocal("nrOfCompletedInstances")));
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
