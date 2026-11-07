package in.coder.gatekeeper.management.controller;

import in.coder.gatekeeper.management.dto.requestDto.TenantOrganizationRequest;
import in.coder.gatekeeper.management.dto.responseDto.OrganizationResponse;
import in.coder.gatekeeper.management.service.OrganizationService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tenant/organization")
public class TenantOrganizationController {

    private final OrganizationService organizationService;


    public TenantOrganizationController(OrganizationService organizationService) {
        this.organizationService = organizationService;
    }
    @GetMapping
    public ResponseEntity<OrganizationResponse> getOwnOrganization() {
        return ResponseEntity.ok(organizationService.getOwnOrganization());
    }

    @PutMapping
    public ResponseEntity<OrganizationResponse> updateOwnOrganization(
            @Valid @RequestBody TenantOrganizationRequest request) {
        return ResponseEntity.ok(organizationService.updateOwnOrganization(request));
    }
}
