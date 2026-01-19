package io.github.safeslope.lockevent.service;

import io.github.safeslope.entities.LockEvent;
import io.github.safeslope.lock.repository.LockRepository;
import io.github.safeslope.lock.service.LockNotFoundException;
import io.github.safeslope.lockevent.repository.LockEventRepository;
import io.github.safeslope.skiresort.repository.SkiResortRepository;
import io.github.safeslope.skiresort.service.SkiResortNotFoundException;
import io.github.safeslope.skiticket.repository.SkiTicketRepository;
import io.github.safeslope.skiticket.service.SkiTicketNotFoundException;
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
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class LockEventServiceTest {

    @Mock
    private LockEventRepository lockEventRepository;

    @Mock
    private SkiResortRepository skiResortRepository;

    @Mock
    private SkiTicketRepository skiTicketRepository;

    @Mock
    private LockRepository lockRepository;

    @InjectMocks
    private LockEventService lockEventService;

    private static final LocalDateTime FIXED_TIME =
        LocalDateTime.of(2026, 1, 1, 12, 0);

    @Test
    void getAll_returnsAllFromRepository() {
        List<LockEvent> events = List.of(
                    LockEvent.builder().build(),
                    LockEvent.builder().build());

        when(lockEventRepository.findAll()).thenReturn(events);

        List<LockEvent> result = lockEventService.getAll();

        assertThat(result).isSameAs(events);
        verify(lockEventRepository).findAll();
        verifyNoMoreInteractions(lockEventRepository, skiResortRepository, skiTicketRepository, lockRepository);
    }

    @Test
    void get_returnsEvent_whenFound() {
        int id = 1;
        LockEvent event = LockEvent.builder().build();
        when(lockEventRepository.findById(id)).thenReturn(Optional.of(event));

        LockEvent result = lockEventService.get(id);

        assertThat(result).isSameAs(event);
        verify(lockEventRepository).findById(id);
        verifyNoMoreInteractions(lockEventRepository, skiResortRepository, skiTicketRepository, lockRepository);
    }


    @Test
    void get_throwsLockEventNotFoundException_whenMissing() {
        int id = 404;
        when(lockEventRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> lockEventService.get(id))
                .isInstanceOf(LockEventNotFoundException.class);

        verify(lockEventRepository).findById(id);
        verifyNoMoreInteractions(lockEventRepository, skiResortRepository, skiTicketRepository, lockRepository);
    }

    @Test
    void getAllByLock_throwsLockNotFoundException_whenLockMissing() {
        int lockId = 10;
        when(lockRepository.existsById(lockId)).thenReturn(false);

        assertThatThrownBy(() -> lockEventService.getAllByLock(lockId))
                .isInstanceOf(LockNotFoundException.class);

        verify(lockRepository).existsById(lockId);
        verify(lockEventRepository, never()).findByLock_IdOrderByEventTimeDesc(anyInt());
        verifyNoMoreInteractions(lockEventRepository, skiResortRepository, skiTicketRepository, lockRepository);
    }

    @Test
    void getAllByLock_returnsEvents_whenLockExists() {
        int lockId = 10;
        List<LockEvent> events = List.of(
                LockEvent.builder().build(),
                LockEvent.builder().build());


        when(lockRepository.existsById(lockId)).thenReturn(true);
        when(lockEventRepository.findByLock_IdOrderByEventTimeDesc(lockId)).thenReturn(events);

        List<LockEvent> result = lockEventService.getAllByLock(lockId);

        assertThat(result).isSameAs(events);
        verify(lockRepository).existsById(lockId);
        verify(lockEventRepository).findByLock_IdOrderByEventTimeDesc(lockId);
        verifyNoMoreInteractions(lockEventRepository, skiResortRepository, skiTicketRepository, lockRepository);
    }

    @Test
    void getAllBySkiTicket_throwsSkiTicketNotFoundException_whenMissing() {
        int skiTicketId = 22;
        when(skiTicketRepository.existsById(skiTicketId)).thenReturn(false);

        assertThatThrownBy(() -> lockEventService.getAllBySkiTicket(skiTicketId))
                .isInstanceOf(SkiTicketNotFoundException.class);

        verify(skiTicketRepository).existsById(skiTicketId);
        verify(lockEventRepository, never()).findBySkiTicket_IdOrderByEventTimeDesc(anyInt());
        verifyNoMoreInteractions(lockEventRepository, skiResortRepository, skiTicketRepository, lockRepository);
    }

    @Test
    void getAllBySkiTicket_returnsEvents_whenTicketExists() {
        int skiTicketId = 22;
        List<LockEvent> events = List.of(LockEvent.builder().build());


        when(skiTicketRepository.existsById(skiTicketId)).thenReturn(true);
        when(lockEventRepository.findBySkiTicket_IdOrderByEventTimeDesc(skiTicketId)).thenReturn(events);

        List<LockEvent> result = lockEventService.getAllBySkiTicket(skiTicketId);

        assertThat(result).isSameAs(events);
        verify(skiTicketRepository).existsById(skiTicketId);
        verify(lockEventRepository).findBySkiTicket_IdOrderByEventTimeDesc(skiTicketId);
        verifyNoMoreInteractions(lockEventRepository, skiResortRepository, skiTicketRepository, lockRepository);
    }

    @Test
    void getAllBySkiResort_throwsSkiResortNotFoundException_whenMissing() {
        int skiResortId = 5;
        when(skiResortRepository.existsById(skiResortId)).thenReturn(false);

        assertThatThrownBy(() -> lockEventService.getAllBySkiResort(skiResortId))
                .isInstanceOf(SkiResortNotFoundException.class);

        verify(skiResortRepository).existsById(skiResortId);
        verify(lockEventRepository, never()).findByLock_Locker_SkiResort_Id(anyInt());
        verifyNoMoreInteractions(lockEventRepository, skiResortRepository, skiTicketRepository, lockRepository);
    }

    @Test
    void getAllBySkiResort_returnsEvents_whenResortExists() {
        int skiResortId = 5;
        List<LockEvent> events = List.of(LockEvent.builder().build());


        when(skiResortRepository.existsById(skiResortId)).thenReturn(true);
        when(lockEventRepository.findByLock_Locker_SkiResort_Id(skiResortId)).thenReturn(events);

        List<LockEvent> result = lockEventService.getAllBySkiResort(skiResortId);

        assertThat(result).isSameAs(events);
        verify(skiResortRepository).existsById(skiResortId);
        verify(lockEventRepository).findByLock_Locker_SkiResort_Id(skiResortId);
        verifyNoMoreInteractions(lockEventRepository, skiResortRepository, skiTicketRepository, lockRepository);
    }

    @Test
    void create_setsEventTime_whenNull_andSaves() {
        LockEvent event = LockEvent.builder()
            .eventTime(FIXED_TIME)
            .build();


        when(lockEventRepository.save(any(LockEvent.class))).thenAnswer(inv -> inv.getArgument(0));

        LockEvent result = lockEventService.create(event);

        ArgumentCaptor<LockEvent> captor = ArgumentCaptor.forClass(LockEvent.class);
        verify(lockEventRepository).save(captor.capture());

        LockEvent saved = captor.getValue();
        assertThat(saved).isSameAs(event);
        assertThat(saved.getEventTime()).isNotNull(); 
        assertThat(result).isSameAs(event);

        verifyNoMoreInteractions(lockEventRepository, skiResortRepository, skiTicketRepository, lockRepository);
    }

    @Test
    void create_doesNotOverrideEventTime_whenAlreadySet_andSaves() {
        LocalDateTime fixed = LocalDateTime.of(2026, 1, 1, 12, 0);
        LockEvent event = LockEvent.builder()
            .eventTime(FIXED_TIME)
            .build();

        when(lockEventRepository.save(any(LockEvent.class))).thenAnswer(inv -> inv.getArgument(0));

        LockEvent result = lockEventService.create(event);

        ArgumentCaptor<LockEvent> captor = ArgumentCaptor.forClass(LockEvent.class);
        verify(lockEventRepository).save(captor.capture());

        LockEvent saved = captor.getValue();
        assertThat(saved.getEventTime()).isEqualTo(fixed);
        assertThat(result).isSameAs(event);

        verifyNoMoreInteractions(lockEventRepository, skiResortRepository, skiTicketRepository, lockRepository);
    }
}
