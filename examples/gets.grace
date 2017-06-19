fun main():nothing


	fun gets(n : int; ref s : char[]) : nothing
	var tmpChar:char;
	var i:int;
	{
	   i<-0;
	   tmpChar <- getc();
	   while(tmpChar # '\n' and i < n-1) do 
	   {
		s[i] <- tmpChar;
	        i<-i+1;
		if (i < n-1) then tmpChar <- getc();
	   }
	   s[i] <-'\0';
	}
{
}
