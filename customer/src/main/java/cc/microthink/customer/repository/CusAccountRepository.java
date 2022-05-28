package cc.microthink.customer.repository;

import cc.microthink.customer.domain.CusAccount;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the CusAccount entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CusAccountRepository extends JpaRepository<CusAccount, Long> {}
