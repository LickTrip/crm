package com.michal.crm.service;

import com.michal.crm.dao.EmailConfigRepository;
import com.michal.crm.dao.FtpConfigRepository;
import com.michal.crm.dao.UsersRepository;
import com.michal.crm.dto.ProfileDto;
import com.michal.crm.model.*;
import com.michal.crm.model.types.ResultTypes;
import com.michal.crm.model.types.RoleTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Objects;

@Service
public class UserService {

    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private RoleService roleService;
    @Autowired
    private EmailConfigRepository emailConfigRepository;
    @Autowired
    private FtpConfigRepository ftpConfigRepository;

    public Users getLoggedUser() {
        UserDetails logUser = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return usersRepository.findByUsername(logUser.getUsername());
    }

    public Users getUserById(int userId){
        return usersRepository.findUsersById(userId);
    }

    public void updateUserInfo(Users updUser) {
        Users oldUser = getLoggedUser();

        if (updUser.getFirstName() != null)
            oldUser.setFirstName(updUser.getFirstName());

        if (updUser.getSurname() != null)
            oldUser.setSurname(updUser.getSurname());

        if (updUser.getUsername() != null)
            oldUser.setUsername(updUser.getUsername());

        oldUser.setTelNumber(updUser.getTelNumber());
        oldUser.setAcademicDegree(updUser.getAcademicDegree());
        oldUser.setCompany(updUser.getCompany());
        oldUser.setWorkPosition(updUser.getWorkPosition());
        oldUser.setBornDate(updUser.getBornDate());
        oldUser.setImage(updUser.getImage());

        if (updUser.getAddress() != null) {
            Addresses upAdd = updUser.getAddress();
            Addresses oldAdd = oldUser.getAddress();
            oldAdd.setCity(upAdd.getCity());
            oldAdd.setState(upAdd.getState());
            oldAdd.setStreet(upAdd.getStreet());
            oldAdd.setStreetNo(upAdd.getStreetNo());
            oldAdd.setZip(upAdd.getZip());
            oldUser.setAddress(oldAdd);
        } else {
            oldUser.setAddress(null);
        }

        usersRepository.save(oldUser);
    }

    public ResultTypes changePass(ProfileDto profileDto) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        Users user = getLoggedUser();
        if (passwordEncoder.matches(profileDto.getPassOld(), user.getPassword())){
            if (Objects.equals(profileDto.getPassNew(), profileDto.getPassConf())){
                user.setPassword(profileDto.getPassNew());
                usersRepository.save(user);
                return ResultTypes.OK;
            }else {
                return ResultTypes.PASS_NOT_MATCH;
            }
        }
        return ResultTypes.WRONG_PASS;
    }

    public ResultTypes registerNewUser(ProfileDto profileDto) {
        Users user = profileDto.getUser();
        if (!profileDto.isAgreeCond())
            return ResultTypes.NO_AGREE_TERMS;

        if (!Objects.equals(profileDto.getPassNew(), profileDto.getPassConf())){
            return ResultTypes.PASS_NOT_MATCH;
        }

        user.setRoles(new ArrayList<Role>(){{add(roleService.getSpecificRole(RoleTypes.SUPER_USER));}});
        profileDto.getUser().setPassword(profileDto.getPassNew());
        profileDto.getUser().setEmailConfig(emailConfigRepository.save(profileDto.getUser().getEmailConfig()));
        profileDto.getUser().setFtpConfig(ftpConfigRepository.save(new UserFtpConfig()));
        usersRepository.save(user);
        return ResultTypes.OK;
    }
}
