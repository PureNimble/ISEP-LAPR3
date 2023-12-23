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
    cmpb $58, %cl           # if token reaches end
    je final_check
    cmpb %bl, %cl           # if input[i] != token[i]
    jne check_token_epilogue
    jmp check_token         # loop check_token

check_token_epilogue:
    popq %rsi
    jmp token_loop
    
final_check:
	cmpb $58, %bl           # if token in string also ends
    je extract_value_prologue
    jmp check_token
	
	
extract_value_prologue:
    popq %rsi
    movl $0, %ecx

extract_value:
    incq %rdi
    movb (%rdi), %bl
    cmpb $46, %bl
    je extract_value
    cmpb $35, %bl
    je end
    cmpb $0, %bl
    je end
    imul $10, %ecx
    movsbl %bl, %eax
    subl $48, %eax
    addl %eax, %ecx
    jmp extract_value

end:
	cmp $0, %ecx
	je fail
	movl %ecx, (%rdx)
	movq $1, %rax
	jmp return
    
fail:
    movq $0, %rax
    jmp return

return:
    popq %rbx
	ret

