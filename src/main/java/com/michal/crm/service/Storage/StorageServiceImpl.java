package com.michal.crm.service.Storage;

import com.michal.crm.dao.ContactFilesRepository;
import com.michal.crm.dao.FilesRepository;
import com.michal.crm.model.Contacts;
import com.michal.crm.model.Users;
import com.michal.crm.model.summaries.ContactFiles;
import com.michal.crm.model.types.StorageType;
import com.michal.crm.service.HelperService;
import com.michal.crm.service.Storage.Exception.StorageException;
import com.michal.crm.service.Storage.Exception.StorageFileNotFoundException;
import com.michal.crm.service.Storage.StorageService;
import com.michal.crm.service.UserService;
import org.apache.tomcat.jni.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Service
public class StorageServiceImpl implements StorageService {

    @Autowired
    private UserService userService;
    @Autowired
    private FilesRepository filesRepository;
    @Autowired
    private ContactFilesRepository contactFilesRepository;
    @Autowired
    private HelperService helperService;

    @Override
    public void init() {
        try {
            testStorageLocation();
        } catch (Exception ex) {
            throw new StorageException("Could not initialize storage", ex);
        }
    }

    @Override
    public com.michal.crm.model.Files store(MultipartFile file, StorageType type) {
        Path path = getFileLocation(type);
        String filename = StringUtils.cleanPath(file.getOriginalFilename());
        com.michal.crm.model.Files newFile;
        try {
            com.michal.crm.model.Files dbFile = filesRepository.findFilesByPathAndUser((path.toAbsolutePath() + "\\" + filename), userService.getLoggedUser());
            if (dbFile != null) {
                throw new StorageException("File " + filename + " already exist");
            }

            if (file.isEmpty()) {
                throw new StorageException("Failed to store empty file " + filename);
            }
            if (filename.contains("..")) {
                // This is a security check
                throw new StorageException(
                        "Cannot store file with relative path outside current directory "
                                + filename);
            }
            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, path.resolve(filename),
                        StandardCopyOption.REPLACE_EXISTING);
            }
            newFile = writeFileInfoToDb(file, type);
        } catch (IOException e) {
            throw new StorageException("Failed to store file " + filename, e);
        }

        return newFile;
    }

    @Override
    public Stream<Path> loadAll(StorageType type) {
        Path path = getFileLocation(type);
        try {
            return Files.walk(path, 1)
                    .filter(mPath -> !mPath.equals(path))
                    .map(path::relativize);
        } catch (IOException e) {
            throw new StorageException("Failed to read stored files", e);
        }
    }

    @Override
    public Path load(String filename, StorageType type) {
        Path path = getFileLocation(type);
        return path.resolve(filename);
    }

    @Override
    public Resource loadAsResource(String filename, StorageType type) {
        try {
            Path file = load(filename, type);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new StorageFileNotFoundException(
                        "Could not read file: " + filename);

            }
        } catch (MalformedURLException e) {
            throw new StorageFileNotFoundException("Could not read file: " + filename, e);
        }
    }

    @Override
    public void deleteAll(StorageType type) {
        Path path = getFileLocation(type);
        FileSystemUtils.deleteRecursively(path.toFile());
    }

    public void deleteFile(com.michal.crm.model.Files file) {
        try {
            deleteFile(file.getPath());
        } catch (IOException ex) {
            throw new StorageException("Failed to delete stored file", ex);
        }

        try {
            deleteFileInfo(file);
        } catch (Exception ex) {
            throw new StorageException("Failed to delete db info file", ex);
        }
    }

    @Override
    public void pairFileWithUser(com.michal.crm.model.Files file, Contacts contact) {
        contactFilesRepository.save(new ContactFiles(file, contact, userService.getLoggedUser()));
    }

    @Override
    public void divideFileWithUser(com.michal.crm.model.Files file, Contacts contact) {
        ContactFiles contactFiles = contactFilesRepository.findContactFilesByFileAndContactAndUser(file, contact, userService.getLoggedUser());
        contactFilesRepository.delete(contactFiles);
    }

    @Override
    public void divideFileWithAllUser(com.michal.crm.model.Files file) {
        List<ContactFiles> contactFilesList = contactFilesRepository.findContactFilesByFileAndUser(file, userService.getLoggedUser());
        contactFilesRepository.deleteAll(contactFilesList);
    }


    private void deleteFile(String path) throws IOException {
        FileSystemUtils.deleteRecursively(Paths.get(path));
    }


    private void deleteFileInfo(com.michal.crm.model.Files file) {
        filesRepository.delete(file);
    }

    @Override
    public List<com.michal.crm.model.Files> loadFilesInfo(StorageType storageType) {
        return filesRepository.findFilesByUserAndStorageType(userService.getLoggedUser(), storageType);
    }

    @Override
    public List<com.michal.crm.model.Files> loadFilesInfo() {
        return filesRepository.findFilesByUser(userService.getLoggedUser());
    }

    @Override
    public com.michal.crm.model.Files loadFileInfo(int fileId) {
        return filesRepository.findFilesByIdAndUser(fileId, userService.getLoggedUser());
    }

    @Override
    public List<com.michal.crm.model.Files> loadContactFilesInfo(Contacts contact) {
        List<ContactFiles> contactFilesList = contactFilesRepository.findContactFilesByContactAndUser(contact, userService.getLoggedUser());
        List<com.michal.crm.model.Files> filesList = new ArrayList<>();
        for (ContactFiles coF : contactFilesList
        ) {
            filesList.add(coF.getFile());
        }
        return filesList;
    }


    private com.michal.crm.model.Files writeFileInfoToDb(MultipartFile file, StorageType type) {
        Path path = getFileLocation(type);
        com.michal.crm.model.Files newFile = new com.michal.crm.model.Files();
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        newFile.setName(fileName);
        String fullPath = path.toAbsolutePath().toString() + "\\" + fileName;
        newFile.setPath(fullPath);
        newFile.setResPath(helperService.getRelPath(fullPath));
        newFile.setSize(file.getSize());
        newFile.setUser(userService.getLoggedUser());
        newFile.setType(file.getContentType());
        newFile.setStorageType(type);
        newFile = filesRepository.save(newFile);
        return newFile;
    }

    private Path testStorageLocation() {
        Users user = userService.getLoggedUser();
        String sRootPath = new File("").getAbsolutePath();
        String sUsersFiles = sRootPath + "\\users_files\\" + user.getFileStorageId();
        Path path = Paths.get(sUsersFiles);

        if (!Files.exists(path)) {
            try {
                Files.createDirectories(path);
                Files.createDirectories(Paths.get(sUsersFiles + StorageType.DOC.getPath()));
                Files.createDirectories(Paths.get(sUsersFiles + StorageType.IMG.getPath()));
                Files.createDirectories(Paths.get(sUsersFiles + StorageType.EMAIL.getPath()));
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return path;
    }

    private Path getFileLocation(StorageType type) {
        Users user = userService.getLoggedUser();
        String sRootPath = new File("").getAbsolutePath();
        String sUsersFiles = sRootPath + "\\users_files\\" + user.getFileStorageId();
        return Paths.get(sUsersFiles + type.getPath());
    }
}
