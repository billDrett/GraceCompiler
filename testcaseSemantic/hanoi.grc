fun solve () : nothing
      fun hanoi (rings : int; ref source, target, auxiliary : char[]) : nothing
         fun move (ref source, target : char[]) : nothing
         {
            puts("Moving from ");
            puts(source);
            puts(" to ");
            puts(target);
            puts(".\n");
         }
      {
         if rings >= 1 then {
            hanoi(rings-1, source, auxiliary, target);
            move(source, target);
            hanoi(rings-1, auxiliary, target, source);
         }
      }

      var NumberOfRings : int;
{
  puts("Rings: ");
  NumberOfRings <- geti();
  hanoi(NumberOfRings, "left", "right", "middle");
}
