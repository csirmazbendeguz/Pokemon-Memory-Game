package net.csirmazbendeguz.memory_game.unit.state;

import net.csirmazbendeguz.memory_game.state.Stopwatch;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Observer;
import java.util.Timer;
import java.util.TimerTask;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StopwatchTest extends MockTimerFactoryTestBase {

    private Stopwatch stopwatch;

    @Mock
    private Observer mockObserver;

    @BeforeEach
    void setup() {
        stopwatch = new Stopwatch(mockTimerFactory);
        stopwatch.addObserver(mockObserver);
    }

    @Test
    void testDefaults() {
        assertEquals(0, stopwatch.getSeconds());
        assertFalse(stopwatch.isRunning());
        verifyZeroInteractions(mockObserver);
    }

    @Test
    void testReset() {
        stopwatch.reset();
        assertEquals(0, stopwatch.getSeconds());
        assertFalse(stopwatch.isRunning());
        verify(mockObserver).update(stopwatch, 0);
    }

    @Test
    void testStart() {
        Timer mockTimer = mockTimer();

        stopwatch.start();

        assertTrue(stopwatch.isRunning());
        assertEquals(0, stopwatch.getSeconds());
        verifyZeroInteractions(mockObserver);

        ArgumentCaptor<TimerTask> timerTaskCaptor = ArgumentCaptor.forClass(TimerTask.class);
        verify(mockTimer).scheduleAtFixedRate(timerTaskCaptor.capture(), eq(0L), eq(1000L));
        TimerTask scheduledTask = timerTaskCaptor.getValue();
        scheduledTask.run();

        assertTrue(stopwatch.isRunning());
        assertEquals(1, stopwatch.getSeconds());
        verify(mockObserver).update(stopwatch, 1);
    }

    @Test
    void testAlreadyRunning() {
        mockTimer();
        stopwatch.start();
        assertThrows(IllegalStateException.class, () -> stopwatch.start(), "The timer is already running.");
    }

    @Test
    void testStop() {
        Timer mockTimer = mockTimer();

        stopwatch.start();
        stopwatch.stop();

        assertFalse(stopwatch.isRunning());
        assertEquals(0, stopwatch.getSeconds());
        verifyZeroInteractions(mockObserver);
        verify(mockTimer).cancel();
    }

    @Test
    void testAlreadyStopped() {
        assertThrows(IllegalStateException.class, () -> stopwatch.stop(), "The timer is already stopped.");
    }

}
