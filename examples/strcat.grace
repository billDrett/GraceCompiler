
fun main():nothing
	fun strcat(ref trg : char[]; ref src : char[]) : nothing
	var i : int;
	var trgsize : int;
	var srcsize : int;
	{	   
	   i<-0;
	   trgsize <- strlen(trg);
	   srcsize <- strlen(src);
	   while(i < srcsize) do {
			trg[i+trgsize] <- src[i];
			i<-i+1;
	   }
	   trg[i+trgsize] <- '\0';
	}
var as:char[10];    
{
   strcpy(as,"hi");
   strcat(as, " mitsos");
   puts(as);
}
