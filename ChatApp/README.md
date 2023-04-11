# TFS Android 2023

## Modularization

| Модуль             | Тип модуля          | Описание                                                                                                                                                        |
|--------------------|---------------------|-----------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `:app`             | Android Application | Объединяет все необходимое для корректной работы приложения.                                                                                                    |
| `core:common`      | Java/Kotlin Library | Общие классы, совместно используемые модулями.                                                                                                                  |
| `core:components`  | Android Library     | Компоненты пользовательского интерфейса, расширения, базовые классы(`BaseFragment` и тд).                                                                       |
| `core:network`     | Android Library     | Api для обработки сетевых запросов и ответов от удаленного источника данных.                                                                                    |
| `core:ui`          | Android Library     | Ресурсы, используемые различными функциями.                                                                                                                     |
| `feature:channels` | Android Library     | Отображает все каналы и подписанные на них с помощью `ChannelFragment` и `HolderChannelFragment`.                                                               |
| `feature:chat`     | Android Library     | Отображает сообщения выбранной темы канала, предоставляет возможность добавить или удалить реакцию на сообщение с помощью `ChatFragment` и `EmojiListFragment`. |
| `feature:profile`  | Android Library     | Отображает профиль пользователя с помощью `ProfileFragment`.                                                                                                    |
| `feature:search`   | Android Library     | Предоставляет возможность поиска по различным элементам.                                                                                                        |
| `feature:users`    | Android Library     | Отображает сведения обо всех пользователях в организации с помощью `UsersFragment`.                                                                             |

## Technology stack

- **Coroutines**, Flow - Асинхронная работа и т.д.
- **Retrofit2*** - Запросы в сеть.
- **Kotlinx Serialization** - Сериализация данных.
- **Coil** - Загрузка изображений по сети, кэширование загруженного.
- **Jetpack Paging 3** - Загрузка и отображение небольших фрагментов данных.
- **Android Navigation Component** - Навигации по приложению.
- **ViewBinding** - Улучшение и оптимизация кода, связанного со слоем с представления.
- **Shimmer-Android** (Facebook library) - Отображение зарузки данных.
