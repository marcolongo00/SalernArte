DROP Database if exists DatabaseSalernarte;
CREATE DATABASE DatabaseSalernarte
CHARACTER SET utf8
COLLATE utf8_general_ci;
use DatabaseSalernarte;

create table UtenteRegistrato(
	id int not null auto_increment,
    email varchar(100) not null unique,
    passwordHash varchar(50) not null,
    tipoUtente varchar(20) not null,
    primary key(id)
);

create table Utente(
	id int not null auto_increment,
	nome varchar(50) not null,
    cognome varchar(50) not null,
    dataDiNascita date not null,    
    sesso int not null, #0(maschio), 1(femmina), 2(non specificato)
    primary key(id),
    foreign key(id) references UtenteRegistrato(id)
    on delete cascade
    on update cascade
);


create table Scolaresca(
	id int not null auto_increment, #codice meccanografico
    istituto varchar(100) not null,
    primary key(id),
    foreign key(id) references UtenteRegistrato(id)
    on delete cascade
    on update cascade
);

create table Amministratore(
	id int not null auto_increment, 
	nome varchar(50) not null,
    cognome varchar(50) not null,
    primary key(id),
    foreign key(id) references UtenteRegistrato(id)
    on delete cascade
    on update cascade
);

create table Organizzatore(
	id int not null auto_increment,
	nome varchar(50) not null,
    cognome varchar(50) not null,
    biografia longtext not null,
    dataDiNascita date not null,    
    sesso int not null, #0(maschio), 1(femmina), 2(non specificato)
    iban varchar(27) not null, 
    primary key(id)
);

create table Evento(
	id int not null auto_increment,
    idOrganizzatore int not null, #manca nel class
	nome varchar(50) not null,
    tipo boolean not null, # true=teatro, false=mostra
    descrizione varchar(320) not null,
    pathFoto varchar(200) not null,
    numBiglietti int not null,
    dataInizio date not null,
	dataFine date not null,
    indirizzo varchar(100) not null,
    sede varchar(100) not null,
    attivo boolean default false, #anche per la modifica
    primary key(id),
    foreign key(idOrganizzatore) references Organizzatore(id)
		on delete cascade
        on update cascade,
    FULLTEXT (nome,descrizione)
);

create table RichiestaEvento( #non c'è nel class da controllare una votla fatto il codice
	id int not null auto_increment,
    idEvento int not null,
    idEventoTemp int not null,
    primary key(id,idEvento,idEventoTemp),
    foreign key (idEvento) references Evento(id)
    on delete cascade
    on update cascade,
    foreign key (idEventoTemp) references Evento(id)
    on delete cascade
    on update cascade
	);
    
    
create table Acquisto(
	numOrdine int not null auto_increment,
    data date not null,
    totale decimal(10,2) not null, 
    idUtente int not null, 
    prodotti longtext,
    primary key(numOrdine),
    foreign key(idUtente) references UtenteRegistrato(id)
    on delete cascade
    on update cascade
);

create table Biglietto(
	id int not null auto_increment,
    evento int not null,
    costo decimal(10,2) not null, 
    acquisto int , 
    primary key(id,evento),
    foreign key(evento) references Evento(id)
		on delete cascade
        on update cascade,
	foreign key(acquisto) references Acquisto(numOrdine)
    on update cascade
);


CREATE TABLE Carrello(
idUtente int not null,
idEvento int not null, ##id evento, poi da qui ricavo i biglietti disponibili" dinamicamente
quantita int not null, # e diversa da 0
primary key(idUtente,idEvento),
foreign key(idUtente) references UtenteRegistrato(id)
on delete cascade
on update cascade,
foreign key(idEvento) references Evento(id)
on delete cascade
on update cascade
);

insert into UtenteRegistrato(id,email,passwordHash,tipoUtente)values
(1,'a.dellapepa5@studenti.unisa.it',sha1('AlessiaAdmin'),'amministratore'),
(2,'m.longo36@studenti.unisa.it',sha1('MarcoAdmin'),'amministratore'),
(3,'l.martino11@studenti.unisa.it',sha1('LuciaAdmin'),'amministratore'),
(4,'pippoprova@example.com',sha1('pippo01'),'utente'),
(5,'plutoprova@example.com',sha1('pluto'),'organizzatore');


insert into amministratore(id,nome,cognome) values
(1,'Alessia','Della Pepa'),
(2,'Marco','Longo'),
(3,'Lucia','Martino');

insert into Utente(id,nome,cognome,dataDiNascita,sesso) values
(4,'pippo','prova','1999-07-21',0);

insert into Organizzatore(id,nome,cognome,biografia,dataDiNascita,sesso,iban) values
(5,'pluto','prova','prova biografia','1999-07-21',0,'IT17J0300203280772191565161');

insert into Evento(idOrganizzatore,nome,tipo,descrizione,pathFoto,numBiglietti,dataInizio,dataFine,indirizzo,sede,attivo) values
(5,'evento di pluto',true,'descrizione evento prova','path prova',3,'2022-07-21','2022-07-26','indirizzo prova','sede prova',false),
(5,'evento di pluto 2',false,'descrizione evento prova 2','path prova 2',3,'2022-07-22','2022-07-27','indirizzo prova 2','sede prova 2',false),
(5,'evento di pluto 2 modificato',false,'descrizione evento prova 2 modificato','path prova 2',3,'2022-07-22','2022-07-27','indirizzo prova 2','sede prova 2',false);

insert into RichiestaEvento(idEvento,idEventoTemp) values
(2,3);
 
 
# eventi da inserire ma non modifica
SELECT 
    *
FROM
    Evento as e
LEFT JOIN
    RichiestaEvento as r on e.id=r.idEvento
WHERE
    r.idEvento IS NULL and e.id not in(
										SELECT idEventoTemp
                                        FROM RichiestaEvento);
#eventi modifica, solo evento modificato da controlalre
SELECT 
    *
FROM
    RichiestaEvento as r
LEFT JOIN
    Evento as e on r.idEventoTemp=e.id;
    
#eventi da modificare, altrimeti retrive solo le richieste
#l'id della richiesta deve rimanre separato o va collegato solo con l'id degli evenit o solo con l'evento da modificare??
select *
from evento
where id in( select idEventoTemp from RichiestaEvento);

insert into Biglietto(id,evento,costo,acquisto) values
(1,1,3.6,null),
(2,1,3.6,null),
(3,1,3.6,null),
(1,2,4,null),
(2,2,4,null),
(3,2,4,null);




update UtenteRegistrato join Utente using(id)
set nome='culo' , email='culo@exampleupdate.com'
where id=4;

#test join retrive utente
SELECT *
FROM UtenteRegistrato JOIN Utente USING(id);

SELECT * FROM UtenteRegistrato JOIN Organizzatore USING(id) WHERE email='plutoprova@example.com' AND passwordHash=SHA1('pluto');
SELECT * FROM Evento as e JOIN Carrello as c on e.id=c.idevento WHERE c.idUtente=1;









