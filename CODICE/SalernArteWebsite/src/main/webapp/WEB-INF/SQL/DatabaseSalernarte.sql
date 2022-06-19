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
	id int not null auto_increment,  #id per scuole non conviene
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
    iban int not null,
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
    indirizzo varchar(100) not null unique,
    sede varchar(100) not null,
    attivo boolean default false, #manca nel class anche se l'avevamo detto
    primary key(id),
    foreign key(idOrganizzatore) references Organizzatore(id)
		on delete cascade
        on update cascade,
    FULLTEXT (nome,descrizione)
);

create table RichiestaEvento( #non c'è nel class 
	id int not null auto_increment,
    idEvento int not null,
    primary key(id,idEvento),
    foreign key (idEvento) references Evento(id)
    on delete cascade
    on update cascade
	);

create table Biglietto(
	id int not null auto_increment,
    evento int not null,
    costo decimal(10,2) not null, #in teoria l'abbiamo messo nella mostra
    fattura int , #servirebbe per la funzione visualizza acquisti effettuati
    primary key(id,evento),
    foreign key(evento) references Evento(id)
		on delete cascade
        on update cascade
);

#non presente nel class diagram
create table Fattura(
	numOrdine int not null auto_increment,
    data date not null,
    totale decimal(10,2) not null, 
    idUtente int not null, 
    prodotti longtext,
    tipoUtente boolean not null, #true= utenteRegistrao, false=Scolaresca
    primary key(numOrdine)
);

#nel class manca anche il carrello
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



