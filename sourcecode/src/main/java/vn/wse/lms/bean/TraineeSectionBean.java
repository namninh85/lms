package vn.wse.lms.bean;

import java.io.Serializable;
import java.util.List;

import vn.wse.lms.entity.Section;
import vn.wse.lms.entity.TraineeTopic;

public class TraineeSectionBean implements Serializable {
	
	public Section section;
	public List<TraineeTopic> traineeTopics;
	public Section getSection() {
		return section;
	}
	public void setSection(Section section) {
		this.section = section;
	}
	public List<TraineeTopic> getTraineeTopics() {
		return traineeTopics;
	}
	public void setTraineeTopics(List<TraineeTopic> traineeTopics) {
		this.traineeTopics = traineeTopics;
	}
	
	
	
	
}
