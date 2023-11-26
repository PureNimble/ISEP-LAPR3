CC = gcc
CFLAGS = -g -Wall

# List of source files
ASRCS := $(filter-out ../USAC05/order.s, $(wildcard ../*/*.s))

# List of object files
AOBJS := $(ASRCS:.s=.o)

OBJS := main.o $(AOBJS)

# Target executable file
EXE = output

# Default target
all: $(EXE)

# Rule to link object files to create executable
$(EXE): $(OBJS)
	$(CC) $(CFLAGS) -o $@ $^

# Rules for generating object files from source files
%.o: %.c
	$(CC) $(CFLAGS) -c $< -o $@

%.o: %.s
	$(CC) $(CFLAGS) -c $< -o $@

run: output
	./output
	
clean:
	rm -f $(EXE) $(OBJS)