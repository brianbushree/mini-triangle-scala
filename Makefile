SRC = scala
SOURCES = $(shell ls $(SRC)/*.scala)
SC = scalac

compile: $(SOURCES:.scala=.class)

%.class: %.scala
	@echo "Compiling $*.scala.."
	@$(SC) -d . $*.scala

clean:
	rm *.class