package com.wwyl.dao.settings;

import java.util.List;

import com.wwyl.Enums;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.wwyl.Enums.TaskMode;
import com.wwyl.dao.BaseRepository;
import com.wwyl.entity.settings.Task;

/**
 * @author fyunli
 */
@Repository
public interface TaskDao extends BaseRepository<Task, Long> {

	@Query("select t from Task t inner join t.accepters o where o.id = ?1 and t.taskMode = ?2 order by t.taskStatus, t.taskTime desc")
	public Page<Task> findTasksByOperator(Long operatorId, TaskMode taskMode, Pageable pageable);

	/**
	 * 考虑到多播情况，对于某操作员，其活跃任务条件包括：
	 * 1、未处理任务，接受人包含操作员
	 * 2、处理中任务，接收人包括操作员，且责任人必须为操作员本人
	 */
	@Query("select t from Task t inner join t.accepters o where o.id = ?1 and (t.taskStatus = 0 OR (t.taskStatus = 1 and t.operator.id = ?1)) and t.taskMode = ?2 order by t.taskTime desc")
	public List<Task> findActiveTasksByOperator(Long operatorId, TaskMode taskMode);

    @Modifying
    @Query("update Task set taskStatus = ?2 where id = ?1")
    public void updateTaskStatus(Long id, Enums.TaskStatus taskStatus);

    @Modifying
    @Query("update Task set taskStatus = ?2 where taskMode = ?1")
    void updateTaskStatus(TaskMode taskMode, Enums.TaskStatus taskStatus);

}
