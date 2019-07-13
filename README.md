### Elohim Dialog Tracker
Reads from the log file of The Talos Principle to track Elohim's dialogs.

Only includes dialogs in the main game. Elohim isn't very talkative in Gehenna or the Demo, they don't need a specific tool to help track them.


Dialogs are "unlocked" in two stages. When the dialog is initially played it gets add to your local talosProgress flags. If you reset checkpoint or stopped the game at this point then the flag would be lost. You need to actually get a checkpoint to properly save them as listened to. The tracker will show both states.

----
Note that this program relies on Croteam's somewhat inconsistent logging. 4 main-game lines use a different function to the rest, which does not log them so they can't be tracked:
- Elohim-047_In_the_beginning_were_the_words - Near OOB deathwalls
- Elohim-049_The_purpose_is_written - C1 Easter Egg Room
- Elohim-068_It_is_written_that - During Messenger Ending Cutscene
- Elohim-071_Rejoice_my_child - During Eternalize Ending Cutscene

Additionally, some dialogs play after a delay from when the function is actually called. They will be saved locally as soon as the function is called, but not logged until they start playing, so it's possible to save some lines without the tracker noticing.
