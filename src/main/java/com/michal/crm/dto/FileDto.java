package com.michal.crm.dto;

import org.springframework.web.multipart.MultipartFile;

public class FileDto {
    private MultipartFile file;
    private MultipartFile[] files;

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }

    public MultipartFile[] getFiles() {
        return files;
    }

    public void setFiles(MultipartFile[] files) {
        this.files = files;
    }

    public FileDto(MultipartFile file, MultipartFile[] files) {
        this.file = file;
        this.files = files;
    }

    public FileDto() {
    }
}
