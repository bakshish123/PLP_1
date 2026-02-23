program TestEncapsulation;

type
  Person = class
  private
    age : integer;

  public
    constructor Create;
    begin
      Self.age := 25;
    end;
  end;

var
  p : Person;

begin
  p := Person.Create;

  p.age := 30;   { ❌ should fail in semantic phase }
end.