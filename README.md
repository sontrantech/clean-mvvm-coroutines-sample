# A simple Android sample project

This project is written in Kotlin 1.3.71.
It was meant to show current temperature in Saigon and the average temperatures of the next days in forecast. 
It's also a sample project which implementing [Clean Architecture](https://medium.com/@dmilicic/a-detailed-guide-on-developing-android-apps-using-the-clean-architecture-pattern-d38d71e94029) and MVVM architectural pattern, using Android Architecture Components, Dagger 2, Retrofit and Coroutines in Kotlin.

## User Interface

### Weather Screen

The Weather screen has 3 parts: 
    - Loading view appears while getting weather data
    - The views which show weather data after getting data successfully from server 
    - Error message and retry button are showed in case the getting data api is failed.  

## Be excellent to each other

Let us debate these ideas vigorously, but let us be excellent to each other in the process!

While healthy debate and contributions are very welcome, trolls are not. Read the [code of conduct](code-of-conduct.md) for detailed information.

## Contributing

Feel free to join in the discussion, file issues, and i'd love to improve and help others to improve our technical knowledge.

## Attribution

All of these ideas and even some of the language are directly influenced by article:

  - [Architecting Android...Reloaded](https://fernandocejas.com/2018/05/07/architecting-android-reloaded/) - An article about architecture on Android applications, aim to improve theirs Scalability, Modularization, Testability, Independence of frameworks, UI and Databases. 

## Author

  * [Tran Xuan Son <Son Tran>](https://github.com/sontrantech/)


I'd like to thank all of the folks who have helped write this project by spreading the knowledge that i learned from.


## Further improvements
    - Feature's navigation 
    - Unit test and Instrumentation test
