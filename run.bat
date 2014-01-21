@ECHO ON

cd IntelligentSystems

del *.class

javac *.java
java -jar tools\PlayGame.jar tools\maps\4planets\map2.txt "java BeamsearchBot" "java FirstBot" "parallel" 25 10000 | python tools/visualizer/visualize_locally.py

@PAUSE