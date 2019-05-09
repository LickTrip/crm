package com.michal.crm.dao;

import com.michal.crm.model.Meetings;
import com.michal.crm.model.Users;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Transactional
public interface MeetingsRepository extends CrudRepository<Meetings, Integer> {

    Meetings findMeetingsByIdAndUser(@Param("id") int id, @Param("user") Users user);

    @Query("select M from Meetings M where M.term <= :night and M.term >= :morning and M.user=:user")
    List<Meetings> getTodayMeetings(@Param("night")Date night, @Param("morning") Date morning, @Param("user") Users user);

    @Query("select M from Meetings M where M.term >= :now and M.complete = :complete and M.user=:user")
    List<Meetings> getActualMeetings(@Param("now")Date now, @Param("complete")boolean complete, @Param("user") Users user);

    @Query("select M from Meetings M where M.term < :now and M.complete = :complete and M.user=:user")
    List<Meetings> getUnmetMeetings(@Param("now")Date now, @Param("complete")boolean complete, @Param("user") Users user);

    @Query("select M from Meetings M where M.complete = :complete and M.user=:user")
    List<Meetings> getHistoryMeetings(@Param("complete")boolean complete, @Param("user") Users user);
}
