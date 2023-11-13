# %rdi = int* vec
# %rsi = int num


.section .text

	.global mediana

mediana:
    pushq %rbx
    movl $2,%ecx
    movl %esi,%eax
    cdq
    idivl %ecx
    movslq %eax, %rbx
    movl (%rdi, %rbx, 4), %eax
    popq %rbx
    ret


