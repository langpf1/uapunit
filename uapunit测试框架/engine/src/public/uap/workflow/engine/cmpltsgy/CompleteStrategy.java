package uap.workflow.engine.cmpltsgy;
public class CompleteStrategy implements ICompleteSgy {
	private String count;
	private String percent;
	private String isNotBunch;// ÊÇ·ñ´®ÐÐ
	public String getCount() {
		return count;
	}
	public void setCount(String count) {
		this.count = count;
	}
	public String getPercent() {
		return percent;
	}
	public void setPercent(String percent) {
		this.percent = percent;
	}
	public String getIsNotBunch() {
		return isNotBunch;
	}
	public void setIsNotBunch(String isNotBunch) {
		this.isNotBunch = isNotBunch;
	}
	public int getStrategyType() {
		return 0;
	}
}
