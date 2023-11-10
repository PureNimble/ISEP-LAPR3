.section .text

	.global copy_num_vec

copy_num_vec:
    #rdi -> array
    #rsi -> length
    #rdx -> &read
    #rcx -> &write
    #r8 -> num
    #r9 -> vec

    movl (%rdx), %r10d            # get read index
    movl (%rcx), %eax             # get write index

    subl %r10d, %eax              # writeIndex -= readIndex
    addl %esi, %eax               # writeIndex += length
    xorl %edx, %edx               # clear edx
    divl %esi                     # calculate writeIndex % length

    xorl %eax, %eax               # clear return value
    incl %edx                     # increment remainder
    cmpl %edx, %r8d
    jg end                        # jump to end if (initialized elements < num)

    movl %r8d, %ecx               # counter for num elements

array_to_vec:
    movl (%rdi, %r10, 4), %r11d   # get element of array[readIndex]
    movl %r11d, (%r9)             # add element to vec
    addq $4, %r9                  # next position of vec
    incl %r10d                    # readIndex++
    cmpl %r10d, %esi              # check end of array
    cmove %eax, %r10d             # if (readIndex == length) readIndex = 0
    loop array_to_vec             # go to next iteration

    incl %eax                     # increment return value

end:
	ret                           #return
