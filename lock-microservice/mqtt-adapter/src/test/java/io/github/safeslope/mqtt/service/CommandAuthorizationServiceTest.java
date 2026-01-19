package io.github.safeslope.mqtt.service;

import io.github.safeslope.entities.Lock;
import io.github.safeslope.entities.Locker;
import io.github.safeslope.lock.service.LockService;
import io.github.safeslope.lockevent.service.LockEventService;
import io.github.safeslope.location.service.LocationService;
import io.github.safeslope.locker.service.LockerService;
import io.github.safeslope.mqtt.dto.DtoConstants;
import io.github.safeslope.mqtt.s2s.AntiAbuseDecision;
import io.github.safeslope.mqtt.s2s.EvaluateResponse;
import io.github.safeslope.mqtt.s2s.VerifyCardResponse;
import io.github.safeslope.skiticket.service.SkiTicketService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestClient;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommandAuthorizationServiceTest {

    @Mock LockService lockService;
    @Mock LockEventService lockEventService;
    @Mock SkiTicketService skiTicketService;
    @Mock LockerService lockerService;
    @Mock LocationService locationService;

    @Mock RestClient skiCardClient;
    @Mock RestClient antiAbuseClient;

    private CommandAuthorizationService service;
    private final Integer resortId = 99;

    private RestClient.ResponseSpec skiResp;
    private RestClient.ResponseSpec antiResp;

    @BeforeEach
    void setUp() {
        service = new CommandAuthorizationService(
                lockService,
                lockEventService,
                skiTicketService,
                lockerService,
                locationService,
                skiCardClient,
                antiAbuseClient,
                resortId
        );
    }

    private Lock givenLock(Integer lockId, Lock.Mode mode, Lock.State state, Integer lockerId) {
        Locker locker = null;
        if (lockerId != null) {
            locker = mock(Locker.class);
            when(locker.getId()).thenReturn(lockerId);
        }

        Lock lock = Lock.builder()
                .id(lockId)
                .mode(mode)
                .state(state)
                .locker(locker)
                .macAddress("AA:BB:CC:DD:EE:FF")
                .build();

        when(lockService.getLock(lockId)).thenReturn(lock);
        return lock;
    }

    private void enableSkiCard() {
        RestClient.RequestBodyUriSpec post = mock(RestClient.RequestBodyUriSpec.class, RETURNS_SELF);
        skiResp = mock(RestClient.ResponseSpec.class);

        when(skiCardClient.post()).thenReturn(post);
        when(post.retrieve()).thenReturn(skiResp);
    }

    private void enableAntiAbuse() {
        RestClient.RequestBodyUriSpec post = mock(RestClient.RequestBodyUriSpec.class, RETURNS_SELF);
        antiResp = mock(RestClient.ResponseSpec.class);

        when(antiAbuseClient.post()).thenReturn(post);
        when(post.retrieve()).thenReturn(antiResp);
    }

    private void stubAntiAbuseDecision(AntiAbuseDecision decision) {
        EvaluateResponse resp = mock(EvaluateResponse.class);
        when(resp.decision()).thenReturn(decision);
        when(antiResp.body(EvaluateResponse.class)).thenReturn(resp);
    }

    private void stubSkiCardValid(boolean valid) {
        VerifyCardResponse resp = mock(VerifyCardResponse.class);
        when(resp.valid()).thenReturn(valid);
        when(skiResp.body(VerifyCardResponse.class)).thenReturn(resp);
    }

    @Test
    void authorize_returnsTrue_forLock_whenNormalUnlocked_andVerificationsAllow() {
        Integer lockId = 10;
        Integer skiTicketId = 20;

        givenLock(lockId, Lock.Mode.NORMAL, Lock.State.UNLOCKED, 30);

        enableAntiAbuse();
        enableSkiCard();
        stubAntiAbuseDecision(AntiAbuseDecision.ALLOW);
        stubSkiCardValid(true);

        boolean result = service.authorize(lockId, DtoConstants.Command.LOCK, skiTicketId);

        assertThat(result).isTrue();
        verify(lockService, times(2)).getLock(lockId);
        verify(antiAbuseClient).post();
        verify(skiCardClient).post();
    }

    @Test
    void authorize_returnsFalse_forLock_whenAntiAbuseDoesNotAllow() {
        Integer lockId = 10;
        Integer skiTicketId = 20;

        givenLock(lockId, Lock.Mode.NORMAL, Lock.State.UNLOCKED, 30);

        enableAntiAbuse();
        stubAntiAbuseDecision(null); // ali DENY

        boolean result = service.authorize(lockId, DtoConstants.Command.LOCK, skiTicketId);

        assertThat(result).isFalse();
        verify(lockService, times(2)).getLock(lockId);
        verify(antiAbuseClient).post();
        verifyNoInteractions(skiCardClient);
    }

    @Test
    void authorize_returnsFalse_forLock_whenSkiCardInvalid() {
        Integer lockId = 10;
        Integer skiTicketId = 20;

        givenLock(lockId, Lock.Mode.NORMAL, Lock.State.UNLOCKED, 30);

        enableAntiAbuse();
        enableSkiCard();
        stubAntiAbuseDecision(AntiAbuseDecision.ALLOW);
        stubSkiCardValid(false);

        boolean result = service.authorize(lockId, DtoConstants.Command.LOCK, skiTicketId);

        assertThat(result).isFalse();
        verify(lockService, times(2)).getLock(lockId);
        verify(antiAbuseClient).post();
        verify(skiCardClient).post();
    }

    @Test
    void authorize_returnsTrue_forUnlock_whenNormalLocked_andVerificationsAllow() {
        Integer lockId = 10;
        Integer skiTicketId = 20;

        givenLock(lockId, Lock.Mode.NORMAL, Lock.State.LOCKED, 30);

        enableAntiAbuse();
        enableSkiCard();
        stubAntiAbuseDecision(AntiAbuseDecision.ALLOW);
        stubSkiCardValid(true);

        boolean result = service.authorize(lockId, DtoConstants.Command.UNLOCK, skiTicketId);

        assertThat(result).isTrue();
        verify(lockService, times(2)).getLock(lockId);
        verify(antiAbuseClient).post();
        verify(skiCardClient).post();
    }

    @Test
    void authorize_returnsTrue_forSetModeToNormal_whenCurrentIsNotNormal() {
        Integer lockId = 10;

        givenLock(lockId, Lock.Mode.SERVICE, Lock.State.UNKNOWN, null);

        boolean result = service.authorize(lockId, DtoConstants.Command.SET_MODE_TO_NORMAL, null);

        assertThat(result).isTrue();
        verify(lockService).getLock(lockId);
        verifyNoInteractions(antiAbuseClient, skiCardClient);
    }

    @Test
    void authorize_returnsFalse_forSetModeToNormal_whenAlreadyNormal() {
        Integer lockId = 10;

        givenLock(lockId, Lock.Mode.NORMAL, Lock.State.UNKNOWN, null);

        boolean result = service.authorize(lockId, DtoConstants.Command.SET_MODE_TO_NORMAL, null);

        assertThat(result).isFalse();
        verify(lockService).getLock(lockId);
        verifyNoInteractions(antiAbuseClient, skiCardClient);
    }
}
