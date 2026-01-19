package io.github.safeslope.mqtt.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.github.safeslope.entities.Lock;
import io.github.safeslope.entities.Locker;
import io.github.safeslope.entities.SkiResort;
import io.github.safeslope.lock.repository.LockRepository;
import io.github.safeslope.lock.service.LockNotFoundException;
import io.github.safeslope.mqtt.MqttLockAdapter;
import io.github.safeslope.mqtt.dto.CommandDto;
import io.github.safeslope.mqtt.dto.DtoConstants;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
class MqttLockServiceTest {

    @Mock
    private LockRepository lockRepository;

    @Mock
    private MqttLockAdapter mqttAdapter;

    @InjectMocks
    private MqttLockService mqttLockService;

    @Test
    void unlock_sendsUnlockCommand_whenLockExists() throws Exception {
        String mac = "AA:BB:CC:DD:EE:FF";

        SkiResort resort = mock(SkiResort.class);
        when(resort.getTenantId()).thenReturn(11);
        when(resort.getId()).thenReturn(22);

        Locker locker = mock(Locker.class);
        when(locker.getId()).thenReturn(33);
        when(locker.getSkiResort()).thenReturn(resort);

        Lock lock = Lock.builder()
                .id(44)
                .macAddress(mac)
                .locker(locker)
                .build();

        when(lockRepository.findByMacAddress(mac)).thenReturn(lock);

        mqttLockService.unlock(mac);

     
        ArgumentCaptor<CommandDto> captor = ArgumentCaptor.forClass(CommandDto.class);
        verify(mqttAdapter).sendCommand(eq(11), eq(22), eq(33), captor.capture());

        CommandDto dto = captor.getValue();
        assertThat(dto).isNotNull();
        assertThat(dto.getCommand()).isEqualTo(DtoConstants.Command.UNLOCK);
        assertThat(dto.getLockId()).isEqualTo(44);
        assertThat(dto.getLockerId()).isEqualTo(33);
        assertThat(dto.getSkiTicketId()).isNull();
        assertThat(dto.getMsgId()).isNotNull();
        assertThat(dto.getTimestamp()).isNotNull();


        verify(lockRepository).findByMacAddress(mac);
        verifyNoMoreInteractions(lockRepository, mqttAdapter);
    }

    @Test
    void lock_sendsLockCommand_whenLockExists() throws Exception {
  
        String mac = "AA:BB:CC:DD:EE:FF";

        SkiResort resort = mock(SkiResort.class);
        when(resort.getTenantId()).thenReturn(1);
        when(resort.getId()).thenReturn(2);

        Locker locker = mock(Locker.class);
        when(locker.getId()).thenReturn(3);
        when(locker.getSkiResort()).thenReturn(resort);

        Lock lock = Lock.builder()
                .id(4)
                .macAddress(mac)
                .locker(locker)
                .build();

        when(lockRepository.findByMacAddress(mac)).thenReturn(lock);

        mqttLockService.lock(mac);

        ArgumentCaptor<CommandDto> captor = ArgumentCaptor.forClass(CommandDto.class);
        verify(mqttAdapter).sendCommand(eq(1), eq(2), eq(3), captor.capture());

        CommandDto dto = captor.getValue();
        assertThat(dto.getCommand()).isEqualTo(DtoConstants.Command.LOCK);
        assertThat(dto.getLockId()).isEqualTo(4);
        assertThat(dto.getLockerId()).isEqualTo(3);
        assertThat(dto.getSkiTicketId()).isNull();


        verify(lockRepository).findByMacAddress(mac);
        verifyNoMoreInteractions(lockRepository, mqttAdapter);
    }

    @Test
    void unlock_throwsLockNotFoundException_whenMacUnknown() {
        String mac = "00:00:00:00:00:00";
        when(lockRepository.findByMacAddress(mac)).thenReturn(null);

        assertThatThrownBy(() -> mqttLockService.unlock(mac))
                .isInstanceOf(LockNotFoundException.class);

        verify(lockRepository).findByMacAddress(mac);
        verifyNoInteractions(mqttAdapter);
    }

    @Test
    void lock_throwsRuntimeException_whenMqttFails() throws Exception {
        String mac = "AA:BB:CC:DD:EE:FF";

        SkiResort resort = mock(SkiResort.class);
        when(resort.getTenantId()).thenReturn(1);
        when(resort.getId()).thenReturn(2);

        Locker locker = mock(Locker.class);
        when(locker.getId()).thenReturn(3);
        when(locker.getSkiResort()).thenReturn(resort);

        Lock lock = Lock.builder().id(4).macAddress(mac).locker(locker).build();
        when(lockRepository.findByMacAddress(mac)).thenReturn(lock);

        doThrow(new MqttException(0))
                .when(mqttAdapter).sendCommand(anyInt(), anyInt(), anyInt(), any(CommandDto.class));

        assertThatThrownBy(() -> mqttLockService.lock(mac))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Failed sending LOCK command via MQTT");
    }

    @Test
    void unlock_throwsRuntimeException_whenSerializationFails() throws Exception {
        String mac = "AA:BB:CC:DD:EE:FF";

        SkiResort resort = mock(SkiResort.class);
        when(resort.getTenantId()).thenReturn(1);
        when(resort.getId()).thenReturn(2);

        Locker locker = mock(Locker.class);
        when(locker.getId()).thenReturn(3);
        when(locker.getSkiResort()).thenReturn(resort);

        Lock lock = Lock.builder().id(4).macAddress(mac).locker(locker).build();
        when(lockRepository.findByMacAddress(mac)).thenReturn(lock);

        doThrow(new JsonProcessingException("boom") {})
                .when(mqttAdapter).sendCommand(anyInt(), anyInt(), anyInt(), any(CommandDto.class));

        assertThatThrownBy(() -> mqttLockService.unlock(mac))
                .isInstanceOf(RuntimeException.class);
    }
}

