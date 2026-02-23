program TestConstructor;

type
  Person = class
  public
    age : integer;

    constructor Create;
    begin
      Self.age := 100;
    end;
  end;

var
  p : Person;

begin
  p := Person.Create;
end.