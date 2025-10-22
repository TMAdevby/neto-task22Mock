import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import ru.netology.entity.Country;
import ru.netology.entity.Location;
import ru.netology.geo.GeoServiceImpl;

import java.util.stream.Stream;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.params.provider.Arguments.of;


import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


public class GeoServiceImplTest {

    private GeoServiceImpl geoServiceImpl;

    @BeforeEach
    void setUp() {
        geoServiceImpl = new GeoServiceImpl();
    }

    static Stream<Arguments> provideIpAndLocation() {

        return Stream.of(
                of("127.0.0.1", new Location(null, null, null, 0)),
                of("172.0.32.11", new Location("Moscow", Country.RUSSIA, "Lenina", 15)),
                of("96.44.183.149", new Location("New York", Country.USA, " 10th Avenue", 32)),
                of("172.16.0.1", new Location("Moscow", Country.RUSSIA, null, 0)),
                of("172.31.255.254", new Location("Moscow", Country.RUSSIA, null, 0)),
                of("172.20.50.100", new Location("Moscow", Country.RUSSIA, null, 0)),
                of("96.16.0.1", new Location("New York", Country.USA, null, 0)),
                of("96.31.255.254", new Location("New York", Country.USA, null, 0)),
                of("96.20.50.100", new Location("New York", Country.USA, null, 0))
        );
    }

    @ParameterizedTest(name = "IP:{0} -> {1}")
    @MethodSource("provideIpAndLocation")
    @DisplayName("Получение расположения по id")
    void givenIp_validTask_returnCorrectLocation(String ip, Location expectedLocation) {
        // given:
        // when:
        Location location = geoServiceImpl.byIp(ip);
        // then:
        assertThat(location).isEqualTo(expectedLocation);
    }

    @Test
    @DisplayName("Исключение при вызове byCoordinates")
    void givenCoordinates_byCoordinates_thenThrowRuntimeException() {
        // given:
        double latitude = 12.24;
        double longitude = 56.82;
        // when:
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            geoServiceImpl.byCoordinates(latitude, longitude);
        });
        // then:
        MatcherAssert.assertThat(exception.getMessage(), containsString("Not implemented"));
    }


}
