package com.stouduo.dcw.domain;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "formvalue")
public class FormValue implements Serializable {
    private String id;
    private String value;
    private String author;
    private Date createTime;
    private Date lastModifyTime;
    private String submitIP;
    private String browser;
    private String os;
    private String lastModifyPerson;
    private String form;
    private boolean del;

    @Column(name = "del")
    public boolean getDel() {
        return del;
    }

    public void setDel(boolean del) {
        this.del = del;
    }

    @Column(name = "form")
    public String getForm() {
        return form;
    }

    public void setForm(String form) {
        this.form = form;
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

    @Column(name = "value", nullable = false, columnDefinition="TEXT")
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
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

    @Column(name = "submitip", nullable = false, length = 64)
    public String getSubmitIP() {
        return submitIP;
    }

    public void setSubmitIP(String submitIP) {
        this.submitIP = submitIP;
    }

    @Column(name = "browser", nullable = false, length = 36)
    public String getBrowser() {
        return browser;
    }

    public void setBrowser(String browser) {
        this.browser = browser;
    }

    @Column(name = "os", nullable = false, length = 36)
    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    @Column(name = "lastmodifyperson", nullable = false, length = 36)
    public String getLastModifyPerson() {
        return lastModifyPerson;
    }

    public void setLastModifyPerson(String lastModifyPerson) {
        this.lastModifyPerson = lastModifyPerson;
    }
}
