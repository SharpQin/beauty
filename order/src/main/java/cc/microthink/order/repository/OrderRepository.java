package cc.microthink.order.repository;

import cc.microthink.order.domain.Order;
import cc.microthink.order.domain.enumeration.OrderStatus;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

/**
 * Spring Data SQL repository for the Order entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByStatusAndCreatedTimeLessThan(OrderStatus status, Instant deadline);

}
