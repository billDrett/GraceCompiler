fun solve () : nothing
      fun hanoi (rings : int; ref source, target, auxiliary : char[]) : nothing
         fun move (ref source, target : char[]) : nothing
     	 var NumberOfRings1 : int;
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
  writeString("Rings: ");
  NumberOfRings <- geti();
  hanoi(NumberOfRings, "left", "right", "middle");
}
