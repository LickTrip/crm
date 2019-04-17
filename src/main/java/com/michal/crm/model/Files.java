package com.michal.crm.model;

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
    @Size(min = 1, max = 20)
    @Column(name = "file_name")
    private String name;

    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "file_type")
    private String type;

    @Column(name = "file_size")
    private int size;

    @Lob
    @Column(name = "file_path")
    private String path;

    @Column(name = "upload_date")
    private Date uploadDate;

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

    public int getSize() {
        return size;
    }

    public String getPath() {
        return path;
    }

    public Date getUploadDate() {
        return uploadDate;
    }
    //endregion

    //region setters
    public void setName(String name) {
        this.name = name;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setUploadDate(Date uploadDate) {
        this.uploadDate = uploadDate;
    }
    //endregion

    public Files(){}

    public Files(String name, String type, int size, String path, Date uploadDate) {
        this.name = name;
        this.type = type;
        this.size = size;
        this.path = path;
        this.uploadDate = uploadDate;
    }
}
