package oslomet.testing;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;
import org.springframework.mock.web.MockHttpSession;
import oslomet.testing.DAL.BankRepository;
import oslomet.testing.Models.Kunde;
import oslomet.testing.Sikkerhet.Sikkerhet;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static net.bytebuddy.matcher.ElementMatchers.any;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class EnhetstestSikkerhetController {
    @InjectMocks
    private Sikkerhet sikkerhetsController;

    @Mock
    private BankRepository bankrep;

    @Mock
    private MockHttpSession session;

    @Test
    public void test_sjekkLoggInn(){
        //arrange
        Kunde kunde = new Kunde ("01010110523", "Lene", "Jensen", "Askerveien 22", "3270", "Oslo", "22224444", "HeiHei");
        when(bankrep.sjekkLoggInn(kunde.getPersonnummer(), kunde.getPassord())).thenReturn("OK");
    }

    @Test
    public void testskjekkLoggInnFeil(){
        //arrange
        Kunde kunde = new Kunde ("01010110523", "Lene", "Jensen", "Askerveien 22", "3270", "Oslo", "22224444", "HeiHei");
        when(bankrep.sjekkLoggInn(kunde.getPersonnummer(), kunde.getPassord())).thenReturn("Feil");

        //act
        String resultat = sikkerhetsController.sjekkLoggInn("01010110523", "HeiHei");

        //assert
        assertEquals("Feil i personnummer eller passord", resultat);
    }

    @Test
    public void test_loggetInn(){
        //arrange
        Map<String,Object> attributes = new HashMap<String,Object>();
        doAnswer(new Answer<Object>(){
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                String key = (String) invocation.getArguments()[0];
                return attributes.get(key);
            }
        }).when(session).getAttribute(anyString());

        doAnswer(new Answer<Object>(){
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                String key = (String) invocation.getArguments()[0];
                Object value = invocation.getArguments()[1];
                attributes.put(key, value);
                return null;
            }
        }).when(session).setAttribute(anyString(), anyString());

        session.setAttribute("Innlogget", "01010110523");

        //act
        String resultat = sikkerhetsController.loggetInn();

        //assert
        assertEquals("01010110523", resultat);
    }

    @Test
    public void test_loggetInnFeil(){
        //arrange
        session.setAttribute("Innlogget", null);

        //act
        String resultat = sikkerhetsController.loggetInn();

        //assert
        assertNull(resultat);
    }

    @Test
    public void test_loggUt(){
        //arrange
        session.setAttribute("Innlogget", "12345678901");

        //act
        sikkerhetsController.loggUt();
        String resultat = (String) session.getAttribute("Innlogget");

        //assert
        assertNull(resultat);
    }

    @Test
    public void test_logInnAdmin(){
        //arrange
        session.setAttribute("Innlogget", "Admin");

        //act
        String resultat = sikkerhetsController.loggInnAdmin("Admin", "Admin");

        //assert
        assertEquals("Logget inn", resultat);
    }

    @Test
    public void test_loggInnAdminFeil(){
        //arrange
        session.setAttribute("Innlogget", null);

        //act
        String resultat = sikkerhetsController.loggInnAdmin(anyString(), anyString());

        //assert
        assertEquals("Ikke logget inn", resultat);
    }


}
