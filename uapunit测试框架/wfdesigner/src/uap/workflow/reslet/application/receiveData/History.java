package uap.workflow.reslet.application.receiveData;

import java.util.List;

public class History {
   /**ÀúÊ·¼ÇÂ¼*/
	private List<HistoricActivity> historicActivities ;
	/**Í¼Æ¬url*/
	
	private String imageUrl ;
	public List<HistoricActivity> getHistoricActivities() {
		return historicActivities;
	}
	public void setHistoricActivities(List<HistoricActivity> historicActivities) {
		this.historicActivities = historicActivities;
	}
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	
}
