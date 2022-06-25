DROP Database if exists DatabaseSalernarte;
CREATE DATABASE DatabaseSalernarte
CHARACTER SET utf8
COLLATE utf8_general_ci;
use DatabaseSalernarte;

create table UtenteRegistrato(
	id int not null auto_increment,
	nome varchar(50) not null,
    cognome varchar(50) not null,
    email varchar(100) not null unique,
    passwordHash varchar(50) not null,
    dataDiNascita date not null,    
    sesso int not null, #0(maschio), 1(femmina), 2(non specificato)
    primary key(id)
);


create table Scolaresca(
	id int not null auto_increment,  
    email varchar(100) not null unique,
    passwordHash varchar(50) not null,
    istituto varchar(100) not null,
    primary key(id)
);

create table Amministratore(
	id int not null auto_increment, 
	nome varchar(50) not null,
    cognome varchar(50) not null,
    email varchar(100) not null unique,
    passwordHash varchar(50) not null,
    primary key(id)
);

create table Organizzatore(
	id int not null auto_increment,
	nome varchar(50) not null,
    cognome varchar(50) not null,
    email varchar(100) not null unique,
    passwordHash varchar(50) not null,
    biografia longtext not null,
    dataDiNascita date not null,    
    sesso int not null, #0(maschio), 1(femmina), 2(non specificato)
    azienda varchar(100) not null,
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

create table RichiestaEvento( #non c'è nel class 
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

create table Biglietto(
	id int not null auto_increment,
    evento int not null,
    costo decimal(10,2) not null, 
    fattura int , 
    primary key(id,evento),
    foreign key(evento) references Evento(id)
		on delete cascade
        on update cascade
);


create table Fattura(
	numOrdine int not null auto_increment,
    data date not null,
    totale decimal(10,2) not null, 
    idUtente int not null, 
    prodotti longtext,
    tipoUtente boolean not null, #true= utenteRegistrao, false=Scolaresca
    primary key(numOrdine)
);

CREATE TABLE Carrello(
idUser int not null,
idProd int not null, ##id mostra, poi da qui ricavo i biglietti disponibili" dinamicamente
quantita int not null, # e diversa da 0
tipoUtente boolean not null, #attenzione al codice ricorda di implementare
primary key(idUser,idProd,tipoUtente),
foreign key(idProd) references Evento(id)
on delete cascade
on update cascade
);

insert into amministratore(nome,cognome,email,passwordHash) value
('Alessia','Della Pepa', 'a.dellapepa5@studenti.unisa.it',sha1('AlessiaAdmin')),
('Marco','Longo', 'm.longo36@studenti.unisa.it',sha1('MarcoAdmin')),
('Lucia','Martino', 'l.martino11@studenti.unisa.it',sha1('LuciaAdmin'));

insert into UtenteRegistrato(nome,cognome,email,passwordHash,dataDiNascita,sesso) values
('pippo','prova','pippoprova@example.com',sha1('pippo01'),'1999-07-21',0);

insert into Organizzatore(nome,cognome,email,passwordHash,biografia,dataDiNascita,sesso,azienda,iban) values
('pluto','prova','plutoprova@example.com',sha1('pluto'),'prova biografia','1999-07-21',0,'azienda prova','IT17J0300203280772191565161');

insert into Evento(idOrganizzatore,nome,tipo,descrizione,pathFoto,numBiglietti,dataInizio,dataFine,indirizzo,sede,attivo) values
(1,'evento di pluto',true,'descrizione evento prova','path prova',3,'2022-07-21','2022-07-26','indirizzo prova','sede prova',true),
(1,'evento di pluto 2',true,'descrizione evento prova 2','path prova 2',3,'2022-07-22','2022-07-27','indirizzo prova 2','sede prova 2',true);

insert into Biglietto(id,evento,costo,fattura) values
(1,1,3.6,null),
(2,1,3.6,null),
(3,1,3.6,null),
(1,2,4,null),
(2,2,4,null),
(3,2,4,null);





