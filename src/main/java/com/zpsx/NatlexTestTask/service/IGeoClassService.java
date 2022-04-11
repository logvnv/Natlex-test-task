package com.zpsx.NatlexTestTask.service;

import com.zpsx.NatlexTestTask.domain.GeoClass;
import com.zpsx.NatlexTestTask.domain.dto.GeoClassPostRequestBody;
import com.zpsx.NatlexTestTask.domain.dto.GeoClassPutRequestBody;

import java.util.List;

public interface IGeoClassService {
    List<GeoClass> readAllGeoClasses();
    GeoClass readGeoClassByCodeAndName(String code, String name);
    GeoClass createGeoClass(GeoClassPostRequestBody postBody);
    GeoClass readGeoClass(long id);
    GeoClass updateGeoClass(GeoClassPutRequestBody putBody);
    GeoClass deleteGeoClass(long id);
}
