package vn.wse.lms.repository;

import java.util.List;

import vn.wse.lms.entity.Section;

public interface SectionRepositoryCustom {
	public void deleteSectionNotInListByCourseId(List<Section> sections,Long courseId);

	public void deleteAllSectionByCourseId(Long courseId);

}
