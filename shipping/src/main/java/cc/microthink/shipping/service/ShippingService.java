package cc.microthink.shipping.service;

import cc.microthink.shipping.domain.Shipping;
import cc.microthink.shipping.repository.ShippingRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Shipping}.
 */
@Service
@Transactional
public class ShippingService {

    private final Logger log = LoggerFactory.getLogger(ShippingService.class);

    private final ShippingRepository shippingRepository;

    public ShippingService(ShippingRepository shippingRepository) {
        this.shippingRepository = shippingRepository;
    }

    /**
     * Save a shipping.
     *
     * @param shipping the entity to save.
     * @return the persisted entity.
     */
    public Shipping save(Shipping shipping) {
        log.debug("Request to save Shipping : {}", shipping);
        return shippingRepository.save(shipping);
    }

    /**
     * Update a shipping.
     *
     * @param shipping the entity to save.
     * @return the persisted entity.
     */
    public Shipping update(Shipping shipping) {
        log.debug("Request to save Shipping : {}", shipping);
        return shippingRepository.save(shipping);
    }

    /**
     * Partially update a shipping.
     *
     * @param shipping the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Shipping> partialUpdate(Shipping shipping) {
        log.debug("Request to partially update Shipping : {}", shipping);

        return shippingRepository
            .findById(shipping.getId())
            .map(existingShipping -> {
                if (shipping.getOrderId() != null) {
                    existingShipping.setOrderId(shipping.getOrderId());
                }
                if (shipping.getOrderSerial() != null) {
                    existingShipping.setOrderSerial(shipping.getOrderSerial());
                }
                if (shipping.getCustId() != null) {
                    existingShipping.setCustId(shipping.getCustId());
                }
                if (shipping.getCustAddress() != null) {
                    existingShipping.setCustAddress(shipping.getCustAddress());
                }
                if (shipping.getStatus() != null) {
                    existingShipping.setStatus(shipping.getStatus());
                }
                if (shipping.getCreatedTime() != null) {
                    existingShipping.setCreatedTime(shipping.getCreatedTime());
                }
                if (shipping.getSentTime() != null) {
                    existingShipping.setSentTime(shipping.getSentTime());
                }
                if (shipping.getAchievedTime() != null) {
                    existingShipping.setAchievedTime(shipping.getAchievedTime());
                }

                return existingShipping;
            })
            .map(shippingRepository::save);
    }

    /**
     * Get all the shippings.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Shipping> findAll(Pageable pageable) {
        log.debug("Request to get all Shippings");
        return shippingRepository.findAll(pageable);
    }

    /**
     * Get one shipping by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Shipping> findOne(Long id) {
        log.debug("Request to get Shipping : {}", id);
        return shippingRepository.findById(id);
    }

    /**
     * Delete the shipping by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Shipping : {}", id);
        shippingRepository.deleteById(id);
    }
}
