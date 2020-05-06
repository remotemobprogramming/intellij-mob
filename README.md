# IntelliJ plugin for Remote Mob Programming

![Java CI with Gradle](https://github.com/nowsprinting/intellij-mob/workflows/Java%20CI%20with%20Gradle/badge.svg)

<!-- ![mob Logo](logo.svg) -->

Swift handover for <a href="https://www.remotemobprogramming.org/">remote mob programming</a> using git.
It keeps your master branch clean and creates WIP commits on <code>mob-session</code> branch.

It is a port of <a href="https://github.com/remotemobprogramming/mob">mob</a> to the IntelliJ plugin.


## How to install

1. Download zip file from [Latest release](https://github.com/nowsprinting/intellij-mob/releases)
1. Settings(Windows, Linux)/Preferences(macOS)... | Plugins | upper gear icon | Install Plugin from Disk...

This method is **temporary**.
Publish to JetBrains plugins repository in the near future.


## How to use

VCS | Mob | Start Mob Programming as Typist... (or shortcut ALT+M, S)

![menu](documents/menu.png)

![start dialog](documents/start.png)

Click OK, Switch to a separate branch.

When handover to the next person,

VCS | Mob | Next : Handover to Next Typist... (or shortcut ALT+M, N)

![next dialog](documents/next.png)

Continue with "Start" and handover to the next person with "Next".
Continue with "Start" and handover to the next person with "Next".
Continue with "Start" and handover to the next person with "Next".
...

When you're done,

VCS | Mob | Done : Finish Mob Session...

After confirm in dialog,
Get your changes into the staging area of the `master` branch. 
Please commit & push into base branch yourself.


## How does it work

- Start Mob Programming as Typist : creates branch `mob-session` and pulls from `origin/mob-session`
- Next : pushes all changes to `origin/mob-session`in a `mob next [ci-skip]` commit
- Done : squashes all changes in `mob-session` into staging of `master` and removes `mob-session` and `origin/mob-session`
- Set "Timer" in start dialog : start a specific minute timer, notify by balloon if expired
- Select "Also activates screenshare in zoom" in start dialog : start screen sharing in Zoom (macOS or Linux with xdotool olny. and requires Zoom configuration)
- Select "Stay in the WIP branch after 'Next' instead of checking out base branch" in next dialog : after handover code, stay on mob session branch
- Resets Any Unfinished Mob Session : deletes `mob-session` and `origin/mob-session`

### Zoom Screen Share Integration

The "Also activates screenshare in zoom" feature uses the zoom keyboard shortcut "Start/Stop Screen Sharing". This only works if you
- make the shortcut globally available (Zoom > Preferences > Keyboard Shortcuts), and
- keep the default shortcut at CMD+SHIFT+S (macOS)/ ALT+S (Linux).

[More tips on setting up Zoom for effective screen sharing.](https://effectivehomeoffice.com/setup-zoom-for-effective-screen-sharing/)


## How to configure

Open Settings(Windows, Linux)/Preferences(macOS) > Tools > Mob

![preferences](documents/preferences.png)


## Milestones

### Beta

- Display timer on status bar
- Restart timer
- Open commit & push dialog after done. with add Co-authored-by trailer to commit comment, if possible

### Release

- Integration tests
- Refactor: dialogs uses `DialogWrapper`
- Refactor: tests about `GitRepository`
- Validate on dialogs
- Support multiple repository
- Publish JetBrains plugins repository


## How to contribute

Open an issue or create a pull request.


## Credits

Original [mob](https://github.com/remotemobprogramming/mob) developed and maintained by [Dr. Simon Harrer](https://twitter.com/simonharrer).

<!-- Original contributions and testing by Jochen Christ, Martin Huber, Franziska Dessart, and Nikolas Hermann. Thank you! -->

<!-- Logo designed by [Sonja Scheungrab](https://twitter.com/multebaerr). -->

Port to IntelliJ plugin by [Koji Hasegawa](https://twitter.com/nowsprinting)