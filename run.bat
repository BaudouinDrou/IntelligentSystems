@ECHO ON

cd IntelligentSystems

del *.class

javac *.java
java -jar tools\PlayGame.jar tools\maps\4planets\map3.txt "java BeamsearchBot" "java FirstparallelBot" "parallel" 25 100 | python tools/visualizer/visualize_locally.py

@PAUSE