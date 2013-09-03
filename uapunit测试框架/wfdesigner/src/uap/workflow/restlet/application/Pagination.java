package uap.workflow.restlet.application;

public class Pagination {
	
	/**记录总条数*/
	private long TotalRecords ;
	 
	/**总页数*/
	private int TotalPages ;
    
	/**页面大小*/
    private int PageSize ;
    
    /**当前页面*/
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
