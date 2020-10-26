# ARP Spoofing alert documentation

The document describes the event hierarchy of the ARP spoofing detection module 
```
ARPPacketEvent--|->ARPReplyEvent---------|                  |->ARPDuplicateIPAlert
       |         |                        |-->ARPCacheUpdate-|
       |         |->ARPAnnouncementEvent--|                  |->ARPCacheFloodAlert
       |                    |
       v                    v
         ARPMultipleUnaskedForAnnouncementAlert
```
* ARPPacketEvent is raised whenever the interface received an ARP packet.
* ARPReplyEvent is raised whenever the machine broadcast a request to know the hardware address of an IP and got a reply.
* ARPAnnouncementEvent is raised whenever a gratuitous ARP announcement is made.
* ARPMultipleUnaskedForAnnouncementAlert is raised whenever multiple gratuitous ARP announcement is detected in a time frame;
* ARPCacheUpdate is raised whenever a new mapping entry is created for the ARP Cache.
* ARPDuplicateIPAlert is raised when an IP is being mapped to multiple MAC addresses in the ARP cache.
* ARPCacheFloodAlert is raised when the ARP cache has too many entries.