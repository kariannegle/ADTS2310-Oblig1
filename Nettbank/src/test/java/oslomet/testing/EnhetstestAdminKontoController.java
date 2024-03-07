package oslomet.testing;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import oslomet.testing.API.AdminKontoController;
import oslomet.testing.DAL.AdminRepository;
import oslomet.testing.Models.Konto;
import oslomet.testing.Models.Transaksjon;
import oslomet.testing.Sikkerhet.Sikkerhet;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class EnhetstestAdminKontoController {

    @InjectMocks
    // denne skal testes
    private AdminKontoController kontoController;

    @Mock
    // denne skal mock'es
    private AdminRepository repository;

    @Mock
    // denne skal mock'es
    private Sikkerhet sjekk;

    @Test
    public void test_hentAlleKontiOK() {
        // Arrange
        List<Transaksjon> konto1transaksjoner = new ArrayList<>();
        List<Transaksjon> konto2transaksjoner = new ArrayList<>();
        Konto konto1 = new Konto("05068924604", "41925811793",
                13495.41, "Brukskonto", "NOK", konto1transaksjoner);
        Konto konto2 = new Konto("23129735499", "92081352378",
                116_296.96, "Sparekonto", "NOK", konto2transaksjoner);

        List<Konto> kontoliste = new ArrayList<>();
        kontoliste.add(konto1);
        kontoliste.add(konto2);

        when(sjekk.loggetInn()).thenReturn(konto1.getPersonnummer(), konto2.getPersonnummer());
        when(repository.hentAlleKonti()).thenReturn(kontoliste);

        // Act
        List<Konto> resultat = kontoController.hentAlleKonti();

        // Assert
        assertEquals(kontoliste, resultat);
    }

    @Test
    public void test_hentAlleKontiFeil(){
        //arrange
        when(sjekk.loggetInn()).thenReturn(null);

        //act
        List<Konto> resultat = kontoController.hentAlleKonti();

        //assert
        assertNull(resultat);
    }

    @Test
    public void test_resgistrerKontoOK(){
        //arrange
        List<Transaksjon> konto1transaksjoner = new ArrayList<>();
        Konto konto1 = new Konto("05068924604", "41925811793",
                13495.41, "Brukskonto", "NOK", konto1transaksjoner);

        when(sjekk.loggetInn()).thenReturn(konto1.getPersonnummer());
        when(repository.registrerKonto(any(Konto.class))).thenReturn("OK");

        //act
        String resultat = kontoController.registrerKonto(konto1);

        //assert
        assertEquals("OK", resultat);
    }

    @Test
    public void test_resgistrerKontoFeil(){
        //arrange
        List<Transaksjon> konto1transaksjoner = new ArrayList<>();
        Konto konto1 = new Konto ("05068924604", "41925811793",
                13495.41, "Brukskonto", "NOK", konto1transaksjoner);

        when(sjekk.loggetInn()).thenReturn(null);

        //act
        String resultat = kontoController.registrerKonto(konto1);

        //assert
        assertEquals("Ikke innlogget", resultat);
    }

    @Test
    public void test_endreKontoOK(){
        //arrange
        List<Transaksjon> konto1transaksjoner = new ArrayList<>();
        Konto konto1 = new Konto("05068924604", "41925811793",
                13495.41, "Brukskonto", "NOK", konto1transaksjoner);

        when(sjekk.loggetInn()).thenReturn(konto1.getPersonnummer());
        when(repository.endreKonto(any(Konto.class))).thenReturn("OK");

        //act
        String resultat = kontoController.endreKonto(konto1);

        //assert
        assertEquals("OK", resultat);
    }

    @Test
    public void test_endreKontoFeil(){
        //arrange
        List<Transaksjon> konto1transaksjoner = new ArrayList<>();
        Konto konto1 = new Konto("05068924604", "41925811793",
                13495.41, "Brukskonto", "NOK", konto1transaksjoner);

        when(sjekk.loggetInn()).thenReturn(null);

        //act
        String resultat = kontoController.endreKonto(konto1);

        //assert
        assertEquals("Ikke innlogget", resultat);
    }

    @Test
    public void test_slettKontoOK(){
        //arrange
        List<Transaksjon> konto1transaksjoner = new ArrayList<>();
        Konto konto1 = new Konto("05068924604", "41925811793",
                13495.41, "Brukskonto", "NOK", konto1transaksjoner);

        when(sjekk.loggetInn()).thenReturn(konto1.getPersonnummer());
        when(repository.slettKonto("05068924604")).thenReturn("OK");

        //act
        String resultat = kontoController.slettKonto(konto1.getPersonnummer());

        //assert
        assertEquals("OK", resultat);
    }

    @Test
    public void test_slettKontoFeil(){
        //arrange
        List<Transaksjon> konto1transaksjoner = new ArrayList<>();
        Konto konto1 = new Konto("05068924604", "41925811793",
                13495.41, "Brukskonto", "NOK", konto1transaksjoner);

        when(sjekk.loggetInn()).thenReturn(null);

        //act
        String resultat = kontoController.slettKonto(konto1.getPersonnummer());

        //assert
        assertEquals("Ikke innlogget", resultat);
    }

}
