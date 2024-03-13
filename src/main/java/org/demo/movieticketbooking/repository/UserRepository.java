package org.demo.movieticketbooking.repository;

import org.demo.movieticketbooking.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findNameByEmail(String email);
}
