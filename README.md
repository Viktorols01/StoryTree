# StoryTree
## What is it?
Create interactive text-based stories and play them.
Comes with an editor and a file reader.

## How to create JAR file
Unfortunately I wrote this project in 2024 back when I didn't use Maven and instead ran the code using vscode.
Here's a bash script that can be used on UNIX systems:
```bash
./build.sh
```
Which creates two JAR files in the dist directory. To run:
```bash
java -jar dist/editor.jar
```
```bash
java -jar dist/reader.jar
```

## How to use
Use the editor to create a new graphical story which can also be saved as a story file.
Play stories using the reader program.
### File extensions
- **st** - story file, only for playing.
- **gst** - graphical story file, can be played and edited.