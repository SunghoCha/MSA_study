package com.sh.catalogservice.service;

import com.sh.catalogservice.jpa.CatalogEntity;
import com.sh.catalogservice.jpa.CatalogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class CatalogServiceImpl implements CatalogService {

    private final CatalogRepository catalogRepository;

    @Override
    public List<CatalogEntity> getAllCatalogs() {
        return catalogRepository.findAll();
    }
}
