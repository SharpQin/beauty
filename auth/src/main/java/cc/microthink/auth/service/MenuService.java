package cc.microthink.auth.service;

import cc.microthink.auth.domain.Menu;
import cc.microthink.auth.repository.MenuRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link Menu}.
 */
@Service
@Transactional
public class MenuService {

    private final Logger log = LoggerFactory.getLogger(MenuService.class);

    private final MenuRepository menuRepository;

    public MenuService(MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }

    /**
     * Save a menu.
     *
     * @param menu the entity to save.
     * @return the persisted entity.
     */
    public Mono<Menu> save(Menu menu) {
        log.debug("Request to save Menu : {}", menu);
        return menuRepository.save(menu);
    }

    /**
     * Update a menu.
     *
     * @param menu the entity to save.
     * @return the persisted entity.
     */
    public Mono<Menu> update(Menu menu) {
        log.debug("Request to save Menu : {}", menu);
        return menuRepository.save(menu);
    }

    /**
     * Partially update a menu.
     *
     * @param menu the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<Menu> partialUpdate(Menu menu) {
        log.debug("Request to partially update Menu : {}", menu);

        return menuRepository
            .findById(menu.getId())
            .map(existingMenu -> {
                if (menu.getName() != null) {
                    existingMenu.setName(menu.getName());
                }
                if (menu.getTitle() != null) {
                    existingMenu.setTitle(menu.getTitle());
                }
                if (menu.getLink() != null) {
                    existingMenu.setLink(menu.getLink());
                }
                if (menu.getMethod() != null) {
                    existingMenu.setMethod(menu.getMethod());
                }
                if (menu.getAuthKey() != null) {
                    existingMenu.setAuthKey(menu.getAuthKey());
                }
                if (menu.getShowed() != null) {
                    existingMenu.setShowed(menu.getShowed());
                }
                if (menu.getParent() != null) {
                    existingMenu.setParent(menu.getParent());
                }

                return existingMenu;
            })
            .flatMap(menuRepository::save);
    }

    /**
     * Get all the menus.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<Menu> findAll(Pageable pageable) {
        log.debug("Request to get all Menus");
        return menuRepository.findAllBy(pageable);
    }

    /**
     * Returns the number of menus available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return menuRepository.count();
    }

    /**
     * Get one menu by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<Menu> findOne(Long id) {
        log.debug("Request to get Menu : {}", id);
        return menuRepository.findById(id);
    }

    /**
     * Delete the menu by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete Menu : {}", id);
        return menuRepository.deleteById(id);
    }
}
