# httpLogEvent
The following diagram describes the event hierarchy that is used to alert user about security issues pattern that can be observed in the http log file:
```
                                        httpLogEvent
                                             /\
                                            /  \
                                           /    \
TargetOneAccount--------httpFailedLoginEvent    httpFileTooLargeEvent--------OneSameFile
                                /\                       /\
                               /  \                     /  \
                              /    \                   /    \
                        SameIP     Consecutive     SameIP   Consecutive
                        (Botnet)                  (Botnet)

```
+ httpLogEvent is extracted directly from the log
+ httpFailedLoginEvent is raised when a failed login attempt is detected
    + httpFailedLogInEventSameIPEvent is raised when multiple failed attempts to log in from one IP are detected (Might be a botnet)
    + httpFailedLoginEventTargetOneAccount is raised when multiple failed login attempt targeting one account are detected (Dictionary attack)
    + httpConsecutiveFailedLoginEvent is raised when multiple failed login attempts are detected (ddos or dictionary attack)
+ httpFileTooLargeEvent is raised when user HTTP Post a large file or the file is too large to be handle by the web server.
    + httpFileTooLargeSameIPEvent is raised when a user from one same IP is trying to post multiple large files. (Botnet)
    + httpConsecutiveFileTooLargeEvent is raised when too many large files are received or refused by the web server (dos attack)
    + httpFileTooLargeSameFile is raised when multiple requests are sending very large files that have the same size are detected. (dos attack)