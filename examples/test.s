.intel_syntax noprefix # Use Intel syntax instead of AT&T
.text
.global main
main:
Label_0: hello_grace: 
push ebp
mov ebp, esp
sub esp, 48

lea edi, mystring
mov al, byte ptr [edi]
mov al, byte ptr [edi-1]
mov al, byte ptr [edi+2]
mov al, 65
mov byte ptr [edi+2], al
mov al, 43
mov al, byte ptr [edi+2]

Label_18: mov esp, ebp
pop ebp
ret










puti_grace:
	push ebp
	mov ebp, esp
		
	mov eax, dword ptr[ebp+16]
	push eax
	mov eax, OFFSET FLAT:integer
	push eax

	call printf
	add esp, 4

	mov esp, ebp
   	pop ebp
    ret

putc_grace:
	push ebp
	mov ebp, esp
		
	movzx eax, byte ptr[ebp+16]
	push eax
	mov eax, OFFSET FLAT:char
	push eax

	call printf
	add esp, 4

	mov esp, ebp
   	pop ebp
    ret

geti_grace:
	push ebp
	mov ebp, esp

 # This is the address of the local variable that we're passing to scanf
    mov eax, dword ptr[ebp+12]
    push eax

    # Pass the format string literal to scanf
    mov eax, OFFSET FLAT:scanf_fmt
    push eax
    call scanf
    add esp, 8

	mov esp, ebp
   	pop ebp
    ret

puts_grace: 
	push ebp
	mov ebp, esp
	sub esp, 20
	Label_1P: mov eax, 0
	mov dword ptr [ebp-8], eax
	Label_2P: mov eax, dword ptr [ebp-8]
	mov ecx, 1
	imul ecx
	mov ecx, dword ptr [ebp+16]
	add eax, ecx
	mov dword ptr [ebp-12], eax
	Label_3P: mov edi, dword ptr [ebp-12]
	movzx eax, byte ptr [edi]
	mov edx, 0
	cmp eax, edx
	jne Label_5P
	Label_4P: jmp Label_11P
	Label_5P: mov eax, dword ptr [ebp-8]
	mov ecx, 1
	imul ecx
	mov ecx, dword ptr [ebp+16]
	add eax, ecx
	mov dword ptr [ebp-16], eax
	Label_6P: mov edi, dword ptr [ebp-16]
	movzx eax, byte ptr [edi]
	push eax
	Label_7P: sub esp, 4
	mov esi, dword ptr [ebp+8]
	#push dword ptr [esi+8]
	push 0
	call putc_grace
	add esp, 12
	Label_8P: mov eax, dword ptr [ebp-8]
	mov edx, 1
	add eax, edx
	mov dword ptr [ebp-20], eax
	Label_9P: mov eax, dword ptr [ebp-20]
	mov dword ptr [ebp-8], eax
	Label_10P: jmp Label_2P
	Label_11P: mov esp, ebp
	pop ebp
	ret




.data
    integer: .asciz  "%d"
    char: .asciz "%c"
    mystring: .asciz "Assembly"
scanf_fmt: .asciz  "%d"


