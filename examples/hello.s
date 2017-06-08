.intel_syntax noprefix # Use Intel syntax instead of AT&T
.text
.global main
main:
push ebp
mov ebp, esp
sub esp, 56
Label_1: mov eax, 1
mov ecx, 4
imul ecx
lea ecx, dword ptr [ebp-20]
add eax, ecx
mov dword ptr [ebp-28], eax
Label_2: mov eax, 1
mov edi, dword ptr [ebp-28]
mov dword ptr [edi], eax
Label_3: mov eax, 0
mov ecx, 4
imul ecx
lea ecx, dword ptr [ebp-20]
add eax, ecx
mov dword ptr [ebp-32], eax
Label_4: mov eax, 0
mov edi, dword ptr [ebp-32]
mov dword ptr [edi], eax
Label_5: mov eax, 2
mov ecx, 4
imul ecx
lea ecx, dword ptr [ebp-20]
add eax, ecx
mov dword ptr [ebp-36], eax
Label_6: mov eax, 200
mov edi, dword ptr [ebp-36]
mov dword ptr [edi], eax
Label_7: mov eax, 0
mov ecx, 4
imul ecx
lea ecx, dword ptr [ebp-20]
add eax, ecx
mov dword ptr [ebp-40], eax
Label_8: mov edi, dword ptr [ebp-40]
mov eax, dword ptr [edi]
mov edx, 0
cmp eax, edx
je Label_10
Label_9: jmp Label_31
Label_10: mov eax, 1
mov edx, 1
cmp eax, edx
je Label_12
Label_11: jmp Label_31
Label_12: mov eax, 1
mov ecx, 4
imul ecx
lea ecx, dword ptr [ebp-20]
add eax, ecx
mov dword ptr [ebp-44], eax
Label_13: mov edi, dword ptr [ebp-44]
mov eax, dword ptr [edi]
mov edx, 4
cmp eax, edx
jg Label_31
Label_14: jmp Label_15
Label_15: mov eax, 20
mov dword ptr [ebp-24], eax
Label_16: mov eax, 2
mov ecx, 4
imul ecx
lea ecx, dword ptr [ebp-20]
add eax, ecx
mov dword ptr [ebp-48], eax
Label_17: mov edi, dword ptr [ebp-48]
mov eax, dword ptr [edi]
mov edx, 2
cmp eax, edx
jge Label_23
Label_18: jmp Label_19
Label_19: mov eax, dword ptr [ebp-24]
cdq
mov ebx, 10
idiv ebx
mov dword ptr [ebp-52], eax
Label_20: mov eax, dword ptr [ebp-52]
mov ecx, 4
imul ecx
lea ecx, dword ptr [ebp-20]
add eax, ecx
mov dword ptr [ebp-56], eax
Label_21: mov edi, dword ptr [ebp-56]
mov eax, dword ptr [edi]
mov dword ptr [ebp-24], eax
Label_22: jmp Label_31
Label_23: mov eax, 2
mov edx, 1
cmp eax, edx
jl Label_29
Label_24: jmp Label_25
Label_25: mov eax, 1
mov edx, 1
cmp eax, edx
je Label_27
Label_26: jmp Label_30
Label_27: mov eax, 1
mov edx, 1
cmp eax, edx
jl Label_29
Label_28: jmp Label_30
Label_29: jmp Label_31
Label_30: mov eax, 16
mov dword ptr [ebp-24], eax
Label_31: mov esp, ebp
pop ebp
ret










.data
fmt: .asciz "1Hello world!\n"
