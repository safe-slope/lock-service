package io.github.safeslope.skiticket.service;

import io.github.safeslope.entities.SkiTicket;
import io.github.safeslope.skiticket.repository.SkiTicketRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SkiTicketServiceTest {

    @Mock
    private SkiTicketRepository repo;

    @InjectMocks
    private SkiTicketService skiTicketService;

    @Test
    void getAll_returnsAllFromRepository() {
        List<SkiTicket> tickets = List.of(
                SkiTicket.builder().build(),
                SkiTicket.builder().build()
        );
        when(repo.findAll()).thenReturn(tickets);

        List<SkiTicket> result = skiTicketService.getAll();

        assertThat(result).isSameAs(tickets);
        verify(repo).findAll();
        verifyNoMoreInteractions(repo);
    }

    @Test
    void get_returnsTicket_whenFound() {
        int id = 1;
        SkiTicket ticket = SkiTicket.builder().build();
        when(repo.findById(id)).thenReturn(Optional.of(ticket));

        SkiTicket result = skiTicketService.get(id);

        assertThat(result).isSameAs(ticket);
        verify(repo).findById(id);
        verifyNoMoreInteractions(repo);
    }


    @Test
    void update_throwsSkiTicketNotFoundException_whenMissing() {
        int id = 10;
        SkiTicket updated = SkiTicket.builder().build();
        when(repo.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> skiTicketService.update(id, updated))
                .isInstanceOf(SkiTicketNotFoundException.class);

        verify(repo).findById(id);
        verify(repo, never()).save(any());
        verifyNoMoreInteractions(repo);
    }


    @Test
    void delete_deletesById_whenExists() {
        int id = 55;
        when(repo.existsById(id)).thenReturn(true);

        skiTicketService.delete(id);

        verify(repo).existsById(id);
        verify(repo).deleteById(id);
        verifyNoMoreInteractions(repo);
    }

    @Test
    void getAllBySkiResortId_returnsTickets() {
        int resortId = 7;
        List<SkiTicket> tickets = List.of(SkiTicket.builder().build());

        when(repo.findBySkiResort_Id(resortId)).thenReturn(tickets);

        List<SkiTicket> result = skiTicketService.getAllBySkiResortId(resortId);

        assertThat(result).isSameAs(tickets);
        verify(repo).findBySkiResort_Id(resortId);
        verifyNoMoreInteractions(repo);
    }
}
