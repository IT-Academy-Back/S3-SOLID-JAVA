# Objectiu pedagògic — Exercici D (Dependency Inversion Principle)

> Document intern per a formadors.  
> No forma part de l'enunciat que rep l'alumne.

---

## 1. Propòsit de l'exercici

Aquest exercici no pretén que l'alumne memoritzi:

> “DIP = crear una interfície i injectar-la.”

L'objectiu és que aprengui a detectar **detalls externs que condicionen un cas d'ús** i a fer que aquestes dependències siguin controlables des de fora.

L'aprenentatge central és:

> **La lògica de l'aplicació no hauria d'estar lligada innecessàriament a detalls concrets d'infraestructura o de l'entorn.**

El domini de reserves utilitza dues dependències diferents:

```text
H2BookingRepository
LocalDateTime.now()
```

Una és visible i tecnològica.

L'altra és una dependència amagada.

Aquesta combinació és intencionada.

---

# 2. Per què aquest exercici està dissenyat així?

Molts exercicis de DIP són:

```text
Service
  ↓
MySqlRepository
```

i la solució és evident:

```text
crear Repository interface
```

Això pot fer que l'alumne memoritzi:

> “DIP = posar una interfície davant de la base de dades.”

Aquest exercici introdueix també el rellotge del sistema perquè l'alumne descobreixi que una dependència no és només una classe pròpia.

També poden ser dependències:

```text
temps
random
filesystem
xarxa
variables d'entorn
APIs
```

---

# 3. Resultats d'aprenentatge

L'alumne hauria de poder:

1. Identificar una dependència d'infraestructura concreta.
2. Identificar una dependència amagada com el temps.
3. Diferenciar Dependency Injection de Dependency Inversion.
4. Separar la necessitat del cas d'ús de la tecnologia que la satisfà.
5. Fer tests del servei sense base de dades.
6. Fer tests temporals deterministes.
7. Utilitzar `Clock` quan és apropiat.
8. Evitar crear abstraccions redundants.
9. Explicar per què el canvi millora testabilitat i evolució.

---

# 4. Per què utilitzem reserves?

El domini de reserves és habitual i permet un cas d'ús clar:

```text
cancelBooking(customerId, bookingId)
```

La cancel·lació necessita:

```text
carregar reserva
comprovar propietari
conèixer l'hora actual
aplicar la regla de 24 h
guardar
```

Això introdueix de manera natural persistència i temps sense afegir una API externa o un framework.

---

# 5. Què ha de contenir el starter?

## Model

```text
Booking
BookingStatus
```

`Booking` conserva comportament com:

```text
belongsTo(...)
cancel(now)
```

## Persistència

```text
H2BookingRepository
InMemoryBookingRepository
```

Tots dos repositoris estan implementats i tenen proves pròpies. Ofereixen les operacions `save`, `findById` i `findAllOrderedByStart`, però no comparteixen encara cap abstracció de producció.

El de memòria assigna identificadors, actualitza reserves, rebutja actualitzacions desconegudes i retorna les dades ordenades. Les dades retornades són còpies: modificar una reserva requereix tornar-la a desar, igual que amb H2.

No hi ha inicialment una abstracció `BookingRepository`. `BookingService` i `BookingConsole` continuen rebent el tipus concret H2, i l'opció de memòria del menú informa que falta connectar-la. Aconseguir que totes dues opcions funcionin és part del primer repte.

## Servei

```text
BookingService
```

depèn del repositori H2 concret i obté el temps amb:

```java
LocalDateTime.now()
```

---

# 6. Per què H2 és una dependència útil?

H2 és una dependència real d'infraestructura.

Permet que l'alumne observi:

> “Per provar una regla del servei necessito preparar una base de dades.”

Això no significa que els tests amb H2 siguin dolents.

La distinció que volem ensenyar és:

```text
test del cas d'ús
vs
test d'integració amb persistència
```

La implementació en memòria es proporciona perquè l'alumne pugui concentrar-se en el contracte que necessita el cas d'ús i en la inversió de les dependències. La gestió d'identificadors i actualitzacions no és l'objectiu principal de l'activitat.

Les còpies eviten que un test passi només perquè el servei ha modificat l'objecte compartit amb un `Map`: el cas d'ús ha de continuar desant explícitament els canvis. Construir un altre repositori es pot proposar com a ampliació.

---

# 7. Per què `LocalDateTime.now()` és central?

És una dependència menys evident.

Quan el servei crida:

```java
LocalDateTime.now()
```

decideix ell mateix quina font temporal utilitza.

Això dificulta provar exactament:

```text
24 h + 1 min
24 h exactes
23 h 59 min
```

La pregunta pedagògica és:

> **Per què el test s'ha d'adaptar al rellotge real en lloc de controlar el temps de l'escenari?**

---

# 8. Per què utilitzem `Clock` i no una interfície pròpia?

Java ja proporciona:

```java
Clock
```

No volem transmetre que aplicar DIP sempre implica crear una nova interfície.

Aquesta decisió reforça la idea:

> utilitza l'abstracció adequada si ja existeix.

---

# 9. Primer problema: tests acoblats a H2

Els tests del servei necessiten infraestructura fins i tot per validar una regla d'accés.

La pregunta detonant és:

> **Per què una regla del cas d'ús necessita una base de dades real per ser provada?**

L'objectiu és que l'alumne separi:

```text
què necessita BookingService
```

de:

```text
com es persisteixen les reserves
```

---

# 10. Canvi d'infraestructura: PostgreSQL

El requisit de PostgreSQL no existeix per fer instal·lar PostgreSQL.

Serveix per preguntar:

> **Per què `BookingService` hauria de canviar si canvia el motor de persistència?**

La implementació PostgreSQL no és necessària.

El canvi és un estímul de disseny.

---

# 11. Segon problema: temps no determinista

Després de treballar la persistència, l'alumne analitza:

```java
LocalDateTime.now()
```

La seqüència és deliberada:

```text
dependència evident
      ↓
dependència amagada
```

Això amplia el concepte de DIP.

---

# 12. Per què no afegim també UUID, API externa, email...?

Podríem afegir moltes dependències, però el nivell 1 necessita focus.

Amb:

```text
H2 + Clock
```

ja podem ensenyar dues formes diferents de dependència.

Afegir random, HTTP o serveis externs augmentaria càrrega cognitiva i diluiria el principi.

---

# 13. Solució esperada

Una solució adequada ha de permetre:

- que `BookingService` no depengui d'H2 com a tecnologia;
- conservar H2 com una implementació funcional;
- connectar des de `BookingApp` qualsevol dels dos repositoris proporcionats;
- provar el servei amb el repositori en memòria proporcionat;
- controlar el temps des de tests;
- mantenir la regla de cancel·lació a `Booking`;
- diferenciar DI de DIP.

No hi ha una obligació de noms concrets.

---

# 14. Solucions superficials

## Injecció de `H2BookingRepository`

```java
BookingService(H2BookingRepository repository)
```

és DI, però el servei continua depenent d'H2.

## Crear una interfície `TimeProvider` sense necessitat

Si `Clock` resol el problema, una abstracció pròpia pot ser soroll.

## Moure tota la regla temporal al servei

Pot empobrir el model.

## `Thread.sleep()`

No és una estratègia adequada per fer deterministes els tests.

## Usar `LocalDateTime.now().plusHours(...)` als tests

Pot evitar alguns errors, però el test continua depenent del rellotge real.

---

# 15. Evidències d'aprenentatge

1. L'alumne identifica H2 com a detall.
2. Pot explicar per què la injecció per constructor no és suficient si el tipus continua sent concret.
3. Pot provar `BookingService` sense H2.
4. Detecta `LocalDateTime.now()` com una dependència.
5. Utilitza temps fix als tests.
6. Manté tests d'integració amb H2 quan tenen sentit.
7. Pot seleccionar les dues persistències des de l'aplicació sense modificar les regles del servei.

---

# 16. Criteris d'avaluació

## Insuficient

- només mou el `new`;
- confon DI amb DIP;
- manté H2 al tipus del servei;
- fa tests temporals dependents del rellotge real;
- elimina H2 completament sense mantenir integració.

## Adequat

- separa capacitat de persistència de la implementació;
- injecta dependències;
- utilitza `Clock`;
- crea tests deterministes;
- conserva `Booking` amb comportament.

## Avançat

- diferencia tests unitaris i d'integració;
- explica dependències ocultes;
- evita abstraccions redundants;
- connecta el principi amb APIs, filesystem, LLMs, etc.

---

# 17. Feedback recomanat

> Quina tecnologia coneix ara `BookingService`?

> Si demà H2 canvia per PostgreSQL, què hauràs de modificar?

> Aquesta injecció per constructor ja és DIP o encara depens del tipus concret?

> Qui decideix quina hora és en aquest test?

> Pots reproduir exactament el límit de 24 hores?

> Necessites crear una abstracció nova o Java ja en proporciona una?

---

# 18. Connexió amb el roadmap

Prepara:

- Spring DI;
- repositoris;
- ports i adaptadors;
- clients HTTP;
- serveis externs;
- storage;
- email;
- pagaments;
- serveis d'IA;
- tests deterministes.

La idea és entendre DIP abans que Spring faci la injecció automàticament.

---

# 19. Què NO forma part de l'exercici

No s'avalua:

- PostgreSQL real;
- Docker;
- Spring;
- JPA;
- HTTP;
- APIs externes;
- arquitectura hexagonal completa;
- Mockito obligatori.

---

# 20. Checklist del formador

- [ ] el starter funciona amb H2;
- [ ] el repositori en memòria també està implementat i provat per separat;
- [ ] les còpies dels repositoris obliguen a desar explícitament els canvis;
- [ ] el menú explica que l'opció de memòria està pendent de connectar;
- [ ] `BookingService` depèn del tipus concret H2;
- [ ] `LocalDateTime.now()` està dins del flux del servei;
- [ ] els tests inicials fan visible la dependència;
- [ ] la regla de 24 h té límits clars;
- [ ] el test del límit exacte està deshabilitat expressament al starter i l'alumne l'ha d'habilitar en fer controlable el temps;
- [ ] PostgreSQL és només un canvi conceptual;
- [ ] `Clock` es pot introduir sense inventar abstracció pròpia;
- [ ] després del refactoring existeixen tests del servei sense H2;
- [ ] continuen existint tests d'integració útils per H2.

---

# 21. Fonts i criteris

Referències útils:

- [Java — Clock](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/time/Clock.html)
- [Martin Fowler — Inversion of Control](https://martinfowler.com/bliki/InversionOfControl.html)
- [Martin Fowler — Refactoring](https://refactoring.com/)
- [IntelliJ IDEA — Refactoring Source Code](https://www.jetbrains.com/help/idea/refactoring-source-code.html)
- [Cornell Center for Teaching Innovation — Setting Learning Outcomes](https://teaching.cornell.edu/setting-learning-outcomes)

---

# 22. Idea pedagògica que s'ha de conservar

```text
cas d'ús
      ↓
dependència concreta visible
      ↓
problema de test/evolució
      ↓
inversió
      ↓
dependència amagada
      ↓
control del temps
      ↓
tests deterministes
```

El domini de reserves és substituïble.

La combinació **infraestructura evident + dependència amagada** no ho és.
