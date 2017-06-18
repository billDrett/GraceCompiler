.intel_syntax noprefix
.text
.global main
Label_0: reverse_1: 
push ebp
mov ebp, esp
sub esp, 40
Label_1: mov esi, dword ptr [ebp+16]
push esi
Label_2: lea esi, dword ptr [ebp-16]
push esi
Label_3: mov esi, dword ptr [ebp+8]
push dword ptr [esi+8]
call strlen_grace
add esp, 12
Label_4: mov eax, dword ptr [ebp-16]
mov dword ptr [ebp-8], eax
Label_5: mov eax, 0
mov dword ptr [ebp-4], eax
Label_6: mov eax, dword ptr [ebp-4]
mov edx, dword ptr [ebp-8]
cmp eax, edx
jl Label_8
Label_7: jmp Label_16
Label_8: mov eax, dword ptr [ebp-4]
mov ecx, 1
imul ecx
mov esi, dword ptr [ebp+8]
lea ecx, byte ptr [esi-32]
add eax, ecx
mov dword ptr [ebp-20], eax
Label_9: mov eax, dword ptr [ebp-8]
mov edx, dword ptr [ebp-4]
sub eax, edx
mov dword ptr [ebp-24], eax
Label_10: mov eax, dword ptr [ebp-24]
mov edx, 1
sub eax, edx
mov dword ptr [ebp-28], eax
Label_11: mov eax, dword ptr [ebp-28]
mov ecx, 1
imul ecx
mov ecx, dword ptr [ebp+16]
add eax, ecx
mov dword ptr [ebp-32], eax
Label_12: mov edi, dword ptr [ebp-32]
movzx eax, byte ptr [edi]
mov edi, dword ptr [ebp-20]
mov byte ptr [edi], al
Label_13: mov eax, dword ptr [ebp-4]
mov edx, 1
add eax, edx
mov dword ptr [ebp-36], eax
Label_14: mov eax, dword ptr [ebp-36]
mov dword ptr [ebp-4], eax
Label_15: jmp Label_6
Label_16: mov eax, dword ptr [ebp-4]
mov ecx, 1
imul ecx
mov esi, dword ptr [ebp+8]
lea ecx, byte ptr [esi-32]
add eax, ecx
mov dword ptr [ebp-40], eax
Label_17: mov eax, 0
mov edi, dword ptr [ebp-40]
mov byte ptr [edi], al
Label_18: mov esp, ebp
pop ebp
ret
Label_19: main: 
main_grace: 
push ebp
mov ebp, esp
sub esp, 44
Label_20: mov eax, OFFSET FLAT: l1
mov dword ptr [ebp-36], eax
Label_21: mov esi, dword ptr [ebp-36]
push esi
Label_22: sub esp, 4
push ebp
call reverse_1
add esp, 12
Label_23: lea esi, byte ptr [ebp-32]
push esi
Label_24: sub esp, 4
push dword ptr [ebp+8]
call puts_grace
add esp, 12
Label_25: mov esp, ebp
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

    	mov eax, dword ptr[ebp+12]
    	push eax
	
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
	l1: .asciz "\n!dlrow olleH"
