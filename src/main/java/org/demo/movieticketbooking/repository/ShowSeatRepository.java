package org.demo.movieticketbooking.repository;

import jakarta.persistence.LockModeType;
import jakarta.persistence.QueryHint;
import org.demo.movieticketbooking.model.ShowSeat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ShowSeatRepository extends JpaRepository<ShowSeat, Long> {

    Optional<ShowSeat> findById(Long id);

    List<ShowSeat> findShowSeatsByShowsId(Long showId);


    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @QueryHints({@QueryHint(name = "jakarta.persistence.lock.timeout", value = "0")})
    List<ShowSeat> findShowSeatsByCinemaSeatIdInAndShowsId(List<Long> seatIds, Long showId);

    List<ShowSeat> findByBookingId(Long id);

    @Query(value = "SELECT ss.* FROM bookmyshow.booking b  join bookmyshow.show_seat ss on b.id=ss.booking_id WHERE " +
            "b" + ".created_timestamp  <= :tenMinutesAgo and b.status not in ('BOOKED') ;", nativeQuery = true)
    List<ShowSeat> findExpiredSeats(LocalDateTime tenMinutesAgo);
}
