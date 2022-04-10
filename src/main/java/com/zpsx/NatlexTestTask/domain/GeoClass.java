package com.zpsx.NatlexTestTask.domain;

import com.zpsx.NatlexTestTask.domain.dto.GeoClassPostRequestBody;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Getter @Setter
@NoArgsConstructor
public class GeoClass {
    @Id @GeneratedValue private long id;
    @Column(length = 16) private String code;
    private String name;

    public GeoClass(GeoClassPostRequestBody postBody){
        this.code = postBody.getCode();
        this.name = postBody.getName();
    }

    public GeoClass(String code, String name){
        this.code = code;
        this.name = name;
    }
}
