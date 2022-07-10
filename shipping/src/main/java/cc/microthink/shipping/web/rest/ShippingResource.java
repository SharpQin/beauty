package cc.microthink.shipping.web.rest;

import cc.microthink.shipping.domain.Shipping;
import cc.microthink.shipping.repository.ShippingRepository;
import cc.microthink.shipping.service.ShippingService;
import cc.microthink.shipping.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link cc.microthink.shipping.domain.Shipping}.
 */
@RestController
@RequestMapping("/api")
public class ShippingResource {

    private final Logger log = LoggerFactory.getLogger(ShippingResource.class);

    private static final String ENTITY_NAME = "shippingShipping";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ShippingService shippingService;

    private final ShippingRepository shippingRepository;

    public ShippingResource(ShippingService shippingService, ShippingRepository shippingRepository) {
        this.shippingService = shippingService;
        this.shippingRepository = shippingRepository;
    }

    /**
     * {@code POST  /shippings} : Create a new shipping.
     *
     * @param shipping the shipping to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new shipping, or with status {@code 400 (Bad Request)} if the shipping has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/shippings")
    public ResponseEntity<Shipping> createShipping(@RequestBody Shipping shipping) throws URISyntaxException {
        log.debug("REST request to save Shipping : {}", shipping);
        if (shipping.getId() != null) {
            throw new BadRequestAlertException("A new shipping cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Shipping result = shippingService.save(shipping);
        return ResponseEntity
            .created(new URI("/api/shippings/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /shippings/:id} : Updates an existing shipping.
     *
     * @param id the id of the shipping to save.
     * @param shipping the shipping to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated shipping,
     * or with status {@code 400 (Bad Request)} if the shipping is not valid,
     * or with status {@code 500 (Internal Server Error)} if the shipping couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/shippings/{id}")
    public ResponseEntity<Shipping> updateShipping(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Shipping shipping
    ) throws URISyntaxException {
        log.debug("REST request to update Shipping : {}, {}", id, shipping);
        if (shipping.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, shipping.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!shippingRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Shipping result = shippingService.update(shipping);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, shipping.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /shippings/:id} : Partial updates given fields of an existing shipping, field will ignore if it is null
     *
     * @param id the id of the shipping to save.
     * @param shipping the shipping to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated shipping,
     * or with status {@code 400 (Bad Request)} if the shipping is not valid,
     * or with status {@code 404 (Not Found)} if the shipping is not found,
     * or with status {@code 500 (Internal Server Error)} if the shipping couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/shippings/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Shipping> partialUpdateShipping(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Shipping shipping
    ) throws URISyntaxException {
        log.debug("REST request to partial update Shipping partially : {}, {}", id, shipping);
        if (shipping.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, shipping.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!shippingRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Shipping> result = shippingService.partialUpdate(shipping);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, shipping.getId().toString())
        );
    }

    /**
     * {@code GET  /shippings} : get all the shippings.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of shippings in body.
     */
    @GetMapping("/shippings")
    public ResponseEntity<List<Shipping>> getAllShippings(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Shippings");
        Page<Shipping> page = shippingService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /shippings/:id} : get the "id" shipping.
     *
     * @param id the id of the shipping to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the shipping, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/shippings/{id}")
    public ResponseEntity<Shipping> getShipping(@PathVariable Long id) {
        log.debug("REST request to get Shipping : {}", id);
        Optional<Shipping> shipping = shippingService.findOne(id);
        return ResponseUtil.wrapOrNotFound(shipping);
    }

    /**
     * {@code DELETE  /shippings/:id} : delete the "id" shipping.
     *
     * @param id the id of the shipping to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/shippings/{id}")
    public ResponseEntity<Void> deleteShipping(@PathVariable Long id) {
        log.debug("REST request to delete Shipping : {}", id);
        shippingService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
