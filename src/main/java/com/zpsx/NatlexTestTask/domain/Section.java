package com.zpsx.NatlexTestTask.domain;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
@NoArgsConstructor
@JsonPropertyOrder({ "id", "name", "geoClasses" })
public class Section {
    @Id @GeneratedValue private long id;
    @NotBlank private String name;

    @ManyToMany(fetch = FetchType.EAGER)
    private List<GeoClass> geoClasses;

    public Section(String name){
        this.name = name;
        this.geoClasses = new ArrayList<>();
    }

    public Section(String name, List<GeoClass> geoClasses){
        this.name = name;
        this.geoClasses = geoClasses;
    }
}
