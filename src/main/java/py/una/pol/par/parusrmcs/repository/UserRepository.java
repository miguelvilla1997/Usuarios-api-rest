package py.una.pol.par.parusrmcs.repository;

import java.util.Collection;
import py.una.pol.par.commons.entity.Entity;
import py.una.pol.par.commons.repository.Repository;

/**
 *
 * @author Mauricio Machuca
 * @param <User>
 * @param <Integer>
 */
public interface UserRepository<User, Integer> extends Repository<User, Integer> {

    /**
     *
     * @param name
     * @param lastname
     * @param users
     * @return
     */
    boolean containsNameLastname(String name, String lastname,Collection<User> users);

    /**
     *
     * @param name
     * @param lastname
     * @param users
     * @return
     */
    public Collection<User> findByNameLastname(String name, String lastname,Collection<User> users);

    public Entity findByLoginName(String loginName);

}
