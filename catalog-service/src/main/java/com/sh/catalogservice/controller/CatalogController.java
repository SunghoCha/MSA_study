package com.sh.catalogservice.controller;

import com.sh.catalogservice.service.CatalogService;
import com.sh.catalogservice.vo.ResponseCatalog;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(("/catalog-service"))
@RequiredArgsConstructor
public class CatalogController {

    private final Environment env;
    private final CatalogService catalogService;
    private final ModelMapper modelMapper;

    @GetMapping("/health-check")
    public String status() {
        return String.format("It's Working in Catalog Service on PORT %s",
                env.getProperty("local.server.port"));
    }

    @GetMapping("/catalogs")
    public ResponseEntity<List<ResponseCatalog>> getCatalogs() {
        List<ResponseCatalog> catalogs = catalogService.getAllCatalogs().stream()
                .map(catalog -> modelMapper.map(catalog, ResponseCatalog.class))
                .toList();

        return ResponseEntity.ok().body(catalogs);
    }
}
