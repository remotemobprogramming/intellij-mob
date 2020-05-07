# Swift git handover with mob IntelliJ plugin

![build](https://github.com/nowsprinting/intellij-mob/workflows/build/badge.svg)

![mob Logo](documents/logo.svg)

Swift [git handover](https://www.remotemobprogramming.org/#git-handover) with mob IntelliJ plugin.

- mob IntelliJ plugin is ported [mob](https://github.com/remotemobprogramming/mob) command line tool
- mob is the fasted way to [handover code via git](https://www.remotemobprogramming.org/#git-handover)
- mob keeps your `master` branch clean
- mob creates WIP commits on the `mob-session` branch
- mob notifies you when it's time to handover


## How to install

1. Download zip file from [Latest release](https://github.com/nowsprinting/intellij-mob/releases)
1. Settings(Windows, Linux)/Preferences(macOS)... | Plugins | upper gear icon | Install Plugin from Disk...

This method is **temporary**.
Publish to JetBrains plugins repository in the near future.


## How to use

VCS | Mob | Start Mob Programming as Typist... (or ALT+M, S)

![menu](documents/menu.png)

![start dialog](documents/start.png)

Click OK, So switched to a separate branch. Start mob programming!

When handover to the next person,

VCS | Mob | Next : Handover to Next Typist... (or ALT+M, N)

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
- Select "Stay in WIP branch after executing 'Next' and checkout base branch" in next dialog : after handover code, stay on mob session branch
- Resets Any Unfinished Mob Session : deletes `mob-session` and `origin/mob-session`

### Zoom Screen Share Integration

The "Also activates screenshare in zoom" feature uses the zoom keyboard shortcut "Start/Stop Screen Sharing". This only works if you
- make the shortcut globally available (Zoom > Preferences > Keyboard Shortcuts), and
- keep the default shortcut at CMD+SHIFT+S (macOS)/ ALT+S (Linux).
- following setting under System Preferences is required on macOS Catalina (or later?); Security & Privacy -> Privacy tab -> Accessibility, And add your JetBrains IDE .app

[More tips on setting up Zoom for effective screen sharing.](https://effectivehomeoffice.com/setup-zoom-for-effective-screen-sharing/)


## How to configure

Open Settings(Windows, Linux)/Preferences(macOS) > Tools > Mob

![preferences](documents/preferences.png)

Settings are saved in `.idea/mob.xml`

If you want a voice notification when the timer expires,
Open Event Log > Settings or Preferences... > Appearance & Behavior > Notifications, And turn on "Read aloud" on "Mob Timer" row. (macOS only)

![notification settings](documents/preferences_notification.png)


## Troubleshoot

To see `idea.log`, in JetBrains Toolbox, open Settings | Configuration, And click "Show logs directory" button.

If necessary, Get the trace level log.
Open Help | Diagnostic Tools | Debug Log Settings…, And input `#com.nowsprinting.intellij-mob:trace`


## Milestones

### Beta

- Display timer on status bar
- Restart timer
- Open commit & push dialog after done. with add `Co-authored-by:` trailer to commit message, if possible

### GM

- Integration tests
- Refactor: dialogs uses `DialogWrapper`
- Refactor: tests about `GitRepository`
- Input Validation on dialogs
- Support multiple repository
- Possible rollback on "done" by splitting the done process into two phases
- Publish JetBrains plugins repository


## How to contribute

Open an issue or create a pull request.


## Credits

Original [mob](https://github.com/remotemobprogramming/mob) developed and maintained by [Dr. Simon Harrer](https://twitter.com/simonharrer).

<!-- Original contributions and testing by Jochen Christ, Martin Huber, Franziska Dessart, and Nikolas Hermann. Thank you! -->

Original logo designed by [Sonja Scheungrab](https://twitter.com/multebaerr).

Port to IntelliJ plugin by [Koji Hasegawa](https://twitter.com/nowsprinting)