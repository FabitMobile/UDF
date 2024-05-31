# UDF - Unidirectional Data Flow
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
* Store coroutines - kotlin + Coroutines Flow store implementation

`implementation 'com.github.FabitMobile.udf:store-coroutines:{latest-version}'`

* Store RxJava - kotlin + RxJava2 store implementation

`implementation 'com.github.FabitMobile.udf:store-rxjava:{latest-version}'`

* ViewController Compose - for Android Compose with coroutines

`implementation 'com.github.FabitMobile.udf:viewcontroller-compose:{latest-version}'`

* ViewController View RxJava - for Android View with RxJava2

`implementation 'com.github.FabitMobile.udf:viewcontroller-view-rxjava:{latest-version}'`
