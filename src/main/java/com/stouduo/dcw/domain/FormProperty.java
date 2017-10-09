package com.stouduo.dcw.domain;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "formproperty")
public class FormProperty implements Serializable {
    private String id;
    private String type;
    private String value;
    private String form;


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

    @Column(name = "type", nullable = false, length = 36)
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Column(name = "value", nullable = false, length = 255)
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Column(name = "form", nullable = false, length = 36)
    public String getForm() {
        return form;
    }

    public void setForm(String form) {
        this.form = form;
    }
}
