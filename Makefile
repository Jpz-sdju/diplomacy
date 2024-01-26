
FIR_OPT = --full-stacktrace -td build --output-file top.v

verilog:
	mill exp.test.runMain run $(FIR_OPT)

help:
	mill exp.test.runMain run --help

clean:
	rm -rf ./out
	rm -rf ./build