package com.sh.catalogservice.service;

import com.sh.catalogservice.jpa.CatalogEntity;

import java.util.List;

public interface CatalogService {
    List<CatalogEntity> getAllCatalogs();
}
