# tensorflowmemorytest
Tests a memory leak issue in TensorFlow

## Usage
You need to have maven installed then just do:

```mvn test```

Open a process monitor (like Activity Monitor on Mac, or top on Linux) and see if the java process's memory grows and grows.

## Systems observed with memory leak
So far it's observed to grow memory on 
- Mac OS Sierra with TensorFlow 1.1 (the one from Maven) on Oracle Java 8 (1.8.0_51)
- CentOS 7 on Docker on Open JDK 8 (1.8.0.131) with both TensorFlow 1.1 (from Maven) and 1.2_rc0
