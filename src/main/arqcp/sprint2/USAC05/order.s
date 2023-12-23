.section .data
	
.section .text

	.global sort_array

#######################################################	
sort_array:

    # rdi - int* vec
    # esi - int num

	pushq %rbx
    movl %esi,%ecx                      # num = size of vec
    decl %ecx                           # last position = num - 1

    test %ecx,%ecx                         # if(num <= 1)
    jle end

    first_loop:                         # for(i=num - 1;i!=0;i--)
    
    movl %ecx,%ebx
    decl %ebx
    
    second_loop:                        # for(j=i-1;j<0;j--)

        test %ebx,%ebx                 
        jl exit_loop2

        movl (%rdi,%rbx,4),%edx         # vec[j]
        movl (%rdi,%rcx,4),%eax         # vec[i]

        cmp %edx,%eax                   # vec[j] > vec[i]
        jg no_swap                      # IMPORTANT: if the order is accending jump = jg, if the order is decending jump = jl 
        
        movl %eax,(%rdi,%rbx,4)         # vec[j] = vec[i]
        movl %edx,(%rdi,%rcx,4)         # vec[i] = vec[j]

    no_swap:
        decl %ebx
        jmp second_loop
    exit_loop2:
        loop first_loop
    end:
		popq %rbx
        ret
