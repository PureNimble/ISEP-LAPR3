# %rdi = char* input
# %rsi = char* token
# %rdx = int* output


.section .text

	.global extract_token

extract_token:
    movl (%rdx), %ecx

    pushq %rsi

token_loop:
    popq %rsi
    movb (%rdi), %r8b
    movb (%rsi), %r9b
    cmpb %r8b, %r9b         #compare first char of token with al
    je check_token
    incq %rdi               # input[i++]
    jmp token_loop

check_token:
    incq %rdi               # input[i++]
    incq %rsi               # token[i++]
    movb (%rdi), %r8b
    movb (%rsi), %r9b
    cmpb %r8b, %r9b         # if input[i] != token[i]
    jne token_loop
    cmpb $58, %r8b          # if input[i] == ':'
    je extract_value
    jmp check_token         # loop check_token

extract_value:
    incq %rdi
    movb (%rdi), %r8b
    cmpb $46, %r8b
    je end
    cmpb $35, %r8b
    je end
    cmpb $0, %r8b
    je end
    imul $10, %ecx
    movsbl %r8b, %eax
    addl %eax, %ecx
    jmp extract_value

end:
	ret
