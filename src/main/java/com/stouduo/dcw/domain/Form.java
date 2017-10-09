package com.stouduo.dcw.domain;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "form")
public class Form implements Serializable {
    private String id;
    private String author;
    private Date createTime;
    private Date lastModifyTime;
    private int submitPrivilege;
    private int submitCountLimited;
    private boolean collectFlag;

    @Column(name = "iscollect", nullable = false)
    public boolean getCollectFlag() {
        return collectFlag;
    }

    public void setCollectFlag(boolean collectFlag) {
        this.collectFlag = collectFlag;
    }


    @Id
    @Column(name = "id", unique = true, nullable = false, length = 36)
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Column(name = "author", nullable = false, length = 36)
    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    @Column(name = "createtime", nullable = false, length = 36)
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Column(name = "lastmodifytime", nullable = false, length = 36)
    public Date getLastModifyTime() {
        return lastModifyTime;
    }

    public void setLastModifyTime(Date lastModifyTime) {
        this.lastModifyTime = lastModifyTime;
    }

    @Column(name = "submitprivilege")
    public int getSubmitPrivilege() {
        return submitPrivilege;
    }

    public void setSubmitPrivilege(int submitPrivilege) {
        this.submitPrivilege = submitPrivilege;
    }

    @Column(name = "submitcountlimited")
    public int getSubmitCountLimited() {
        return submitCountLimited;
    }

    public void setSubmitCountLimited(int submitCountLimited) {
        this.submitCountLimited = submitCountLimited;
    }
}
