package com.github.supercodingspring.service.mapper;

import com.github.supercodingspring.repository.airlineTicket.AirlineTicket;
import com.github.supercodingspring.web.dto.airline.Ticket;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-05-10T14:24:12+0900",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 11.0.16.1 (Microsoft)"
)
public class TicketMapperImpl implements TicketMapper {

    @Override
    public Ticket airlineTicketToTicket(AirlineTicket airlineTicket) {
        if ( airlineTicket == null ) {
            return null;
        }

        Ticket ticket = new Ticket();

        ticket.setDepart( airlineTicket.getDepartureLocation() );
        ticket.setArrival( airlineTicket.getArrivalLocation() );
        ticket.setDepartureTime( TicketMapper.localDateTimeToString( airlineTicket.getDepartureAt() ) );
        ticket.setReturnTime( TicketMapper.localDateTimeToString( airlineTicket.getReturnAt() ) );
        ticket.setTicketId( airlineTicket.getTicketId() );

        return ticket;
    }
}
