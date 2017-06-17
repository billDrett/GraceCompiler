fun main():nothing


	fun puts (ref s : char[]) : nothing
	var tmpChar:char[5];
	var i:int;
	{
	
	   i<-0;
	   while(s[i] # '\0') do {
		putc(s[i]);
	        i<-i+1;
	   }
	}

{
 
}
