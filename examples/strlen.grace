fun main():nothing
	fun strlen(ref s : char[]) : int
	var counter : int;
	{
	   counter<-0;
	   while(s[counter] # '\0') do {
	        counter <- counter+1;
	   }
	   return counter;
	}
{

}
