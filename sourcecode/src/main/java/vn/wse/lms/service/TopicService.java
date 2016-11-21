package vn.wse.lms.service;

import vn.wse.lms.entity.Topic;

public interface TopicService {

	Topic findById(Long topicId);

	void save(Topic topic) throws Exception;

}
