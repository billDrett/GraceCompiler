fun main():nothing
	fun strcmp(ref s1,s2 : char[]) : int
	var i : int;
	{	   
	   i <- 0;
	   while(s1[i] # '\0' and s2[i] # '\0') do {
			if (s1[i] # s2[i]) then
				return ord(s1[i]) - ord(s2[i]);
			i<-i+1;
	   }
	   return ord(s1[i]) - ord(s2[i]);
	}
{
	puti(strcmp("helloA", "hello"));
	puti(strcmp("hello", "hello"));
}

