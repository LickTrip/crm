package com.michal.crm.service.Storage;

import com.michal.crm.model.Contacts;
import com.michal.crm.model.Files;
import com.michal.crm.model.types.StorageType;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

public interface StorageService {

    void init();

    com.michal.crm.model.Files store(MultipartFile file, StorageType type);

    Stream<Path> loadAll(StorageType type);

    Path load(String filename, StorageType type);

    Resource loadAsResource(String filename, StorageType type);

    List<Files> loadFilesInfo(StorageType storageType);

    List<Files> loadFilesInfo();

    Files loadFileInfo(int fileId);

    List<Files> loadContactFilesInfo(Contacts contact);

    void deleteAll(StorageType type);

    void deleteFile(Files file);

    void pairFileWithUser(Files file, Contacts contact);

    void divideFileWithUser(Files file, Contacts contact);

    void divideFileWithAllUser(Files file);
}
