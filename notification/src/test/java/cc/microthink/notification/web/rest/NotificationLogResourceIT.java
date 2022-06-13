package cc.microthink.notification.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import cc.microthink.notification.IntegrationTest;
import cc.microthink.notification.domain.NotificationLog;
import cc.microthink.notification.domain.enumeration.NotifyStatus;
import cc.microthink.notification.domain.enumeration.NotifyType;
import cc.microthink.notification.repository.NotificationLogRepository;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link NotificationLogResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class NotificationLogResourceIT {

    private static final Long DEFAULT_CUST_ID = 1L;
    private static final Long UPDATED_CUST_ID = 2L;

    private static final String DEFAULT_CUST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_CUST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_CONTENT = "AAAAAAAAAA";
    private static final String UPDATED_CONTENT = "BBBBBBBBBB";

    private static final NotifyType DEFAULT_NOTIFY_TYPE = NotifyType.EMAIL;
    private static final NotifyType UPDATED_NOTIFY_TYPE = NotifyType.SMS;

    private static final NotifyStatus DEFAULT_STATUS = NotifyStatus.SUCCESS;
    private static final NotifyStatus UPDATED_STATUS = NotifyStatus.FAIL;

    private static final Instant DEFAULT_NOTIFY_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_NOTIFY_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/notification-logs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private NotificationLogRepository notificationLogRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restNotificationLogMockMvc;

    private NotificationLog notificationLog;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NotificationLog createEntity(EntityManager em) {
        NotificationLog notificationLog = new NotificationLog()
            .custId(DEFAULT_CUST_ID)
            .custName(DEFAULT_CUST_NAME)
            .title(DEFAULT_TITLE)
            .content(DEFAULT_CONTENT)
            .notifyType(DEFAULT_NOTIFY_TYPE)
            .status(DEFAULT_STATUS)
            .notifyTime(DEFAULT_NOTIFY_TIME);
        return notificationLog;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NotificationLog createUpdatedEntity(EntityManager em) {
        NotificationLog notificationLog = new NotificationLog()
            .custId(UPDATED_CUST_ID)
            .custName(UPDATED_CUST_NAME)
            .title(UPDATED_TITLE)
            .content(UPDATED_CONTENT)
            .notifyType(UPDATED_NOTIFY_TYPE)
            .status(UPDATED_STATUS)
            .notifyTime(UPDATED_NOTIFY_TIME);
        return notificationLog;
    }

    @BeforeEach
    public void initTest() {
        notificationLog = createEntity(em);
    }

    @Test
    @Transactional
    void createNotificationLog() throws Exception {
        int databaseSizeBeforeCreate = notificationLogRepository.findAll().size();
        // Create the NotificationLog
        restNotificationLogMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(notificationLog))
            )
            .andExpect(status().isCreated());

        // Validate the NotificationLog in the database
        List<NotificationLog> notificationLogList = notificationLogRepository.findAll();
        assertThat(notificationLogList).hasSize(databaseSizeBeforeCreate + 1);
        NotificationLog testNotificationLog = notificationLogList.get(notificationLogList.size() - 1);
        assertThat(testNotificationLog.getCustId()).isEqualTo(DEFAULT_CUST_ID);
        assertThat(testNotificationLog.getCustName()).isEqualTo(DEFAULT_CUST_NAME);
        assertThat(testNotificationLog.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testNotificationLog.getContent()).isEqualTo(DEFAULT_CONTENT);
        assertThat(testNotificationLog.getNotifyType()).isEqualTo(DEFAULT_NOTIFY_TYPE);
        assertThat(testNotificationLog.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testNotificationLog.getNotifyTime()).isEqualTo(DEFAULT_NOTIFY_TIME);
    }

    @Test
    @Transactional
    void createNotificationLogWithExistingId() throws Exception {
        // Create the NotificationLog with an existing ID
        notificationLog.setId(1L);

        int databaseSizeBeforeCreate = notificationLogRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restNotificationLogMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(notificationLog))
            )
            .andExpect(status().isBadRequest());

        // Validate the NotificationLog in the database
        List<NotificationLog> notificationLogList = notificationLogRepository.findAll();
        assertThat(notificationLogList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = notificationLogRepository.findAll().size();
        // set the field null
        notificationLog.setTitle(null);

        // Create the NotificationLog, which fails.

        restNotificationLogMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(notificationLog))
            )
            .andExpect(status().isBadRequest());

        List<NotificationLog> notificationLogList = notificationLogRepository.findAll();
        assertThat(notificationLogList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllNotificationLogs() throws Exception {
        // Initialize the database
        notificationLogRepository.saveAndFlush(notificationLog);

        // Get all the notificationLogList
        restNotificationLogMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(notificationLog.getId().intValue())))
            .andExpect(jsonPath("$.[*].custId").value(hasItem(DEFAULT_CUST_ID.intValue())))
            .andExpect(jsonPath("$.[*].custName").value(hasItem(DEFAULT_CUST_NAME)))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT)))
            .andExpect(jsonPath("$.[*].notifyType").value(hasItem(DEFAULT_NOTIFY_TYPE.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].notifyTime").value(hasItem(DEFAULT_NOTIFY_TIME.toString())));
    }

    @Test
    @Transactional
    void getNotificationLog() throws Exception {
        // Initialize the database
        notificationLogRepository.saveAndFlush(notificationLog);

        // Get the notificationLog
        restNotificationLogMockMvc
            .perform(get(ENTITY_API_URL_ID, notificationLog.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(notificationLog.getId().intValue()))
            .andExpect(jsonPath("$.custId").value(DEFAULT_CUST_ID.intValue()))
            .andExpect(jsonPath("$.custName").value(DEFAULT_CUST_NAME))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.content").value(DEFAULT_CONTENT))
            .andExpect(jsonPath("$.notifyType").value(DEFAULT_NOTIFY_TYPE.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.notifyTime").value(DEFAULT_NOTIFY_TIME.toString()));
    }

    @Test
    @Transactional
    void getNonExistingNotificationLog() throws Exception {
        // Get the notificationLog
        restNotificationLogMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewNotificationLog() throws Exception {
        // Initialize the database
        notificationLogRepository.saveAndFlush(notificationLog);

        int databaseSizeBeforeUpdate = notificationLogRepository.findAll().size();

        // Update the notificationLog
        NotificationLog updatedNotificationLog = notificationLogRepository.findById(notificationLog.getId()).get();
        // Disconnect from session so that the updates on updatedNotificationLog are not directly saved in db
        em.detach(updatedNotificationLog);
        updatedNotificationLog
            .custId(UPDATED_CUST_ID)
            .custName(UPDATED_CUST_NAME)
            .title(UPDATED_TITLE)
            .content(UPDATED_CONTENT)
            .notifyType(UPDATED_NOTIFY_TYPE)
            .status(UPDATED_STATUS)
            .notifyTime(UPDATED_NOTIFY_TIME);

        restNotificationLogMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedNotificationLog.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedNotificationLog))
            )
            .andExpect(status().isOk());

        // Validate the NotificationLog in the database
        List<NotificationLog> notificationLogList = notificationLogRepository.findAll();
        assertThat(notificationLogList).hasSize(databaseSizeBeforeUpdate);
        NotificationLog testNotificationLog = notificationLogList.get(notificationLogList.size() - 1);
        assertThat(testNotificationLog.getCustId()).isEqualTo(UPDATED_CUST_ID);
        assertThat(testNotificationLog.getCustName()).isEqualTo(UPDATED_CUST_NAME);
        assertThat(testNotificationLog.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testNotificationLog.getContent()).isEqualTo(UPDATED_CONTENT);
        assertThat(testNotificationLog.getNotifyType()).isEqualTo(UPDATED_NOTIFY_TYPE);
        assertThat(testNotificationLog.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testNotificationLog.getNotifyTime()).isEqualTo(UPDATED_NOTIFY_TIME);
    }

    @Test
    @Transactional
    void putNonExistingNotificationLog() throws Exception {
        int databaseSizeBeforeUpdate = notificationLogRepository.findAll().size();
        notificationLog.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNotificationLogMockMvc
            .perform(
                put(ENTITY_API_URL_ID, notificationLog.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(notificationLog))
            )
            .andExpect(status().isBadRequest());

        // Validate the NotificationLog in the database
        List<NotificationLog> notificationLogList = notificationLogRepository.findAll();
        assertThat(notificationLogList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchNotificationLog() throws Exception {
        int databaseSizeBeforeUpdate = notificationLogRepository.findAll().size();
        notificationLog.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNotificationLogMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(notificationLog))
            )
            .andExpect(status().isBadRequest());

        // Validate the NotificationLog in the database
        List<NotificationLog> notificationLogList = notificationLogRepository.findAll();
        assertThat(notificationLogList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamNotificationLog() throws Exception {
        int databaseSizeBeforeUpdate = notificationLogRepository.findAll().size();
        notificationLog.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNotificationLogMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(notificationLog))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the NotificationLog in the database
        List<NotificationLog> notificationLogList = notificationLogRepository.findAll();
        assertThat(notificationLogList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateNotificationLogWithPatch() throws Exception {
        // Initialize the database
        notificationLogRepository.saveAndFlush(notificationLog);

        int databaseSizeBeforeUpdate = notificationLogRepository.findAll().size();

        // Update the notificationLog using partial update
        NotificationLog partialUpdatedNotificationLog = new NotificationLog();
        partialUpdatedNotificationLog.setId(notificationLog.getId());

        partialUpdatedNotificationLog.custId(UPDATED_CUST_ID).custName(UPDATED_CUST_NAME).content(UPDATED_CONTENT);

        restNotificationLogMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNotificationLog.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedNotificationLog))
            )
            .andExpect(status().isOk());

        // Validate the NotificationLog in the database
        List<NotificationLog> notificationLogList = notificationLogRepository.findAll();
        assertThat(notificationLogList).hasSize(databaseSizeBeforeUpdate);
        NotificationLog testNotificationLog = notificationLogList.get(notificationLogList.size() - 1);
        assertThat(testNotificationLog.getCustId()).isEqualTo(UPDATED_CUST_ID);
        assertThat(testNotificationLog.getCustName()).isEqualTo(UPDATED_CUST_NAME);
        assertThat(testNotificationLog.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testNotificationLog.getContent()).isEqualTo(UPDATED_CONTENT);
        assertThat(testNotificationLog.getNotifyType()).isEqualTo(DEFAULT_NOTIFY_TYPE);
        assertThat(testNotificationLog.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testNotificationLog.getNotifyTime()).isEqualTo(DEFAULT_NOTIFY_TIME);
    }

    @Test
    @Transactional
    void fullUpdateNotificationLogWithPatch() throws Exception {
        // Initialize the database
        notificationLogRepository.saveAndFlush(notificationLog);

        int databaseSizeBeforeUpdate = notificationLogRepository.findAll().size();

        // Update the notificationLog using partial update
        NotificationLog partialUpdatedNotificationLog = new NotificationLog();
        partialUpdatedNotificationLog.setId(notificationLog.getId());

        partialUpdatedNotificationLog
            .custId(UPDATED_CUST_ID)
            .custName(UPDATED_CUST_NAME)
            .title(UPDATED_TITLE)
            .content(UPDATED_CONTENT)
            .notifyType(UPDATED_NOTIFY_TYPE)
            .status(UPDATED_STATUS)
            .notifyTime(UPDATED_NOTIFY_TIME);

        restNotificationLogMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNotificationLog.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedNotificationLog))
            )
            .andExpect(status().isOk());

        // Validate the NotificationLog in the database
        List<NotificationLog> notificationLogList = notificationLogRepository.findAll();
        assertThat(notificationLogList).hasSize(databaseSizeBeforeUpdate);
        NotificationLog testNotificationLog = notificationLogList.get(notificationLogList.size() - 1);
        assertThat(testNotificationLog.getCustId()).isEqualTo(UPDATED_CUST_ID);
        assertThat(testNotificationLog.getCustName()).isEqualTo(UPDATED_CUST_NAME);
        assertThat(testNotificationLog.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testNotificationLog.getContent()).isEqualTo(UPDATED_CONTENT);
        assertThat(testNotificationLog.getNotifyType()).isEqualTo(UPDATED_NOTIFY_TYPE);
        assertThat(testNotificationLog.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testNotificationLog.getNotifyTime()).isEqualTo(UPDATED_NOTIFY_TIME);
    }

    @Test
    @Transactional
    void patchNonExistingNotificationLog() throws Exception {
        int databaseSizeBeforeUpdate = notificationLogRepository.findAll().size();
        notificationLog.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNotificationLogMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, notificationLog.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(notificationLog))
            )
            .andExpect(status().isBadRequest());

        // Validate the NotificationLog in the database
        List<NotificationLog> notificationLogList = notificationLogRepository.findAll();
        assertThat(notificationLogList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchNotificationLog() throws Exception {
        int databaseSizeBeforeUpdate = notificationLogRepository.findAll().size();
        notificationLog.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNotificationLogMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(notificationLog))
            )
            .andExpect(status().isBadRequest());

        // Validate the NotificationLog in the database
        List<NotificationLog> notificationLogList = notificationLogRepository.findAll();
        assertThat(notificationLogList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamNotificationLog() throws Exception {
        int databaseSizeBeforeUpdate = notificationLogRepository.findAll().size();
        notificationLog.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNotificationLogMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(notificationLog))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the NotificationLog in the database
        List<NotificationLog> notificationLogList = notificationLogRepository.findAll();
        assertThat(notificationLogList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteNotificationLog() throws Exception {
        // Initialize the database
        notificationLogRepository.saveAndFlush(notificationLog);

        int databaseSizeBeforeDelete = notificationLogRepository.findAll().size();

        // Delete the notificationLog
        restNotificationLogMockMvc
            .perform(delete(ENTITY_API_URL_ID, notificationLog.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<NotificationLog> notificationLogList = notificationLogRepository.findAll();
        assertThat(notificationLogList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
