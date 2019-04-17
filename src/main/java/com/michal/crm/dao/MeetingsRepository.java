package com.michal.crm.dao;

import com.michal.crm.model.Meetings;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Transactional
public interface MeetingsRepository extends CrudRepository<Meetings, Integer> {

    @Query("select M from Meetings M where M.term <= :night and M.term >= :morning")
    List<Meetings> getTodayMeetings(@Param("night")Date night, @Param("morning") Date morning);

    @Query("select M from Meetings M where M.term >= :now and M.complete = :complete")
    List<Meetings> getActualMeetings(@Param("now")Date now, @Param("complete")boolean complete);

    @Query("select M from Meetings M where M.term < :now and M.complete = :complete")
    List<Meetings> getUnmetMeetings(@Param("now")Date now, @Param("complete")boolean complete);

    @Query("select M from Meetings M where M.complete = :complete")
    List<Meetings> getHistoryMeetings(@Param("complete")boolean complete);
}
