package com.michal.crm.model;

import com.michal.crm.model.types.StorageType;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

@Entity
@Table(name = "files")
public class Files {

    @Id
    @GeneratedValue
    @Column(name = "file_id")
    private Integer id;

    @NotNull
    @OneToOne
    @JoinColumn(name = "user_id")
    private Users user;

    @NotNull
    @Size(min = 1, max = 30)
    @Column(name = "file_name")
    private String name;

    @Column(name = "file_type")
    private String type;

    @Column(name = "file_size")
    private long size;

    @NotNull
    @Column(name = "file_path")
    private String path;

    @NotNull
    @Column(name = "res_path")
    private String resPath;

    @Column(name = "upload_date")
    private Date uploadDate;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "academic_degree")
    private StorageType storageType;

    //region getters
    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public long getSize() {
        return size;
    }

    public String getPath() {
        return path;
    }

    public Date getUploadDate() {
        return uploadDate;
    }

    public Users getUser() {
        return user;
    }

    public StorageType getStorageType() {
        return storageType;
    }

    public String getResPath() {
        return resPath;
    }

    //endregion

    //region setters
    public void setName(String name) {
        this.name = name;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setUploadDate(Date uploadDate) {
        this.uploadDate = uploadDate;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public void setStorageType(StorageType storageType) {
        this.storageType = storageType;
    }

    public void setResPath(String resPath) {
        this.resPath = resPath;
    }

    //endregion

    public Files(){
        this.uploadDate = new Date();
    }

    public Files(Users user, String name, String type, long size, String path, String resPath, Date uploadDate, StorageType storageType) {
        this.user = user;
        this.name = name;
        this.type = type;
        this.size = size;
        this.path = path;
        this.resPath = resPath;
        this.uploadDate = uploadDate;
        this.storageType = storageType;
    }
}
