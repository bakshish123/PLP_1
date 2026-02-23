program TestObject;

type
  Person = class
  public
    age : integer;

    constructor Create;
    begin
      Self.age := 20;
    end;
  end;

var
  p : Person;

begin
  p := Person.Create;
  p.age := 30;
end.