# N-Bodies simulation in java
Second assignment of the "Concurrent and Distributed Programming" course.
Link to the actual assignment [here](https://docs.google.com/document/d/1u0YI-9BbNr3gvVvLpMMYGHXAVRbPvz3RVrjej-He9zw/edit).

# How to run JPF
Remember to use at least 2 threads to run JPF. Don't use more than 2 bodies and 2 steps.

Run the JPF container from the root directory of the project.
```
docker run -v $PWD:/home -it gianlucaaguzzi/pcd-jpf:latest /bin/bash
```

Build the project and run JPF.
```
./gradlew build
java -jar $JPF app/src/main/java/nbodies/TestLiveness.jpf
```
