create_jar() {
  jar_name=$1
  class_name=$2
  jar cfe dist/$jar_name.jar src.main.$class_name \
  -C classfiles .
}

# clear previous temp
rm -rf classfiles
mkdir classfiles

# compile to .class
javac -d classfiles $(find src -name "*.java")

# clear previous dist
rm -rf dist
mkdir dist

# create jar files
create_jar reader ReaderProgram
create_jar editor EditorProgram
