package com.michal.crm.service;

import com.michal.crm.dao.FtpConfigRepository;
import com.michal.crm.dto.ProfileDto;
import com.michal.crm.model.UserFtpConfig;
import com.michal.crm.model.types.ResultTypes;
import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.Objects;

@Service
public class FtpService {
    @Autowired
    private UserService userService;
    @Autowired
    private FtpConfigRepository ftpConfigRepository;

    public ResultTypes updateFtpInfo(ProfileDto profileDto) {
        UserFtpConfig oldFtpConf = userService.getLoggedUser().getFtpConfig();
        UserFtpConfig newFtpConf = profileDto.getUser().getFtpConfig();

        if (newFtpConf.getFtpName() != null)
            oldFtpConf.setFtpName(newFtpConf.getFtpName());

        if (newFtpConf.getFtpHost() != null)
            oldFtpConf.setFtpHost(newFtpConf.getFtpHost());

        if (newFtpConf.getFtpPort() > 0)
            oldFtpConf.setFtpPort(newFtpConf.getFtpPort());

        ResultTypes resultTypes = ResultTypes.OK;
        if (!Objects.equals(profileDto.getPassNew(), "")) {
            if (Objects.equals(profileDto.getPassNew(), profileDto.getPassConf())) {
                oldFtpConf.setFtpPassword(profileDto.getPassNew());
            } else {
                resultTypes = ResultTypes.PASS_NOT_MATCH;
            }
        }

        ftpConfigRepository.save(oldFtpConf);
        return resultTypes;
    }
}
