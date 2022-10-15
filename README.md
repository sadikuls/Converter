# Currency Converter

## Overview of the project
 - Convert currency based on API data
 - Sync data every 5 sec.
 - Smooth UI
 - Offline using capability.
 - Flexible commission fee applicable.

Data:-
 - Currently all data getting from API.
 - Cache data to Room db after retriving from server.

## Application data flow (Clean Architecture)
The app has 4 layer
1. View(Presentation layer).
2. ViewModel
3. Repository(Model)
4. Use Case

The Internal machanism of the application is given bellow.

**Presentation Layout ->(Request) -> ViewModel -> (Request)-> (UseCase) -> Repository -> got data from(Server/Asset)->store data -> RoomDb -> observe db -> Presentation Layout**

## Technology used

- **Kotlin** -> 100% Kotlin used for this project.
- **MVVM Clean Architecture** -> To write testable and decoupled code Clean architecture has used in this project.
- **Retrofit** -> For networking related task (Added but not being used)
- **Room** -> for store persistance data
- **Coroutine** -> for background/long running task
- **Dagger-hilt** -> Dependency injection
- **GSON** -> json parser/mapper
- **View-Binding** -> for bind view and set data
- **Unit Test**

## Project architecture

## Followed MVVM-Clean Architecture pattern by following DRY principle
 - **core** -> di,extensions
 - **data** -> localdb,preference
 - **domain** -> adapter,repository,usecase
 - **presentation** -> ui part
 
## UnitTests has been included for all business logic

