package cc.microthink.notification.web.rest;

import cc.microthink.notification.domain.NotificationLog;
import cc.microthink.notification.repository.NotificationLogRepository;
import cc.microthink.notification.service.NotificationLogService;
import cc.microthink.notification.web.rest.errors.BadRequestAlertException;
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
 * REST controller for managing {@link cc.microthink.notification.domain.NotificationLog}.
 */
@RestController
@RequestMapping("/api")
public class NotificationLogResource {

    private final Logger log = LoggerFactory.getLogger(NotificationLogResource.class);

    private static final String ENTITY_NAME = "notificationNotificationLog";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final NotificationLogService notificationLogService;

    private final NotificationLogRepository notificationLogRepository;

    public NotificationLogResource(NotificationLogService notificationLogService, NotificationLogRepository notificationLogRepository) {
        this.notificationLogService = notificationLogService;
        this.notificationLogRepository = notificationLogRepository;
    }

    /**
     * {@code POST  /notification-logs} : Create a new notificationLog.
     *
     * @param notificationLog the notificationLog to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new notificationLog, or with status {@code 400 (Bad Request)} if the notificationLog has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/notification-logs")
    public ResponseEntity<NotificationLog> createNotificationLog(@Valid @RequestBody NotificationLog notificationLog)
        throws URISyntaxException {
        log.debug("REST request to save NotificationLog : {}", notificationLog);
        if (notificationLog.getId() != null) {
            throw new BadRequestAlertException("A new notificationLog cannot already have an ID", ENTITY_NAME, "idexists");
        }
        NotificationLog result = notificationLogService.save(notificationLog);
        return ResponseEntity
            .created(new URI("/api/notification-logs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /notification-logs/:id} : Updates an existing notificationLog.
     *
     * @param id the id of the notificationLog to save.
     * @param notificationLog the notificationLog to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated notificationLog,
     * or with status {@code 400 (Bad Request)} if the notificationLog is not valid,
     * or with status {@code 500 (Internal Server Error)} if the notificationLog couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/notification-logs/{id}")
    public ResponseEntity<NotificationLog> updateNotificationLog(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody NotificationLog notificationLog
    ) throws URISyntaxException {
        log.debug("REST request to update NotificationLog : {}, {}", id, notificationLog);
        if (notificationLog.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, notificationLog.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!notificationLogRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        NotificationLog result = notificationLogService.update(notificationLog);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, notificationLog.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /notification-logs/:id} : Partial updates given fields of an existing notificationLog, field will ignore if it is null
     *
     * @param id the id of the notificationLog to save.
     * @param notificationLog the notificationLog to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated notificationLog,
     * or with status {@code 400 (Bad Request)} if the notificationLog is not valid,
     * or with status {@code 404 (Not Found)} if the notificationLog is not found,
     * or with status {@code 500 (Internal Server Error)} if the notificationLog couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/notification-logs/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<NotificationLog> partialUpdateNotificationLog(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody NotificationLog notificationLog
    ) throws URISyntaxException {
        log.debug("REST request to partial update NotificationLog partially : {}, {}", id, notificationLog);
        if (notificationLog.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, notificationLog.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!notificationLogRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<NotificationLog> result = notificationLogService.partialUpdate(notificationLog);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, notificationLog.getId().toString())
        );
    }

    /**
     * {@code GET  /notification-logs} : get all the notificationLogs.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of notificationLogs in body.
     */
    @GetMapping("/notification-logs")
    public ResponseEntity<List<NotificationLog>> getAllNotificationLogs(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of NotificationLogs");
        Page<NotificationLog> page = notificationLogService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /notification-logs/:id} : get the "id" notificationLog.
     *
     * @param id the id of the notificationLog to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the notificationLog, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/notification-logs/{id}")
    public ResponseEntity<NotificationLog> getNotificationLog(@PathVariable Long id) {
        log.debug("REST request to get NotificationLog : {}", id);
        Optional<NotificationLog> notificationLog = notificationLogService.findOne(id);
        return ResponseUtil.wrapOrNotFound(notificationLog);
    }

    /**
     * {@code DELETE  /notification-logs/:id} : delete the "id" notificationLog.
     *
     * @param id the id of the notificationLog to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/notification-logs/{id}")
    public ResponseEntity<Void> deleteNotificationLog(@PathVariable Long id) {
        log.debug("REST request to delete NotificationLog : {}", id);
        notificationLogService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
