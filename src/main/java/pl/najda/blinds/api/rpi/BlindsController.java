package pl.najda.blinds.api.rpi;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.najda.blinds.api.model.BlindCommand;

import java.util.Collection;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class BlindsController {

    private static final Logger LOG = LoggerFactory.getLogger(BlindsController.class);

    private static final GpioController GPIO = GpioFactory.getInstance();

    private static final GpioPinDigitalOutput LEFT = GPIO.provisionDigitalOutputPin(RaspiPin.GPIO_23, "Left", PinState.LOW);
    private static final GpioPinDigitalOutput RIGHT = GPIO.provisionDigitalOutputPin(RaspiPin.GPIO_24, "Right", PinState.LOW);
    private static final GpioPinDigitalOutput UP = GPIO.provisionDigitalOutputPin(RaspiPin.GPIO_25, "Up", PinState.LOW);
    private static final GpioPinDigitalOutput DOWN = GPIO.provisionDigitalOutputPin(RaspiPin.GPIO_22, "Down", PinState.LOW);
    private static final GpioPinDigitalOutput STOP = GPIO.provisionDigitalOutputPin(RaspiPin.GPIO_21, "Stop", PinState.LOW);

    private static final int DELAY_IN_MILLIS = 150;

    private final AtomicInteger currentChannel;

    public BlindsController(Integer initialChannel) {
        this.currentChannel = new AtomicInteger(initialChannel);
    }

    private static void pulse(GpioPinDigitalOutput pin) {
        pin.high();
        try {
            Thread.sleep(DELAY_IN_MILLIS);
        } catch (InterruptedException e) {
            throw new RuntimeException("Sleep was interrupted!", e);
        }
        pin.low();
        try {
            Thread.sleep(DELAY_IN_MILLIS / 3);
        } catch (InterruptedException e) {
            throw new RuntimeException("Sleep was interrupted!", e);
        }
    }

    public int getCurrentChannel() {
        return currentChannel.get();
    }

    public String execute(Integer channel, BlindCommand command) {
        final int diff = channel - currentChannel.get();
        LOG.info("Calculated difference is: {}", diff);
        final GpioPinDigitalOutput direction = diff > 0 ? RIGHT : LEFT;

        final GpioPinDigitalOutput action;
        switch (command) {
            case OPEN:
                action = UP;
                break;
            case CLOSE:
                action = DOWN;
                break;
            case STOP:
                action = STOP;
                break;
            default:
                throw new IllegalArgumentException("Unknown BlindCommand value:" + command);
        }

        // we are doing diff + 1 presses in order to include an additional press to wake the remote control
        // FIXME: this is actually a bit error prone as if someone will quickly execute another command it will be executed on a different channel then intended
        for (int i = 0; i <= diff; ++i) {
            pulse(direction);

        }
        LOG.info("Setting new channel value to: {}", currentChannel.get() + diff);
        currentChannel.getAndUpdate(currentChannel -> currentChannel + diff);
        pulse(action);

        return String.format("Executed %s on %d channel", command, channel);
    }

    public Collection<String> execute(Collection<Integer> channels, BlindCommand command) {

        return channels.stream().map(channel -> execute(channel, command))
                .collect(Collectors.toList());

    }
}
