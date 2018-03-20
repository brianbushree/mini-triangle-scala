SRC = scala
SOURCES = scala/Scanner.scala \
	scala/ScannerTest.scala \
	scala/AST.scala \
	scala/Parser.scala \
	scala/ParserTest.scala
SC = scalac

compile: $(SOURCES:.scala=.class)

%.class: %.scala
	@echo "Compiling $*.scala.."
	@$(SC) -d . $*.scala

clean:
	rm *.class