program TestEncapsulation;

type
  Person = class
  private
    age : integer;
  public
    procedure SetAge;
    function GetAge : integer;
  end;

var
  p : Person;
  x : integer;

begin
end.