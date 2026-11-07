package in.coder.gatekeeper.management.service;

import in.coder.gatekeeper.management.dto.requestDto.LoginRequest;
import in.coder.gatekeeper.management.dto.responseDto.LoginResponse;
import in.coder.gatekeeper.management.entity.ManagementUser;
import in.coder.gatekeeper.management.entity.OrganizationStatus;
import in.coder.gatekeeper.management.exception.ApiException;
import in.coder.gatekeeper.management.repository.ManagementUserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
public class AuthenticationService {
    private final ManagementUserRepository managementUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtEncoder jwtEncoder;
    private final long expiresInSeconds;

    public AuthenticationService(ManagementUserRepository managementUserRepository,
                                 PasswordEncoder passwordEncoder, JwtEncoder jwtEncoder,
                                 @Value("${security.jwt.expiration-seconds}")long expiresInSeconds){
        this.managementUserRepository=managementUserRepository;
        this.passwordEncoder=passwordEncoder;
        this.jwtEncoder=jwtEncoder;
        this.expiresInSeconds=expiresInSeconds;
    }
    @Transactional(readOnly = true)
    public LoginResponse login(LoginRequest request){

        ManagementUser user=managementUserRepository.findByUserName(request.getUsername())
                .orElseThrow(()->invalidCredentials());

        if(!user.isEnabled() || !passwordEncoder.matches(request.getPassword(), user.getPasswordHash())){
            throw invalidCredentials();
        }
        if (user.getOrganization() != null && user.getOrganization().getStatus() != OrganizationStatus.ACTIVE){
            throw  new ApiException(HttpStatus.FORBIDDEN,"IThe organization is not active ");
        }

        Instant issuedAt= Instant.now();
        String role = user.getRole().getName();
        Long organizationId=user.getOrganization()== null ? null : user.getOrganization().getId();

        JwtClaimsSet.Builder claims= JwtClaimsSet.builder()
                .issuer("gatekeeper-api-management")
                .issuedAt(issuedAt)
                .expiresAt(issuedAt.plusSeconds(expiresInSeconds))
                .subject(user.getUserName())
                .claim("role",role);

        if(organizationId != null){
            claims.claim("organizationId",organizationId);
        }

        JwsHeader header = JwsHeader.with(MacAlgorithm.HS256).build();
        String token = jwtEncoder.encode(
                JwtEncoderParameters.from(header,claims.build())).getTokenValue();
        return  new LoginResponse(token,"Bearer",expiresInSeconds,role,organizationId);


    }

    private RuntimeException invalidCredentials(){
        return  new ApiException(HttpStatus.UNAUTHORIZED,"Invalid username or password");
    }
}
