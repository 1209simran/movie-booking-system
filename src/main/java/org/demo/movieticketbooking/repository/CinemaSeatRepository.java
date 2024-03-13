package org.demo.movieticketbooking.repository;

import org.demo.movieticketbooking.model.CinemaSeat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CinemaSeatRepository extends JpaRepository<CinemaSeat, Long> {

    Optional<CinemaSeat> findById(Long id);

    List<CinemaSeat> findSeatByScreenId(Long screenId);
}
