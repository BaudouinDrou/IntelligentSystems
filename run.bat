@ECHO OFF

cd IntelligentSystems

javac *.java
java -jar tools\PlayGame.jar tools\maps\8planets\map2.txt "java LookaheadBot" "java FirstBot" | python tools/visualizer/visualize_locally.py

@PAUSE