.intel_syntax noprefix
.text
.global main
Label_0: swap_2: 
push ebp
mov ebp, esp
sub esp, 4
Label_1: mov esi, dword ptr [ebp+16]
mov eax, dword ptr [esi]
mov dword ptr [ebp-4], eax
Label_2: mov esi, dword ptr [ebp+20]
mov eax, dword ptr [esi]
mov esi, dword ptr [ebp+16]
mov dword ptr [esi], eax
Label_3: mov eax, dword ptr [ebp-4]
mov esi, dword ptr [ebp+20]
mov dword ptr [esi], eax
Label_4: mov esp, ebp
pop ebp
ret
Label_5: bsort_1: 
push ebp
mov ebp, esp
sub esp, 40
Label_6: mov eax, 1
mov dword ptr [ebp-4], eax
Label_7: mov eax, dword ptr [ebp-4]
mov edx, 0
cmp eax, edx
jg Label_9
Label_8: jmp Label_30
Label_9: mov eax, 0
mov dword ptr [ebp-4], eax
Label_10: mov eax, 0
mov dword ptr [ebp-8], eax
Label_11: mov eax, dword ptr [ebp+16]
mov edx, 1
sub eax, edx
mov dword ptr [ebp-12], eax
Label_12: mov eax, dword ptr [ebp-8]
mov edx, dword ptr [ebp-12]
cmp eax, edx
jl Label_14
Label_13: jmp Label_29
Label_14: mov eax, dword ptr [ebp-8]
mov ecx, 4
imul ecx
mov ecx, dword ptr [ebp+20]
add eax, ecx
mov dword ptr [ebp-16], eax
Label_15: mov eax, dword ptr [ebp-8]
mov edx, 1
add eax, edx
mov dword ptr [ebp-20], eax
Label_16: mov eax, dword ptr [ebp-20]
mov ecx, 4
imul ecx
mov ecx, dword ptr [ebp+20]
add eax, ecx
mov dword ptr [ebp-24], eax
Label_17: mov edi, dword ptr [ebp-16]
mov eax, dword ptr [edi]
mov edi, dword ptr [ebp-24]
mov edx, dword ptr [edi]
cmp eax, edx
jg Label_19
Label_18: jmp Label_26
Label_19: mov eax, dword ptr [ebp-8]
mov ecx, 4
imul ecx
mov ecx, dword ptr [ebp+20]
add eax, ecx
mov dword ptr [ebp-28], eax
Label_20: mov eax, dword ptr [ebp-8]
mov edx, 1
add eax, edx
mov dword ptr [ebp-32], eax
Label_21: mov eax, dword ptr [ebp-32]
mov ecx, 4
imul ecx
mov ecx, dword ptr [ebp+20]
add eax, ecx
mov dword ptr [ebp-36], eax
Label_22: mov esi, dword ptr [ebp-36]
push esi
Label_23: mov esi, dword ptr [ebp-28]
push esi
Label_24: sub esp, 4
push ebp
call swap_2
add esp, 16
Label_25: mov eax, 1
mov dword ptr [ebp-4], eax
Label_26: mov eax, dword ptr [ebp-8]
mov edx, 1
add eax, edx
mov dword ptr [ebp-40], eax
Label_27: mov eax, dword ptr [ebp-40]
mov dword ptr [ebp-8], eax
Label_28: jmp Label_11
Label_29: jmp Label_7
Label_30: mov esp, ebp
pop ebp
ret
Label_31: putArray_3: 
push ebp
mov ebp, esp
sub esp, 32
Label_32: mov esi, dword ptr [ebp+16]
push esi
Label_33: sub esp, 4
mov esi, dword ptr [ebp+8]
push dword ptr [esi+8]
call puts_grace
add esp, 12
Label_34: mov eax, 0
mov dword ptr [ebp-4], eax
Label_35: mov eax, dword ptr [ebp-4]
mov edx, dword ptr [ebp+20]
cmp eax, edx
jl Label_37
Label_36: jmp Label_48
Label_37: mov eax, dword ptr [ebp-4]
mov edx, 0
cmp eax, edx
jg Label_39
Label_38: jmp Label_42
Label_39: mov eax, OFFSET FLAT: l1
mov dword ptr [ebp-12], eax
Label_40: mov esi, dword ptr [ebp-12]
push esi
Label_41: sub esp, 4
mov esi, dword ptr [ebp+8]
push dword ptr [esi+8]
call puts_grace
add esp, 12
Label_42: mov eax, dword ptr [ebp-4]
mov ecx, 4
imul ecx
mov ecx, dword ptr [ebp+24]
add eax, ecx
mov dword ptr [ebp-20], eax
Label_43: mov edi, dword ptr [ebp-20]
mov eax, dword ptr [edi]
push eax
Label_44: sub esp, 4
mov esi, dword ptr [ebp+8]
push dword ptr [esi+8]
call puti_grace
add esp, 12
Label_45: mov eax, dword ptr [ebp-4]
mov edx, 1
add eax, edx
mov dword ptr [ebp-24], eax
Label_46: mov eax, dword ptr [ebp-24]
mov dword ptr [ebp-4], eax
Label_47: jmp Label_35
Label_48: mov eax, OFFSET FLAT: l2
mov dword ptr [ebp-28], eax
Label_49: mov esi, dword ptr [ebp-28]
push esi
Label_50: sub esp, 4
mov esi, dword ptr [ebp+8]
push dword ptr [esi+8]
call puts_grace
add esp, 12
Label_51: mov esp, ebp
pop ebp
ret
Label_52: main: 
main_grace: 
push ebp
mov ebp, esp
sub esp, 124
Label_53: mov eax, 65
mov dword ptr [ebp-4], eax
Label_54: mov eax, 0
mov dword ptr [ebp-8], eax
Label_55: mov eax, dword ptr [ebp-8]
mov edx, 16
cmp eax, edx
jl Label_57
Label_56: jmp Label_67
Label_57: mov eax, dword ptr [ebp-4]
mov ecx, 137
imul eax, ecx
mov dword ptr [ebp-76], eax
Label_58: mov eax, dword ptr [ebp-76]
mov edx, 220
add eax, edx
mov dword ptr [ebp-80], eax
Label_59: mov eax, dword ptr [ebp-80]
mov edx, dword ptr [ebp-8]
add eax, edx
mov dword ptr [ebp-84], eax
Label_60: mov eax, dword ptr [ebp-84]
cdq
mov ebx, 101
idiv ebx
mov dword ptr [ebp-88], edx
Label_61: mov eax, dword ptr [ebp-88]
mov dword ptr [ebp-4], eax
Label_62: mov eax, dword ptr [ebp-8]
mov ecx, 4
imul ecx
lea ecx, dword ptr [ebp-72]
add eax, ecx
mov dword ptr [ebp-92], eax
Label_63: mov eax, dword ptr [ebp-4]
mov edi, dword ptr [ebp-92]
mov dword ptr [edi], eax
Label_64: mov eax, dword ptr [ebp-8]
mov edx, 1
add eax, edx
mov dword ptr [ebp-96], eax
Label_65: mov eax, dword ptr [ebp-96]
mov dword ptr [ebp-8], eax
Label_66: jmp Label_55
Label_67: mov eax, OFFSET FLAT: l3
mov dword ptr [ebp-100], eax
Label_68: lea esi, dword ptr [ebp-72]
push esi
Label_69: mov eax, 16
push eax
Label_70: mov esi, dword ptr [ebp-100]
push esi
Label_71: sub esp, 4
push ebp
call putArray_3
add esp, 20
Label_72: lea esi, dword ptr [ebp-72]
push esi
Label_73: mov eax, 16
push eax
Label_74: sub esp, 4
push ebp
call bsort_1
add esp, 16
Label_75: mov eax, OFFSET FLAT: l4
mov dword ptr [ebp-116], eax
Label_76: lea esi, dword ptr [ebp-72]
push esi
Label_77: mov eax, 16
push eax
Label_78: mov esi, dword ptr [ebp-116]
push esi
Label_79: sub esp, 4
push ebp
call putArray_3
add esp, 20
Label_80: mov esp, ebp
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
	l1: .asciz ", "
	l2: .asciz "\n"
	l3: .asciz "Initial array: "
	l4: .asciz "Sorted array: "
