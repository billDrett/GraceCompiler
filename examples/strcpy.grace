fun main():nothing
	fun strcpy(ref trg : char[]; ref src : char[]) : nothing
	var i : int;
	var size : int;
	{	   
	   i<-0;
	   size <- strlen(src);
	   while(i < size) do {
		trg[i] <- src[i];
		i<-i+1;
	   }
	   trg[i] <- '\0';
	}

var myarray : char[10];
{
	strcpy(myarray, "hello");
	puts(myarray);

}

