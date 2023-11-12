# %rdi = char* input
# %rsi = char* token
# %rdx = int* output


.section .text

	.global extract_token

extract_token:
    pushq %rbx

token_loop:
    movb (%rdi), %bl
    movb (%rsi), %cl
    cmpb %bl, %cl           #compare first char of token with al
    je check_token_prologue
    incq %rdi               # input[i++]
    jmp token_loop

check_token_prologue:
    pushq %rsi

check_token:
    incq %rdi               # input[i++]
    incq %rsi               # token[i++]
    movb (%rdi), %bl
    movb (%rsi), %cl
    cmpb $0, %cl           # if token reaches end
    je extract_value_prologue
    cmpb %bl, %cl           # if input[i] != token[i]
    jne check_token_epilogue
    jmp check_token         # loop check_token

check_token_epilogue:
    popq %rsi
    jmp token_loop

extract_value_prologue:
    popq %rsi

extract_value:
    incq %rdi
    movb (%rdi), %bl
    cmpb $46, %bl
    je end
    cmpb $35, %bl
    je end
    cmpb $0, %bl
    je end
    movl (%rdx), %ecx
    imul $10, %ecx
    movsbl %bl, %eax
    subl $48, %eax
    addl %eax, %ecx
    movl %ecx, (%rdx)
    jmp extract_value

end:
    popq %rbx
	ret
