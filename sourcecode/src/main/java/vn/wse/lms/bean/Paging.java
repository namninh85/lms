package vn.wse.lms.bean;



public class Paging {
	public static final int NUMROWS_PAGE = 10;
	private Integer numRowsPage;
	private Long numRows;
	private Integer currentPage;

	public Paging() {}
	public Paging(Long numRows, Integer currentPage) {
		this.numRows = numRows;
		this.currentPage = currentPage;
	}

	public Long getNumRows() {
		return numRows;
	}
	
	public void setNumRows(Long numRows) {
		this.numRows = numRows;
	}

	public Integer getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(Integer currentPage) {
		this.currentPage = currentPage;
	}
	
	public int getNumRowsPage() {
		return numRowsPage!=null?numRowsPage:NUMROWS_PAGE;
	}

	public void setNumRowsPage(Integer numRowsPage) {
		this.numRowsPage = numRowsPage;
	}
	
	public Integer getMaxPage() {
		if(numRows == null) return 1; 
		return numRows%getNumRowsPage()>0?(Math.round(numRows/getNumRowsPage()) + 1):Math.round(numRows/getNumRowsPage());
	}
	public Integer getMinPage() {
		if(numRows == null) return null; 
		return 1;
	}
	
	public boolean hasNext() {
		if(currentPage == null) return false;
		return currentPage<getMaxPage();
	}
}
