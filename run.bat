@ECHO OFF

cd IntelligentSystems

javac *.java
java -jar tools\PlayGame.jar tools\maps\4planets\map2.txt "java HillclimbingBot" "java FirstBot" | python tools/visualizer/visualize_locally.py

@PAUSE