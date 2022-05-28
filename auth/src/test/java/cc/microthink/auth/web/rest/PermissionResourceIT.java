package cc.microthink.auth.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import cc.microthink.auth.IntegrationTest;
import cc.microthink.auth.domain.Permission;
import cc.microthink.auth.repository.EntityManager;
import cc.microthink.auth.repository.PermissionRepository;
import java.time.Duration;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;

/**
 * Integration tests for the {@link PermissionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class PermissionResourceIT {

    private static final String DEFAULT_AUTH_KEY = "AAAAAAAAAA";
    private static final String UPDATED_AUTH_KEY = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/permissions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PermissionRepository permissionRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Permission permission;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Permission createEntity(EntityManager em) {
        Permission permission = new Permission().authKey(DEFAULT_AUTH_KEY);
        return permission;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Permission createUpdatedEntity(EntityManager em) {
        Permission permission = new Permission().authKey(UPDATED_AUTH_KEY);
        return permission;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Permission.class).block();
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
        permission = createEntity(em);
    }

    @Test
    void createPermission() throws Exception {
        int databaseSizeBeforeCreate = permissionRepository.findAll().collectList().block().size();
        // Create the Permission
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(permission))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Permission in the database
        List<Permission> permissionList = permissionRepository.findAll().collectList().block();
        assertThat(permissionList).hasSize(databaseSizeBeforeCreate + 1);
        Permission testPermission = permissionList.get(permissionList.size() - 1);
        assertThat(testPermission.getAuthKey()).isEqualTo(DEFAULT_AUTH_KEY);
    }

    @Test
    void createPermissionWithExistingId() throws Exception {
        // Create the Permission with an existing ID
        permission.setId(1L);

        int databaseSizeBeforeCreate = permissionRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(permission))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Permission in the database
        List<Permission> permissionList = permissionRepository.findAll().collectList().block();
        assertThat(permissionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkAuthKeyIsRequired() throws Exception {
        int databaseSizeBeforeTest = permissionRepository.findAll().collectList().block().size();
        // set the field null
        permission.setAuthKey(null);

        // Create the Permission, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(permission))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Permission> permissionList = permissionRepository.findAll().collectList().block();
        assertThat(permissionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllPermissionsAsStream() {
        // Initialize the database
        permissionRepository.save(permission).block();

        List<Permission> permissionList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(Permission.class)
            .getResponseBody()
            .filter(permission::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(permissionList).isNotNull();
        assertThat(permissionList).hasSize(1);
        Permission testPermission = permissionList.get(0);
        assertThat(testPermission.getAuthKey()).isEqualTo(DEFAULT_AUTH_KEY);
    }

    @Test
    void getAllPermissions() {
        // Initialize the database
        permissionRepository.save(permission).block();

        // Get all the permissionList
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
            .value(hasItem(permission.getId().intValue()))
            .jsonPath("$.[*].authKey")
            .value(hasItem(DEFAULT_AUTH_KEY));
    }

    @Test
    void getPermission() {
        // Initialize the database
        permissionRepository.save(permission).block();

        // Get the permission
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, permission.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(permission.getId().intValue()))
            .jsonPath("$.authKey")
            .value(is(DEFAULT_AUTH_KEY));
    }

    @Test
    void getNonExistingPermission() {
        // Get the permission
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putNewPermission() throws Exception {
        // Initialize the database
        permissionRepository.save(permission).block();

        int databaseSizeBeforeUpdate = permissionRepository.findAll().collectList().block().size();

        // Update the permission
        Permission updatedPermission = permissionRepository.findById(permission.getId()).block();
        updatedPermission.authKey(UPDATED_AUTH_KEY);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedPermission.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(updatedPermission))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Permission in the database
        List<Permission> permissionList = permissionRepository.findAll().collectList().block();
        assertThat(permissionList).hasSize(databaseSizeBeforeUpdate);
        Permission testPermission = permissionList.get(permissionList.size() - 1);
        assertThat(testPermission.getAuthKey()).isEqualTo(UPDATED_AUTH_KEY);
    }

    @Test
    void putNonExistingPermission() throws Exception {
        int databaseSizeBeforeUpdate = permissionRepository.findAll().collectList().block().size();
        permission.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, permission.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(permission))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Permission in the database
        List<Permission> permissionList = permissionRepository.findAll().collectList().block();
        assertThat(permissionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchPermission() throws Exception {
        int databaseSizeBeforeUpdate = permissionRepository.findAll().collectList().block().size();
        permission.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(permission))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Permission in the database
        List<Permission> permissionList = permissionRepository.findAll().collectList().block();
        assertThat(permissionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamPermission() throws Exception {
        int databaseSizeBeforeUpdate = permissionRepository.findAll().collectList().block().size();
        permission.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(permission))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Permission in the database
        List<Permission> permissionList = permissionRepository.findAll().collectList().block();
        assertThat(permissionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdatePermissionWithPatch() throws Exception {
        // Initialize the database
        permissionRepository.save(permission).block();

        int databaseSizeBeforeUpdate = permissionRepository.findAll().collectList().block().size();

        // Update the permission using partial update
        Permission partialUpdatedPermission = new Permission();
        partialUpdatedPermission.setId(permission.getId());

        partialUpdatedPermission.authKey(UPDATED_AUTH_KEY);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedPermission.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedPermission))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Permission in the database
        List<Permission> permissionList = permissionRepository.findAll().collectList().block();
        assertThat(permissionList).hasSize(databaseSizeBeforeUpdate);
        Permission testPermission = permissionList.get(permissionList.size() - 1);
        assertThat(testPermission.getAuthKey()).isEqualTo(UPDATED_AUTH_KEY);
    }

    @Test
    void fullUpdatePermissionWithPatch() throws Exception {
        // Initialize the database
        permissionRepository.save(permission).block();

        int databaseSizeBeforeUpdate = permissionRepository.findAll().collectList().block().size();

        // Update the permission using partial update
        Permission partialUpdatedPermission = new Permission();
        partialUpdatedPermission.setId(permission.getId());

        partialUpdatedPermission.authKey(UPDATED_AUTH_KEY);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedPermission.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedPermission))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Permission in the database
        List<Permission> permissionList = permissionRepository.findAll().collectList().block();
        assertThat(permissionList).hasSize(databaseSizeBeforeUpdate);
        Permission testPermission = permissionList.get(permissionList.size() - 1);
        assertThat(testPermission.getAuthKey()).isEqualTo(UPDATED_AUTH_KEY);
    }

    @Test
    void patchNonExistingPermission() throws Exception {
        int databaseSizeBeforeUpdate = permissionRepository.findAll().collectList().block().size();
        permission.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, permission.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(permission))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Permission in the database
        List<Permission> permissionList = permissionRepository.findAll().collectList().block();
        assertThat(permissionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchPermission() throws Exception {
        int databaseSizeBeforeUpdate = permissionRepository.findAll().collectList().block().size();
        permission.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(permission))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Permission in the database
        List<Permission> permissionList = permissionRepository.findAll().collectList().block();
        assertThat(permissionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamPermission() throws Exception {
        int databaseSizeBeforeUpdate = permissionRepository.findAll().collectList().block().size();
        permission.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(permission))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Permission in the database
        List<Permission> permissionList = permissionRepository.findAll().collectList().block();
        assertThat(permissionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deletePermission() {
        // Initialize the database
        permissionRepository.save(permission).block();

        int databaseSizeBeforeDelete = permissionRepository.findAll().collectList().block().size();

        // Delete the permission
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, permission.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Permission> permissionList = permissionRepository.findAll().collectList().block();
        assertThat(permissionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
