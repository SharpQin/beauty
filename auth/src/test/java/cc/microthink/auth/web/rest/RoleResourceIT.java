package cc.microthink.auth.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

import cc.microthink.auth.IntegrationTest;
import cc.microthink.auth.domain.Permission;
import cc.microthink.auth.domain.Role;
import cc.microthink.auth.repository.EntityManager;
import cc.microthink.auth.repository.RoleRepository;
import cc.microthink.auth.service.RoleService;
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
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Integration tests for the {@link RoleResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class RoleResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DSC = "AAAAAAAAAA";
    private static final String UPDATED_DSC = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/roles";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private RoleRepository roleRepository;

    @Mock
    private RoleRepository roleRepositoryMock;

    @Mock
    private RoleService roleServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Role role;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Role createEntity(EntityManager em) {
        Role role = new Role().name(DEFAULT_NAME).dsc(DEFAULT_DSC);
        // Add required entity
        //TODO Authorities
        return role;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Role createUpdatedEntity(EntityManager em) {
        Role role = new Role().name(UPDATED_NAME).dsc(UPDATED_DSC);
        // Add required entity
        return role;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll("rel_role__permissions").block();
            em.deleteAll(Role.class).block();
        } catch (Exception e) {
            // It can fail, if other entities are still referring this - it will be removed later.
        }
        PermissionResourceIT.deleteEntities(em);
    }

    @AfterEach
    public void cleanup() {
        deleteEntities(em);
    }

    @BeforeEach
    public void initTest() {
        deleteEntities(em);
        role = createEntity(em);
    }

    @Test
    void createRole() throws Exception {
        int databaseSizeBeforeCreate = roleRepository.findAll().collectList().block().size();
        // Create the Role
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(role))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Role in the database
        List<Role> roleList = roleRepository.findAll().collectList().block();
        assertThat(roleList).hasSize(databaseSizeBeforeCreate + 1);
        Role testRole = roleList.get(roleList.size() - 1);
        assertThat(testRole.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testRole.getDsc()).isEqualTo(DEFAULT_DSC);
    }

    @Test
    void createRoleWithExistingId() throws Exception {
        // Create the Role with an existing ID
        role.setId(1L);

        int databaseSizeBeforeCreate = roleRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(role))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Role in the database
        List<Role> roleList = roleRepository.findAll().collectList().block();
        assertThat(roleList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = roleRepository.findAll().collectList().block().size();
        // set the field null
        role.setName(null);

        // Create the Role, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(role))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Role> roleList = roleRepository.findAll().collectList().block();
        assertThat(roleList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllRoles() {
        // Initialize the database
        roleRepository.save(role).block();

        // Get all the roleList
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
            .value(hasItem(role.getId().intValue()))
            .jsonPath("$.[*].name")
            .value(hasItem(DEFAULT_NAME))
            .jsonPath("$.[*].dsc")
            .value(hasItem(DEFAULT_DSC));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllRolesWithEagerRelationshipsIsEnabled() {
        when(roleServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=true").exchange().expectStatus().isOk();

        verify(roleServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllRolesWithEagerRelationshipsIsNotEnabled() {
        when(roleServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=true").exchange().expectStatus().isOk();

        verify(roleServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    void getRole() {
        // Initialize the database
        roleRepository.save(role).block();

        // Get the role
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, role.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(role.getId().intValue()))
            .jsonPath("$.name")
            .value(is(DEFAULT_NAME))
            .jsonPath("$.dsc")
            .value(is(DEFAULT_DSC));
    }

    @Test
    void getNonExistingRole() {
        // Get the role
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putNewRole() throws Exception {
        // Initialize the database
        roleRepository.save(role).block();

        int databaseSizeBeforeUpdate = roleRepository.findAll().collectList().block().size();

        // Update the role
        Role updatedRole = roleRepository.findById(role.getId()).block();
        updatedRole.name(UPDATED_NAME).dsc(UPDATED_DSC);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedRole.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(updatedRole))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Role in the database
        List<Role> roleList = roleRepository.findAll().collectList().block();
        assertThat(roleList).hasSize(databaseSizeBeforeUpdate);
        Role testRole = roleList.get(roleList.size() - 1);
        assertThat(testRole.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testRole.getDsc()).isEqualTo(UPDATED_DSC);
    }

    @Test
    void putNonExistingRole() throws Exception {
        int databaseSizeBeforeUpdate = roleRepository.findAll().collectList().block().size();
        role.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, role.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(role))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Role in the database
        List<Role> roleList = roleRepository.findAll().collectList().block();
        assertThat(roleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchRole() throws Exception {
        int databaseSizeBeforeUpdate = roleRepository.findAll().collectList().block().size();
        role.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(role))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Role in the database
        List<Role> roleList = roleRepository.findAll().collectList().block();
        assertThat(roleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamRole() throws Exception {
        int databaseSizeBeforeUpdate = roleRepository.findAll().collectList().block().size();
        role.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(role))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Role in the database
        List<Role> roleList = roleRepository.findAll().collectList().block();
        assertThat(roleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateRoleWithPatch() throws Exception {
        // Initialize the database
        roleRepository.save(role).block();

        int databaseSizeBeforeUpdate = roleRepository.findAll().collectList().block().size();

        // Update the role using partial update
        Role partialUpdatedRole = new Role();
        partialUpdatedRole.setId(role.getId());

        partialUpdatedRole.dsc(UPDATED_DSC);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedRole.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedRole))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Role in the database
        List<Role> roleList = roleRepository.findAll().collectList().block();
        assertThat(roleList).hasSize(databaseSizeBeforeUpdate);
        Role testRole = roleList.get(roleList.size() - 1);
        assertThat(testRole.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testRole.getDsc()).isEqualTo(UPDATED_DSC);
    }

    @Test
    void fullUpdateRoleWithPatch() throws Exception {
        // Initialize the database
        roleRepository.save(role).block();

        int databaseSizeBeforeUpdate = roleRepository.findAll().collectList().block().size();

        // Update the role using partial update
        Role partialUpdatedRole = new Role();
        partialUpdatedRole.setId(role.getId());

        partialUpdatedRole.name(UPDATED_NAME).dsc(UPDATED_DSC);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedRole.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedRole))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Role in the database
        List<Role> roleList = roleRepository.findAll().collectList().block();
        assertThat(roleList).hasSize(databaseSizeBeforeUpdate);
        Role testRole = roleList.get(roleList.size() - 1);
        assertThat(testRole.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testRole.getDsc()).isEqualTo(UPDATED_DSC);
    }

    @Test
    void patchNonExistingRole() throws Exception {
        int databaseSizeBeforeUpdate = roleRepository.findAll().collectList().block().size();
        role.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, role.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(role))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Role in the database
        List<Role> roleList = roleRepository.findAll().collectList().block();
        assertThat(roleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchRole() throws Exception {
        int databaseSizeBeforeUpdate = roleRepository.findAll().collectList().block().size();
        role.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(role))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Role in the database
        List<Role> roleList = roleRepository.findAll().collectList().block();
        assertThat(roleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamRole() throws Exception {
        int databaseSizeBeforeUpdate = roleRepository.findAll().collectList().block().size();
        role.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(role))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Role in the database
        List<Role> roleList = roleRepository.findAll().collectList().block();
        assertThat(roleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteRole() {
        // Initialize the database
        roleRepository.save(role).block();

        int databaseSizeBeforeDelete = roleRepository.findAll().collectList().block().size();

        // Delete the role
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, role.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Role> roleList = roleRepository.findAll().collectList().block();
        assertThat(roleList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
