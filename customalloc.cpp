
#include <stddef.h>
#include <cxxabi.h>
#include <iostream>
#include <vector>

template <typename T>
struct CustomAlloc {

     using size_type = size_t;
     using value_type = T;
     using pointer = T*;
     using const_pointer = const T*;
     using reference = T&;
     using const_reference = const T&;
     using difference_type = ptrdiff_t;

     CustomAlloc() {}
     CustomAlloc ( const CustomAlloc & ) {}
     ~CustomAlloc() {}

     pointer allocate ( size_type n ) {
          int s;
          char* p=abi::__cxa_demangle ( typeid ( T ).name(),0,0, &s);
          std::cout << "Allocating "
                    << n << " objects of "
                    << n+sizeof ( T )
                    << " bytes. "
                    << typeid ( T ).name() << "=" << p
                    << std::endl;

          free ( p );
          return reinterpret_cast<T*> (
                    new char[n*sizeof ( T )] );
     }

     void deallocate ( pointer p, size_type n ) {
          delete[] reinterpret_cast<char *> ( p );
          std::cout << "Deallocating "
                    << n << " objects of "
                    << n+sizeof ( T )
                    << " bytes. "
                    << typeid ( T ).name() << "=" << p
                    << std::endl;
     }
};

int main(){

std::vector<int, CustomAlloc<int>> v;

v.push_back(1);
v.push_back(2);
v.push_back(3);
v.push_back(4);
v.push_back(5);
v.push_back(6);
v.push_back(7);
v.push_back(8);
v.push_back(9);
v.push_back(10);
v.push_back(11);
v.push_back(12);
v.push_back(13);
v.push_back(14);
v.push_back(15);

    for(int x : v){
        std::cout << x << std::endl;
    }

    return 0;

}
