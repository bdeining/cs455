all: compile
	@echo '[INFO] Done!'

clean:
	@echo '[INFO] Cleaning up...'
	@rm -rf target
	@rm -rf cs455

compile:
	@echo '[INFO] Compiling source...'
	@javac -d . src/main/java/cs455/overlay/**/*.java