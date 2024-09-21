CC = gcc
CFLAGS = -g -Wall -Wextra -fanalyzer

# List of source files in c
ASRC := $(wildcard ../*/*.c)

# List of object files in s
ASRCS := $(wildcard ../*/*.s)

# List of object files
AOBJS := $(ASRC:.c=.o) $(ASRCS:.s=.o)

OBJS := $(AOBJS)

# Target executable file
EXE = output

# Default target
all: $(EXE)

# Rule to link object files to create executable
$(EXE): $(OBJS)
	$(CC) $(CFLAGS) -o $@ $^ -lm

# Rules for generating object files from source files
%.o: %.c
	$(CC) $(CFLAGS) -c $< -o $@

%.o: %.s
	$(CC) $(CFLAGS) -c $< -o $@

run: output
	./output
	
clean:
	rm -f $(EXE) $(OBJS)