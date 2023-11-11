.section .text

	.global enqueue_value

#######################################################	
enqueue_value:
    # rdi - int* array
    # esi - int length of array
    # rdx - int* read pointer
    # rcx - int* write pointer
    # r8d - int value to enqueue

    decl %esi                       # get last position

    movl $-1, %r9d                  # helper for compare

    movl (%rdx), %eax               # get read index
    movl (%rcx), %ebx               # get write index

    cmp %eax, %ebx                  # empty buffer case
    cmove %r9d, %ebx      

    cmp %esi, %ebx                  # if(array[write] == array[last])
    cmove %r9d, %ebx  
    incl %ebx                       # write++

    cmp %ebx, %eax                  # if(array[read] == array[write])
    jne end

    cmp %esi, %eax                  # if(array[read] == array[last])
    cmove %r9d, %eax
    incl %eax

end:
    movl %eax, (%rdx)               # array[read] = read
    movl %ebx, (%rcx)               # array[write] = write

    movl %r8d, (%rdi, %rbx, 4)      # array[read] = value

    ret
