#SparkOR Integration Tests

This repository aims at realizing the integration and validation tests for the library [SparkOR](https://github.com/saagie/spark-or).

##How to run the tests

The following steps are needed to build the tests:
- Clone the repository.
- Create a lib subdirectory inside of the cloned repository if it does not exist.
- Install lp-solve (apt-get install lp-solve on Ubuntu).
- Download lp_solve_5.5.2.0_java.zip on https://sourceforge.net/projects/lpsolve/files/lpsolve/5.5.2.0/
- Extract lib/ux64/liblpsolve55j.so and lpsolve55j.jar into the lib directory you previously created.
- Add the directory containing liblpsolve55.so to your library path (on Ubuntu: export LD_LIBRARY_PATH=/usr/lib/lp_solve/
- Build the tests using sbt compile
- Run the tests using sbt run (or sbt "run --disable-lpsolve" if you do not want to use lpsolve).

##Wiki

The Wiki is accessible [here](https://github.com/saagie/spark-or-integration-tests/wiki)

It contains a lot of information including :
  * the [architecture](https://github.com/saagie/spark-or-integration-tests/wiki/Architecture) of SparkOR Integration Tests
  * how to use the library to make you own [tests]()
