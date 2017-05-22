fun solve () : nothing 
var i,n :int;
      fun hanoi (rings : int; ref source, target, auxiliary : char[]) : nothing 
         fun move (ref source, target : char[]) : nothing{ i <-1;}
	{i <-2;}
{
   $$   if n<0              then 
	    i<- 0;
      else 
	    if n<2         then 
		i<- 1;
      	    else
	    {
		if n=2         then 
			i<- 2;
      		else 
			if n mod 2 = 0 then 
				i<- 3;
      			else {
        			i <- 4;
        
       
      			}
          
        	i<-100;
	   }

	i<-10000;
$$
	i <-12;
	while not(n < 10)  do
	{
		n <- n+1;
	}
	i<-1;
}

