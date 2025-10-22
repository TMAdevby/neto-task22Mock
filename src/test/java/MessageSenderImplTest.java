import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import ru.netology.entity.Country;
import ru.netology.entity.Location;
import ru.netology.geo.GeoService;
import ru.netology.i18n.LocalizationService;
import ru.netology.sender.MessageSender;
import ru.netology.sender.MessageSenderImpl;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class MessageSenderImplTest {

    @Test
    void testSendMessage_WhenCountryIsRussia_ReturnsRussianMessage(){
        GeoService geoService = Mockito.mock(GeoService.class);
        Mockito.when(geoService.byIp("172.0.32.11"))
                .thenReturn(new Location("Moscow", Country.RUSSIA, "Lenina", 15));

        LocalizationService localizationService = Mockito.mock(LocalizationService.class);
        Mockito.when(localizationService.locale(Country.RUSSIA))
                .thenReturn("Добро пожаловать");

        MessageSenderImpl messageSenderImpl = new MessageSenderImpl(geoService, localizationService);
        Map<String,String> headers = new HashMap<>();
        headers.put(MessageSenderImpl.IP_ADDRESS_HEADER, "172.0.32.11");

        String message = messageSenderImpl.send(headers);
        //String expectedMessage = localizationService.locale(Country.RUSSIA);

        Assertions.assertEquals("Добро пожаловать",message);

//        Mockito.verify(geoService, Mockito.times(1)).byIp("172.0.32.11");
//        Mockito.verify(localizationService,Mockito.times(3)).locale(Country.RUSSIA);

        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);
        Mockito.verify(geoService).byIp(argumentCaptor.capture());
        Assertions.assertEquals("172.0.32.11", argumentCaptor.getValue());

        ArgumentCaptor<Country> countryCaptor = ArgumentCaptor.forClass(Country.class);
        Mockito.verify(localizationService, Mockito.times(2)).locale(countryCaptor.capture());
        Assertions.assertEquals(Country.RUSSIA, countryCaptor.getValue());
    }

    @Test
    void testSendMessage_WhenIpMissing_ReturnsDefaultMessage(){
        GeoService geoService = Mockito.mock(GeoService.class);
        Mockito.when(geoService.byIp(""))
                .thenReturn(new Location("Moscow", Country.USA, "Lenina", 15));

        LocalizationService localizationService = Mockito.mock(LocalizationService.class);
        Mockito.when(localizationService.locale(Country.USA))
                .thenReturn("Welcome");

        MessageSenderImpl messageSenderImpl = new MessageSenderImpl(geoService, localizationService);
        Map<String,String> headers = new HashMap<>();
        headers.put(MessageSenderImpl.IP_ADDRESS_HEADER, "");

        String message = messageSenderImpl.send(headers);
 //       String expectedMessage = localizationService.locale(Country.GERMANY);

        Assertions.assertEquals("Welcome",message);

         Mockito.verify(geoService, Mockito.never()).byIp(Mockito.anyString());

        ArgumentCaptor<Country> countryCaptor = ArgumentCaptor.forClass(Country.class);
        Mockito.verify(localizationService).locale(countryCaptor.capture());
        Assertions.assertEquals(Country.USA, countryCaptor.getValue());
    }

//  Чтобы проверить IP = null нужно поменять реализацию:
//  String ipAddress = headers.get(IP_ADDRESS_HEADER);  без String.valueOf

}
