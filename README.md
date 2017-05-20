# tensorflowmemorytest
Tests a memory leak issue in TensorFlow

## Usage
You need to have maven installed then just do:
mvn test

Open a process monitor (like Activity Monitor on Mac) and see if the java process's memory grows and grows.

## Systems observed with memory leak
So far it's observed to grow memory on 
- Mac OS Sierra with TensorFlow 1.1 (the one from Maven) on Java 8
