package cc.microthink.customer.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import cc.microthink.customer.IntegrationTest;
import cc.microthink.customer.domain.CusAccount;
import cc.microthink.customer.domain.enumeration.AccountType;
import cc.microthink.customer.repository.CusAccountRepository;
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
 * Integration tests for the {@link CusAccountResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CusAccountResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_CARD_NO = "AAAAAAAAAA";
    private static final String UPDATED_CARD_NO = "BBBBBBBBBB";

    private static final AccountType DEFAULT_ACCOUNT_TYPE = AccountType.BANK;
    private static final AccountType UPDATED_ACCOUNT_TYPE = AccountType.CREDIT;

    private static final String ENTITY_API_URL = "/api/cus-accounts";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CusAccountRepository cusAccountRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCusAccountMockMvc;

    private CusAccount cusAccount;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CusAccount createEntity(EntityManager em) {
        CusAccount cusAccount = new CusAccount().name(DEFAULT_NAME).cardNo(DEFAULT_CARD_NO).accountType(DEFAULT_ACCOUNT_TYPE);
        return cusAccount;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CusAccount createUpdatedEntity(EntityManager em) {
        CusAccount cusAccount = new CusAccount().name(UPDATED_NAME).cardNo(UPDATED_CARD_NO).accountType(UPDATED_ACCOUNT_TYPE);
        return cusAccount;
    }

    @BeforeEach
    public void initTest() {
        cusAccount = createEntity(em);
    }

    @Test
    @Transactional
    void createCusAccount() throws Exception {
        int databaseSizeBeforeCreate = cusAccountRepository.findAll().size();
        // Create the CusAccount
        restCusAccountMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cusAccount)))
            .andExpect(status().isCreated());

        // Validate the CusAccount in the database
        List<CusAccount> cusAccountList = cusAccountRepository.findAll();
        assertThat(cusAccountList).hasSize(databaseSizeBeforeCreate + 1);
        CusAccount testCusAccount = cusAccountList.get(cusAccountList.size() - 1);
        assertThat(testCusAccount.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testCusAccount.getCardNo()).isEqualTo(DEFAULT_CARD_NO);
        assertThat(testCusAccount.getAccountType()).isEqualTo(DEFAULT_ACCOUNT_TYPE);
    }

    @Test
    @Transactional
    void createCusAccountWithExistingId() throws Exception {
        // Create the CusAccount with an existing ID
        cusAccount.setId(1L);

        int databaseSizeBeforeCreate = cusAccountRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCusAccountMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cusAccount)))
            .andExpect(status().isBadRequest());

        // Validate the CusAccount in the database
        List<CusAccount> cusAccountList = cusAccountRepository.findAll();
        assertThat(cusAccountList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = cusAccountRepository.findAll().size();
        // set the field null
        cusAccount.setName(null);

        // Create the CusAccount, which fails.

        restCusAccountMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cusAccount)))
            .andExpect(status().isBadRequest());

        List<CusAccount> cusAccountList = cusAccountRepository.findAll();
        assertThat(cusAccountList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCardNoIsRequired() throws Exception {
        int databaseSizeBeforeTest = cusAccountRepository.findAll().size();
        // set the field null
        cusAccount.setCardNo(null);

        // Create the CusAccount, which fails.

        restCusAccountMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cusAccount)))
            .andExpect(status().isBadRequest());

        List<CusAccount> cusAccountList = cusAccountRepository.findAll();
        assertThat(cusAccountList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCusAccounts() throws Exception {
        // Initialize the database
        cusAccountRepository.saveAndFlush(cusAccount);

        // Get all the cusAccountList
        restCusAccountMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cusAccount.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].cardNo").value(hasItem(DEFAULT_CARD_NO)))
            .andExpect(jsonPath("$.[*].accountType").value(hasItem(DEFAULT_ACCOUNT_TYPE.toString())));
    }

    @Test
    @Transactional
    void getCusAccount() throws Exception {
        // Initialize the database
        cusAccountRepository.saveAndFlush(cusAccount);

        // Get the cusAccount
        restCusAccountMockMvc
            .perform(get(ENTITY_API_URL_ID, cusAccount.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(cusAccount.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.cardNo").value(DEFAULT_CARD_NO))
            .andExpect(jsonPath("$.accountType").value(DEFAULT_ACCOUNT_TYPE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingCusAccount() throws Exception {
        // Get the cusAccount
        restCusAccountMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCusAccount() throws Exception {
        // Initialize the database
        cusAccountRepository.saveAndFlush(cusAccount);

        int databaseSizeBeforeUpdate = cusAccountRepository.findAll().size();

        // Update the cusAccount
        CusAccount updatedCusAccount = cusAccountRepository.findById(cusAccount.getId()).get();
        // Disconnect from session so that the updates on updatedCusAccount are not directly saved in db
        em.detach(updatedCusAccount);
        updatedCusAccount.name(UPDATED_NAME).cardNo(UPDATED_CARD_NO).accountType(UPDATED_ACCOUNT_TYPE);

        restCusAccountMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCusAccount.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedCusAccount))
            )
            .andExpect(status().isOk());

        // Validate the CusAccount in the database
        List<CusAccount> cusAccountList = cusAccountRepository.findAll();
        assertThat(cusAccountList).hasSize(databaseSizeBeforeUpdate);
        CusAccount testCusAccount = cusAccountList.get(cusAccountList.size() - 1);
        assertThat(testCusAccount.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCusAccount.getCardNo()).isEqualTo(UPDATED_CARD_NO);
        assertThat(testCusAccount.getAccountType()).isEqualTo(UPDATED_ACCOUNT_TYPE);
    }

    @Test
    @Transactional
    void putNonExistingCusAccount() throws Exception {
        int databaseSizeBeforeUpdate = cusAccountRepository.findAll().size();
        cusAccount.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCusAccountMockMvc
            .perform(
                put(ENTITY_API_URL_ID, cusAccount.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cusAccount))
            )
            .andExpect(status().isBadRequest());

        // Validate the CusAccount in the database
        List<CusAccount> cusAccountList = cusAccountRepository.findAll();
        assertThat(cusAccountList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCusAccount() throws Exception {
        int databaseSizeBeforeUpdate = cusAccountRepository.findAll().size();
        cusAccount.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCusAccountMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cusAccount))
            )
            .andExpect(status().isBadRequest());

        // Validate the CusAccount in the database
        List<CusAccount> cusAccountList = cusAccountRepository.findAll();
        assertThat(cusAccountList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCusAccount() throws Exception {
        int databaseSizeBeforeUpdate = cusAccountRepository.findAll().size();
        cusAccount.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCusAccountMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cusAccount)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CusAccount in the database
        List<CusAccount> cusAccountList = cusAccountRepository.findAll();
        assertThat(cusAccountList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCusAccountWithPatch() throws Exception {
        // Initialize the database
        cusAccountRepository.saveAndFlush(cusAccount);

        int databaseSizeBeforeUpdate = cusAccountRepository.findAll().size();

        // Update the cusAccount using partial update
        CusAccount partialUpdatedCusAccount = new CusAccount();
        partialUpdatedCusAccount.setId(cusAccount.getId());

        partialUpdatedCusAccount.name(UPDATED_NAME).cardNo(UPDATED_CARD_NO);

        restCusAccountMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCusAccount.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCusAccount))
            )
            .andExpect(status().isOk());

        // Validate the CusAccount in the database
        List<CusAccount> cusAccountList = cusAccountRepository.findAll();
        assertThat(cusAccountList).hasSize(databaseSizeBeforeUpdate);
        CusAccount testCusAccount = cusAccountList.get(cusAccountList.size() - 1);
        assertThat(testCusAccount.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCusAccount.getCardNo()).isEqualTo(UPDATED_CARD_NO);
        assertThat(testCusAccount.getAccountType()).isEqualTo(DEFAULT_ACCOUNT_TYPE);
    }

    @Test
    @Transactional
    void fullUpdateCusAccountWithPatch() throws Exception {
        // Initialize the database
        cusAccountRepository.saveAndFlush(cusAccount);

        int databaseSizeBeforeUpdate = cusAccountRepository.findAll().size();

        // Update the cusAccount using partial update
        CusAccount partialUpdatedCusAccount = new CusAccount();
        partialUpdatedCusAccount.setId(cusAccount.getId());

        partialUpdatedCusAccount.name(UPDATED_NAME).cardNo(UPDATED_CARD_NO).accountType(UPDATED_ACCOUNT_TYPE);

        restCusAccountMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCusAccount.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCusAccount))
            )
            .andExpect(status().isOk());

        // Validate the CusAccount in the database
        List<CusAccount> cusAccountList = cusAccountRepository.findAll();
        assertThat(cusAccountList).hasSize(databaseSizeBeforeUpdate);
        CusAccount testCusAccount = cusAccountList.get(cusAccountList.size() - 1);
        assertThat(testCusAccount.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCusAccount.getCardNo()).isEqualTo(UPDATED_CARD_NO);
        assertThat(testCusAccount.getAccountType()).isEqualTo(UPDATED_ACCOUNT_TYPE);
    }

    @Test
    @Transactional
    void patchNonExistingCusAccount() throws Exception {
        int databaseSizeBeforeUpdate = cusAccountRepository.findAll().size();
        cusAccount.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCusAccountMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, cusAccount.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(cusAccount))
            )
            .andExpect(status().isBadRequest());

        // Validate the CusAccount in the database
        List<CusAccount> cusAccountList = cusAccountRepository.findAll();
        assertThat(cusAccountList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCusAccount() throws Exception {
        int databaseSizeBeforeUpdate = cusAccountRepository.findAll().size();
        cusAccount.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCusAccountMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(cusAccount))
            )
            .andExpect(status().isBadRequest());

        // Validate the CusAccount in the database
        List<CusAccount> cusAccountList = cusAccountRepository.findAll();
        assertThat(cusAccountList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCusAccount() throws Exception {
        int databaseSizeBeforeUpdate = cusAccountRepository.findAll().size();
        cusAccount.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCusAccountMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(cusAccount))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CusAccount in the database
        List<CusAccount> cusAccountList = cusAccountRepository.findAll();
        assertThat(cusAccountList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCusAccount() throws Exception {
        // Initialize the database
        cusAccountRepository.saveAndFlush(cusAccount);

        int databaseSizeBeforeDelete = cusAccountRepository.findAll().size();

        // Delete the cusAccount
        restCusAccountMockMvc
            .perform(delete(ENTITY_API_URL_ID, cusAccount.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CusAccount> cusAccountList = cusAccountRepository.findAll();
        assertThat(cusAccountList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
