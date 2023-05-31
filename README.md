# ChatApp

## Description

A messaging application, a mobile client for the Zulip API.

## Features

- [X] Single Activity
- [X] Multi-module architecture
- [X] Light and Dark theme
- [X] Ability to create your own channel ("+" button on the screen with
  channels `Channels Fragment`)
- [X] The ability to open messages for both the channel (**long tap** on the channel
  in `Channels Fragment`) and the
  topic (click on the topic in `Channels Fragment`)
- [X] The ability to write to different topics (only from the **chat channel** in `ChatFragment`)
- [X] Ability to go to the channel topic from **channel chat** to `ChatFragment` (click on the
  topic)
- [X] The ability to put a reaction, delete, edit and copy a message (long tap on
  message in `Chat Fragment`)

## Requirements

To fully work with the Zulip API used in the application, an API key is required.
[How to get an API key](https://zulip.com/api/api-keys ).
After receiving the key, you need to add the following lines to the `local.properties` file:

```
username=<логин>
password=<API-ключ>
base_url=<URL>
```

## Arhitecture

The application follows the principles of pure architecture, the representation layer implements the
UDF architecture
using the `Elmslie' library.
****Example** implementation of the `feature:channel` module

![ChatApp-Architecture](../images/Acrhitecture.png)

## Modularization

| Модуль             | Тип модуля          | Описание                                                                                                                                                                                          |
|--------------------|---------------------|---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `:app`             | Android Application | Combines everything necessary for the correct operation of the application.                                                                                                                       |
| `core:common`      | Java/Kotlin Library | Common classes, for use by other modules.                                                                                                                                                         |
| `core:components`  | Android Library     | UI components, extension functions, base classes.                                                                                                                                                 |
| `core:network`     | Android Library     | API for processing network requests and responses from a remote data source.                                                                                                                      |
| `core:database`    | Android Library     | Implementation of a local database using the 'Room` library.                                                                                                                                      |
| `core:auth`        | Android Library     | Classes responsible for authorization logic.                                                                                                                                                      |
| `core:ui`          | Android Library     | Resources used by modules.                                                                                                                                                                        |
| `feature:channels` | Android Library     | Displaying channels using `Channel Fragment` and `HolderChannelFragment`, the ability to add a channel (`AddChannelFragment').                                                                    |
| `feature:chat`     | Android Library     | Displaying channel/topic messages (`ChatFragment'), the ability to change the message (`Action Fragment` and `EditMessageFragment`), choosing an emoji to add a reaction (`SelectEmojiFragment`). |
| `feature:profile`  | Android Library     | Displaying the user profile using the `Profile Fragment'.                                                                                                                                         |
| `feature:users`    | Android Library     | Displaying information about all users in the organization using the `Users Fragment'                                                                                                             |

## Технологии

- **Coroutines, Flow** - Asynchronous operation.
- **-**Retrofit 2** - Requests to the network.
- **- **Ok Http** - Requests to the network.
- **-**Kotlin Serialization** - Serialization of data.
- **-**Co il** - Uploading images over the network, caching downloaded images.
- **Jetpack Paging 3** - Loading data in small parts.
- **Android Navigation Component** - Navigation through the application.
- **ViewBinding** - Improvement and optimization of the code associated with the view layer.
- **-**Shimmer** - Data loading display.
- **Dagger2** - Dependency injection.
- **Elmslie** - Simplification of the implementation of the ELM architecture.
- **Room** - Abstraction layer on top of SQLite, allowing free access to
  the database.
- **DataStore** - Storing data in the form of key-value pairs using Coroutines and Flow.
- **Timber** is a Logger that provides a utility on top of the usual Log class.
- **Leak Canary** - Detection of memory leaks.

## Screenshots

### Light theme

![![ChatApp-Light Theme](../images/screenshots_light_theme.png)

### Dark theme

![![ChatApp-DarkTheme](../images/screenshots_dark_theme.png)

