package uap.workflow.restlet.application;

public class Pagination {
	
	/**��¼������*/
	private long TotalRecords ;
	 
	/**��ҳ��*/
	private int TotalPages ;
    
	/**ҳ���С*/
    private int PageSize ;
    
    /**��ǰҳ��*/
    private int PageNumber ;
     
	public int getTotalPages() {
		return TotalPages;
	}
	public void setTotalPages(int totalPages) {
		TotalPages = totalPages;
	}
	public long getTotalRecords() {
		return TotalRecords;
	}
	public void setTotalRecords(long totalRecords) {
		TotalRecords = totalRecords;
	}
	public int getPageSize() {
		return PageSize;
	}
	public void setPageSize(int pageSize) {
		PageSize = pageSize;
	}
	public int getPageNumber() {
		return PageNumber;
	}
	public void setPageNumber(int pageNumber) {
		PageNumber = pageNumber;
	}

}
