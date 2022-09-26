# k8navigator
GUI for kubectl commands

Build in Componse for Desktop in Kotlin.

## Must have

- kubectl CLI installed (under homebrew)
- aaws CLI installed
- macos (for now)

The process builder adds a path env to know where kubectl, aws is located.
The ssh and logs commands trigger a "osascript" which is a MacOS specific command.


## Distributable

By default, its build for MacOS. 
To create your own distributable you will find under Gradle
tasks, a task named createDistributable.

## TODOs

- namespace support []
- command history display at the bottom []

