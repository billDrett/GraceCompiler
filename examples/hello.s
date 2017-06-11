.intel_syntax noprefix # Use Intel syntax instead of AT&T
.text
.global main
main:


Label_4: hello_grace: 
push ebp
mov ebp, esp
sub esp, 8
Label_5: mov eax, 33
 
    push eax


    


    mov eax, OFFSET FLAT:char
    push eax

    call printf
add esp, 4
pop eax

push eax


    


    mov eax, OFFSET FLAT:char
    push eax

    call printf

 mov esp, ebp
    pop ebp
    ret
puti_grace:
	push ebp
	mov ebp, esp
		
	mov eax, dword ptr[ebp+16]
	push eax
	mov eax, OFFSET FLAT:in
	push eax

	call printf
	add esp, 4

	mov esp, ebp
   	pop ebp
    	ret

.data
    integer: .asciz  "%d"
    char: .asciz "%c"

