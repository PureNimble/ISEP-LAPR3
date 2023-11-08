.section .text

	.global copy_num_vec

copy_num_vec:
    movl $0, %eax

    cmpl %ecx, %edx
    jl end

array_to_vec:
    movl (%rdi, %rsi, 4), %r10d
    movl %r10d, (%r8)
    addq $4, %r8
    incl %esi
    cmpl %esi, %edx
    cmove %eax, %esi
    loop array_to_vec

    incl %eax

end:
	ret
