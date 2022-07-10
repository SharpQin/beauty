package cc.microthink.payment.web.rest;

import static cc.microthink.payment.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import cc.microthink.payment.IntegrationTest;
import cc.microthink.payment.domain.Payment;
import cc.microthink.payment.domain.enumeration.PaymentStatus;
import cc.microthink.payment.domain.enumeration.PaymentType;
import cc.microthink.payment.repository.PaymentRepository;
import java.math.BigDecimal;
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
 * Integration tests for the {@link PaymentResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PaymentResourceIT {

    private static final Long DEFAULT_ORDER_ID = 1L;
    private static final Long UPDATED_ORDER_ID = 2L;

    private static final String DEFAULT_ORDER_SERIAL = "AAAAAAAAAA";
    private static final String UPDATED_ORDER_SERIAL = "BBBBBBBBBB";

    private static final Long DEFAULT_CUST_ID = 1L;
    private static final Long UPDATED_CUST_ID = 2L;

    private static final String DEFAULT_ACCINFO = "AAAAAAAAAA";
    private static final String UPDATED_ACCINFO = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_MONEY = new BigDecimal(0);
    private static final BigDecimal UPDATED_MONEY = new BigDecimal(1);

    private static final PaymentType DEFAULT_PAYMENT_TYPE = PaymentType.Wechat;
    private static final PaymentType UPDATED_PAYMENT_TYPE = PaymentType.Alipay;

    private static final PaymentStatus DEFAULT_PAYMENT_STATUS = PaymentStatus.PENDING;
    private static final PaymentStatus UPDATED_PAYMENT_STATUS = PaymentStatus.CANCEL;

    private static final Instant DEFAULT_PAYMENT_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_PAYMENT_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/payments";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPaymentMockMvc;

    private Payment payment;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Payment createEntity(EntityManager em) {
        Payment payment = new Payment()
            .orderId(DEFAULT_ORDER_ID)
            .orderSerial(DEFAULT_ORDER_SERIAL)
            .custId(DEFAULT_CUST_ID)
            .accinfo(DEFAULT_ACCINFO)
            .money(DEFAULT_MONEY)
            .paymentType(DEFAULT_PAYMENT_TYPE)
            .paymentStatus(DEFAULT_PAYMENT_STATUS)
            .paymentTime(DEFAULT_PAYMENT_TIME);
        return payment;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Payment createUpdatedEntity(EntityManager em) {
        Payment payment = new Payment()
            .orderId(UPDATED_ORDER_ID)
            .orderSerial(UPDATED_ORDER_SERIAL)
            .custId(UPDATED_CUST_ID)
            .accinfo(UPDATED_ACCINFO)
            .money(UPDATED_MONEY)
            .paymentType(UPDATED_PAYMENT_TYPE)
            .paymentStatus(UPDATED_PAYMENT_STATUS)
            .paymentTime(UPDATED_PAYMENT_TIME);
        return payment;
    }

    @BeforeEach
    public void initTest() {
        payment = createEntity(em);
    }

    @Test
    @Transactional
    void createPayment() throws Exception {
        int databaseSizeBeforeCreate = paymentRepository.findAll().size();
        // Create the Payment
        restPaymentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(payment)))
            .andExpect(status().isCreated());

        // Validate the Payment in the database
        List<Payment> paymentList = paymentRepository.findAll();
        assertThat(paymentList).hasSize(databaseSizeBeforeCreate + 1);
        Payment testPayment = paymentList.get(paymentList.size() - 1);
        assertThat(testPayment.getOrderId()).isEqualTo(DEFAULT_ORDER_ID);
        assertThat(testPayment.getOrderSerial()).isEqualTo(DEFAULT_ORDER_SERIAL);
        assertThat(testPayment.getCustId()).isEqualTo(DEFAULT_CUST_ID);
        assertThat(testPayment.getAccinfo()).isEqualTo(DEFAULT_ACCINFO);
        assertThat(testPayment.getMoney()).isEqualByComparingTo(DEFAULT_MONEY);
        assertThat(testPayment.getPaymentType()).isEqualTo(DEFAULT_PAYMENT_TYPE);
        assertThat(testPayment.getPaymentStatus()).isEqualTo(DEFAULT_PAYMENT_STATUS);
        assertThat(testPayment.getPaymentTime()).isEqualTo(DEFAULT_PAYMENT_TIME);
    }

    @Test
    @Transactional
    void createPaymentWithExistingId() throws Exception {
        // Create the Payment with an existing ID
        payment.setId(1L);

        int databaseSizeBeforeCreate = paymentRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPaymentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(payment)))
            .andExpect(status().isBadRequest());

        // Validate the Payment in the database
        List<Payment> paymentList = paymentRepository.findAll();
        assertThat(paymentList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkMoneyIsRequired() throws Exception {
        int databaseSizeBeforeTest = paymentRepository.findAll().size();
        // set the field null
        payment.setMoney(null);

        // Create the Payment, which fails.

        restPaymentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(payment)))
            .andExpect(status().isBadRequest());

        List<Payment> paymentList = paymentRepository.findAll();
        assertThat(paymentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPayments() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList
        restPaymentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(payment.getId().intValue())))
            .andExpect(jsonPath("$.[*].orderId").value(hasItem(DEFAULT_ORDER_ID.intValue())))
            .andExpect(jsonPath("$.[*].orderSerial").value(hasItem(DEFAULT_ORDER_SERIAL)))
            .andExpect(jsonPath("$.[*].custId").value(hasItem(DEFAULT_CUST_ID.intValue())))
            .andExpect(jsonPath("$.[*].accinfo").value(hasItem(DEFAULT_ACCINFO)))
            .andExpect(jsonPath("$.[*].money").value(hasItem(sameNumber(DEFAULT_MONEY))))
            .andExpect(jsonPath("$.[*].paymentType").value(hasItem(DEFAULT_PAYMENT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].paymentStatus").value(hasItem(DEFAULT_PAYMENT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].paymentTime").value(hasItem(DEFAULT_PAYMENT_TIME.toString())));
    }

    @Test
    @Transactional
    void getPayment() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get the payment
        restPaymentMockMvc
            .perform(get(ENTITY_API_URL_ID, payment.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(payment.getId().intValue()))
            .andExpect(jsonPath("$.orderId").value(DEFAULT_ORDER_ID.intValue()))
            .andExpect(jsonPath("$.orderSerial").value(DEFAULT_ORDER_SERIAL))
            .andExpect(jsonPath("$.custId").value(DEFAULT_CUST_ID.intValue()))
            .andExpect(jsonPath("$.accinfo").value(DEFAULT_ACCINFO))
            .andExpect(jsonPath("$.money").value(sameNumber(DEFAULT_MONEY)))
            .andExpect(jsonPath("$.paymentType").value(DEFAULT_PAYMENT_TYPE.toString()))
            .andExpect(jsonPath("$.paymentStatus").value(DEFAULT_PAYMENT_STATUS.toString()))
            .andExpect(jsonPath("$.paymentTime").value(DEFAULT_PAYMENT_TIME.toString()));
    }

    @Test
    @Transactional
    void getNonExistingPayment() throws Exception {
        // Get the payment
        restPaymentMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewPayment() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        int databaseSizeBeforeUpdate = paymentRepository.findAll().size();

        // Update the payment
        Payment updatedPayment = paymentRepository.findById(payment.getId()).get();
        // Disconnect from session so that the updates on updatedPayment are not directly saved in db
        em.detach(updatedPayment);
        updatedPayment
            .orderId(UPDATED_ORDER_ID)
            .orderSerial(UPDATED_ORDER_SERIAL)
            .custId(UPDATED_CUST_ID)
            .accinfo(UPDATED_ACCINFO)
            .money(UPDATED_MONEY)
            .paymentType(UPDATED_PAYMENT_TYPE)
            .paymentStatus(UPDATED_PAYMENT_STATUS)
            .paymentTime(UPDATED_PAYMENT_TIME);

        restPaymentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPayment.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedPayment))
            )
            .andExpect(status().isOk());

        // Validate the Payment in the database
        List<Payment> paymentList = paymentRepository.findAll();
        assertThat(paymentList).hasSize(databaseSizeBeforeUpdate);
        Payment testPayment = paymentList.get(paymentList.size() - 1);
        assertThat(testPayment.getOrderId()).isEqualTo(UPDATED_ORDER_ID);
        assertThat(testPayment.getOrderSerial()).isEqualTo(UPDATED_ORDER_SERIAL);
        assertThat(testPayment.getCustId()).isEqualTo(UPDATED_CUST_ID);
        assertThat(testPayment.getAccinfo()).isEqualTo(UPDATED_ACCINFO);
        assertThat(testPayment.getMoney()).isEqualByComparingTo(UPDATED_MONEY);
        assertThat(testPayment.getPaymentType()).isEqualTo(UPDATED_PAYMENT_TYPE);
        assertThat(testPayment.getPaymentStatus()).isEqualTo(UPDATED_PAYMENT_STATUS);
        assertThat(testPayment.getPaymentTime()).isEqualTo(UPDATED_PAYMENT_TIME);
    }

    @Test
    @Transactional
    void putNonExistingPayment() throws Exception {
        int databaseSizeBeforeUpdate = paymentRepository.findAll().size();
        payment.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPaymentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, payment.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(payment))
            )
            .andExpect(status().isBadRequest());

        // Validate the Payment in the database
        List<Payment> paymentList = paymentRepository.findAll();
        assertThat(paymentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPayment() throws Exception {
        int databaseSizeBeforeUpdate = paymentRepository.findAll().size();
        payment.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaymentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(payment))
            )
            .andExpect(status().isBadRequest());

        // Validate the Payment in the database
        List<Payment> paymentList = paymentRepository.findAll();
        assertThat(paymentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPayment() throws Exception {
        int databaseSizeBeforeUpdate = paymentRepository.findAll().size();
        payment.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaymentMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(payment)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Payment in the database
        List<Payment> paymentList = paymentRepository.findAll();
        assertThat(paymentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePaymentWithPatch() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        int databaseSizeBeforeUpdate = paymentRepository.findAll().size();

        // Update the payment using partial update
        Payment partialUpdatedPayment = new Payment();
        partialUpdatedPayment.setId(payment.getId());

        partialUpdatedPayment.orderSerial(UPDATED_ORDER_SERIAL).money(UPDATED_MONEY).paymentStatus(UPDATED_PAYMENT_STATUS);

        restPaymentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPayment.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPayment))
            )
            .andExpect(status().isOk());

        // Validate the Payment in the database
        List<Payment> paymentList = paymentRepository.findAll();
        assertThat(paymentList).hasSize(databaseSizeBeforeUpdate);
        Payment testPayment = paymentList.get(paymentList.size() - 1);
        assertThat(testPayment.getOrderId()).isEqualTo(DEFAULT_ORDER_ID);
        assertThat(testPayment.getOrderSerial()).isEqualTo(UPDATED_ORDER_SERIAL);
        assertThat(testPayment.getCustId()).isEqualTo(DEFAULT_CUST_ID);
        assertThat(testPayment.getAccinfo()).isEqualTo(DEFAULT_ACCINFO);
        assertThat(testPayment.getMoney()).isEqualByComparingTo(UPDATED_MONEY);
        assertThat(testPayment.getPaymentType()).isEqualTo(DEFAULT_PAYMENT_TYPE);
        assertThat(testPayment.getPaymentStatus()).isEqualTo(UPDATED_PAYMENT_STATUS);
        assertThat(testPayment.getPaymentTime()).isEqualTo(DEFAULT_PAYMENT_TIME);
    }

    @Test
    @Transactional
    void fullUpdatePaymentWithPatch() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        int databaseSizeBeforeUpdate = paymentRepository.findAll().size();

        // Update the payment using partial update
        Payment partialUpdatedPayment = new Payment();
        partialUpdatedPayment.setId(payment.getId());

        partialUpdatedPayment
            .orderId(UPDATED_ORDER_ID)
            .orderSerial(UPDATED_ORDER_SERIAL)
            .custId(UPDATED_CUST_ID)
            .accinfo(UPDATED_ACCINFO)
            .money(UPDATED_MONEY)
            .paymentType(UPDATED_PAYMENT_TYPE)
            .paymentStatus(UPDATED_PAYMENT_STATUS)
            .paymentTime(UPDATED_PAYMENT_TIME);

        restPaymentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPayment.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPayment))
            )
            .andExpect(status().isOk());

        // Validate the Payment in the database
        List<Payment> paymentList = paymentRepository.findAll();
        assertThat(paymentList).hasSize(databaseSizeBeforeUpdate);
        Payment testPayment = paymentList.get(paymentList.size() - 1);
        assertThat(testPayment.getOrderId()).isEqualTo(UPDATED_ORDER_ID);
        assertThat(testPayment.getOrderSerial()).isEqualTo(UPDATED_ORDER_SERIAL);
        assertThat(testPayment.getCustId()).isEqualTo(UPDATED_CUST_ID);
        assertThat(testPayment.getAccinfo()).isEqualTo(UPDATED_ACCINFO);
        assertThat(testPayment.getMoney()).isEqualByComparingTo(UPDATED_MONEY);
        assertThat(testPayment.getPaymentType()).isEqualTo(UPDATED_PAYMENT_TYPE);
        assertThat(testPayment.getPaymentStatus()).isEqualTo(UPDATED_PAYMENT_STATUS);
        assertThat(testPayment.getPaymentTime()).isEqualTo(UPDATED_PAYMENT_TIME);
    }

    @Test
    @Transactional
    void patchNonExistingPayment() throws Exception {
        int databaseSizeBeforeUpdate = paymentRepository.findAll().size();
        payment.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPaymentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, payment.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(payment))
            )
            .andExpect(status().isBadRequest());

        // Validate the Payment in the database
        List<Payment> paymentList = paymentRepository.findAll();
        assertThat(paymentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPayment() throws Exception {
        int databaseSizeBeforeUpdate = paymentRepository.findAll().size();
        payment.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaymentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(payment))
            )
            .andExpect(status().isBadRequest());

        // Validate the Payment in the database
        List<Payment> paymentList = paymentRepository.findAll();
        assertThat(paymentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPayment() throws Exception {
        int databaseSizeBeforeUpdate = paymentRepository.findAll().size();
        payment.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaymentMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(payment)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Payment in the database
        List<Payment> paymentList = paymentRepository.findAll();
        assertThat(paymentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePayment() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        int databaseSizeBeforeDelete = paymentRepository.findAll().size();

        // Delete the payment
        restPaymentMockMvc
            .perform(delete(ENTITY_API_URL_ID, payment.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Payment> paymentList = paymentRepository.findAll();
        assertThat(paymentList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
