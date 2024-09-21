CC = gcc
CFLAGS = -g -Wall

# List of object files, generated from corresponding source files
OBJS = main.o asm.o order.o

# Target executable file
EXE = output

# Default target
all: $(EXE)

# Rule to link object files to create executable
$(EXE): $(OBJS)
	$(CC) $(CFLAGS) -o $@ $^

# Rules for generating object files from source files
%.o: %.c
	$(CC) $(CFLAGS) -c $<

%.o: %.s
	$(CC) $(CFLAGS) -c $<

run: output
	./output
	
clean:
	rm -f $(EXE) $(OBJS)