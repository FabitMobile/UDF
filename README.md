# UDF - Unidirectional Data Flow
[![](https://www.jitpack.io/v/FabitMobile/udf.svg)](https://www.jitpack.io/#FabitMobile/udf)

Check [wiki](https://github.com/FabitMobile/UDF/wiki/Core) or [example](https://github.com/FabitMobile/UDF/tree/main/example-app) for more information 
## Download
Library is distributed through JitPack

#### Add repository in the root build.gradle
```
allprojects {
 repositories {
    maven { url "https://jitpack.io" }
 }
}
```

#### Add required modules:

* ViewController Coroutines - for Android View with coroutines

`implementation 'com.github.FabitMobile.udf:viewcontroller-coroutines:{latest-version}'`

* ViewController Compose - for Compose with coroutines

`implementation 'com.github.FabitMobile.udf:viewcontroller-compose:{latest-version}'`

* ViewController View RxJava - for Android View with RxJava2

`implementation 'com.github.FabitMobile.udf:viewcontroller-view-rxjava:{latest-version}'`

#### Store dependencies only if not using viewController:

* Store coroutines - kotlin + Coroutines Flow store implementation

`implementation 'com.github.FabitMobile.udf:store-coroutines:{latest-version}'`

* Store RxJava - kotlin + RxJava2 store implementation

`implementation 'com.github.FabitMobile.udf:store-rxjava:{latest-version}'`
