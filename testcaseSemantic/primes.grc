fun main () : nothing
    fun prime (n : int) : int
      var i : int;
    {
      if n<0              then return prime(-1);
      else if n<2         then return 0;
      else if n=2         then return 1;
      else if n mod 2 = 0 then return 0;
      else {
        i <- 3;
        while i <= n div 2 do {
          if n mod i = 0 then
            return 0;
          i <- i + 2;
        }
        return 1;
      }
    }

    var limit, number, counter : int;

{
   puts("Limit: ");
   limit <- geti();
   puts("Primes:\n");
   counter <- 0;
   if limit >= 2 then {
      counter <- counter + 1;
      puti(2);
      puts("\n");
   }
   if limit >= 3 then {
      counter <- counter + 1;
      puti(3);
      puts("\n");
   }
   number <- 6;
   while number <= limit do {
      if prime(number - 1) = 1 then {
         counter <- counter + 1;
         puti(number - 1);
         puts("\n");
      }
      if number # limit and prime(number + 1) = 1 then {
         counter <- counter + 1;
         puti(number + 1);
         puts("\n");
      }
      number <- number + 6;
   }
   puts("\nTotal: ");
   puti(counter);
   puts("\n");
}
