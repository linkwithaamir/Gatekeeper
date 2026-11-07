package in.coder.gatekeeper.management.service;

import in.coder.gatekeeper.management.dto.responseDto.HealthResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HealthService {

    private final JdbcTemplate jdbcTemplate;

    public String getHealth(){
        String databaseStatus="DOWN";
    try {
        Integer result = jdbcTemplate.queryForObject("Select 1", Integer.class);
        if (Integer.valueOf(1).equals(result)) {
            }
        }catch(Exception e){
        System.out.println("Health Checked Failed");


        }
        return  databaseStatus;
      }

    }

