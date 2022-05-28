package cc.microthink.customer.web.rest;

import cc.microthink.customer.domain.CusAccount;
import cc.microthink.customer.repository.CusAccountRepository;
import cc.microthink.customer.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link cc.microthink.customer.domain.CusAccount}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class CusAccountResource {

    private final Logger log = LoggerFactory.getLogger(CusAccountResource.class);

    private static final String ENTITY_NAME = "customerCusAccount";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CusAccountRepository cusAccountRepository;

    public CusAccountResource(CusAccountRepository cusAccountRepository) {
        this.cusAccountRepository = cusAccountRepository;
    }

    /**
     * {@code POST  /cus-accounts} : Create a new cusAccount.
     *
     * @param cusAccount the cusAccount to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new cusAccount, or with status {@code 400 (Bad Request)} if the cusAccount has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/cus-accounts")
    public ResponseEntity<CusAccount> createCusAccount(@Valid @RequestBody CusAccount cusAccount) throws URISyntaxException {
        log.debug("REST request to save CusAccount : {}", cusAccount);
        if (cusAccount.getId() != null) {
            throw new BadRequestAlertException("A new cusAccount cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CusAccount result = cusAccountRepository.save(cusAccount);
        return ResponseEntity
            .created(new URI("/api/cus-accounts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /cus-accounts/:id} : Updates an existing cusAccount.
     *
     * @param id the id of the cusAccount to save.
     * @param cusAccount the cusAccount to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cusAccount,
     * or with status {@code 400 (Bad Request)} if the cusAccount is not valid,
     * or with status {@code 500 (Internal Server Error)} if the cusAccount couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/cus-accounts/{id}")
    public ResponseEntity<CusAccount> updateCusAccount(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CusAccount cusAccount
    ) throws URISyntaxException {
        log.debug("REST request to update CusAccount : {}, {}", id, cusAccount);
        if (cusAccount.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cusAccount.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cusAccountRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CusAccount result = cusAccountRepository.save(cusAccount);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, cusAccount.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /cus-accounts/:id} : Partial updates given fields of an existing cusAccount, field will ignore if it is null
     *
     * @param id the id of the cusAccount to save.
     * @param cusAccount the cusAccount to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cusAccount,
     * or with status {@code 400 (Bad Request)} if the cusAccount is not valid,
     * or with status {@code 404 (Not Found)} if the cusAccount is not found,
     * or with status {@code 500 (Internal Server Error)} if the cusAccount couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/cus-accounts/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CusAccount> partialUpdateCusAccount(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CusAccount cusAccount
    ) throws URISyntaxException {
        log.debug("REST request to partial update CusAccount partially : {}, {}", id, cusAccount);
        if (cusAccount.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cusAccount.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cusAccountRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CusAccount> result = cusAccountRepository
            .findById(cusAccount.getId())
            .map(existingCusAccount -> {
                if (cusAccount.getName() != null) {
                    existingCusAccount.setName(cusAccount.getName());
                }
                if (cusAccount.getCardNo() != null) {
                    existingCusAccount.setCardNo(cusAccount.getCardNo());
                }
                if (cusAccount.getAccountType() != null) {
                    existingCusAccount.setAccountType(cusAccount.getAccountType());
                }

                return existingCusAccount;
            })
            .map(cusAccountRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, cusAccount.getId().toString())
        );
    }

    /**
     * {@code GET  /cus-accounts} : get all the cusAccounts.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of cusAccounts in body.
     */
    @GetMapping("/cus-accounts")
    public List<CusAccount> getAllCusAccounts() {
        log.debug("REST request to get all CusAccounts");
        return cusAccountRepository.findAll();
    }

    /**
     * {@code GET  /cus-accounts/:id} : get the "id" cusAccount.
     *
     * @param id the id of the cusAccount to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the cusAccount, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/cus-accounts/{id}")
    public ResponseEntity<CusAccount> getCusAccount(@PathVariable Long id) {
        log.debug("REST request to get CusAccount : {}", id);
        Optional<CusAccount> cusAccount = cusAccountRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(cusAccount);
    }

    /**
     * {@code DELETE  /cus-accounts/:id} : delete the "id" cusAccount.
     *
     * @param id the id of the cusAccount to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/cus-accounts/{id}")
    public ResponseEntity<Void> deleteCusAccount(@PathVariable Long id) {
        log.debug("REST request to delete CusAccount : {}", id);
        cusAccountRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
