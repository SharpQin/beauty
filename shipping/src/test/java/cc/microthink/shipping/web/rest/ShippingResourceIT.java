package cc.microthink.shipping.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import cc.microthink.shipping.IntegrationTest;
import cc.microthink.shipping.domain.Shipping;
import cc.microthink.shipping.domain.enumeration.ShippingStatus;
import cc.microthink.shipping.repository.ShippingRepository;
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
 * Integration tests for the {@link ShippingResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ShippingResourceIT {

    private static final Long DEFAULT_ORDER_ID = 1L;
    private static final Long UPDATED_ORDER_ID = 2L;

    private static final String DEFAULT_ORDER_SERIAL = "AAAAAAAAAA";
    private static final String UPDATED_ORDER_SERIAL = "BBBBBBBBBB";

    private static final Long DEFAULT_CUST_ID = 1L;
    private static final Long UPDATED_CUST_ID = 2L;

    private static final String DEFAULT_CUST_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_CUST_ADDRESS = "BBBBBBBBBB";

    private static final ShippingStatus DEFAULT_STATUS = ShippingStatus.NON;
    private static final ShippingStatus UPDATED_STATUS = ShippingStatus.PICKUP;

    private static final Instant DEFAULT_CREATED_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_SENT_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_SENT_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_ACHIEVED_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_ACHIEVED_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/shippings";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ShippingRepository shippingRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restShippingMockMvc;

    private Shipping shipping;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Shipping createEntity(EntityManager em) {
        Shipping shipping = new Shipping()
            .orderId(DEFAULT_ORDER_ID)
            .orderSerial(DEFAULT_ORDER_SERIAL)
            .custId(DEFAULT_CUST_ID)
            .custAddress(DEFAULT_CUST_ADDRESS)
            .status(DEFAULT_STATUS)
            .createdTime(DEFAULT_CREATED_TIME)
            .sentTime(DEFAULT_SENT_TIME)
            .achievedTime(DEFAULT_ACHIEVED_TIME);
        return shipping;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Shipping createUpdatedEntity(EntityManager em) {
        Shipping shipping = new Shipping()
            .orderId(UPDATED_ORDER_ID)
            .orderSerial(UPDATED_ORDER_SERIAL)
            .custId(UPDATED_CUST_ID)
            .custAddress(UPDATED_CUST_ADDRESS)
            .status(UPDATED_STATUS)
            .createdTime(UPDATED_CREATED_TIME)
            .sentTime(UPDATED_SENT_TIME)
            .achievedTime(UPDATED_ACHIEVED_TIME);
        return shipping;
    }

    @BeforeEach
    public void initTest() {
        shipping = createEntity(em);
    }

    @Test
    @Transactional
    void createShipping() throws Exception {
        int databaseSizeBeforeCreate = shippingRepository.findAll().size();
        // Create the Shipping
        restShippingMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(shipping)))
            .andExpect(status().isCreated());

        // Validate the Shipping in the database
        List<Shipping> shippingList = shippingRepository.findAll();
        assertThat(shippingList).hasSize(databaseSizeBeforeCreate + 1);
        Shipping testShipping = shippingList.get(shippingList.size() - 1);
        assertThat(testShipping.getOrderId()).isEqualTo(DEFAULT_ORDER_ID);
        assertThat(testShipping.getOrderSerial()).isEqualTo(DEFAULT_ORDER_SERIAL);
        assertThat(testShipping.getCustId()).isEqualTo(DEFAULT_CUST_ID);
        assertThat(testShipping.getCustAddress()).isEqualTo(DEFAULT_CUST_ADDRESS);
        assertThat(testShipping.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testShipping.getCreatedTime()).isEqualTo(DEFAULT_CREATED_TIME);
        assertThat(testShipping.getSentTime()).isEqualTo(DEFAULT_SENT_TIME);
        assertThat(testShipping.getAchievedTime()).isEqualTo(DEFAULT_ACHIEVED_TIME);
    }

    @Test
    @Transactional
    void createShippingWithExistingId() throws Exception {
        // Create the Shipping with an existing ID
        shipping.setId(1L);

        int databaseSizeBeforeCreate = shippingRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restShippingMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(shipping)))
            .andExpect(status().isBadRequest());

        // Validate the Shipping in the database
        List<Shipping> shippingList = shippingRepository.findAll();
        assertThat(shippingList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllShippings() throws Exception {
        // Initialize the database
        shippingRepository.saveAndFlush(shipping);

        // Get all the shippingList
        restShippingMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(shipping.getId().intValue())))
            .andExpect(jsonPath("$.[*].orderId").value(hasItem(DEFAULT_ORDER_ID.intValue())))
            .andExpect(jsonPath("$.[*].orderSerial").value(hasItem(DEFAULT_ORDER_SERIAL)))
            .andExpect(jsonPath("$.[*].custId").value(hasItem(DEFAULT_CUST_ID.intValue())))
            .andExpect(jsonPath("$.[*].custAddress").value(hasItem(DEFAULT_CUST_ADDRESS)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].createdTime").value(hasItem(DEFAULT_CREATED_TIME.toString())))
            .andExpect(jsonPath("$.[*].sentTime").value(hasItem(DEFAULT_SENT_TIME.toString())))
            .andExpect(jsonPath("$.[*].achievedTime").value(hasItem(DEFAULT_ACHIEVED_TIME.toString())));
    }

    @Test
    @Transactional
    void getShipping() throws Exception {
        // Initialize the database
        shippingRepository.saveAndFlush(shipping);

        // Get the shipping
        restShippingMockMvc
            .perform(get(ENTITY_API_URL_ID, shipping.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(shipping.getId().intValue()))
            .andExpect(jsonPath("$.orderId").value(DEFAULT_ORDER_ID.intValue()))
            .andExpect(jsonPath("$.orderSerial").value(DEFAULT_ORDER_SERIAL))
            .andExpect(jsonPath("$.custId").value(DEFAULT_CUST_ID.intValue()))
            .andExpect(jsonPath("$.custAddress").value(DEFAULT_CUST_ADDRESS))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.createdTime").value(DEFAULT_CREATED_TIME.toString()))
            .andExpect(jsonPath("$.sentTime").value(DEFAULT_SENT_TIME.toString()))
            .andExpect(jsonPath("$.achievedTime").value(DEFAULT_ACHIEVED_TIME.toString()));
    }

    @Test
    @Transactional
    void getNonExistingShipping() throws Exception {
        // Get the shipping
        restShippingMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewShipping() throws Exception {
        // Initialize the database
        shippingRepository.saveAndFlush(shipping);

        int databaseSizeBeforeUpdate = shippingRepository.findAll().size();

        // Update the shipping
        Shipping updatedShipping = shippingRepository.findById(shipping.getId()).get();
        // Disconnect from session so that the updates on updatedShipping are not directly saved in db
        em.detach(updatedShipping);
        updatedShipping
            .orderId(UPDATED_ORDER_ID)
            .orderSerial(UPDATED_ORDER_SERIAL)
            .custId(UPDATED_CUST_ID)
            .custAddress(UPDATED_CUST_ADDRESS)
            .status(UPDATED_STATUS)
            .createdTime(UPDATED_CREATED_TIME)
            .sentTime(UPDATED_SENT_TIME)
            .achievedTime(UPDATED_ACHIEVED_TIME);

        restShippingMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedShipping.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedShipping))
            )
            .andExpect(status().isOk());

        // Validate the Shipping in the database
        List<Shipping> shippingList = shippingRepository.findAll();
        assertThat(shippingList).hasSize(databaseSizeBeforeUpdate);
        Shipping testShipping = shippingList.get(shippingList.size() - 1);
        assertThat(testShipping.getOrderId()).isEqualTo(UPDATED_ORDER_ID);
        assertThat(testShipping.getOrderSerial()).isEqualTo(UPDATED_ORDER_SERIAL);
        assertThat(testShipping.getCustId()).isEqualTo(UPDATED_CUST_ID);
        assertThat(testShipping.getCustAddress()).isEqualTo(UPDATED_CUST_ADDRESS);
        assertThat(testShipping.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testShipping.getCreatedTime()).isEqualTo(UPDATED_CREATED_TIME);
        assertThat(testShipping.getSentTime()).isEqualTo(UPDATED_SENT_TIME);
        assertThat(testShipping.getAchievedTime()).isEqualTo(UPDATED_ACHIEVED_TIME);
    }

    @Test
    @Transactional
    void putNonExistingShipping() throws Exception {
        int databaseSizeBeforeUpdate = shippingRepository.findAll().size();
        shipping.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restShippingMockMvc
            .perform(
                put(ENTITY_API_URL_ID, shipping.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(shipping))
            )
            .andExpect(status().isBadRequest());

        // Validate the Shipping in the database
        List<Shipping> shippingList = shippingRepository.findAll();
        assertThat(shippingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchShipping() throws Exception {
        int databaseSizeBeforeUpdate = shippingRepository.findAll().size();
        shipping.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restShippingMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(shipping))
            )
            .andExpect(status().isBadRequest());

        // Validate the Shipping in the database
        List<Shipping> shippingList = shippingRepository.findAll();
        assertThat(shippingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamShipping() throws Exception {
        int databaseSizeBeforeUpdate = shippingRepository.findAll().size();
        shipping.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restShippingMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(shipping)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Shipping in the database
        List<Shipping> shippingList = shippingRepository.findAll();
        assertThat(shippingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateShippingWithPatch() throws Exception {
        // Initialize the database
        shippingRepository.saveAndFlush(shipping);

        int databaseSizeBeforeUpdate = shippingRepository.findAll().size();

        // Update the shipping using partial update
        Shipping partialUpdatedShipping = new Shipping();
        partialUpdatedShipping.setId(shipping.getId());

        partialUpdatedShipping.orderId(UPDATED_ORDER_ID).createdTime(UPDATED_CREATED_TIME);

        restShippingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedShipping.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedShipping))
            )
            .andExpect(status().isOk());

        // Validate the Shipping in the database
        List<Shipping> shippingList = shippingRepository.findAll();
        assertThat(shippingList).hasSize(databaseSizeBeforeUpdate);
        Shipping testShipping = shippingList.get(shippingList.size() - 1);
        assertThat(testShipping.getOrderId()).isEqualTo(UPDATED_ORDER_ID);
        assertThat(testShipping.getOrderSerial()).isEqualTo(DEFAULT_ORDER_SERIAL);
        assertThat(testShipping.getCustId()).isEqualTo(DEFAULT_CUST_ID);
        assertThat(testShipping.getCustAddress()).isEqualTo(DEFAULT_CUST_ADDRESS);
        assertThat(testShipping.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testShipping.getCreatedTime()).isEqualTo(UPDATED_CREATED_TIME);
        assertThat(testShipping.getSentTime()).isEqualTo(DEFAULT_SENT_TIME);
        assertThat(testShipping.getAchievedTime()).isEqualTo(DEFAULT_ACHIEVED_TIME);
    }

    @Test
    @Transactional
    void fullUpdateShippingWithPatch() throws Exception {
        // Initialize the database
        shippingRepository.saveAndFlush(shipping);

        int databaseSizeBeforeUpdate = shippingRepository.findAll().size();

        // Update the shipping using partial update
        Shipping partialUpdatedShipping = new Shipping();
        partialUpdatedShipping.setId(shipping.getId());

        partialUpdatedShipping
            .orderId(UPDATED_ORDER_ID)
            .orderSerial(UPDATED_ORDER_SERIAL)
            .custId(UPDATED_CUST_ID)
            .custAddress(UPDATED_CUST_ADDRESS)
            .status(UPDATED_STATUS)
            .createdTime(UPDATED_CREATED_TIME)
            .sentTime(UPDATED_SENT_TIME)
            .achievedTime(UPDATED_ACHIEVED_TIME);

        restShippingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedShipping.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedShipping))
            )
            .andExpect(status().isOk());

        // Validate the Shipping in the database
        List<Shipping> shippingList = shippingRepository.findAll();
        assertThat(shippingList).hasSize(databaseSizeBeforeUpdate);
        Shipping testShipping = shippingList.get(shippingList.size() - 1);
        assertThat(testShipping.getOrderId()).isEqualTo(UPDATED_ORDER_ID);
        assertThat(testShipping.getOrderSerial()).isEqualTo(UPDATED_ORDER_SERIAL);
        assertThat(testShipping.getCustId()).isEqualTo(UPDATED_CUST_ID);
        assertThat(testShipping.getCustAddress()).isEqualTo(UPDATED_CUST_ADDRESS);
        assertThat(testShipping.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testShipping.getCreatedTime()).isEqualTo(UPDATED_CREATED_TIME);
        assertThat(testShipping.getSentTime()).isEqualTo(UPDATED_SENT_TIME);
        assertThat(testShipping.getAchievedTime()).isEqualTo(UPDATED_ACHIEVED_TIME);
    }

    @Test
    @Transactional
    void patchNonExistingShipping() throws Exception {
        int databaseSizeBeforeUpdate = shippingRepository.findAll().size();
        shipping.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restShippingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, shipping.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(shipping))
            )
            .andExpect(status().isBadRequest());

        // Validate the Shipping in the database
        List<Shipping> shippingList = shippingRepository.findAll();
        assertThat(shippingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchShipping() throws Exception {
        int databaseSizeBeforeUpdate = shippingRepository.findAll().size();
        shipping.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restShippingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(shipping))
            )
            .andExpect(status().isBadRequest());

        // Validate the Shipping in the database
        List<Shipping> shippingList = shippingRepository.findAll();
        assertThat(shippingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamShipping() throws Exception {
        int databaseSizeBeforeUpdate = shippingRepository.findAll().size();
        shipping.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restShippingMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(shipping)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Shipping in the database
        List<Shipping> shippingList = shippingRepository.findAll();
        assertThat(shippingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteShipping() throws Exception {
        // Initialize the database
        shippingRepository.saveAndFlush(shipping);

        int databaseSizeBeforeDelete = shippingRepository.findAll().size();

        // Delete the shipping
        restShippingMockMvc
            .perform(delete(ENTITY_API_URL_ID, shipping.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Shipping> shippingList = shippingRepository.findAll();
        assertThat(shippingList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
