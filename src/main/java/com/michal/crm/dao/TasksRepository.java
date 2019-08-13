package com.michal.crm.dao;

import com.michal.crm.model.Tasks;
import com.michal.crm.model.Users;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Transactional
public interface TasksRepository extends CrudRepository<Tasks, Integer> {
    Tasks findTasksByIdAndUser(@Param("id") int taskID, @Param("user") Users user);

    @Query("select T from Tasks T where T.term <= :night and T.term >= :morning and T.user = :user")
    List<Tasks> getTodayTasks(@Param("night") Date night, @Param("morning") Date morning, @Param("user") Users user);

    @Query("select T from Tasks T where T.term >= :now and T.complete = :complete and T.user = :user")
    List<Tasks> getActualTasks(@Param("now") Date now, @Param("complete") boolean complete, @Param("user") Users user);

    @Query("select T from Tasks T where T.term < :now and T.complete = :complete and T.user = :user")
    List<Tasks> getUnmetTasks(@Param("now") Date now, @Param("complete") boolean complete, @Param("user") Users user);

    @Query("select T from Tasks T where T.complete = :complete and T.user = :user")
    List<Tasks> getHistoryTasks(@Param("complete") boolean complete, @Param("user") Users user);
}
