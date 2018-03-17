# Mango

[![Discord](https://img.shields.io/discord/352874955957862402.svg)](https://discord.gg/KUFmKXN)
[![License](https://img.shields.io/github/license/Minespree/Mango.svg)](LICENSE)
![Documentation](https://img.shields.io/badge/docs-javadocs-green.svg)

This repo contains the code of Mango, the main shared library that we planned to use all across the systems of the former Minespree Network.

As you might have noticed, most base frameworks ([Feather](https://github.com/Minespree/Feather) and [Dominion](https://github.com/Minespree/Dominion) in particular) have lots of duplicated code that we planned to remove by adding this base library that was platform agnostic (so that it could also be used by our PlayPen plugins, [Zygote](https://github.com/Minespree/Zygote) in particular).
We never got to use the library because the server closed down, but there were WIP branches for each base project (that we've decided not to open source because they are heavily broken in their current state).

The intention we had by creating this project was to improve our developer satisfaction and fixing long standing issues caused by old legacy modules (e.g. the punishment system).
We knew this would require a major refactor of most of our main projects but the long-term improvement of several aspects of the network would be worth the time and effort put into this process.

Besides the removal of some branding and configuration data, it is more or less unmodified. It is probably not _directly_ useful to third parties in its current state, but it may be help in understanding how the Minespree network operated.

We are quite open to the idea of evolving this into something more generally useful. If you would like to contribute to this effort, talk to us in [Discord](https://discord.gg/KUFmKXN).

## Requirements

To build Mango, the following will need to be installed and available from your shell:

* [JDK 8](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html) version 131 or later (older versions _might_ work)
* [Git](https://git-scm.com/)
* [Maven](https://maven.apache.org/)

You can find detailed installation instructions for these tools on the [Getting started](https://github.com/Minespree/Docs/blob/master/setup/DEPENDENCIES.md) docs page.

## Getting started

Mango is required by other packages and as so, you will need to install it to your local `.m2` repo by running:

```
mvn install
```

You can also deploy it to your own Maven repo to use the GitLab CI Docker image and make distribution easier. [Instructions](https://github.com/Minespree/Docs/blob/master/deploy/PLAYPEN_DEPLOYER.md)

**Please note** that this project was never used in production and is not complete. Please, only use this project on a development environment.

## Architecture

This repo contains the following components:

* Database models used by all parts of the network
* Connectable and Connector API (used to abstract the connection to external services)
* MongoDB and Redis managers
* Scheduler API
* Base Player classes (with Morphia annotations)
* Repository API (supports notifications, serialization, safe types...)
* JSON serializer (using gson)
* Game and Server interfaces
* Weighted random chooser
* Collection utils
* Time utils
* Several "worker" services that do miscellaneous background tasks or respond to Redis Pub/Sub messages
* Miscellaneous utils (UUIDNameKeypair, ArrayUtils, Collectors...)

Please check the Javadocs for detailed explanations on each component.

## Contributing

Please note that this library is shared with non-Bukkit Java apps such as BungeeCord and PlayPen, and as so, we can't depend on Spigot/BungeeCord dependencies (except for the bungeecord-chat library).

Consider carefully if your changes/additions are necessary, and if you are sure these will be needed by most dependent projects. There's likely a better place to place extra methods:

- If you plan to add a Bukkit-only library, add your changes to [Wizard](https://gitlab.minespree.net/minespree/games/Wizard) or [Feather](https://github.com/Minespree/Feather). The former could be used in a vanilla Spigot server, so make sure you don't add unnecessary changes.
- If you plan to add anything chat/translation related, add your changes to [Babel](https://github.com/minespree/Babel).
- If you only need to add a general game mechanic, add your changes to [Rise](https://github.com/minespree/Rise). 

## Authors

This project was maintained by the Minespree Network team. If you have any questions or problems, feel free to reach out to the specific writers and maintainers of this project:

<table>
  <tbody>
    <tr>
      <td align="center">
        <a href="https://github.com/hugmanrique">
          <img width="150" height="150" src="https://github.com/hugmanrique.png?v=3&s=150">
          </br>
          Hugmanrique
        </a>
      </td>
    </tr>
  <tbody>
</table>

## Coding Guidelines

As this project was never completed, some of these guidelines were never applied/implemented.

* General
  * Use your judgement always. Any rule can be broken with a good reason. Don't follow a rule without understanding its purpose.
  * Write code for readability above all else. Always think about how another developer would work with your code.
  * Avoid repetitive code. Factor out the repetition, if there is a reasonable way to do so.
* Formatting
  * No tabs; use 4 spaces instead
  * No trailing whitespaces
  * No CRLF line endings, LF only, put your git's `core.autocrlf` on `true`.
  * No 80 column limit or 'weird' midstatement newlines.
  * Use whatever layout you feel makes the code most readable, even if that differs from case to case.
* Comments
  * Try to write code that is obvious enough so that it doesn't need to be explained with comments.
  * In places where a reader might be confused or miss something important, use comments to fill them in.
  * Don't put redundant or obvious things in comments or javadocs.
  * Ensure your IDE is not inserting any generated comments.
* Nulls
  * Strongly prefer `java.util.Optional` over `null`, generally speaking.
  * Use empty collections as nil values for collections.
  * Use `@Nullable` wherever nulls are allowed. Place it before the type, if possible.
  * Don't use `@Nonnull`. Assume anything (in our own code) without `@Nullable` is never null.
  * Use `checkNotNull` on constructor arguments for manually created objects.
* Structure
  * Design classes to do [one thing only](https://en.wikipedia.org/wiki/Single_responsibility_principle). If a class provides multiple services, break them down into separate public interfaces and keep the class private.
  * Use `final` fields, and create immutable data types wherever possible.
  * Don't create unnecessary getters and setters, only what is actually used.
  * No mutable static fields, collections, or any other static state (there are a few exceptions, such as caches and `ThreadLocal`s).
  * Getters don't have to start with `get`, but they can if you think it's important.
* Exceptions
  * Detect errors as early as possible, ideally at server startup. This applies to both user errors and internal assertions.
  * Only catch specific exceptions that you are expecting and can handle thoroughly. Don't hide exceptions that other handlers need to know about.
  * Avoid catching common exceptions like `IllegalArgumentException`, because it's hard to be certain where they come from. If you need to catch them, keep the code inside the `try` block as small as possible.
  * Don't catch all exceptions or try to handle internal errors for no particular reason.
  * Ensure that all errors are seen by a human who knows how to fix them:
    * Pure user errors only need to be sent to the user who provided the bad input
    * Any unexpected exception on any server must notify developers somehow i.e. some system logger. You can tell the user about the error so they know what's going on, but don't expect them to deal with it.
* Concurrency
  * Avoid concurrency. It's hard, and we don't have a general solution for doing it safely.
  * A Bukkit server has a single "main thread" where most game logic runs, and multiple background threads for I/O and serialization. All `Event`s (except `AsyncEvent`s) and scheduled tasks (except async tasks) run on the main thread.
  * Our own modules use background thread pools for database operations.
  * Never block the main thread on database calls, or any other I/O operation. Use `ListenableFuture`s and `FutureCallback`s to handle database results.
  * Don't use the Bukkit API or any of our own APIs from a background thread, unless it is explicitly allowed by the API. Use `SyncTaskManager` to get back on the main thread from a background thread.
  * A Bungee server is entirely multi-threaded. Handlers for a specific event run in sequence, but separate events and tasks can run concurrently.
* Localization
  * All in-game text must be localized.
  * Use a `BabelMessageType` to display the localized message. However, it must pass through the `Session#sendMessage` before being sent to the player. That will apply the server-side translations.
  * Any changes to the templates must be done directly in the MongoDB translations document.
* Testing
  * TODO
* Logging
  * TODO
* Utilities
  * TODO

## Workflow

* We use Git, with a typical [feature branch workflow](https://www.atlassian.com/git/tutorials/comparing-workflows/feature-branch-workflow).
* Trivial changes and emergency fixes can be merged straight to the `master` branch
* Any significant change requires a PR, and code review by at least one other developer. This applies indiscriminately to all developers. Everyone should have their code reviewed, and anyone can review anyone else's code.
* Once a change has been merged to master, it should be deployed ASAP so that problems can be found. Deploying several old changes at once just makes it harder to trace bugs to their source.
* Without automated tests, we rely heavily on user reports and server logs to discover regressions. Developers should be around for at least a few hours after their change is deployed, in case something breaks.

## License

Mango is free software: you can redistribute it and/or modify it under the terms of the GNU Affero General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
                                
This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License for more details.

A copy of the GNU Affero General Public License is included in the file LICENSE, and can also be found at https://www.gnu.org/licenses/agpl-3.0.en.html

**The AGPL license is quite restrictive, please make sure you understand it. If you run a modified version of this software as a network service, anyone who can use that service must also have access to the modified source code.**