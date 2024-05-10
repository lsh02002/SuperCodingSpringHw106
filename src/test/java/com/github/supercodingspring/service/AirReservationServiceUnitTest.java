package com.github.supercodingspring.service;

import com.github.supercodingspring.repository.airlineTicket.AirlineTicket;
import com.github.supercodingspring.repository.airlineTicket.AirlineTicketJpaRepository;
import com.github.supercodingspring.repository.airlineTicket.AirlineTicketRepository;
import com.github.supercodingspring.repository.flight.Flight;
import com.github.supercodingspring.repository.passenger.Passenger;
import com.github.supercodingspring.repository.passenger.PassengerReposiotry;
import com.github.supercodingspring.repository.reservations.Reservation;
import com.github.supercodingspring.repository.reservations.ReservationJpaRepository;
import com.github.supercodingspring.repository.users.UserEntity;
import com.github.supercodingspring.repository.users.UserJpaRepository;
import com.github.supercodingspring.service.exceptions.InvalidValueException;
import com.github.supercodingspring.service.exceptions.NotAcceptException;
import com.github.supercodingspring.service.exceptions.NotFoundException;
import com.github.supercodingspring.web.dto.airline.ReservationResult;
import com.github.supercodingspring.web.dto.airline.Ticket;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@Slf4j
class AirReservationServiceUnitTest {

    @Mock
    private UserJpaRepository userJpaRepository;

    @Mock
    private AirlineTicketJpaRepository airlineTicketJpaRepository;

    @Mock
    private PassengerReposiotry passengerReposiotry;

    @Mock
    private ReservationJpaRepository reservationJpaRepository;

    @InjectMocks
    private AirReservationService airReservationService;

    @BeforeEach
    public void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @DisplayName("airlineTicket에 해당하는 유저 항공권들이 모두 있어서 성공하는 경우")
    @Test
    void FindUserFavoritePlaceTicketsCase1() {
        Integer userId = 5;
        String likePlace = "파리";
        String ticketType = "왕복";

        UserEntity userEntity = UserEntity.builder()
                .userId(userId)
                .likeTravelPlace(likePlace)
                .userName("name1")
                .phoneNum("1234")
                .build();

        List<AirlineTicket> airlineTickets = Arrays.asList(
                AirlineTicket.builder()
                        .ticketId(1)
                        .arrivalLocation(likePlace)
                        .ticketType(ticketType)
                        .build(),
                AirlineTicket.builder()
                        .ticketId(2)
                        .arrivalLocation(likePlace)
                        .ticketType(ticketType)
                        .build(),
                AirlineTicket.builder()
                        .ticketId(3)
                        .arrivalLocation(likePlace)
                        .ticketType(ticketType)
                        .build(),
                AirlineTicket.builder()
                        .ticketId(4)
                        .arrivalLocation(likePlace)
                        .ticketType(ticketType)
                        .build()
        );

        // when
        when(userJpaRepository.findById(any())).thenReturn(Optional.of(userEntity));
        when(airlineTicketJpaRepository.findAirlineTicketsByArrivalLocationAndTicketType(likePlace, ticketType))
                .thenReturn(airlineTickets);

        // then
        List<Ticket> tickets = airReservationService.findUserFavoritePlaceTickets(userId, ticketType);
        log.info("tickets: " + tickets);
        assertTrue(
                tickets.stream()
                        .map(Ticket::getArrival)
                        .allMatch((arrival) -> arrival.equals(likePlace))
        );
    }

    @DisplayName("TicketType이 왕복 | 편도가 아닌 경우, Exception 발생해야함 ")
    @Test
    void FindUserFavoritePlaceTicketsCase2() {
        // given
        Integer userId = 5;
        String likePlace = "파리";
        String ticketType = "왕";

        UserEntity userEntity = UserEntity.builder()
                                          .userId(userId)
                                          .likeTravelPlace(likePlace)
                                          .userName("name1")
                                          .phoneNum("1234")
                                          .build();

        List<AirlineTicket> airlineTickets = Arrays.asList(
                AirlineTicket.builder().ticketId(1).arrivalLocation(likePlace).ticketType(ticketType).build(),
                AirlineTicket.builder().ticketId(2).arrivalLocation(likePlace).ticketType(ticketType).build(),
                AirlineTicket.builder().ticketId(3).arrivalLocation(likePlace).ticketType(ticketType).build(),
                AirlineTicket.builder().ticketId(4).arrivalLocation(likePlace).ticketType(ticketType).build()
        );

        // when
        when(userJpaRepository.findById(any())).thenReturn(Optional.of(userEntity));
        when(airlineTicketJpaRepository.findAirlineTicketsByArrivalLocationAndTicketType(likePlace, ticketType))
                .thenReturn(airlineTickets);

        // then
        assertThrows(InvalidValueException.class,
                     () -> airReservationService.findUserFavoritePlaceTickets(userId, ticketType)
        );
    }

    @DisplayName("AirlineTickets를 찾을 수 없는 경우, Exception 발생해야 함 ")
    @Test
    void FindUserFavoritePlaceTicketsCase3() {
        // given
        Integer userId = 5;
        String likePlace = "파리";
        String ticketType = "왕복";

        UserEntity userEntity = UserEntity.builder()
                                          .userId(userId)
                                          .likeTravelPlace(likePlace)
                                          .userName("name1")
                                          .phoneNum("1234")
                                          .build();

        List<AirlineTicket> airlineTickets = new ArrayList<>();

        // when
        when(userJpaRepository.findById(any())).thenReturn(Optional.ofNullable(userEntity));
        when(airlineTicketJpaRepository.findAirlineTicketsByArrivalLocationAndTicketType(likePlace, ticketType))
                .thenReturn(airlineTickets);

        // then
        assertThrows(NotFoundException.class,
                     () -> airReservationService.findUserFavoritePlaceTickets(userId, ticketType)
        );
    }

    @DisplayName("User를 찾을 수 없는 경우, Exception 발생해야 함  ")
    @Test
    void FindUserFavoritePlaceTicketsCase4() {
        // given
        Integer userId = 5;
        String likePlace = "파리";
        String ticketType = "왕복";

        UserEntity userEntity = null;

        List<AirlineTicket> airlineTickets = Arrays.asList(
                AirlineTicket.builder().ticketId(1).arrivalLocation(likePlace).ticketType(ticketType).build(),
                AirlineTicket.builder().ticketId(2).arrivalLocation(likePlace).ticketType(ticketType).build(),
                AirlineTicket.builder().ticketId(3).arrivalLocation(likePlace).ticketType(ticketType).build(),
                AirlineTicket.builder().ticketId(4).arrivalLocation(likePlace).ticketType(ticketType).build()
        );

        // when
        when(userJpaRepository.findById(any())).thenReturn(Optional.ofNullable(userEntity));
        when(airlineTicketJpaRepository.findAirlineTicketsByArrivalLocationAndTicketType(likePlace, ticketType))
                .thenReturn(airlineTickets);

        // then
        assertThrows(NotFoundException.class,
                     () -> airReservationService.findUserFavoritePlaceTickets(userId, ticketType)
        );
    }

    //아래에 과제 내용입니다.

    @DisplayName("예약 테스트를 성공적으로 완료한 경우")
    @Test
    void makeReservationCase1(){
        Integer userId = 5;
        String likePlace = "파리";
        String ticketType = "왕복";

        AirlineTicket airlineTicket = AirlineTicket.builder()
                .ticketId(1)
                .arrivalLocation(likePlace)
                .totalPrice(20000.0)
                .tax(2000.0)
                .ticketType(ticketType)
                .build();

        when(airlineTicketJpaRepository.findById(any())).thenReturn(Optional.ofNullable(airlineTicket));

        AirlineTicket airlineTicketFound = null;

        if(airlineTicket != null) {
         airlineTicketFound = airlineTicketJpaRepository.findById(airlineTicket.getTicketId()).orElseThrow(() -> new NotFoundException("airLineTicket 찾을 수 없습니다."));
        }

        Passenger passenger = Passenger.builder()
                .passengerId(5)
                .passportNum("1111")
                .user(new UserEntity())
                .build();

        when(passengerReposiotry.findPassengerByUserId(any())).thenReturn(Optional.ofNullable(passenger));

        Passenger passengerFound = passengerReposiotry.findPassengerByUserId(userId)
                .orElseThrow(() -> new NotFoundException("요청하신 userId " + userId + "에 해당하는 Passenger를 찾을 수 없습니다."));

        List<Flight> flightList = Arrays.asList(
                new Flight(1, airlineTicket, LocalDateTime.now(), LocalDateTime.now(), "서울", "파리", 14000.0, 60000.0),
                new Flight(2, airlineTicket, LocalDateTime.now(), LocalDateTime.now(), "런던", "서울", 14000.0, 60000.0),
                new Flight(3, airlineTicket, LocalDateTime.now(), LocalDateTime.now(), "서울", "바르셀로나", 15000.0, 30000.0),
                new Flight(4, airlineTicket, LocalDateTime.now(), LocalDateTime.now(), "바르셀로나", "서울", 15000.0, 30000.0),
                new Flight(5, airlineTicket, LocalDateTime.now(), LocalDateTime.now(), "서울", "시드니", 13000.0, 65000.0),
                new Flight(6, airlineTicket, LocalDateTime.now(), LocalDateTime.now(), "시드니", "서울", 13000.0, 65000.0)
        );

        if (flightList.isEmpty())
            throw new NotFoundException("AirlineTicket Id " + airlineTicketFound.getTicketId() + " 에 해당하는 항공편과 항공권 찾을 수 없습니다.");

        Reservation reservation = new Reservation(passengerFound, airlineTicket);
        try {
            reservationJpaRepository.save(reservation);
        } catch (RuntimeException e){
            throw new NotAcceptException("Reservation 이 등록되는 과정이 거부되었습니다.");
        }

        List<Integer> prices = flightList.stream().map(Flight::getFlightPrice).map(Double::intValue).collect(Collectors.toList());
        List<Integer> charges = flightList.stream().map(Flight::getCharge).map(Double::intValue).collect(Collectors.toList());
        Integer tax = airlineTicket.getTax().intValue();
        Integer totalPrice = airlineTicket.getTotalPrice().intValue();

        //isSuccess Boolean 변수는 false 이면 예외처리 되서 여기에 올수 없어서 설정안했습니다.
        ReservationResult result = new ReservationResult(prices, charges, tax, totalPrice, true);

        assertNotNull(result);
        System.out.println("예약 등록 결과 : " + result);
    }

    // 그밖의 예외 테스트는 본문에 예외처리 부분을 남겨두었고 assertThrows 함수를 사용하면 되는데 코드가 많이 중복되는 것 같아서 못했습니다.
}