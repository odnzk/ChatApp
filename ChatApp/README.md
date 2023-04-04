| Module name        | Type            | Description                                                                                                                          |
|--------------------|-----------------|--------------------------------------------------------------------------------------------------------------------------------------|
| `:app`             | Android Application | Brings everything together, required for the app to function correctly.                                                               |
| `core:common`      | Java/Kotlin Library | Common classes shared between modules.                                                                                               |
| `core:components`  | Android Library | UI components, extensions, base classes(`BaseFragment` and etc).                                                                       |
| `core:network`     | Android Library | Api for handling network requests and responses from a remote data source.                                                           |
| `core:ui`          | Android Library | Resources used by different features.                                                                                                |
| `feature:channels` | Android Library | Displays all and subscribed channels using `ChannelFragment` and `HolderChannelFragment`.                                            |
| `feature:chat`     | Android Library | Displays messages of the selected channel topic, provides the ability to add or remove a reaction to a message using `ChatFragment` and `EmojiListFragment`. |
| `feature:profile`  | Android Library | Displays user profile using `ProfileFragment`.                                                                                       |
| `feature:search`   | Android Library | Provides the ability to seacrh different items.                                                                                      |
| `feature:users`    | Android Library | Displays details of all users in the organization using `UsersFragment`.                                                             |
