program TestDestructor;

type
  Person = class
  public
    age : integer;

    constructor Create;
    begin
      Self.age := 10;
    end;

    destructor Destroy;
    begin
      Self.age := 0;
    end;
  end;

var
  p : Person;

begin
  p := Person.Create;
end.