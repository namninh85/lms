package vn.wse.lms.repository;

import java.util.List;

import vn.wse.lms.entity.Topic;

public interface TopicRepositoryCustom {
	public void deleteTopicNotInListBySectionId(List<Topic> topics,Long sectionId);

	public void deleteAllTopicBySectionId(Long sectionId);

}
