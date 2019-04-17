package com.michal.crm.dao;

import com.michal.crm.model.Tasks;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Transactional
public interface TasksRepository extends CrudRepository<Tasks, Integer>{
    @Query("select T from Tasks T where T.term <= :night and T.term >= :morning")
    List<Tasks> getTodayTasks(@Param("night")Date night, @Param("morning") Date morning);

    @Query("select T from Tasks T where T.term >= :now and T.complete = :complete")
    List<Tasks> getActualTasks(@Param("now")Date now, @Param("complete")boolean complete);

    @Query("select T from Tasks T where T.term < :now and T.complete = :complete")
    List<Tasks> getUnmetTasks(@Param("now")Date now, @Param("complete")boolean complete);

    @Query("select T from Tasks T where T.complete = :complete")
    List<Tasks> getHistoryTasks(@Param("complete")boolean complete);
}
