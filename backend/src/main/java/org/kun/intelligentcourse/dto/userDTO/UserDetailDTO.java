package org.kun.intelligentcourse.dto.userDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserDetailDTO {
    private Long id;

    private String username;

    private String email;

    private String phone;

    private String avatarUrl;

    private Boolean blocked;

    private List<String> roles;
}
