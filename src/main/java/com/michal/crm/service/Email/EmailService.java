package com.michal.crm.service.Email;

import com.michal.crm.dto.EmailDto;
import com.michal.crm.dto.ProfileDto;
import com.michal.crm.model.types.ResultTypes;

import java.io.IOException;
import java.util.List;

public interface EmailService {
    EmailDto addItemToTable(int itemId, boolean isCont);
    ResultTypes updateEmailInfo(ProfileDto profileDto);
    int generateListId(List<EmailDto> emailDtoList);
    List<EmailDto> removeItem(List<EmailDto> itemsList, int itemId);
    void updateOutlookPath(String path);
    boolean openInOutLook(List<EmailDto> emailList);
    String getEmails(List<EmailDto> emailDtoList);
}
