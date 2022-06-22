package cc.microthink.common.dto.customer;

import lombok.*;
import lombok.experimental.Accessors;

import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Accessors(chain = true)
public class CustomerDTO {

    private Long id;

    private String name;

    private String nickName;

    private String phone;

    private String email;

    private String langKey;

    private Integer vip;

    private Integer level;

    private Integer point;

    private List<AddressDTO> addresses;

    private List<AccountDTO> accounts;

    public CustomerDTO(Long id, String name, String nickName, String phone, String email, String langKey, Integer vip, Integer level, Integer point) {
        this.id = id;
        this.name = name;
        this.nickName = nickName;
        this.phone = phone;
        this.email = email;
        this.langKey = langKey;
        this.vip = vip;
        this.level = level;
        this.point = point;
    }


}
