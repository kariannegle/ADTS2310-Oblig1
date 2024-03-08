package oslomet.testing;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import oslomet.testing.API.AdminKundeController;
import oslomet.testing.DAL.AdminRepository;
import oslomet.testing.Models.Kunde;
import oslomet.testing.Sikkerhet.Sikkerhet;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class EnhetstestAdminKundeController {
    @InjectMocks
    private AdminKundeController adminKundeController;

    @Mock
    private AdminRepository repository;

    @Mock
    private Sikkerhet sjekk;

    @Test
    public void test_hentAlleOK() {
        //arrange
        Kunde kunde1 = new Kunde("01010110523", "Per", "Hansen", "Osloveien 82", "1234", "Oslo", "12345678", "HeiHei");
        Kunde kunde2 = new Kunde("02020210634", "Line", "Jensen", "Askerveien 2", "1235", "Asker", "92876789", "HeiHei");

        List<Kunde> kundeliste = new ArrayList<>();
        kundeliste.add(kunde1);
        kundeliste.add(kunde2);

        //sjekk om innlogget
        when(sjekk.loggetInn()).thenReturn(kunde1.getPersonnummer());

        //hent kunder fra repository
        when(repository.hentAlleKunder()).thenReturn(kundeliste);

        List<Kunde> resultat = adminKundeController.hentAlle();

        //assert
        assertEquals(kundeliste, resultat);
    }

    @Test
    public void test_hentAlleFeil(){
        when(sjekk.loggetInn()).thenReturn(null);
        List<Kunde> resultat = adminKundeController.hentAlle();
        assertNull(resultat);
    }

    @Test
    public void test_lagreKundeOK(){
        //arrange
        Kunde kunde = new Kunde("01010110523", "Per", "Hansen", "Osloveien 82", "1234", "Oslo", "12345678", "HeiHei");
        when(sjekk.loggetInn()).thenReturn(kunde.getPersonnummer());
        when(repository.registrerKunde(any(Kunde.class))).thenReturn("OK");

        String resultat = adminKundeController.lagreKunde(kunde);

        assertEquals("OK", resultat);
    }

    @Test
    public void test_lagreKundeFeil(){
        //arrange
        Kunde kunde = new Kunde("01010110523", "Per", "Hansen", "Osloveien 82", "1234", "Oslo", "12345678", "HeiHei");
        when(sjekk.loggetInn()).thenReturn(null);

        String resultat = adminKundeController.lagreKunde(kunde);

        assertEquals("Ikke logget inn", resultat);
    }

    @Test
    public void test_endreKundeOK() {
        //arrange
        Kunde enKunde = new Kunde("01010110523", "Per", "Hansen", "Osloveien 82", "1234", "Oslo", "12345678", "HeiHei");
        when(sjekk.loggetInn()).thenReturn(enKunde.getPersonnummer());
        when(repository.endreKundeInfo(any(Kunde.class))).thenReturn("OK");
        String resultat = adminKundeController.endre(enKunde);
        assertEquals("OK", resultat);
    }

    @Test
    public void test_endreKundeFeil() {
        //arrange
        Kunde enKunde = new Kunde("01010110523", "Per", "Hansen", "Osloveien 82", "1234", "Oslo", "12345678", "HeiHei");
        when(sjekk.loggetInn()).thenReturn(null);
        String resultat = adminKundeController.endre(enKunde);
        assertEquals("Ikke logget inn", resultat);
    }

    @Test
    public void test_slettKundeOK() {
        //arrange
        Kunde enKunde = new Kunde("01010110523", "Per", "Hansen", "Osloveien 82", "1234", "Oslo", "12345678", "HeiHei");
        when(sjekk.loggetInn()).thenReturn(enKunde.getPersonnummer());
        when(repository.slettKunde(any(String.class))).thenReturn("OK");
        String resultat = adminKundeController.slett(enKunde.getPersonnummer());
        assertEquals("OK", resultat);
    }

    @Test
    public void test_slettKundeFeil(){
        //arrange
        Kunde enKunde = new Kunde("01010110523", "Per", "Hansen", "Osloveien 82", "1234", "Oslo", "12345678", "HeiHei");
        when(sjekk.loggetInn()).thenReturn(null);
        String resultat = adminKundeController.slett(enKunde.getPersonnummer());
        assertEquals("Ikke logget inn", resultat);
    }


}
