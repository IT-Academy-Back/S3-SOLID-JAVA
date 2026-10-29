# D — Principi d'inversió de dependències (DIP)

En aquesta activitat refactoritzaràs una aplicació de reserves de sales. Practicaràs com separar les necessitats d'un cas d'ús dels detalls de persistència i de la font de l'hora actual.

El principi que treballaràs és:

> Els mòduls d'alt nivell i els detalls que utilitzen han de dependre d'abstraccions adequades.

L'exercici té dos reptes: poder substituir la persistència i fer controlable el temps. Les implementacions de persistència ja es proporcionen; el teu treball és revisar les dependències i connectar els components mantenint les regles de negoci.

## La situació

Un client pot cancel·lar una reserva si:

- la reserva existeix i li pertany;
- falten almenys 24 hores per al seu inici, inclòs el límit exacte.

`Booking` protegeix el canvi d'estat amb `cancel(now)`. Conserva aquest comportament al model.

Comença per aquests fitxers:

- [BookingService.java](service/BookingService.java): coordina la cancel·lació.
- [Booking.java](model/Booking.java): conté les dades i la regla de cancel·lació.
- [H2BookingRepository.java](repository/H2BookingRepository.java): desa les reserves amb JDBC i H2.
- [InMemoryBookingRepository.java](repository/InMemoryBookingRepository.java): les desa en un `Map`.
- [BookingApp.java](BookingApp.java): crea i connecta els components.
- [BookingConsole.java](console_menu/BookingConsole.java): presenta el menú.

## 1. Comprova el punt de partida

Necessites JDK 17 o superior i Maven. Des de l'arrel del projecte, executa:

```bash
mvn test
```

Els tests actius han de passar. `BookingServiceTest` inclou expressament un test deshabilitat, `ownerShouldCancelExactly24HoursBeforeStart`, que treballaràs al segon repte.

Importa el projecte Maven a IntelliJ IDEA i executa el `main` de `BookingApp`:

- L'opció 1 arrenca el menú amb H2.
- L'opció 2 informa que la persistència en memòria encara està pendent de connectar i continua amb H2.

Les dues implementacions ja funcionen per separat i tenen tests. Aconseguir que l'opció 2 utilitzi realment el repositori en memòria és part del primer repte.

`H2BookingRepository` manté una connexió oberta i implementa `AutoCloseable`. El punt d'entrada s'encarrega de tancar-la quan s'acaba l'aplicació.

### Contracte de persistència proporcionat

| Operació | Comportament |
|---|---|
| `save(booking)` | Assigna un identificador a una reserva nova i retorna una reserva amb aquest id. Si ja té id, l'actualitza; si l'id és desconegut, falla. |
| `findById(id)` | Retorna la reserva o un `Optional` buit. |
| `findAllOrderedByStart()` | Retorna les reserves ordenades per inici i, en cas d'empat, per id. |

Les dades llegides són còpies: modificar una reserva requereix tornar a cridar `save` per desar el canvi. Cada instància del repositori en memòria té les seves dades; seleccionar una altra persistència no implica migrar-les.

## 2. Primer repte: poder substituir la persistència

Llegeix el servei i la consola i prepara una anàlisi breu a `EXPLICACIO.md`, en aquesta mateixa carpeta:

- Quines operacions necessiten sobre les reserves?
- Per què rebre el repositori pel constructor encara els lliga a H2?
- Si tots dos repositoris ofereixen les operacions necessàries, què impedeix passar el de memòria al servei actual?
- On s'hauria de decidir quina implementació utilitza l'aplicació?

A partir de l'anàlisi, defineix el contracte que necessiten els consumidors i refactoritza les dependències. Justifica les operacions i la ubicació de l'abstracció; les classes concretes poden oferir altres operacions que el cas d'ús no necessiti.

Adapta els dos repositoris proporcionats al disseny i fes que `BookingApp` connecti el triat a l'inici. La consola ha de mostrar la persistència seleccionada i funcionar amb qualsevol de les dues.

Comprova que:

- H2 continua funcionant amb les seves proves d'integració.
- L'opció en memòria utilitza realment `InMemoryBookingRepository`.
- El servei i la consola coneixen el contracte necessari per treballar amb reserves.
- Pots provar les regles del servei amb el repositori en memòria proporcionat.
- Les comprovacions dels tests inicials es conserven.

No cal implementar PostgreSQL, afegir un framework ni reescriure l'interior dels repositoris. La diferència entre injecció de dependències i inversió de dependències ha de quedar explicada amb el codi de l'exercici.

## 3. Segon repte: poder controlar el temps

Observa d'on obté l'hora actual `BookingService` i quin efecte té als tests. Habilita `ownerShouldCancelExactly24HoursBeforeStart` i analitza què passa entre preparar una reserva i executar el servei.

Fes que el test pugui proporcionar una hora coneguda i aconsegueix que els casos temporals siguin deterministes. Java proporciona [java.time.Clock](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/time/Clock.html); investiga si cobreix la necessitat que has identificat.

Mantén la regla de cancel·lació al model i comprova aquests tres límits amb temps controlat:

| Temps que falta per a l'inici | Resultat |
|---|---|
| 24 hores i 1 minut | Cancel·lació permesa |
| 24 hores exactes | Cancel·lació permesa |
| 23 hores i 59 minuts | Cancel·lació rebutjada |

Els tests temporals del servei han de donar el mateix resultat en qualsevol moment d'execució. Evita `Thread.sleep()`, canvis al rellotge del sistema o marges artificials al voltant del límit. Quan aquest repte estigui resolt, el test ha de quedar habilitat.

## 4. Refactoritza i justifica en passos petits

Utilitza les eines de refactoring d'IntelliJ IDEA quan siguin adequades, per exemple **Extract Interface** o **Change Signature**. Registra les operacions que hagis utilitzat.

Executa els tests després de cada moviment i crea un commit per cada transformació coherent. Pots adaptar la preparació dels tests al disseny mantenint les seves dades i comprovacions. Afegeix proves dels components nous amb comportament propi.

Completa `EXPLICACIO.md` amb:

1. Les dependències inicials i el problema concret que provocava cadascuna.
2. El contracte triat i on es construeixen i connecten les implementacions.
3. La diferència entre DI i DIP aplicada al teu codi.
4. Com controles el temps i quins casos protegeixen els tests.
5. Les operacions de refactoring i els commits que mostren el procés.

## Què s'avaluarà

- El cas d'ús i la consola permeten substituir la persistència mitjançant una abstracció justificada.
- Les dues opcions del menú funcionen realment i conserven les mateixes regles de negoci.
- Els tests del servei poden executar-se sense H2 i amb el temps controlat.
- Es conserven les proves útils dels repositoris i es prova el límit exacte de 24 hores.
- `Booking` continua protegint el seu estat.
- El codi i `EXPLICACIO.md` permeten entendre les decisions i els moviments de refactoring.

Construir un altre repositori des de zero pot ser una ampliació posterior. L'activitat principal consisteix a aconseguir que les implementacions proporcionades siguin substituïbles des del punt de configuració de l'aplicació.
