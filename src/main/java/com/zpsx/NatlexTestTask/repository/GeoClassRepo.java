package com.zpsx.NatlexTestTask.repository;

import com.zpsx.NatlexTestTask.domain.GeoClass;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GeoClassRepo extends JpaRepository<GeoClass, Long> {
    Optional<GeoClass> findByCodeAndName(String code, String name);
}
