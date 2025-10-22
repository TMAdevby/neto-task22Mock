import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import ru.netology.entity.Country;
import ru.netology.i18n.LocalizationService;
import ru.netology.i18n.LocalizationServiceImpl;

import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.params.provider.Arguments.of;

public class LocalizationServiceImplTest {

    LocalizationService localizationService;

    @BeforeEach
    void setUp(){
        localizationService = new LocalizationServiceImpl();
    }

    static Stream<Arguments> getMessageByCountry() {

        return Stream.of(
                of(Country.RUSSIA, "Добро пожаловать"),
                of(Country.USA, "Welcome"),
                of(Country.BRAZIL, "Welcome"),
                of(Country.GERMANY, "Welcome")
        );
    }

    @ParameterizedTest(name = "Country:{0} -> {1}")
    @MethodSource("getMessageByCountry")
    @DisplayName("Получение сообщения по названию страны")
    void givenCountry_validTask_returnCorrectMessage(Country country, String expectedMessage) {
        // given:
        // when:
        String message = localizationService.locale(country);
        // then:
        assertThat(message).isEqualTo(expectedMessage);
    }


}
