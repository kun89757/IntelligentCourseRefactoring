package org.kun.intelligentcourse.dto.authDTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JWTResponseDTO {

    private String accessToken;

    private String refreshToken;
}
