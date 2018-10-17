#include <stdio.h>
#include <unistd.h>
#include <string.h>

/*	
 *	Clean Code verzió: Bogacsovics Gergő
 *	Tutor: Vincze Tamás Zoltán
 *
 */

/* A kulcs maximum méretének illetve a buffer méretének beállítása. */
#define MAX_KULCS 100
#define BUFFER_MERET 256


int
main (int argc, char **argv)
{

  /* A töréshez szükséges kulcs. */
  char kulcs[MAX_KULCS];
  /*Ebben a változóban tároljuk az eredményt majd. */
  char buffer[BUFFER_MERET];

  /* A program során a kulcs_index mutatja majd az aktuális elemét a kulcsnak, amivel végre szeretnénk majd hajtani a műveletet,*
  /* az olvasott_bajtok pedig megmondja majd, hány bájtot olvastunk be összesen. 						*/
  int kulcs_index = 0;
  int olvasott_bajtok = 0;

  /* A kulcs méretét beállítjuk: a (program neve utáni) 1. prancssori argumentum lesz a kulcs: ennek méretét rögzítjük az strlen függvény segítségével. */
  int kulcs_meret = strlen (argv[1]);
  strncpy (kulcs, argv[1], MAX_KULCS);

  /* Amíg tudunk olvasni, addig fut a ciklus. */
  /* man read -> a read dokumentációja */
  /* Röviden: amíg tudunk olvasni a bemenetről, addig olvasunk és tároljuk azt a bufferben. Azonban ha már nem tudunk több adatot beolvasni, a read függvény *
  /* 0 értéket ad vissza, és a ciklus véget ér.														    */
  while ((olvasott_bajtok = read (0, (void *) buffer, BUFFER_MERET)))
    {

      /* Végigmegyünk a beolvasott bájtokon, és elvégezzük a titkosítást. */
      for (int i = 0; i < olvasott_bajtok; ++i)
	{

	  /* XOR alkalmazása. */
	  buffer[i] = buffer[i] ^ kulcs[kulcs_index];
          /* A kulcs indexének aktualizálása. */
	  kulcs_index = (kulcs_index + 1) % kulcs_meret;

	}

      /* Kiírjuk az eredményt. */
      write (1, buffer, olvasott_bajtok);

    }
}

