package cc.microthink.customer.service;

import cc.microthink.common.dto.customer.AccountDTO;
import cc.microthink.common.dto.customer.AddressDTO;
import cc.microthink.common.dto.customer.CustomerDTO;
import cc.microthink.customer.domain.Customer;
import cc.microthink.customer.repository.CustomerRepository;
import cc.microthink.customer.security.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MKCustomerService {

    private Logger log = LoggerFactory.getLogger(MKCustomerService.class);

    private final CustomerRepository customerRepository;

    public MKCustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public CustomerDTO getCustomerInfo(boolean includeAddress, boolean includeAccount) {

        Optional<String> optUser = SecurityUtils.getCurrentUserLogin();
        if (!optUser.isPresent()) {
            log.warn("getCustomerInfo: Can't get current login user.");
            throw new RuntimeException("No login user.");
        }
        String loginUser = optUser.get();
        Customer customer = customerRepository.findByName(loginUser);
        if (customer == null) {
            throw new RuntimeException("No customer with name " + loginUser);
        }

        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setId(customer.getId())
            .setName(customer.getName())
            .setNickName(customer.getNickName())
            .setEmail(customer.getEmail())
            .setPhone(customer.getPhone())
            .setPoint(customer.getPoint())
            .setLevel(customer.getLevel())
            .setVip(customer.getVip())
            .setLangKey(customer.getLangKey());

        if (includeAddress) {
            List<AddressDTO> addressList = customer.getAddresses().stream()
                .map(address -> new AddressDTO(address.getId(),
                    address.getPostalCode(),
                    address.getCity(),
                    address.getDetails(),
                    address.getDefaultAddr())).collect(Collectors.toList());
            customerDTO.setAddresses(addressList);
        }
        if (includeAccount) {
            List<AccountDTO> accountList = customer.getAccounts().stream()
                .map(account -> new AccountDTO(account.getId(),
                    account.getName(),
                    account.getCardNo(),
                    AccountDTO.AccountType.valueOf(account.getAccountType().toString()))).collect(Collectors.toList());
            customerDTO.setAccounts(accountList);
        }

        return customerDTO;
    }

}
