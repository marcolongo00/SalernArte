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
    primary key(id),
     foreign key(id) references UtenteRegistrato(id)
    on delete cascade
    on update cascade
);

create table Evento(
	id int not null auto_increment,
    idOrganizzatore int not null, #manca nel class
	nome varchar(50) not null,
    tipo boolean not null, # true=teatro, false=mostra
    descrizione varchar(1000) not null,
    pathFoto varchar(200) not null,
    numBiglietti int not null,
    dataInizio date not null,
	dataFine date not null,
    indirizzo varchar(100) not null,
    sede varchar(200) not null,
    attivo boolean default false, #anche per la modifica
    primary key(id),
    foreign key(idOrganizzatore) references Organizzatore(id)
		on delete cascade
        on update cascade,
    FULLTEXT (nome,descrizione)
);

create table RichiestaEvento( 
	id int not null auto_increment,
    idEvento int not null unique,
    idEventoTemp int not null unique,
    nuovoPrezzoBiglietto decimal(10,2) not null,
    primary key(id),
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
    on delete cascade
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
(1,'a.dellapepa5@studenti.unisa.it',sha1('AlessiaAdmin.1'),'amministratore'),
(2,'m.longo36@studenti.unisa.it',sha1('MarcoAdmin.1'),'amministratore'),
(3,'l.martino11@studenti.unisa.it',sha1('LuciaAdmin.1'),'amministratore'),
(4,'pippoUtente@example.com',sha1('Pippo.1'),'utente'),
(5,'plutoOrg@example.com',sha1('Pluto.1'),'organizzatore'),
(6, 'insegnante@example.com', sha1('Scuola.1'),'scolaresca');


insert into amministratore(id,nome,cognome) values
(1,'Alessia','Della Pepa'),
(2,'Marco','Longo'),
(3,'Lucia','Martino');

insert into Utente(id,nome,cognome,dataDiNascita,sesso) values
(4,'Pippo','Utente','1999-07-21',0);

insert into Scolaresca(id,istituto) values
(6,'sandro penna');

insert into Organizzatore(id,nome,cognome,biografia,dataDiNascita,sesso,iban) values
(5,'pluto','Organizzatore','prova biografia','1999-07-21',0,'IT17J0300203280772191565161');

insert into Evento(id,idOrganizzatore,nome,tipo,descrizione,pathFoto,numBiglietti,dataInizio,dataFine,indirizzo,sede,attivo) values
(1, 5, "Uomo e galantuomo", 1, "Uomo e galantuomo narra la storia di una compagnia di guitti scritturati per una serie di recite in uno stabilimento balneare. \r\n\r\nUna commedia dal sapore \u00e2\u0080\u0098scarpettiano\u00e2\u0080\u0099, in cui si ritrovano temi cari a Eduardo: l\u00e2\u0080\u0099atavica lotta tra la faticosa miseria di chi tira a campare e la fatua ricchezza di chi pu\u00c3\u00b2 giocare con la vita delle persone; il perbenismo farisaico di nobili e borghesi; l\u00e2\u0080\u0099irriverente critica a un teatro declamatorio o sciatto e cialtronesco.", "./immaginiEventi/teatro.it-uomo-e-galantuomo-geppy-gleijeses-date-biglietti.jpg", 20, "2023-03-18", "2023-04-16", "Piazza Matteo Luciani, 23, 84121 Salerno SA", "al teatro Giuseppe Verdi", 1),
 (2, 5, "Perfetti sconosciuti", 1, "Paolo Genovese debutta come regista teatrale portando in scena l\u00e2\u0080\u0099adattamento del suo film Perfetti sconosciuti del 2016. Una brillante commedia sull\u00e2\u0080\u0099amicizia, sull\u00e2\u0080\u0099amore e sul tradimento, che porter\u00c3\u00a0 quattro coppie di amici a confrontarsi e a scoprire di essere \u00e2\u0080\u009cPerfetti sconosciuti\u00e2\u0080\u009d. \r\n\r\nOgnuno di noi ha tre vite: una pubblica, una privata e una segreta. Un tempo quella segreta era ben protetta nell\u00e2\u0080\u0099archivio della nostra memoria, oggi nelle sim dei nostri telefoni. \r\n\r\nCosa succederebbe se quella minuscola schedina si mettesse a parlare? Eva e Rocco decidono di invitare a cena alcuni amici di vecchia data: Cosimo con Bianca, Lele con Carlotta e Peppe. A un certo punto della serata Eva propone di fare un gioco della verit\u00c3\u00a0 ovvero mettere tutti i cellulari sul tavolo e, per la durata della cena, condividere messaggi e telefonate.", "./immaginiEventi/Teatro.it-perfetti-sconosciuti-paolo-genovese-date-tour-biglietti.jpg", 50, "2023-03-12", "2023-04-30", "Piazza Matteo Luciani, 23, 84121 Salerno SA", "al teatro Giuseppe Verdi", 1),
 (3, 5, "Il berretto a sonagli ", 1, "Ciampa, umile scrivano che ricorre alla follia per mantenere la facciata di rispettabilit\u00c3\u00a0 del suo triste matrimonio, \u00c3\u00a8 il primo dei grandi personaggi pirandelliani che si prende un\u00e2\u0080\u0099amara rivincita dalle umiliazioni di una vita.", "./immaginiEventi/teatro.it-il-berretto-a-sonagli-man.jpg", 100, "2023-05-25", "2023-06-16", "Piazza Matteo Luciani, 23, 84121 Salerno SA", "al teatro Giuseppe Verdi", 1),
 (4, 5, "Klimt Virtual Experience", 0, "Uno straordinario viaggio nel mondo di Gustav Klimt realizzato con il patrocinio del Comune di Salerno, promosso dalla ProCulTur e prodotto e organizzato dalla Alta Classe Lab con la Next Event. Una manifestazione che condurr\u00c3\u00a0 i visitatori nell'affascinante mondo del pittore austriaco considerato uno dei pi\u00c3\u00b9 significativi artisti della secessione viennese.", "./immaginiEventi/KLIMT.jpg", 50, "2023-05-21", "2023-06-24", "Piazza Conforti Abate, 21", "Chiesa dell\u00e2\u0080\u0099Addolorata all\u00e2\u0080\u0099interno del Complesso Monumentale di Santa Sofia", 1),
 (5, 5, "STREET ART", 0, "La mostra nasce dal desiderio e dall\u00e2\u0080\u0099esigenza di IABO di raccogliere all\u00e2\u0080\u0099interno di un unico spazio espositivo la storia dei suoi ultimi 20 anni di attivit\u00c3\u00a0, operando una sintesi complessa, in grado di mettere in luce, in modo chiaro ed esaustivo, ricerche, tappe salienti e traguardi che hanno segnato il suo percorso artistico, senza tralasciare, invece evocandole, quelle esperienze che, prima del 2003 e nel corso degli anni successivi, hanno contribuito a rafforzare la sua potenza espressiva e le sue doti comunicative.", "./immaginiEventi/136355-invito.jpg", 30, "2023-02-27", "2023-03-16", "Piazza Matteo Luciani, 23, 84121 Salerno SA", "al teatro Giuseppe Verdi", 1),
 (6, 5, "IL TEMPO SOSPESO", 0, "Il Focus presso Magazzini Fotografici, con una scelta di scatti in gran parte inediti, rende conto per la prima volta del mondo interiore di Luciano e del suo universo privato. Egli per tutta la vita visse proiettato fuori, verso \u00e2\u0080\u0098la sua gente\u00e2\u0080\u0099 e verso quella di paesi anche lontanissimi dalla sua amata Napoli.\r\nL\u00e2\u0080\u0099obiettivo \u00c3\u00a8 quello di sondare l\u00e2\u0080\u0099archivio alla ricerca di una diramazione inesplorata, per far luce sui momenti di pausa, le relazioni private, gli affetti degli amici pi\u00c3\u00b9 cari, la lunga relazione con Macha Merril, i bambini, i viaggi del cuore, il tempo sospeso.", "./immaginiEventi/136365-Il_tempo_sospeso_Focus_su_Luciano_D_Alessandro_Magazzini_Fotografici_2_.jpg", 76, "2023-02-25", "2023-04-28", "Piazza Matteo Luciani, 23, 84121 Salerno SA", "al teatro Giuseppe Verdi", 1);

#insert into RichiestaEvento(idEvento,idEventoTemp,nuovoPrezzoBiglietto) values;
#insert into Acquisto(numOrdine,data,totale,idUtente,prodotti) value; 
 insert into Biglietto(id,evento,costo,acquisto) values
 (1, 1, 24.0, null),
 (2, 1, 24.0, null),
 (3, 1, 24.0, null),
 (4, 1, 24.0, null),
 (5, 1, 24.0, null),
 (6, 1, 24.0, null),
 (7, 1, 24.0, null),
 (8, 1, 24.0, null),
 (9, 1, 24.0, null),
 (10, 1, 24.0, null),
 (11, 1, 24.0, null),
 (12, 1, 24.0, null),
 (13, 1, 24.0, null),
 (14, 1, 24.0, null),
 (15, 1, 24.0, null),
 (16, 1, 24.0, null),
 (17, 1, 24.0, null),
 (18, 1, 24.0, null),
 (19, 1, 24.0, null),
 (20, 1, 24.0, null),
 (21, 2, 15.0, null),
 (22, 2, 15.0, null),
 (23, 2, 15.0, null),
 (24, 2, 15.0, null),
 (25, 2, 15.0, null),
 (26, 2, 15.0, null),
 (27, 2, 15.0, null),
 (28, 2, 15.0, null),
 (29, 2, 15.0, null),
 (30, 2, 15.0, null),
 (31, 2, 15.0, null),
 (32, 2, 15.0, null),
 (33, 2, 15.0, null),
 (34, 2, 15.0, null),
 (35, 2, 15.0, null),
 (36, 2, 15.0, null),
 (37, 2, 15.0, null),
 (38, 2, 15.0, null),
 (39, 2, 15.0, null),
 (40, 2, 15.0, null),
 (41, 2, 15.0, null),
 (42, 2, 15.0, null),
 (43, 2, 15.0, null),
 (44, 2, 15.0, null),
 (45, 2, 15.0, null),
 (46, 2, 15.0, null),
 (47, 2, 15.0, null),
 (48, 2, 15.0, null),
 (49, 2, 15.0, null),
 (50, 2, 15.0, null),
 (51, 2, 15.0, null),
 (52, 2, 15.0, null),
 (53, 2, 15.0, null),
 (54, 2, 15.0, null),
 (55, 2, 15.0, null),
 (56, 2, 15.0, null),
 (57, 2, 15.0, null),
 (58, 2, 15.0, null),
 (59, 2, 15.0, null),
 (60, 2, 15.0, null),
 (61, 2, 15.0, null),
 (62, 2, 15.0, null),
 (63, 2, 15.0, null),
 (64, 2, 15.0, null),
 (65, 2, 15.0, null),
 (66, 2, 15.0, null),
 (67, 2, 15.0, null),
 (68, 2, 15.0, null),
 (69, 2, 15.0, null),
 (70, 2, 15.0, null),
 (71, 3, 34.0, null),
 (72, 3, 34.0, null),
 (73, 3, 34.0, null),
 (74, 3, 34.0, null),
 (75, 3, 34.0, null),
 (76, 3, 34.0, null),
 (77, 3, 34.0, null),
 (78, 3, 34.0, null),
 (79, 3, 34.0, null),
 (80, 3, 34.0, null),
 (81, 3, 34.0, null),
 (82, 3, 34.0, null),
 (83, 3, 34.0, null),
 (84, 3, 34.0, null),
 (85, 3, 34.0, null),
 (86, 3, 34.0, null),
 (87, 3, 34.0, null),
 (88, 3, 34.0, null),
 (89, 3, 34.0, null),
 (90, 3, 34.0, null),
 (91, 3, 34.0, null),
 (92, 3, 34.0, null),
 (93, 3, 34.0, null),
 (94, 3, 34.0, null),
 (95, 3, 34.0, null),
 (96, 3, 34.0, null),
 (97, 3, 34.0, null),
 (98, 3, 34.0, null),
 (99, 3, 34.0, null),
 (100, 3, 34.0, null),
 (101, 3, 34.0, null),
 (102, 3, 34.0, null),
 (103, 3, 34.0, null),
 (104, 3, 34.0, null),
 (105, 3, 34.0, null),
 (106, 3, 34.0, null),
 (107, 3, 34.0, null),
 (108, 3, 34.0, null),
 (109, 3, 34.0, null),
 (110, 3, 34.0, null),
 (111, 3, 34.0, null),
 (112, 3, 34.0, null),
 (113, 3, 34.0, null),
 (114, 3, 34.0, null),
 (115, 3, 34.0, null),
 (116, 3, 34.0, null),
 (117, 3, 34.0, null),
 (118, 3, 34.0, null),
 (119, 3, 34.0, null),
 (120, 3, 34.0, null),
 (121, 3, 34.0, null),
 (122, 3, 34.0, null),
 (123, 3, 34.0, null),
 (124, 3, 34.0, null),
 (125, 3, 34.0, null),
 (126, 3, 34.0, null),
 (127, 3, 34.0, null),
 (128, 3, 34.0, null),
 (129, 3, 34.0, null),
 (130, 3, 34.0, null),
 (131, 3, 34.0, null),
 (132, 3, 34.0, null),
 (133, 3, 34.0, null),
 (134, 3, 34.0, null),
 (135, 3, 34.0, null),
 (136, 3, 34.0, null),
 (137, 3, 34.0, null),
 (138, 3, 34.0, null),
 (139, 3, 34.0, null),
 (140, 3, 34.0, null),
 (141, 3, 34.0, null),
 (142, 3, 34.0, null),
 (143, 3, 34.0, null),
 (144, 3, 34.0, null),
 (145, 3, 34.0, null),
 (146, 3, 34.0, null),
 (147, 3, 34.0, null),
 (148, 3, 34.0, null),
 (149, 3, 34.0, null),
 (150, 3, 34.0, null),
 (151, 3, 34.0, null),
 (152, 3, 34.0, null),
 (153, 3, 34.0, null),
 (154, 3, 34.0, null),
 (155, 3, 34.0, null),
 (156, 3, 34.0, null),
 (157, 3, 34.0, null),
 (158, 3, 34.0, null),
 (159, 3, 34.0, null),
 (160, 3, 34.0, null),
 (161, 3, 34.0, null),
 (162, 3, 34.0, null),
 (163, 3, 34.0, null),
 (164, 3, 34.0, null),
 (165, 3, 34.0, null),
 (166, 3, 34.0, null),
 (167, 3, 34.0, null),
 (168, 3, 34.0, null),
 (169, 3, 34.0, null),
 (170, 3, 34.0, null),
 (171, 4, 12.0, null),
 (172, 4, 12.0, null),
 (173, 4, 12.0, null),
 (174, 4, 12.0, null),
 (175, 4, 12.0, null),
 (176, 4, 12.0, null),
 (177, 4, 12.0, null),
 (178, 4, 12.0, null),
 (179, 4, 12.0, null),
 (180, 4, 12.0, null),
 (181, 4, 12.0, null),
 (182, 4, 12.0, null),
 (183, 4, 12.0, null),
 (184, 4, 12.0, null),
 (185, 4, 12.0, null),
 (186, 4, 12.0, null),
 (187, 4, 12.0, null),
 (188, 4, 12.0, null),
 (189, 4, 12.0, null),
 (190, 4, 12.0, null),
 (191, 4, 12.0, null),
 (192, 4, 12.0, null),
 (193, 4, 12.0, null),
 (194, 4, 12.0, null),
 (195, 4, 12.0, null),
 (196, 4, 12.0, null),
 (197, 4, 12.0, null),
 (198, 4, 12.0, null),
 (199, 4, 12.0, null),
 (200, 4, 12.0, null),
 (201, 4, 12.0, null),
 (202, 4, 12.0, null),
 (203, 4, 12.0, null),
 (204, 4, 12.0, null),
 (205, 4, 12.0, null),
 (206, 4, 12.0, null),
 (207, 4, 12.0, null),
 (208, 4, 12.0, null),
 (209, 4, 12.0, null),
 (210, 4, 12.0, null),
 (211, 4, 12.0, null),
 (212, 4, 12.0, null),
 (213, 4, 12.0, null),
 (214, 4, 12.0, null),
 (215, 4, 12.0, null),
 (216, 4, 12.0, null),
 (217, 4, 12.0, null),
 (218, 4, 12.0, null),
 (219, 4, 12.0, null),
 (220, 4, 12.0, null),
 (221, 5, 20.0, null),
 (222, 5, 20.0, null),
 (223, 5, 20.0, null),
 (224, 5, 20.0, null),
 (225, 5, 20.0, null),
 (226, 5, 20.0, null),
 (227, 5, 20.0, null),
 (228, 5, 20.0, null),
 (229, 5, 20.0, null),
 (230, 5, 20.0, null),
 (231, 5, 20.0, null),
 (232, 5, 20.0, null),
 (233, 5, 20.0, null),
 (234, 5, 20.0, null),
 (235, 5, 20.0, null),
 (236, 5, 20.0, null),
 (237, 5, 20.0, null),
 (238, 5, 20.0, null),
 (239, 5, 20.0, null),
 (240, 5, 20.0, null),
 (241, 5, 20.0, null),
 (242, 5, 20.0, null),
 (243, 5, 20.0, null),
 (244, 5, 20.0, null),
 (245, 5, 20.0, null),
 (246, 5, 20.0, null),
 (247, 5, 20.0, null),
 (248, 5, 20.0, null),
 (249, 5, 20.0, null),
 (250, 5, 20.0, null),
 (251, 6, 23.0, null),
 (252, 6, 23.0, null),
 (253, 6, 23.0, null),
 (254, 6, 23.0, null),
 (255, 6, 23.0, null),
 (256, 6, 23.0, null),
 (257, 6, 23.0, null),
 (258, 6, 23.0, null),
 (259, 6, 23.0, null),
 (260, 6, 23.0, null),
 (261, 6, 23.0, null),
 (262, 6, 23.0, null),
 (263, 6, 23.0, null),
 (264, 6, 23.0, null),
 (265, 6, 23.0, null),
 (266, 6, 23.0, null),
 (267, 6, 23.0, null),
 (268, 6, 23.0, null),
 (269, 6, 23.0, null),
 (270, 6, 23.0, null),
 (271, 6, 23.0, null),
 (272, 6, 23.0, null),
 (273, 6, 23.0, null),
 (274, 6, 23.0, null),
 (275, 6, 23.0, null),
 (276, 6, 23.0, null),
 (277, 6, 23.0, null),
 (278, 6, 23.0, null),
 (279, 6, 23.0, null),
 (280, 6, 23.0, null),
 (281, 6, 23.0, null),
 (282, 6, 23.0, null),
 (283, 6, 23.0, null),
 (284, 6, 23.0, null),
 (285, 6, 23.0, null),
 (286, 6, 23.0, null),
 (287, 6, 23.0, null),
 (288, 6, 23.0, null),
 (289, 6, 23.0, null),
 (290, 6, 23.0, null),
 (291, 6, 23.0, null),
 (292, 6, 23.0, null),
 (293, 6, 23.0, null),
 (294, 6, 23.0, null),
 (295, 6, 23.0, null),
 (296, 6, 23.0, null),
 (297, 6, 23.0, null),
 (298, 6, 23.0, null),
 (299, 6, 23.0, null),
 (300, 6, 23.0, null),
 (301, 6, 23.0, null),
 (302, 6, 23.0, null),
 (303, 6, 23.0, null),
 (304, 6, 23.0, null),
 (305, 6, 23.0, null),
 (306, 6, 23.0, null),
 (307, 6, 23.0, null),
 (308, 6, 23.0, null),
 (309, 6, 23.0, null),
 (310, 6, 23.0, null),
 (311, 6, 23.0, null),
 (312, 6, 23.0, null),
 (313, 6, 23.0, null),
 (314, 6, 23.0, null),
 (315, 6, 23.0, null),
 (316, 6, 23.0, null),
 (317, 6, 23.0, null),
 (318, 6, 23.0, null),
 (319, 6, 23.0, null),
 (320, 6, 23.0, null),
 (321, 6, 23.0, null),
 (322, 6, 23.0, null),
 (323, 6, 23.0, null),
 (324, 6, 23.0, null),
 (325, 6, 23.0, null),
 (326, 6, 23.0, null);
 
# insert into Carrello(idUtente,idevento,quantita) values;
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





 /*prova update con join
update UtenteRegistrato join Utente using(id)
set nome='culo' , email='culo@exampleupdate.com'
where id=4;
*/

#SELECT * FROM UtenteRegistrato JOIN Organizzatore USING(id) WHERE email='plutoprova@example.com' AND passwordHash=SHA1('pluto');
#SELECT * FROM Evento as e JOIN Carrello as c on e.id=c.idevento WHERE c.idUtente=1;

SELECT * 
FROM Evento 
WHERE idOrganizzatore=5 and id not in (select idEvento from RichiestaEvento);





