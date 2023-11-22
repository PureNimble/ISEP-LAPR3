.section .text

	.global enqueue_value

#######################################################	
enqueue_value:
    # rdi - int* array
    # esi - int length of array
    # rdx - int* read pointer
    # rcx - int* write pointer
    # r8d - int value to enqueue

    movl $-1, %r9d                # helper for compare

    movl (%rdx), %r10d            # get read index
    movl (%rcx), %eax             # get write index

    cmpl %eax, %r10d              # if(readIndex == writeIndex)
    je end

    pushq %rax                    # save write index
    pushq %rdx                    # save read index

    incl %eax                     # increment write index
    xorl %edx, %edx               # clear edx
    divl %esi                     # calculate (writeIndex + 1) % length
    
    movl %edx, %r11d              # save remainder

    popq %rdx                     # restore readIndex
    popq %rax                     # restore writeIndex
    
    decl %esi                     # get last position

    cmpl %r11d, %r10d
    cmovg %r9d, %eax
    jl end                        # if(readIndex < remainder)

    cmpl %esi, %r10d              # if(readIndex == last)
    cmove %r9d, %r10d
    
    incl %r10d                    # readIndex++
    movl %r10d, (%rdx)            # array[read] = read

end:
    cmpl %esi, %eax               # if(array[write] == array[last])
    cmove %r9d, %eax
    incl %eax                     # write++
    movl %eax, (%rcx)             # array[write] = write
    movl %r8d, (%rdi, %rax, 4)    # array[write] = value

    ret
