package com.michal.crm.service;

import com.michal.crm.model.Users;
import com.michal.crm.model.auxObjects.UserCacheInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@CacheConfig(cacheNames = "users")
public class CacheService {

    @Autowired
    private UserService userService;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private List<UserCacheInfo> users = new ArrayList<>();

    public UserCacheInfo findCacheLoggedUserInfo(){
        Users user = userService.getLoggedUser();
        UserCacheInfo userCacheInfo = new UserCacheInfo(user.getId(),user.getFirstName(), user.getSurname(), user.getCompany(), user.getImage());
        return userCacheInfo;
    }

    @Cacheable(value = "userLoggedInfo")
    public UserCacheInfo getUserInfo(){
        if(this.users.size()>0){
            logger.info("*** Cache loaded ***");
        }else {
            setUserInfo(findCacheLoggedUserInfo());
            logger.info("*** Cache loaded ***");
        }
        return this.users.get(0);
    }

    @CachePut(value = "userLoggedInfo")
    public UserCacheInfo editUserInfo(UserCacheInfo cacheInfo){
        try {
            this.users.set(0, cacheInfo);
            logger.info("*** Cache updated ***");
            return this.users.get(0);
        }catch (Exception ex){
            logger.error("*** Error during cache UPDATE ***");
            logger.error(ex.getMessage());
        }
        return cacheInfo;
    }

    @CachePut(value = "userLoggedInfo")
    public UserCacheInfo setUserInfo(UserCacheInfo cacheInfo){
        try {
            this.users.add(cacheInfo);
            logger.info("*** Cache set ***");
            return this.users.get(0);
        }catch (Exception ex){
            logger.error("*** Error during cache SET ***");
            logger.error(ex.getMessage());
        }
        return cacheInfo;
    }
}
