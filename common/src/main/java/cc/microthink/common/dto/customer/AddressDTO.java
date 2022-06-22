package cc.microthink.common.dto.customer;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AddressDTO {

    private Long id;

    private String postalCode;

    private String city;

    private String details;

    private Boolean defaultAddr;

    public AddressDTO(Long id, String postalCode, String city, String details, Boolean defaultAddr) {
        this.id = id;
        this.postalCode = postalCode;
        this.city = city;
        this.details = details;
        this.defaultAddr = defaultAddr;
    }


}
