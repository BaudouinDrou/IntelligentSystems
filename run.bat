@ECHO ON

cd IntelligentSystems

del *.class

javac *.java
java -jar tools\PlayGame.jar tools\maps\larger\map3.txt "java MyAdaptiveBot" "java FirstparallelBot" "parallel" 25 1000 | python tools/visualizer/visualize_locally.py

@PAUSE