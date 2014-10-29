#include <mpi.h>
#include <iostream>

using namespace std;

const int size = 10;

float a[size][size];
float b[size][size];
float c[size][size];

void multiply(int istart, int iend)
{
    for (int i = istart; i <= iend; ++i) {
        for (int j = 0; j < size; ++j) {
            for (int k = 0; k < size; ++k) {
                c[i][j] += a[i][k] * b[k][j];
            }
        }
    }
}

int main(int argc, char* argv[])
{
    int rank, nproc;
    int istart, iend;

    MPI_Init(&argc, &argv);
    MPI_Comm_size(MPI_COMM_WORLD, &nproc);
    MPI_Comm_rank(MPI_COMM_WORLD, &rank);

    if (rank == 0) {
        // Initialize buffers.
        for (int i = 0; i < size; ++i) {
            for (int j = 0; j < size; ++j) {
                a[i][j] = i*size+j;
                b[i][j] = i*size+j;
                c[i][j] = 0.0f;

            }
        }
    }


/*
	if(rank == 0)
	{

		cout<<"MATRIZ A"<<endl;
		for (int i = 0; i < size; ++i)
		{
			for (int j = 0; j < size; ++j)
		    	{
		        	cout<<a[i][j]<<" ";
		    	}
			cout<<endl;
	        }

		cout<<endl;
		
	}
	
	if(rank == 0)
	{
		cout<<"MATRIX B"<<endl;
		for (int i = 0; i < size; ++i)
		{
			for (int j = 0; j < size; ++j)
		    	{
		        	cout<<b[i][j]<<" ";
		    	}
			cout<<endl;
	        }
		
	}

*/	

    MPI_Bcast(a, size*size, MPI_FLOAT, 0,MPI_COMM_WORLD);
    MPI_Bcast(b, size*size, MPI_FLOAT, 0,MPI_COMM_WORLD);
    MPI_Bcast(c, size*size, MPI_FLOAT, 0,MPI_COMM_WORLD);

    istart = (size / nproc) * rank;
    iend = (size / nproc) * (rank + 1) - 1;

    
    multiply(istart, iend);

    MPI_Gather(c + (size/nproc*rank),
               size*size/nproc,
               MPI_FLOAT,
               c + (size/nproc*rank),
               size*size/nproc,
               MPI_FLOAT,
               0,
               MPI_COMM_WORLD);

    if (rank == 0) {

        if (size % nproc > 0) {
            multiply((size/nproc)*nproc, size-1);
        }
    }

/*

	if(rank == 0)
	{

		cout<<"RESULT"<<endl;
		for (int i = 0; i < size; ++i)
		{
			for (int j = 0; j < size; ++j)
		    	{
		        	cout<<c[i][j]<<" ";
		    	}
			cout<<endl;
	        }
		
	}
*/

    MPI_Finalize();
    return 0;
}
