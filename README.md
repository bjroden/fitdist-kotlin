# Fitdist-Kotlin

This library is a package for performing statistical distribution fitting.
The goal is to provide similar functionality to the 
[fitdistrplus](https://cran.r-project.org/web/packages/fitdistrplus/index.html)
library. This library supports estimating distribution parameters, performing
goodness-of-fit tests, ranking goodness-of-fit tests on a common scale, and
creating data needed for visual representations such as P-P plots and Q-Q 
plots.

The API uses the [Kotlin Simulation Library](https://github.com/rossetti/KSL)
for the distribution classes that are necessary for all of the above 
functionality.

## Running

This project uses the [gradle](https://gradle.org/) build system. If you have
gradle installed, either manually or through an IDE, that can be used for 
building and testing the library. Alternatively, the gradlew script can be
used without installing gradle (gradlew.bat on windows, gradlew on *nix).

For IntelliJ users, the "JUnit Tests" run configuration is provided for 
running the test suite. 

If using gradlew, the library can be built with:

    ./gradlew build

And the unit tests can be run with:

    ./gradlew test --info

The API documentation is provided through KDoc. An HTML view can be generated
using [dokka](https://github.com/Kotlin/dokka). This can be done using the 
gradle "dokkaHtml" task:

    ./gradlew dokkaHtml
    
This will generate the documentation in the build/dokka directory.