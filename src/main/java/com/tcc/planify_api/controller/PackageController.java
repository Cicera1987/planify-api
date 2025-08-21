package com.tcc.planify_api.controller;
import com.tcc.planify_api.docs.PackageServiceApi;
import com.tcc.planify_api.dto.packageServices.PackageCreateDTO;
import com.tcc.planify_api.dto.packageServices.PackageDTO;
import com.tcc.planify_api.service.PackageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/packages")
public class PackageController implements PackageServiceApi {

  private final PackageService packageService;

  @Override
  public ResponseEntity<List<PackageDTO>> getAllPackages() {
    List<PackageDTO> packages = packageService.listPackagesByOwner();
    return ResponseEntity.ok(packages);
  }

  @Override
  public ResponseEntity<PackageDTO> createPackage(@Valid @RequestBody PackageCreateDTO packageCreateDTO) {
    PackageDTO createdPackage = packageService.createPackage(packageCreateDTO);
    return ResponseEntity.status(HttpStatus.CREATED).body(createdPackage);
  }

  @Override
  public ResponseEntity<PackageDTO> updatePackage(@PathVariable("id") Long id,
                                                  @Valid @RequestBody PackageCreateDTO packageUpdateDTO) {
    PackageDTO updatedPackage = packageService.updatePackage(id, packageUpdateDTO);
    return ResponseEntity.ok(updatedPackage);
  }

  @Override
  public ResponseEntity<Void> deletePackage(@PathVariable("id") Long id) {
    packageService.deletePackage(id);
    return ResponseEntity.noContent().build();
  }
}