package cc.microthink.auth.service;

import cc.microthink.auth.domain.Role;
import cc.microthink.auth.repository.RoleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link Role}.
 */
@Service
@Transactional
public class RoleService {

    private final Logger log = LoggerFactory.getLogger(RoleService.class);

    private final RoleRepository roleRepository;

    private final RedisService redisService;

    public RoleService(RoleRepository roleRepository, RedisService redisService) {
        this.roleRepository = roleRepository;
        this.redisService = redisService;
    }

    /**
     * Save a role.
     *
     * @param role the entity to save.
     * @return the persisted entity.
     */
    public Mono<Role> save(Role role) {
        log.debug("Request to save Role : {}", role);
        role.changeForSave();
        //update redis
        return roleRepository.save(role).doOnSuccess(r -> redisService.saveRedisRoleAuthorities(r));
    }

    /**
     * Update a role.
     *
     * @param role the entity to save.
     * @return the persisted entity.
     */
    public Mono<Role> update(Role role) {
        log.debug("Request to save Role : {}", role);
        role.changeForSave();
        //TODO update redis
        return roleRepository.save(role).doOnSuccess(r -> redisService.saveRedisRoleAuthorities(r));
    }

    /**
     * Partially update a role.
     *
     * @param role the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<Role> partialUpdate(Role role) {
        log.debug("Request to partially update Role : {}", role);
        role.changeForSave();
        return roleRepository
            .findById(role.getId())
            .map(existingRole -> {
                if (role.getName() != null) {
                    existingRole.setName(role.getName());
                }
                if (role.getAuthorities() != null) {
                    existingRole.setAuthorities(role.getAuthorities());
                    //TODO update redis
                }
                if (role.getDsc() != null) {
                    existingRole.setDsc(role.getDsc());
                }

                return existingRole;
            })
            .flatMap(roleRepository::save).doOnSuccess(r -> redisService.saveRedisRoleAuthorities(r));
    }

    /**
     * Get all the roles.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<Role> findAll(Pageable pageable) {
        log.debug("Request to get all Roles");
        return roleRepository.findAllBy(pageable);
    }

    /**
     * Get all the roles with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Flux<Role> findAllWithEagerRelationships(Pageable pageable) {
        return roleRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Returns the number of roles available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return roleRepository.count();
    }

    /**
     * Get one role by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)

    public Mono<Role> findOne(Long id) {
        log.debug("Request to get Role : {}", id);
        return roleRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the role by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete Role : {}", id);
        //update redis
        return roleRepository.findById(id).zipWith(roleRepository.deleteById(id)).map(t -> {
            redisService.removeRedisRole(t.getT1().getName());
            return t.getT2();
        });
    }
}
