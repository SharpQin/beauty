package cc.microthink.auth.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import cc.microthink.auth.IntegrationTest;
import cc.microthink.auth.domain.Authority;
import cc.microthink.auth.repository.EntityManager;
import cc.microthink.auth.repository.AuthorityRepository;

import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;

/**
 * Integration tests for the {@link AuthorityResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class AuthorityResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_LINK = "AAAAAAAAAA";
    private static final String UPDATED_LINK = "BBBBBBBBBB";

    private static final String DEFAULT_METHOD = "AAAAAAAAAA";
    private static final String UPDATED_METHOD = "BBBBBBBBBB";

    private static final String DEFAULT_AUTH_KEY = "AAAAAAAAAA";
    private static final String UPDATED_AUTH_KEY = "BBBBBBBBBB";

    private static final Boolean DEFAULT_SHOWED = false;
    private static final Boolean UPDATED_SHOWED = true;

    private static final Long DEFAULT_PARENT = 1L;
    private static final Long UPDATED_PARENT = 2L;

    private static final String ENTITY_API_URL = "/api/menus";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AuthorityRepository menuRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Authority menu;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Authority createEntity(EntityManager em) {
        Authority menu = new Authority()
            .name(DEFAULT_NAME)
            .title(DEFAULT_TITLE)
            .link(DEFAULT_LINK)
            .method(DEFAULT_METHOD)
            .authKey(DEFAULT_AUTH_KEY)
            .showed(DEFAULT_SHOWED)
            .parent(DEFAULT_PARENT);
        return menu;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Authority createUpdatedEntity(EntityManager em) {
        Authority menu = new Authority()
            .name(UPDATED_NAME)
            .title(UPDATED_TITLE)
            .link(UPDATED_LINK)
            .method(UPDATED_METHOD)
            .authKey(UPDATED_AUTH_KEY)
            .showed(UPDATED_SHOWED)
            .parent(UPDATED_PARENT);
        return menu;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Authority.class).block();
        } catch (Exception e) {
            // It can fail, if other entities are still referring this - it will be removed later.
        }
    }

    @AfterEach
    public void cleanup() {
        deleteEntities(em);
    }

    @BeforeEach
    public void initTest() {
        deleteEntities(em);
        menu = createEntity(em);
    }

    @Test
    void createMenu() throws Exception {
        int databaseSizeBeforeCreate = menuRepository.findAll().collectList().block().size();
        // Create the Menu
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(menu))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Menu in the database
        List<Authority> menuList = menuRepository.findAll().collectList().block();
        assertThat(menuList).hasSize(databaseSizeBeforeCreate + 1);
        Authority testMenu = menuList.get(menuList.size() - 1);
        assertThat(testMenu.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testMenu.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testMenu.getLink()).isEqualTo(DEFAULT_LINK);
        assertThat(testMenu.getMethod()).isEqualTo(DEFAULT_METHOD);
        assertThat(testMenu.getAuthKey()).isEqualTo(DEFAULT_AUTH_KEY);
        assertThat(testMenu.getShowed()).isEqualTo(DEFAULT_SHOWED);
        assertThat(testMenu.getParent()).isEqualTo(DEFAULT_PARENT);
    }

    @Test
    void createMenuWithExistingId() throws Exception {
        // Create the Menu with an existing ID
        menu.setId(1L);

        int databaseSizeBeforeCreate = menuRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(menu))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Menu in the database
        List<Authority> menuList = menuRepository.findAll().collectList().block();
        assertThat(menuList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = menuRepository.findAll().collectList().block().size();
        // set the field null
        menu.setName(null);

        // Create the Menu, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(menu))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Authority> menuList = menuRepository.findAll().collectList().block();
        assertThat(menuList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = menuRepository.findAll().collectList().block().size();
        // set the field null
        menu.setTitle(null);

        // Create the Menu, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(menu))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Authority> menuList = menuRepository.findAll().collectList().block();
        assertThat(menuList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkLinkIsRequired() throws Exception {
        int databaseSizeBeforeTest = menuRepository.findAll().collectList().block().size();
        // set the field null
        menu.setLink(null);

        // Create the Menu, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(menu))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Authority> menuList = menuRepository.findAll().collectList().block();
        assertThat(menuList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkMethodIsRequired() throws Exception {
        int databaseSizeBeforeTest = menuRepository.findAll().collectList().block().size();
        // set the field null
        menu.setMethod(null);

        // Create the Menu, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(menu))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Authority> menuList = menuRepository.findAll().collectList().block();
        assertThat(menuList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkAuthKeyIsRequired() throws Exception {
        int databaseSizeBeforeTest = menuRepository.findAll().collectList().block().size();
        // set the field null
        menu.setAuthKey(null);

        // Create the Menu, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(menu))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Authority> menuList = menuRepository.findAll().collectList().block();
        assertThat(menuList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllMenus() {
        // Initialize the database
        menuRepository.save(menu).block();

        // Get all the menuList
        webTestClient
            .get()
            .uri(ENTITY_API_URL + "?sort=id,desc")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.[*].id")
            .value(hasItem(menu.getId().intValue()))
            .jsonPath("$.[*].name")
            .value(hasItem(DEFAULT_NAME))
            .jsonPath("$.[*].title")
            .value(hasItem(DEFAULT_TITLE))
            .jsonPath("$.[*].link")
            .value(hasItem(DEFAULT_LINK))
            .jsonPath("$.[*].method")
            .value(hasItem(DEFAULT_METHOD))
            .jsonPath("$.[*].authKey")
            .value(hasItem(DEFAULT_AUTH_KEY))
            .jsonPath("$.[*].showed")
            .value(hasItem(DEFAULT_SHOWED.booleanValue()))
            .jsonPath("$.[*].parent")
            .value(hasItem(DEFAULT_PARENT.intValue()));
    }

    @Test
    void getMenu() {
        // Initialize the database
        menuRepository.save(menu).block();

        // Get the menu
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, menu.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(menu.getId().intValue()))
            .jsonPath("$.name")
            .value(is(DEFAULT_NAME))
            .jsonPath("$.title")
            .value(is(DEFAULT_TITLE))
            .jsonPath("$.link")
            .value(is(DEFAULT_LINK))
            .jsonPath("$.method")
            .value(is(DEFAULT_METHOD))
            .jsonPath("$.authKey")
            .value(is(DEFAULT_AUTH_KEY))
            .jsonPath("$.showed")
            .value(is(DEFAULT_SHOWED.booleanValue()))
            .jsonPath("$.parent")
            .value(is(DEFAULT_PARENT.intValue()));
    }

    @Test
    void getNonExistingMenu() {
        // Get the menu
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putNewMenu() throws Exception {
        // Initialize the database
        menuRepository.save(menu).block();

        int databaseSizeBeforeUpdate = menuRepository.findAll().collectList().block().size();

        // Update the menu
        Authority updatedMenu = menuRepository.findById(menu.getId()).block();
        updatedMenu
            .name(UPDATED_NAME)
            .title(UPDATED_TITLE)
            .link(UPDATED_LINK)
            .method(UPDATED_METHOD)
            .authKey(UPDATED_AUTH_KEY)
            .showed(UPDATED_SHOWED)
            .parent(UPDATED_PARENT);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedMenu.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(updatedMenu))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Menu in the database
        List<Authority> menuList = menuRepository.findAll().collectList().block();
        assertThat(menuList).hasSize(databaseSizeBeforeUpdate);
        Authority testMenu = menuList.get(menuList.size() - 1);
        assertThat(testMenu.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testMenu.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testMenu.getLink()).isEqualTo(UPDATED_LINK);
        assertThat(testMenu.getMethod()).isEqualTo(UPDATED_METHOD);
        assertThat(testMenu.getAuthKey()).isEqualTo(UPDATED_AUTH_KEY);
        assertThat(testMenu.getShowed()).isEqualTo(UPDATED_SHOWED);
        assertThat(testMenu.getParent()).isEqualTo(UPDATED_PARENT);
    }

    @Test
    void putNonExistingMenu() throws Exception {
        int databaseSizeBeforeUpdate = menuRepository.findAll().collectList().block().size();
        menu.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, menu.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(menu))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Menu in the database
        List<Authority> menuList = menuRepository.findAll().collectList().block();
        assertThat(menuList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchMenu() throws Exception {
        int databaseSizeBeforeUpdate = menuRepository.findAll().collectList().block().size();
        menu.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(menu))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Menu in the database
        List<Authority> menuList = menuRepository.findAll().collectList().block();
        assertThat(menuList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamMenu() throws Exception {
        int databaseSizeBeforeUpdate = menuRepository.findAll().collectList().block().size();
        menu.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(menu))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Menu in the database
        List<Authority> menuList = menuRepository.findAll().collectList().block();
        assertThat(menuList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateMenuWithPatch() throws Exception {
        // Initialize the database
        menuRepository.save(menu).block();

        int databaseSizeBeforeUpdate = menuRepository.findAll().collectList().block().size();

        // Update the menu using partial update
        Authority partialUpdatedMenu = new Authority();
        partialUpdatedMenu.setId(menu.getId());

        partialUpdatedMenu.method(UPDATED_METHOD);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedMenu.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedMenu))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Menu in the database
        List<Authority> menuList = menuRepository.findAll().collectList().block();
        assertThat(menuList).hasSize(databaseSizeBeforeUpdate);
        Authority testMenu = menuList.get(menuList.size() - 1);
        assertThat(testMenu.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testMenu.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testMenu.getLink()).isEqualTo(DEFAULT_LINK);
        assertThat(testMenu.getMethod()).isEqualTo(UPDATED_METHOD);
        assertThat(testMenu.getAuthKey()).isEqualTo(DEFAULT_AUTH_KEY);
        assertThat(testMenu.getShowed()).isEqualTo(DEFAULT_SHOWED);
        assertThat(testMenu.getParent()).isEqualTo(DEFAULT_PARENT);
    }

    @Test
    void fullUpdateMenuWithPatch() throws Exception {
        // Initialize the database
        menuRepository.save(menu).block();

        int databaseSizeBeforeUpdate = menuRepository.findAll().collectList().block().size();

        // Update the menu using partial update
        Authority partialUpdatedMenu = new Authority();
        partialUpdatedMenu.setId(menu.getId());

        partialUpdatedMenu
            .name(UPDATED_NAME)
            .title(UPDATED_TITLE)
            .link(UPDATED_LINK)
            .method(UPDATED_METHOD)
            .authKey(UPDATED_AUTH_KEY)
            .showed(UPDATED_SHOWED)
            .parent(UPDATED_PARENT);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedMenu.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedMenu))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Menu in the database
        List<Authority> menuList = menuRepository.findAll().collectList().block();
        assertThat(menuList).hasSize(databaseSizeBeforeUpdate);
        Authority testMenu = menuList.get(menuList.size() - 1);
        assertThat(testMenu.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testMenu.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testMenu.getLink()).isEqualTo(UPDATED_LINK);
        assertThat(testMenu.getMethod()).isEqualTo(UPDATED_METHOD);
        assertThat(testMenu.getAuthKey()).isEqualTo(UPDATED_AUTH_KEY);
        assertThat(testMenu.getShowed()).isEqualTo(UPDATED_SHOWED);
        assertThat(testMenu.getParent()).isEqualTo(UPDATED_PARENT);
    }

    @Test
    void patchNonExistingMenu() throws Exception {
        int databaseSizeBeforeUpdate = menuRepository.findAll().collectList().block().size();
        menu.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, menu.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(menu))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Menu in the database
        List<Authority> menuList = menuRepository.findAll().collectList().block();
        assertThat(menuList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchMenu() throws Exception {
        int databaseSizeBeforeUpdate = menuRepository.findAll().collectList().block().size();
        menu.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(menu))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Menu in the database
        List<Authority> menuList = menuRepository.findAll().collectList().block();
        assertThat(menuList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamMenu() throws Exception {
        int databaseSizeBeforeUpdate = menuRepository.findAll().collectList().block().size();
        menu.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(menu))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Menu in the database
        List<Authority> menuList = menuRepository.findAll().collectList().block();
        assertThat(menuList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteMenu() {
        // Initialize the database
        menuRepository.save(menu).block();

        int databaseSizeBeforeDelete = menuRepository.findAll().collectList().block().size();

        // Delete the menu
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, menu.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Authority> menuList = menuRepository.findAll().collectList().block();
        assertThat(menuList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
