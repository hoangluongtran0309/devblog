package com.hoangluongtran0309.devblog.admin;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public interface UserRepository extends CrudRepository<User, UserId>, UserRepositoryCustom {

    boolean existsByEmail(Email email);

    Optional<User> findByEmail(Email email);
}
