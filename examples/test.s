.intel_syntax noprefix # Use Intel syntax instead of AT&T
.text
.global main
main:
Label_8: hello_grace: 
push ebp
mov ebp, esp
sub esp, 4
mov byte ptr[ebp-4], 65
mov byte ptr[ebp-3], 65
mov byte ptr[ebp-2], 0

Label_19: lea esi, byte ptr [ebp-4]
push esi
Label_20: sub esp, 4
push dword ptr [ebp+8]
call puts_grace
add esp, 12


Label_21: mov esp, ebp
pop ebp
ret

Label_0: foo_1: 
push ebp
mov ebp, esp
sub esp, 4
Label_1: mov esi, dword ptr [ebp+16]
mov eax, dword ptr [esi]
mov edx, 1
add eax, edx
mov dword ptr [ebp-4], eax
Label_2: mov eax, dword ptr [ebp-4]
mov esi, dword ptr [ebp+16]
mov dword ptr [esi], eax
Label_3: mov esp, ebp
pop ebp
ret
Label_4: foo2_2: 
push ebp
mov ebp, esp
sub esp, 4
Label_5: mov esi, dword ptr [ebp+16]
mov eax, dword ptr [esi]
mov edx, 1
sub eax, edx
mov dword ptr [ebp-4], eax
Label_6: mov eax, dword ptr [ebp-4]
mov esi, dword ptr [ebp+16]
mov dword ptr [esi], eax
Label_7: mov esp, ebp
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
		
	mov eax, byte ptr[ebp+16]
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
	mov eax, 0
	mov dword ptr [ebp-8], eax
	Label_loopPuts: mov eax, dword ptr [ebp-8]
	mov ecx, 1
	imul ecx
	mov ecx, dword ptr [ebp+16]
	add eax, ecx
	mov dword ptr [ebp-12], eax
	mov edi, dword ptr [ebp-12]
	mov eax, dword ptr [edi]
	mov edi, dword ptr [ebp-12]
	mov edx, dword ptr [edi]
	cmp eax, edx
	jne Label_n0Puts
	jmp Label_loopendPuts
	Label_n0Puts: mov eax, dword ptr [ebp-8]
	mov ecx, 1
	imul ecx
	mov ecx, dword ptr [ebp+16]
	add eax, ecx
	mov dword ptr [ebp-16], eax
	mov edi, dword ptr [ebp-16]
	mov eax, dword ptr [edi]
	push eax
	sub esp, 4
	mov esi, dword ptr [ebp+8]
	push dword ptr [esi+8]
	call putc_grace
	add esp, 12
	mov eax, dword ptr [ebp-8]
	mov edx, 1
	add eax, edx
	mov dword ptr [ebp-20], eax
	mov eax, dword ptr [ebp-20]
	mov dword ptr [ebp-8], eax
	jmp Label_loopPuts
	Label_loopendPuts: mov esp, ebp
	pop ebp
	ret


.data
    integer: .asciz  "%d"
    char: .asciz "%c"
scanf_fmt: .asciz  "%d"


