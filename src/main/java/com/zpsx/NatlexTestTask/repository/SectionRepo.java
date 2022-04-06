package com.zpsx.NatlexTestTask.repository;

import com.zpsx.NatlexTestTask.domain.Section;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SectionRepo extends JpaRepository<Section, Long> {
    @Query("SELECT s FROM Section s JOIN s.geoClasses gc WHERE gc.code=:code")
    List<Section> findAllByGeoCode(@Param("code") String code);

    Optional<Section> findByName(String name);
}
