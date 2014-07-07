JC=javac
JVM=java

JAVASRC = 	SnakeGame.java \
			Config.java \
			GraphicsItem.java \
			SnakeAction.java \
		  	Rat.java \
		  	Snake.java \
			GamePanel.java \
		  	Direction.java \
		  	SnakePart.java \
		  	GameWindow.java \
		  	ImageHelper.java

JAVACLASS = SnakeGame

all:
	$(JC) $(JAVASRC)

warn:
	$(JC) -Xlint $(JAVASRC)
run:
	$(JVM) $(JAVACLASS)

clean:
	rm *.class