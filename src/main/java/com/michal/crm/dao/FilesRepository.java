package com.michal.crm.dao;

import com.michal.crm.model.Files;
import com.michal.crm.model.Users;
import com.michal.crm.model.types.StorageType;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface FilesRepository extends CrudRepository<Files, Integer> {
    Files findFilesByIdAndUser(@Param("id") int id, @Param("user") Users user);

    List<Files> findFilesByUserAndStorageType(@Param("user") Users user, @Param("sType") StorageType storageType);

    List<Files> findFilesByUser(@Param("user") Users user);

    Files findFilesByPathAndUser(@Param("path") String path, @Param("user") Users user);
}
