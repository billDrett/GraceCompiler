fun hello () : nothing
var array:char[10];
{
   array[0]<-'h';
   array[1]<-'e';
   array[2]<-'l';
   array[3]<-array[2];
   array[4]<-'o';
   array[5]<-'\n';
   array[6]<-'\0';
   
   puts(array);	
}


