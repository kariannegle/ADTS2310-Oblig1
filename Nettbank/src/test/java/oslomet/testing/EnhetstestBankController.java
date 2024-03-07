package oslomet.testing;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import oslomet.testing.API.BankController;
import oslomet.testing.DAL.BankRepository;
import oslomet.testing.Models.Konto;
import oslomet.testing.Models.Kunde;
import oslomet.testing.Models.Transaksjon;
import oslomet.testing.Sikkerhet.Sikkerhet;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class EnhetstestBankController {

    @InjectMocks
    // denne skal testes
    private BankController bankController;

    @Mock
    // denne skal Mock'es
    private BankRepository repository;

    @Mock
    // denne skal Mock'es
    private Sikkerhet sjekk;

    // initDB
    @Test
    public void test_initDB(){
        when(repository.initDB(any())).thenReturn("OK");
        String resultat = bankController.initDB();
        assertEquals("OK", resultat);
    }

    //hentAlleKonti
    @Test
    public void hentKundeInfo_loggetInn() {

        // arrange
        Kunde enKunde = new Kunde("01010110523",
                "Lene", "Jensen", "Askerveien 22", "3270",
                "Asker", "22224444", "HeiHei");

        when(sjekk.loggetInn()).thenReturn("01010110523");

        when(repository.hentKundeInfo(anyString())).thenReturn(enKunde);

        // act
        Kunde resultat = bankController.hentKundeInfo();

        // assert
        assertEquals(enKunde, resultat);
    }

    @Test
    public void hentKundeInfo_IkkeloggetInn() {

        // arrange
        when(sjekk.loggetInn()).thenReturn(null);

        //act
        Kunde resultat = bankController.hentKundeInfo();

        // assert
        assertNull(resultat);
    }

    @Test
    public void hentKonti_LoggetInn()  {
        // arrange
        List<Konto> konti = new ArrayList<>();
        Konto konto1 = new Konto("105010123456", "01010110523",
                720, "Lønnskonto", "NOK", null);
        Konto konto2 = new Konto("105010123456", "12345678901",
                1000, "Lønnskonto", "NOK", null);
        konti.add(konto1);
        konti.add(konto2);

        when(sjekk.loggetInn()).thenReturn("01010110523");

        when(repository.hentKonti(anyString())).thenReturn(konti);

        // act
        List<Konto> resultat = bankController.hentKonti();

        // assert
        assertEquals(konti, resultat);
    }

    @Test
    public void hentKonti_IkkeLoggetInn()  {
        // arrange

        when(sjekk.loggetInn()).thenReturn(null);

        // act
        List<Konto> resultat = bankController.hentKonti();

        // assert
        assertNull(resultat);
    }

    @Test
    public void hentSaldi_LoggetInn(){

        List<Konto> konti = new ArrayList<>();
        Konto konto1 = new Konto("105010123456", "01010110523",
                720, "Lønnskonto", "NOK", null);
        Konto konto2 = new Konto("105010123456", "12345678901",
                1000, "Lønnskonto", "NOK", null);
        konti.add(konto1);
        konti.add(konto2);

        when(sjekk.loggetInn()).thenReturn("105010123456");

        when(repository.hentSaldi(anyString())).thenReturn(konti);

        List<Konto> resultat = bankController.hentSaldi();

        assertEquals(konti,resultat);

    }


    @Test
    public void hentSaldi_ikkeLoggetInn(){

        when(sjekk.loggetInn()).thenReturn(null);

        // act
        List<Konto> resultat = bankController.hentSaldi();

        // assert
        assertNull(resultat);
    }

    @Test
    public void hentTransaksjoner_OK(){
        //arrage
        List<Transaksjon> transaksjoner= new ArrayList<>();

        Transaksjon tr1 = new Transaksjon(1,"01010110523666",120.0,"29022024","Betaling","Avventer","01010110523");
        Transaksjon tr2 = new Transaksjon(2,"01010110523666",120.0,"29022024","Betaling","Avventer","010101105");

        transaksjoner.add(tr1);
        transaksjoner.add(tr2);

        List <Konto> konti = new ArrayList<>();
        Konto konto1 = new Konto("105010123456", "01010110523",
                720, "Lønnskonto", "NOK", transaksjoner);
        konti.add(konto1);
        konto1.setTransaksjoner(transaksjoner);

        when(sjekk.loggetInn()).thenReturn("105010123456");

        when(repository.hentTransaksjoner(anyString(), anyString(), anyString())).thenReturn(konto1);

        //act
        Konto resultat = bankController.hentTransaksjoner("01010110523", "29022024", "29022024");

        //assert
        assertEquals(konto1, resultat);
    }

    @Test
    public void hentTransaksjoner_ikkeOK(){
        Konto konto1 = new Konto("105010123456", "01010110523",
                720, "Lønnskonto", "NOK", null);

        when(sjekk.loggetInn()).thenReturn(null);

        //act
        Konto resultat = bankController.hentTransaksjoner(null, null, null);
        assertNull(resultat);
    }

    @Test
    public void hentBetaling_OK(){
        //arrage
        List<Transaksjon> transaksjoner= new ArrayList<>();
        Transaksjon transaksjon = new Transaksjon(1,"01010110523666",120.0,"29022024","Betaling","Avventer","01010110523");
        transaksjoner.add(transaksjon);

        Konto konto1 = new Konto("105010123456", "01010110523",
                720, "Lønnskonto", "NOK", transaksjoner);

        when(sjekk.loggetInn()).thenReturn("105010123456");
        when(repository.hentBetalinger(konto1.getPersonnummer())).thenReturn(transaksjoner);

        //act
        List<Transaksjon> resultat = bankController.hentBetalinger();

        //assert
        assertEquals(transaksjoner, resultat);
    }

    @Test
    public void hentBetaling_ikkeOK(){
        //arrage
        when(sjekk.loggetInn()).thenReturn(null);

        //act
        List<Transaksjon> resultat = bankController.hentBetalinger();

        //assert
        assertNull(resultat);
    }


    @Test
    public void registrerBetaling_OK(){
        //arrange
        List<Transaksjon> konto1transaksjoner = new ArrayList<>();
        Transaksjon enTransaksjon = new Transaksjon(1,"01010110523666",120.0,"29022024","Betaling","Avventer","01010110523");
        konto1transaksjoner.add(enTransaksjon);
        Konto konto1 = new Konto("105010123456", "01010110523",
                720, "Lønnskonto", "NOK", konto1transaksjoner);

        when(sjekk.loggetInn()).thenReturn("105010123456");
        when(repository.registrerBetaling(enTransaksjon)).thenReturn("OK");

        //act
        String resultat = bankController.registrerBetaling(enTransaksjon);

        //assert
        assertEquals("OK", resultat);
    }

    @Test
    public void registrerBetaling_ikkeOK(){
        //arrange
        Transaksjon enTransaksjon = new Transaksjon(1,"01010110523666",120.0,"29022024","Betaling","Avventer","01010110523");
        when(sjekk.loggetInn()).thenReturn(null);

        //act
        String resultat = bankController.registrerBetaling(enTransaksjon);

        //assert
        assertEquals("Feil", resultat);
    }

    @Test
    public void utforBetaling_OK(){
        //arrange
        List<Transaksjon> betalinger = new ArrayList<>();
        Transaksjon enTransaksjon = new Transaksjon(1,"01010110523666",120.0,"29022024","Betaling","Avventer","01010110523");
        betalinger.add(enTransaksjon);

        Konto konto = new Konto ("105010123456", "01010110523", 720, "Lønnskonto", "NOK", betalinger);

        konto.setTransaksjoner(betalinger);

        when(sjekk.loggetInn()).thenReturn(konto.getPersonnummer());

        when(repository.utforBetaling(enTransaksjon.getTxID())).thenReturn("OK");
        when(repository.hentBetalinger(anyString())).thenReturn(betalinger);

        //act
        List<Transaksjon> resultat = bankController.utforBetaling(enTransaksjon.getTxID());

        //assert
        assertEquals(betalinger, resultat);
    }

    @Test
    public void utforBetaling_ikkeOK() {
        //arrange
        List<Transaksjon> betaling = new ArrayList<>();
        Transaksjon enTransaksjon = new Transaksjon(1, "01010110523666", 120.0, "29022024", "Betaling", "Avventer", "01010110523");
        Konto konto = new Konto("105010123456", "01010110523", 720, "Lønnskonto", "NOK", betaling);

        when(sjekk.loggetInn()).thenReturn(null);

        //act
        List<Transaksjon> resultat = bankController.utforBetaling(enTransaksjon.getTxID());

        //assert
        assertNull(resultat);
    }

    @Test
    public void hentKundeInfo(){
        //arrange
        Kunde kunde = new Kunde("01010110523", "Lene", "Jensen", "Askerveien 22", "3270", "Asker", "22224444", "HeiHei");

        when(sjekk.loggetInn()).thenReturn(kunde.getPersonnummer());
        when(repository.hentKundeInfo(anyString())).thenReturn(kunde);

        //act
        Kunde resultat = bankController.hentKundeInfo();

        //assert
        assertEquals(kunde, resultat);
    }

    @Test
    public void hentKundeInfo_ikkeOK(){
        //arrange
        when(sjekk.loggetInn()).thenReturn(null);

        //act
        Kunde resultat = bankController.hentKundeInfo();

        //assert
        assertNull(resultat);
    }

    @Test
    public void endre_OK(){
        //arrange
        Kunde kunde = new Kunde("01010110523", "Lene", "Jensen", "Askerveien 22", "3270", "Asker", "22224444", "HeiHei");

        when(sjekk.loggetInn()).thenReturn(kunde.getPersonnummer());
        when(repository.endreKundeInfo(any(Kunde.class))).thenReturn(kunde.getPersonnummer());

        //act
        String resultat = bankController.endre(kunde);

        //assert
        assertEquals("OK", resultat);
    }

    @Test
    public void endre_ikkeOK(){
        //arrange
        Kunde kunde = new Kunde("01010110523", "Lene", "Jensen", "Askerveien 22", "3270", "Asker", "22224444", "HeiHei");

        when(sjekk.loggetInn()).thenReturn(null);

        //act
        String resultat = bankController.endre(kunde);

        //assert
        assertEquals("Feil", resultat);
    }
}

