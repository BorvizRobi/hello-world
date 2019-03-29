#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>

typedef struct binfa
{
  int ertek;
  struct binfa *bal_nulla;
  struct binfa *jobb_egy;

} BINFA, *BINFA_PTR;

BINFA_PTR
uj_elem ()
{
  BINFA_PTR p;

  if ((p = (BINFA_PTR) malloc (sizeof (BINFA))) == NULL)
    {
      perror ("memoria");
      exit (EXIT_FAILURE);
    }
  return p;
}

extern void kiir (BINFA_PTR elem);
extern void szabadit (BINFA_PTR elem);

int
main (int argc, char **argv)
{
  char b;

  BINFA_PTR gyoker = uj_elem ();
  gyoker->ertek = '/';
  BINFA_PTR fa = gyoker;

  while (read (0, (void *) &b, 1))
    {
      write (1, &b, 1);
      if (b == '0')
	{
	  if (fa->bal_nulla == NULL)
	    {
	      fa->bal_nulla = uj_elem ();
	      fa->bal_nulla->ertek = 0;
	      fa->bal_nulla->bal_nulla = fa->bal_nulla->jobb_egy = NULL;
	      fa = gyoker;
	    }
	  else
	    {
	      fa = fa->bal_nulla;
	    }
	}
      else
	{
	  if (fa->jobb_egy == NULL)
	    {
	      fa->jobb_egy = uj_elem ();
	      fa->jobb_egy->ertek = 1;
	      fa->jobb_egy->bal_nulla = fa->jobb_egy->jobb_egy = NULL;
	      fa = gyoker;
	    }
	  else
	    {
	      fa = fa->jobb_egy;
	    }
	}
    }

  printf ("\n");
  kiir (gyoker);
  extern int max_melyseg;
  printf ("melyseg=%d", max_melyseg);
  szabadit (gyoker);
}

static int melyseg = 0;
int max_melyseg = 0;

void
kiir (BINFA_PTR elem)
{
  if (elem != NULL)
    {
      ++melyseg;
      if (melyseg > max_melyseg)
	max_melyseg = melyseg;
      kiir (elem->jobb_egy);
      for (int i = 0; i < melyseg; ++i)
	printf ("---");
      printf ("%c(%d)\n", elem->ertek < 2 ? '0' + elem->ertek : elem->ertek,
	      melyseg);
      kiir (elem->bal_nulla);
      --melyseg;
    }
}

void
szabadit (BINFA_PTR elem)
{
  if (elem != NULL)
    {
      szabadit (elem->jobb_egy);
      szabadit (elem->bal_nulla);
      free (elem);
    }
}
