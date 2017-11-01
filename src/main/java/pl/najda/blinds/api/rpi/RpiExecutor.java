package pl.najda.blinds.api.rpi;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;
import pl.najda.blinds.api.model.BlindCommand;

import java.util.Collection;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class RpiExecutor {

    private static final GpioController GPIO = GpioFactory.getInstance();

    private static final GpioPinDigitalOutput LEFT = GPIO.provisionDigitalOutputPin(RaspiPin.GPIO_23, "Left", PinState.LOW);
    private static final GpioPinDigitalOutput RIGHT = GPIO.provisionDigitalOutputPin(RaspiPin.GPIO_24, "Right", PinState.LOW);
    private static final GpioPinDigitalOutput UP = GPIO.provisionDigitalOutputPin(RaspiPin.GPIO_25, "Up", PinState.LOW);
    private static final GpioPinDigitalOutput DOWN = GPIO.provisionDigitalOutputPin(RaspiPin.GPIO_22, "Down", PinState.LOW);
    private static final GpioPinDigitalOutput STOP = GPIO.provisionDigitalOutputPin(RaspiPin.GPIO_21, "Stop", PinState.LOW);

    private static final int DELAY_IN_MILLIS = 200;

    private final AtomicInteger channelNumber;

    public RpiExecutor(Integer initialChanneNumber) {
        this.channelNumber = new AtomicInteger(initialChanneNumber);
    }

    public String execute(Integer channel, BlindCommand command) {
        final int diff = channel - channelNumber.get();
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
        for (int i = 0; i <= diff; ++i) {
            direction.pulse(DELAY_IN_MILLIS);
        }
        channelNumber.getAndUpdate(currentChannel -> currentChannel + diff);
        action.pulse(DELAY_IN_MILLIS);

        return String.format("Executed %s on %d channel", command, channel);
    }

    public Collection<String> execute(Collection<Integer> channels, BlindCommand command) {

        return channels.stream().map(channel -> execute(channel, command))
                .collect(Collectors.toList());

    }
}
