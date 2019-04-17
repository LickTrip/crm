package com.michal.crm.dao;

import com.michal.crm.model.Contacts;
import com.michal.crm.model.Meetings;
import com.michal.crm.model.summaries.MeetingContacts;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface MeetingContactsRepository extends CrudRepository<MeetingContacts,Integer>{

    List<MeetingContacts> findMeetingContactsByMeetingId(@Param("id") int meetingId);

    @Query("select M from MeetingContacts M where M.contact=:cont and M.meeting=:meet")
    MeetingContacts getMeetingContact(@Param("cont")Contacts cont, @Param("meet")Meetings meet);
}
