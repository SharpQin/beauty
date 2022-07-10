package cc.microthink.shipping.repository;

import cc.microthink.shipping.domain.Shipping;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Shipping entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ShippingRepository extends JpaRepository<Shipping, Long> {}
