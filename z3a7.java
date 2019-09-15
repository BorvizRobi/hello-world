
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.FileNotFoundException;

class LZWBinFa
{

     public LZWBinFa ()
    {
	gyoker=new Csomopont('/');
	fa=gyoker;    
}


    void add (char b)
    {
        // Mit kell betenni éppen, '0'-t?
        if (b == '0')
        {
            /* Van '0'-s gyermeke az aktuális csomópontnak?
           megkérdezzük Tőle, a "fa" mutató éppen reá mutat */
            if (fa.nullasGyermek ()==null)	// ha nincs, hát akkor csinálunk
            {
                // elkészítjük, azaz páldányosítunk a '0' betű akt. parammal
                Csomopont uj = new Csomopont ('0');
                // az aktuális csomópontnak, ahol állunk azt üzenjük, hogy
                // jegyezze már be magának, hogy nullás gyereke mostantól van
                // küldjük is Neki a gyerek címét:
                fa.ujNullasGyermek (uj);
                // és visszaállunk a gyökérre (mert ezt diktálja az alg.)
                fa = gyoker;
            }
            else			// ha van, arra rálépünk
            {
                // azaz a "fa" pointer már majd a szóban forgó gyermekre mutat:
                fa = fa.nullasGyermek ();
            }
        }
        // Mit kell betenni éppen, vagy '1'-et?
        else
        {
            if (fa.egyesGyermek ()==null)
            {
                Csomopont uj = new Csomopont ('1');
                fa.ujEgyesGyermek (uj);
                fa = gyoker;
            }
            else
            {
                fa = fa.egyesGyermek ();
            }
        }
    }
    /* A bejárással kapcsolatos függvényeink (túlterhelt kiir-ók, atlag, ratlag stb.) rekurzívak,
     tk. a rekurzív fabejárást valósítják meg (lásd a 3. előadás "Fabejárás" c. fóliáját és társait)
     (Ha a rekurzív függvénnyel általában gondod van => K&R könyv megfelelő része: a 3. ea. izometrikus
     részében ezt "letáncoltuk" :) és külön idéztük a K&R álláspontját :)
   */

public int getMelyseg ()
{
    melyseg = maxMelyseg = 0;
    rmelyseg (gyoker);
    return maxMelyseg - 1;
}

public double getAtlag ()
{
    melyseg = atlagosszeg = atlagdb = 0;
    ratlag (gyoker);
    atlag = ((double) atlagosszeg) / atlagdb;
    return atlag;
}

public double getSzoras ()
{
    atlag = getAtlag ();
    szorasosszeg = 0.0;
    melyseg = atlagdb = 0;

    rszoras (gyoker);

    if (atlagdb - 1 > 0)
        szoras = Math.sqrt (szorasosszeg / (atlagdb - 1));
    else
        szoras = Math.sqrt (szorasosszeg);

    return szoras;
}

public void rmelyseg (Csomopont elem)
{
    if (elem != null)
    {
        ++melyseg;
        if (melyseg > maxMelyseg)
            maxMelyseg = melyseg;
        rmelyseg (elem.egyesGyermek ());
        // ez a postorder bejáráshoz képest
        // 1-el nagyobb mélység, ezért -1
        rmelyseg (elem.nullasGyermek ());
        --melyseg;
    }
}

public void ratlag (Csomopont  elem)
{
    if (elem != null)
    {
        ++melyseg;
        ratlag (elem.egyesGyermek ());
        ratlag (elem.nullasGyermek ());
        --melyseg;
        if (elem.egyesGyermek () == null && elem.nullasGyermek () == null)
        {
            ++atlagdb;
            atlagosszeg += melyseg;
        }
    }
}

public void rszoras (Csomopont  elem)
{
    if (elem != null)
    {
        ++melyseg;
        rszoras (elem.egyesGyermek ());
        rszoras (elem.nullasGyermek ());
        --melyseg;
        if (elem.egyesGyermek () == null && elem.nullasGyermek () == null)
        {
            ++atlagdb;
            szorasosszeg += ((melyseg - atlag) * (melyseg - atlag));
        }
    }
}

public  void kiir (PrintWriter os)
    {
        // Sokkal elegánsabb lenne (és más, a bevezetésben nem kibontandó reentráns kérdések miatt is, mert
        // ugye ha most két helyről hívják meg az objektum ilyen függvényeit, tahát ha kétszer kezd futni az
        // objektum kiir() fgv.-e pl., az komoly hiba, mert elromlana a mélység... tehát a mostani megoldásunk
        // nem reentráns) ha nem használnánk a C verzióban globális változókat, a C++ változatban példánytagot a
        // mélység kezelésére: http://progpater.blog.hu/2011/03/05/there_is_no_spoon
        melyseg = 0;
        // ha nem mondta meg a hívó az üzenetben, hogy hova írjuk ki a fát, akkor a
        // sztenderd out-ra nyomjuk
        kiir (gyoker, os);
    }
    /* már nem használjuk, tartalmát a dtor hívja
  void szabadit (void)
  {
    szabadit (gyoker.egyesGyermek ());
    szabadit (gyoker.nullasGyermek ());
    // magát a gyökeret nem szabadítjuk, hiszen azt nem mi foglaltuk a szabad tárban (halmon).
  }
  */

    /* A változatosság kedvéért ezeket az osztálydefiníció (class LZWBinFa {...};) után definiáljuk,
     hogy kénytelen légy az LZWBinFa és a :: hatókör operátorral minősítve definiálni :) l. lentebb */


    /* Vágyunk, hogy a felépített LZW fát ki tudjuk nyomni ilyenformán: std::cout << binFa;
     de mivel a << operátor is a sztenderd névtérben van, de a using namespace std-t elvből
     nem használjuk bevezető kurzusban, így ez a konstrukció csak az argfüggő névfeloldás miatt
     fordul le (B&L könyv 185. o. teteje) ám itt nem az a lényeg, hanem, hogy a cout ostream
     osztálybeli, így abban az osztályban kéne módosítani, hogy tudjon kiírni LZWBinFa osztálybelieket...
     e helyett a globális << operátort terheljük túl,
     
     a kiFile << binFa azt jelenti, hogy
     
      - tagfüggvényként: kiFile.operator<<(binFa) de ehhez a kiFile valamilyen
      std::ostream stream osztály forrásába kellene beleírni ezt a tagfüggvényt,
      amely ismeri a mi LZW binfánkat...
      
      - globális függvényként: operator<<(kiFile, binFa) és pont ez látszik a következő sorban:
     */
/*
    void kiir (std::ostream & os)
    {
        melyseg = 0;
        kiir (&gyoker, os);
    }
*/

    class Csomopont
    {
    
        /* A paraméter nélküli konstruktor az elepértelmezett '/' "gyökér-betűvel" hozza
       létre a csomópontot, ilyet hívunk a fából, aki tagként tartalmazza a gyökeret.
       Máskülönben, ha valami betűvel hívjuk, akkor azt teszi a "betu" tagba, a két
       gyermekre mutató mutatót pedig nullra állítjuk, C++-ban a 0 is megteszi. */
        Csomopont (char b)
        {
	if(b=='0' || b=='1')
 		betu=b;
	else
		betu='/';
        };

        // Aktuális csomópont, mondd meg nékem, ki a bal oldali gyermeked
        // (a C verzió logikájával műxik ez is: ha nincs, akkor a null megy vissza)
        Csomopont nullasGyermek () 
        {
            return balNulla;
        }
        // Aktuális csomópon,t mondd meg nékem, ki a jobb oldali gyermeked?
        Csomopont egyesGyermek () 
        {
            return jobbEgy;
        }
        // Aktuális csomópont, ímhol legyen a "gy" mutatta csomópont a bal oldali gyereked!
        void ujNullasGyermek (Csomopont  gy)
        {
            balNulla = gy;
        }
        // Aktuális csomópont, ímhol legyen a "gy" mutatta csomópont a jobb oldali gyereked!
        void ujEgyesGyermek (Csomopont  gy)
        {
            jobbEgy = gy;
        }
        // Aktuális csomópont: Te milyen betűt hordozol?
        // (a const kulcsszóval jelezzük, hogy nem bántjuk a példányt)
        char getBetu () 
        {
            return betu;
        }

    
        // friend class LZWBinFa; /* mert ebben a valtozatban az LZWBinFa metódusai nem közvetlenül
        // a Csomopont tagjaival dolgoznak, hanem beállító/lekérdező üzenetekkel érik el azokat */

        // Milyen betűt hordoz a csomópont
     private   char betu;
        // Melyik másik csomópont a bal oldali gyermeke? (a C változatból "örökölt" logika:
        // ha hincs ilyen csermek, akkor balNulla == null) igaz
     private   Csomopont balNulla;
     private   Csomopont jobbEgy;

    };

    /* Mindig a fa "LZW algoritmus logikája szerinti aktuális" csomópontjára mutat */
    Csomopont fa;
    // technikai
   private int melyseg, atlagosszeg, atlagdb;
    private double szorasosszeg;
    


    /* Kiírja a csomópontot az os csatornára. A rekurzió kapcsán lásd a korábbi K&R-es utalást... */
    public void kiir (Csomopont  elem, PrintWriter os)
    {
        // Nem létező csomóponttal nem foglalkozunk... azaz ez a rekurzió leállítása
        if (elem != null)
        {
            ++melyseg;
            kiir (elem.egyesGyermek (), os);
            // ez a postorder bejáráshoz képest
            // 1-el nagyobb mélység, ezért -1
            for (int i = 0; i < melyseg; ++i)
                os.print ("---");
            os.print( elem.getBetu () + "(" + (melyseg-1) + ")\n" );
            kiir (elem.nullasGyermek (), os);
            --melyseg;
        }
    }


			// ha esetleg egyszer majd kiterjesztjük az osztályt, mert
    // akarunk benne valami újdonságot csinálni, vagy meglévő tevékenységet máshogy... stb.
    // akkor ezek látszanak majd a gyerek osztályban is

    /* A fában tagként benne van egy csomópont, ez erősen ki van tüntetve, Ő a gyökér: */
   protected Csomopont gyoker;
   protected int maxMelyseg;
   protected double atlag, szoras;
public static void
usage ()
{
    System.out.println( "Usage: lzwtree in_file -o out_file");
}

public static void  main (String[] args) throws FileNotFoundException, IOException

{
 if (args.length < 3)
    {
        // ha nem annyit kapott a program, akkor felhomályosítjuk erről a júzetr:
        usage ();
        // és jelezzük az operációs rendszer felé, hogy valami gáz volt...
        return ;
    }


String inFile = args[0];

 if(!args[1].equals("-o"))
        {
        	 System.out.println(args[1]);
        	 usage ();
             return;
        }

        BufferedReader input = new BufferedReader(new FileReader(inFile));

        
        if (input == null)
        {
            System.out.println("Nem létezik");
            usage ();
            return;
        }

 PrintWriter output = new PrintWriter(args[2]);

        int b;		
        LZWBinFa binfa = new LZWBinFa();

	 while ((b = input.read ()) != -1)
        {
             if ((char)b == '1' || (char)b == '0')
                	binfa.add((char)b);

        }





        binfa.kiir(output);		

        output.print("depth " + binfa.getMelyseg () + "\n");
        output.print("mean " + binfa.getAtlag () + "\n");
        output.print("var " + binfa.getSzoras () + "\n");

        output.close ();
        input.close ();

        return;

}

};



