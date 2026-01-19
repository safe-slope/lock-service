package io.github.safeslope.lock.service;

import io.github.safeslope.entities.Lock;
import io.github.safeslope.lock.repository.LockRepository;
import io.github.safeslope.locker.repository.LockerRepository;
import io.github.safeslope.locker.service.LockerNotFoundException;
import io.github.safeslope.skiresort.repository.SkiResortRepository;
import io.github.safeslope.skiresort.service.SkiResortNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LockServiceTest {

    @Mock
    private LockRepository lockRepository;

    @Mock
    private LockerRepository lockerRepository;

    @Mock
    private SkiResortRepository skiResortRepository;

    @InjectMocks
    private LockService lockService;

    @Test
    void getAllLocks_returnsAllFromRepository() {
        List<Lock> locks = List.of(new Lock(), new Lock());
        when(lockRepository.findAll()).thenReturn(locks);

        List<Lock> result = lockService.getAllLocks();

        assertThat(result).isSameAs(locks);
        verify(lockRepository).findAll();
        verifyNoMoreInteractions(lockRepository, lockerRepository, skiResortRepository);
    }

    @Test
    void getLock_returnsLock_whenFound() {
        int id = 1;
        Lock lock = new Lock();
        when(lockRepository.findById(id)).thenReturn(Optional.of(lock));

        Lock result = lockService.getLock(id);

        assertThat(result).isSameAs(lock);
        verify(lockRepository).findById(id);
        verifyNoMoreInteractions(lockRepository, lockerRepository, skiResortRepository);
    }

    @Test
    void getLock_throwsLockNotFoundException_whenMissing() {
        int id = 99;
        when(lockRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> lockService.getLock(id))
                .isInstanceOf(LockNotFoundException.class);

        verify(lockRepository).findById(id);
        verifyNoMoreInteractions(lockRepository, lockerRepository, skiResortRepository);
    }

    @Test
    void getByMacAddress_returnsLock_whenRepositoryReturnsNonNull() {
        String mac = "AA:BB:CC:DD:EE:FF";
        Lock lock = new Lock();
        when(lockRepository.findByMacAddress(mac)).thenReturn(lock);

        Lock result = lockService.getByMacAddress(mac);

        assertThat(result).isSameAs(lock);
        verify(lockRepository).findByMacAddress(mac);
        verifyNoMoreInteractions(lockRepository, lockerRepository, skiResortRepository);
    }

    @Test
    void getByMacAddress_throwsLockNotFoundException_whenRepositoryReturnsNull() {
        String mac = "AA:BB:CC:DD:EE:FF";
        when(lockRepository.findByMacAddress(mac)).thenReturn(null);

        assertThatThrownBy(() -> lockService.getByMacAddress(mac))
                .isInstanceOf(LockNotFoundException.class);

        verify(lockRepository).findByMacAddress(mac);
        verifyNoMoreInteractions(lockRepository, lockerRepository, skiResortRepository);
    }

    @Test
    void create_savesAndReturnsLock() {
        Lock input = new Lock();
        Lock saved = new Lock();
        when(lockRepository.save(input)).thenReturn(saved);

        Lock result = lockService.create(input);

        assertThat(result).isSameAs(saved);
        verify(lockRepository).save(input);
        verifyNoMoreInteractions(lockRepository, lockerRepository, skiResortRepository);
    }

    @Test
    void update_throwsLockNotFoundException_whenIdDoesNotExist() {
        int id = 5;
        Lock input = new Lock();
        when(lockRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> lockService.update(id, input))
                .isInstanceOf(LockNotFoundException.class);

        verify(lockRepository).findById(id);
        verify(lockRepository, never()).save(any());
        verifyNoMoreInteractions(lockRepository, lockerRepository, skiResortRepository);
    }

    @Test
    void update_setsIdAndSaves_whenLockExists() {
        int id = 5;
        Lock input = new Lock();

        when(lockRepository.findById(id)).thenReturn(Optional.of(new Lock()));
        when(lockRepository.save(any(Lock.class))).thenAnswer(inv -> inv.getArgument(0));

        Lock result = lockService.update(id, input);

        // preveri, da je service nastavil ID pred save
        ArgumentCaptor<Lock> captor = ArgumentCaptor.forClass(Lock.class);
        verify(lockRepository).save(captor.capture());
        Lock savedArg = captor.getValue();

        assertThat(savedArg).isSameAs(input);
        assertThat(savedArg.getId()).isEqualTo(id);
        assertThat(result).isSameAs(input);

        verify(lockRepository).findById(id);
        verifyNoMoreInteractions(lockRepository, lockerRepository, skiResortRepository);
    }

    @Test
    void delete_throwsLockNotFoundException_whenDoesNotExist() {
        int id = 10;
        when(lockRepository.existsById(id)).thenReturn(false);

        assertThatThrownBy(() -> lockService.delete(id))
                .isInstanceOf(LockNotFoundException.class);

        verify(lockRepository).existsById(id);
        verify(lockRepository, never()).deleteById(anyInt());
        verifyNoMoreInteractions(lockRepository, lockerRepository, skiResortRepository);
    }

    @Test
    void delete_deletesById_whenExists() {
        int id = 10;
        when(lockRepository.existsById(id)).thenReturn(true);

        lockService.delete(id);

        verify(lockRepository).existsById(id);
        verify(lockRepository).deleteById(id);
        verifyNoMoreInteractions(lockRepository, lockerRepository, skiResortRepository);
    }

    @Test
    void getAllByLockerId_throwsLockerNotFoundException_whenLockerMissing() {
        int lockerId = 7;
        when(lockerRepository.existsById(lockerId)).thenReturn(false);

        assertThatThrownBy(() -> lockService.getAllByLockerId(lockerId))
                .isInstanceOf(LockerNotFoundException.class);

        verify(lockerRepository).existsById(lockerId);
        verify(lockRepository, never()).findByLocker_Id(anyInt());
        verifyNoMoreInteractions(lockRepository, lockerRepository, skiResortRepository);
    }

    @Test
    void getAllByLockerId_returnsLocks_whenLockerExists() {
        int lockerId = 7;
        List<Lock> locks = List.of(new Lock(), new Lock());

        when(lockerRepository.existsById(lockerId)).thenReturn(true);
        when(lockRepository.findByLocker_Id(lockerId)).thenReturn(locks);

        List<Lock> result = lockService.getAllByLockerId(lockerId);

        assertThat(result).isSameAs(locks);
        verify(lockerRepository).existsById(lockerId);
        verify(lockRepository).findByLocker_Id(lockerId);
        verifyNoMoreInteractions(lockRepository, lockerRepository, skiResortRepository);
    }

    @Test
    void getAllBySkiResortId_throwsSkiResortNotFoundException_whenResortMissing() {
        int resortId = 3;
        when(skiResortRepository.existsById(resortId)).thenReturn(false);

        assertThatThrownBy(() -> lockService.getAllBySkiResortId(resortId))
                .isInstanceOf(SkiResortNotFoundException.class);

        verify(skiResortRepository).existsById(resortId);
        verify(lockRepository, never()).findByLocker_SkiResort_Id(anyInt());
        verifyNoMoreInteractions(lockRepository, lockerRepository, skiResortRepository);
    }

    @Test
    void getAllBySkiResortId_returnsLocks_whenResortExists() {
        int resortId = 3;
        List<Lock> locks = List.of(new Lock());

        when(skiResortRepository.existsById(resortId)).thenReturn(true);
        when(lockRepository.findByLocker_SkiResort_Id(resortId)).thenReturn(locks);

        List<Lock> result = lockService.getAllBySkiResortId(resortId);

        assertThat(result).isSameAs(locks);
        verify(skiResortRepository).existsById(resortId);
        verify(lockRepository).findByLocker_SkiResort_Id(resortId);
        verifyNoMoreInteractions(lockRepository, lockerRepository, skiResortRepository);
    }
}
