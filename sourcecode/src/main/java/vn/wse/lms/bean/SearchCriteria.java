package vn.wse.lms.bean;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchCriteria<T> {
	
	private Map<String, String> instance;
	private List<Long> checkeds;
	private List<T> resultList;
	private Paging paging = new Paging();
	
	public SearchCriteria(){
		instance = new HashMap<String, String>();
	}
	
	public SearchCriteria( List<T> resultList, Paging paging) {
		this.resultList = resultList;
		this.paging = paging;
	}
	
	public List<Long> getCheckeds() {
		return checkeds;
	}

	public void setCheckeds(List<Long> checkeds) {
		this.checkeds = checkeds;
	}

	public Map<String, String> getInstance() {
		return instance;
	}

	public void setInstance(Map<String, String> instance) {
		this.instance = instance;
	}

	public List<T> getResultList() {
		return resultList;
	}

	public void setResultList(List<T> resultList) {
		this.resultList = resultList;
	}

	public Paging getPaging() {
		return paging;
	}

	public void setPaging(Paging paging) {
		this.paging = paging;
	}
}
