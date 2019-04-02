#include <iostream> 
#include <cstdlib>
#include <cmath>
#include <ctime>

class PolarGen
{
public:
  PolarGen()
  {
	nincsTarolt = true;
	std::srand (std::time(NULL));
  }
  ~PolarGen()
  {
  }
  double kovetkezo();

private:
  bool nincsTarolt;
  double tarolt;

};



double PolarGen::kovetkezo ()
{
  if (nincsTarolt)
  {
	double u1, u2, v1, v2, w;
        do
	  {
	    u1= std::rand() / (RAND_MAX +1.0);
	    u2= std::rand() / (RAND_MAX +1.0);
	    v1=2*u1-1;
	    v2=2*u1-1;
	    w=v1*v1+v2*v2;
           }
	while (w>1);

	double r =std::sqrt ((-2 * std::log(w)) /w);

	tarolt=r*v2;
	nincsTarolt =!nincsTarolt;

	return r* v1;
   }

   else
   {
	nincsTarolt =!nincsTarolt;
	return tarolt;
   }
}

int main (int argc, char **argv)
{
	PolarGen pg;

	for (int i= 0; i<10;++i)
	  std::cout<<pg.kovetkezo ()<< std::endl;

	return 0;
}
