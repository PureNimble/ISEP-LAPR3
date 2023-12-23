# %rdi = int* vec
# %rsi = int num


.section .text

	.global mediana
	.global sort_array

mediana:
    pushq %rbx
    pushq %rsi
    call sort_array
    popq %rsi
    cmp $0, %rsi
    je zero
    movl $2,%ecx
    movl %esi,%eax
    cdq
    idivl %ecx
    movslq %eax, %rbx
    movl (%rdi, %rbx, 4), %eax
    jmp end
    
zero:
	movl $0, %eax
	
end:
    popq %rbx
    ret


