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

        when(sjekk.loggetInn()).thenReturn("105010123456");
        when(repository.hentBetaling(konto1.getPersonnummer())).thenReturn(transaksjoner);

        //act
        List<Transaksjon> resultat = bankController.hentBetaling();

        //assert
        assertEquals(transaksjoner, resultat);
    }

    @Test
    public void hentBetaling_ikkeOK(){
        //arrage
        when(sjekk.loggetInn()).thenReturn(null);

        //act
        List<Transaksjon> resultat = bankController.hentBetaling();

        //assert
        assertNull(resultat);
    }


    @Test
    public void registrerBetaling_LoggetInn(){

        Transaksjon betaling1 = new Transaksjon(1,"01010110523666",120.0,"29022024","Betaling","Avventer","01010110523");

        when(sjekk.loggetInn()).thenReturn("105010123456");

        when(repository.registrerBetaling((any(Transaksjon.class)))).thenReturn("OK");

        String resultat = bankController.registrerBetaling(betaling1);

        assertEquals("OK",resultat);
    }

    @Test
    public void registrerBetaling_IkkeLoggetInn(){

        when(sjekk.loggetInn()).thenReturn(null);

        String resultat = bankController.registrerBetaling(null);

        assertNull(resultat, "Logg inn for å registrere betaling");


    }


}

