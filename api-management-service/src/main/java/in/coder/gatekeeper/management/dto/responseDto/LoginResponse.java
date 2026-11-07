package in.coder.gatekeeper.management.dto.responseDto;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginResponse {

    private String token;
    private String tokenType;
    private long expiresInSecond;
    private String roleId;
    private Long organizationId;

}
