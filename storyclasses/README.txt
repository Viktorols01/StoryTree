StoryTree
- nodes: all nodes within the tree

StoryNode
- text: text within the storynode
- storyOptions: contains pointers to all reachable nodes
- adddedKeys: visiting the node adds the given keys
- removedKeys: visiting the node removes the given keys

StoryOption
- text: text to describe the option; an empty field means the story will simply proceed without choosing
- storyNodeIndex: which node the option leads to
- unlockingKeys: required keys to unlock the option
- lockingKeys: required keys that lock the option
- forced: if the option is unlocked, it is automatically chosen

StoryKey
- key: string to identify the key
- count: what amount of keys do the class represent

StoryExtraNode
- Additional text that will be appended to base text if the prerequisites are fulfilled
- Even though prerequisites are not fulfilled, subsequent extraNodes can be fulfilled and printed