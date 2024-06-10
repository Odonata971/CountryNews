# Country News by [Florian FABRE](https://github.com/Odonata971)

Country News is an Android application that provides news based on the user's selected country. It uses Room for local data storage and Coroutines for asynchronous operations.

## Features

- User authentication
- Display country informations based on selected country
- Add and remove countries from favourites
- Search countries by name

## Tech Stack

- Kotlin
- Room for local data persistence
- Coroutines for asynchronous operations

## Setup

1. Clone the repository
```bash
git clone https://github.com/Odonata971/CountryNews.git
```

2. Open the project in Android Studio Jellyfish | 2023.3.1 Patch 1
  
3. Build and run the project on an emulator or real device

## Project Structure

The project follows the Clean Architecture and MVVM design patterns. Here's a brief overview of the main directories:

- `app/src/main/java/com/florianfabre/countrynews`: Contains the main application code.
  - `data`: Contains the data layer of the application, which includes the following subdirectories:
    - `container`: Contains dependency injection setup and components.
    - `db`: Contains the Room database setup and DAO interfaces.
    - `api`: Contains the API interfaces and network-related classes.
    - `model`: Contains the data models/entities used in the project.
    - `repository`: Contains the repository classes that provide methods for interacting with the data layer.
  - `ui`: Contains the UI layer of the application, which includes the following subdirectories:
    - `countriesRelated`: Contains the Activity, Fragment, and ViewModel classes related to countries.
    - `userRelated`: Contains the Activity, Fragment, and ViewModel classes related to user operations.
    - `navigation`: Contains the navigation graph and related classes.
  - `utilities`: Contains utility classes and methods.
- `app/src/main/res`: Contains the resources for the application, such as drawables, layout files, and values.

## Images

You can find there all the images of the app in this link [Images](https://github.com/vives-android/23-24-final-assignment-sem-2-Odonata971/tree/main/app/src/main/java/com/florianfabre/countrynews/images) 
