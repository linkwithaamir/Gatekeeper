package in.coder.gatekeeper.management.controller;


import in.coder.gatekeeper.management.dto.responseDto.HealthResponse;
import in.coder.gatekeeper.management.service.HealthService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/health")
public class HealthController {

    private String serviceName;
    private String serviceDescription;

    public final HealthService healthService;

    public HealthController(HealthService healthService){
       this.healthService=healthService;

    }


    @GetMapping
    public ResponseEntity<HealthResponse> health(){
      String dbStatus=  healthService.getHealth();
        return ResponseEntity.ok(new HealthResponse("UP",dbStatus, LocalDate.now()));
    }
}
