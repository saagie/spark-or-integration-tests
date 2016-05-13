SparkOR Integration Tests
========
This repository aims at realizing the integration and validation tests for the library [SparkOR](https://github.com/saagie/spark-or).

How to build the tests
========================
The following steps are needed to build the tests:
- Install lp-solve
- Download lp_solve_5.5.2.0_java.zip on https://sourceforge.net/projects/lpsolve/files/lpsolve/5.5.2.0/
- Copy lib/ux64/liblpsolve55j.so and lpsolve55j.jar in a lib/ directory
- Add LD_LIBRARY_PATH=/usr/lib/lp_solve to the environment variables

Wiki
=====
The Wiki is accessible [here](https://github.com/saagie/spark-or-integration-tests/wiki)

It contains a lot of information including :
  * the [architecture](https://github.com/saagie/spark-or-integration-tests/wiki/Architecture) of SparkOR Integration Tests
  * how to use the library to make you own [tests]()
