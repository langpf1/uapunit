package uap.workflow.reslet.application.receiveData;

import java.util.List;

public class History {
   /**��ʷ��¼*/
	private List<HistoricActivity> historicActivities ;
	/**ͼƬurl*/
	
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
