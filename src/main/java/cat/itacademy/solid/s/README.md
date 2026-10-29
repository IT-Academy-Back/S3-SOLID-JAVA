# S — Principi de responsabilitat única (SRP)

En aquesta activitat refactoritzaràs una aplicació de facturació. Practicaràs com identificar responsabilitats a partir dels motius de canvi i com separar-les conservant el comportament del programa.

## La situació

L'aplicació rep una factura i la desa en un fitxer TXT. Pots veure un exemple executant `InvoiceApp`: crea una factura amb dues línies i un total de `125.00`. Per defecte genera `output/invoices/invoice.txt`.

## El teu encàrrec

**Identifica les responsabilitats d'`InvoiceService` i separa-les aplicant el principi de responsabilitat única (SRP), sense modificar el comportament del programa.**

Justifica què has separat i què has mantingut junt. L'activitat consisteix a reorganitzar el codi existent; no has d'afegir funcionalitats.

El principi que treballaràs és:

> Una classe hauria de tenir una única raó per canviar.

Una responsabilitat agrupa comportaments relacionats que canvien pel mateix motiu. Pot incloure diversos mètodes.

El nombre de classes i l'ús d'interfícies no són criteris d'èxit. Les decisions s'han de justificar pels motius de canvi identificats.

## 1. Observa el comportament actual

Comença per aquests fitxers:

- [InvoiceApp.java](InvoiceApp.java): executa l'exemple.
- [Invoice.java](Invoice.java) i [InvoiceLine.java](InvoiceLine.java): representen les dades.
- [InvoiceService.java](InvoiceService.java): conté l'operació `saveToFile()`.

Des d'IntelliJ IDEA, executa el `main` d'`InvoiceApp` amb el directori de treball a l'arrel del projecte. Obre `output/invoices/invoice.txt` i comprova d'on surt el total de `125.00`.

Executa també els tests amb `mvn test` des de l'arrel del projecte. Aquesta ordre compila el codi i executa les proves. Després, si treballes des del terminal, pots iniciar l'exemple amb:

```bash
java -cp target/classes cat.itacademy.solid.s.InvoiceApp
```

`saveToFile(invoice, destination)` genera sempre el mateix format TXT i el desa a la ruta rebuda.

Revisa `InvoiceServiceTest`, a `src/test/java/cat/itacademy/solid/s/`. Les proves criden `saveToFile()` i comproven el contingut TXT generat en una carpeta temporal, el total d'una factura buida i la comunicació d'un error d'escriptura. Confirma que passen abans de modificar el codi i anota qualsevol incidència prèvia de la suite completa.

La carpeta `output/` es crea en executar l'exemple, està exclosa de Git i es conserva després de `mvn clean`.

## 2. Identifica les responsabilitats

Llegeix `saveToFile()` i prepara una anàlisi breu abans de començar a refactoritzar:

- Descriu amb les teves paraules què fa aquest mètode.
- Identifica les responsabilitats que hi trobes i assenyala els fragments de codi que corresponen a cadascuna.
- Explica per què les consideres responsabilitats diferents, tenint en compte els motius pels quals podria canviar cadascuna.
- Justifica si tenir un únic mètode públic implica tenir una única responsabilitat en aquesta classe.

Guarda aquesta anàlisi a `EXPLICACIO.md`, en aquesta mateixa carpeta. La completaràs al final amb el resultat de la refactorització.

## 3. Refactoritza en passos petits

A partir de l'anàlisi, decideix on ha de quedar cada responsabilitat i transforma el codi existent de manera gradual.

Utilitza les eines de refactoring d'IntelliJ IDEA quan siguin adequades i registra les operacions utilitzades.

Com a ajuda, pots utilitzar **Extract Method** per separar fragments de codi en mètodes i **Extract Delegate** per traslladar mètodes a classes noves. Decideix quins fragments extreus i on els situes a partir de les responsabilitats que has identificat.

**Crea un commit per cada moviment de refactoring.** Després de cada moviment, executa `mvn test` i comprova que les proves passen abans de crear el commit i continuar amb el següent. Cada commit ha de recollir una única transformació coherent, amb un missatge que expliqui què has canviat i per què.

Mantén les proves proporcionades i afegeix proves directes dels components nous amb comportament propi. Prova les seves operacions públiques, sense exposar mètodes auxiliars només per poder-los provar. Evita repetir la mateixa lògica de càlcul en components diferents.

Pots adaptar la preparació dels tests al nou disseny, mantenint les dades d'entrada i les comprovacions del comportament original.

### Què has de conservar

- L'operació pública `saveToFile(Invoice invoice, Path destination)` d'`InvoiceService` i el funcionament d'`InvoiceApp`.
- El contingut del TXT per a les mateixes dades: imports, etiquetes, ordre i salts de línia.
- El desament a la destinació rebuda.
- La comunicació d'un error d'escriptura amb `UncheckedIOException`, conservant la causa i el missatge que inclou el número de factura.
- Les comprovacions dels tests existents, sense afeblir-les per adaptar-les al nou disseny.

## 4. Revisa i justifica el resultat

Revisa on ha quedat cadascuna de les responsabilitats identificades. Comprova que pots explicar la responsabilitat de cada component i per què els comportaments que conté han de quedar junts.

Completa `EXPLICACIO.md` amb:

1. Les responsabilitats identificades al codi inicial i on han quedat després de refactoritzar.
2. La responsabilitat de cada component resultant i el motiu pel qual has separat o mantingut junt cada comportament.
3. Les operacions de refactoring utilitzades a IntelliJ IDEA.
4. Els tests afegits i el comportament que protegeixen.

Executa de nou `InvoiceApp` i `mvn test` per comprovar el resultat.

## Què s'avaluarà

Entrega el codi refactoritzat, els tests i `EXPLICACIO.md`. La revisió tindrà en compte que:

- relacionis les responsabilitats amb motius de canvi concrets;
- cada component agrupi comportaments que corresponguin a una responsabilitat justificada;
- es conservi el comportament i les proves existents continuïn passant;
- els components nous amb comportament propi tinguin proves;
- l'historial de Git mostri un commit per cada moviment de refactoring, amb missatges que expliquin els canvis;
- els noms i la distribució del codi permetin entendre el disseny, sense duplicacions ni abstraccions injustificades;
- puguis explicar les decisions i les transformacions fetes amb l'IDE.
