
%Variaveis dinamicas para alterar durante execucao do programa.
:- dynamic movimentos/3 ,balancototal/2, balancocredito/2.


%Factos da informacao do cliente
infocliente(
    123,
    alice,
    nyc_123,
    new_york,
    "01-01-2020"
).

infocliente(
    456,
    bob,
    chi_456,
    chicago,
    "02-02-2020"
).

infocliente(
    789,
    charlie,
    la_789,
    los_angeles,
    "03-03-2020"
).

infocliente(
    752,
    john,
    chi_456,
    chicago,
    "03-03-2020"
).

%factos do balanco total dos clientes
balancototal(
    123,
    2500
).

balancototal(
    456,
    500
).

balancototal(
    789,
    50
).

balancototal(
    752,
    5000
).

%factos dos balancos de credito dos clientes
balancocredito(
    123,
    0
).

balancocredito(
  456,
  5000  
).

balancocredito(
  789,
  2000  
).

%factos dos movimentos dos clientes
movimentos(
    123,
    -100,
    "01-01-2020"
).

movimentos(
    456,
    200,
    "02-02-2020"
).

movimentos(
    789,
    -10,
    "03-03-2020"
).

movimentos(
    789,
    -20,
    "04-03-2020"
).


%Ex 2.a.
getInformacoes(ID,Nome,Posto,Cidade,Data):-
    infocliente(
        ID,
        Nome,
        Posto,
        Cidade,
        Data
    ).
    
%Ex 3.a
getClientesCidade(ID,Nome,Cidade):-
    getInformacoes(ID,Nome,_,Cidade,_).


%Ex 4.a
getElegiveis(ID,Nome,Posto,Cidade,Data):-
    getInformacoes(ID,Nome,Posto,Cidade,Data),
    ((balancocredito(ID,Val),
    Val =:= 0);
    \+balancocredito(ID,_)),
    balancototal(ID,BalTot),
    BalTot > 100.

%Ex 10.a
getElegivel(ID):-
    ((balancocredito(ID,Val),
    Val =:= 0);
    \+balancocredito(ID,_)),
    balancototal(ID,BalTot),
    BalTot > 100.

%Ex 6.a    
getSaldoReal(ID,Saldo):-
    balancototal(ID,SaldoTot),
    (balancocredito(ID,SaldoCred) ->
        Saldo is SaldoTot - SaldoCred;
        Saldo is SaldoTot).

%Ex 7.a
getSaldoCredito(ID,Saldo):-
    balancocredito(ID,Saldo) ->
    Saldo is Saldo;
    Saldo is 0.

%Ex 8.a
getMovimentos(ID,Saldo,Data):-
    movimentos(ID,Saldo,Data).

%Ex 9.a
deposito(ID,Valor):-
    balancototal(ID,Saldo),
    NewSaldo is Saldo+Valor,
    getCurrentDate(Data),
    assertz(movimentos(ID,Valor,Data)),
    retract(balancototal(ID,Saldo)),
    assertz(balancototal(ID,NewSaldo)).

%Ex 9.b
levantamento(ID,Valor):-
    balancototal(ID,Saldo),
    NewSaldo is Saldo-Valor,
    NewSaldo >= 0,
    getCurrentDate(Data),
    ValorLevantamento is - Valor,
    assertz(movimentos(ID,ValorLevantamento,Data)),
    retract(balancototal(ID,Saldo)),
    assertz(balancototal(ID,NewSaldo)).

%Ex 10.b
concedercredito(ID,Valor):-
    balancototal(ID,Saldo),
    NewSaldo is Saldo+Valor,
    balancocredito(ID,ValCredito),
    NewValCredito is ValCredito+Valor,
    retract(balancocredito(ID,ValCredito)),
    assertz(balancocredito(ID,NewValCredito)),
    retract(balancototal(ID,Saldo)),
    assertz(balancototal(ID,NewSaldo)).

%Predicado para obter data atual
getCurrentDate(Date) :-
    get_time(Timestamp),
    stamp_date_time(Timestamp, DateTime, 'UTC'),
    date_time_value(year, DateTime, Year),
    date_time_value(month, DateTime, Month),
    date_time_value(day, DateTime, Day),
    formatar_data(Day,Month,Year,Date).

%Preficado para formatar data
formatar_data(Day, Month, Year, FormattedDate) :-
    atom_number(DayAtom, Day),
    atom_number(MonthAtom, Month),
    atom_number(YearAtom, Year),
    atom_concat(DayAtom, '-', Temp1),
    atom_concat(Temp1, MonthAtom, Temp2),
    atom_concat(Temp2, '-', Temp3),
    atom_concat(Temp3, YearAtom, FormattedDate).


