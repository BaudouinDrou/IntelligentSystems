# a command that works on Linux/Mac
# the arguments are passed in the same order as in the standard command

MAP=larger/map1.txt
PLAYER1=FirstBot
PLAYER2=HillclimbingBot
MODE=$4
NTURN=$5
MAXT=$6

cd IntelligentSystems
javac *.java
java -jar tools/PlayGame.jar tools/maps/$MAP  "java $PLAYER1 " "java $PLAYER2 " $MODE $NTURN $MAXT   | python tools/visualizer/visualize_locally.py 

