# Objectiu pedagògic — Exercici S (Single Responsibility Principle)

> Document intern per a formadors.  
> No forma part de l'enunciat que rep l'alumne.

---

## 1. Propòsit de l'exercici

Aquest exercici no pretén que l'alumne memoritzi:

> “SRP = una classe ha de tenir un únic mètode.”

L'objectiu és que aprengui a detectar **motius de canvi diferents dins d'un mateix component**.

L'aprenentatge central és:

> **Una classe hauria d'agrupar responsabilitats que canvien pel mateix motiu i separar les que evolucionen per raons diferents.**

El starter utilitza un domini senzill de facturació perquè l'alumne identifiqui responsabilitats i les separi a partir dels seus motius de canvi. Dues decisions que pot analitzar són:

```text
regles que determinen els imports
decisions sobre com es presenten
```

L'alumne ha de descobrir que el problema no és la longitud de `InvoiceService`, sinó que canvis en les regles de càlcul i en la presentació obliguen a modificar la mateixa classe. El fitxer és el resultat observable de l'exercici; extreure'n l'escriptura a un component propi és una decisió que es pot justificar, però no un requisit.

---

# 2. Per què aquest exercici està dissenyat així?

No volem donar a l'alumne una classe de 500 línies i demanar-li que la “divideixi”.

Una classe gran pot tenir una sola responsabilitat i una classe petita pot tenir-ne diverses.

Per això el starter és deliberadament petit.

La intenció és que l'alumne hagi d'analitzar:

```text
Què calcula aquesta classe?
Què formata?
Què desa?
Per què podria canviar cadascuna d'aquestes parts?
```

Volem passar de:

> “Aquesta classe fa moltes coses.”

a:

> **“Aquesta classe té diferents motius independents per canviar.”**

---

# 3. Resultats d'aprenentatge

En acabar l'exercici, l'alumne hauria de poder demostrar aquests cinc resultats:

| Objectiu d'aprenentatge | Evidència observable |
|---|---|
| Identificar les responsabilitats del codi inicial. | L'anàlisi relaciona cada responsabilitat amb fragments de `saveToFile()`. |
| Justificar què cal separar i què ha de quedar junt segons els motius de canvi. | `EXPLICACIO.md` explica les decisions amb referències al codi. |
| Redistribuir el comportament en components cohesionats, sense duplicar lògica ni alterar el contracte existent. | El codi resultant separa les responsabilitats justificades i les proves originals conserven les comprovacions i passen. |
| Comprovar directament el comportament dels components resultants. | Els tests específics protegeixen el comportament extret a cada component que té lògica pròpia. |
| Refactoritzar gradualment amb les eines de l'IDE i registrar cada moviment. | L'historial mostra un commit per moviment i `EXPLICACIO.md` identifica les operacions utilitzades. |

Els quatre primers objectius treballen el disseny i la seva verificació. El cinquè és un objectiu complementari de procés: practicar transformacions petites, comprovades amb tests abans de cada commit.

---

# 4. Disseny pedagògic

La seqüència de l'exercici ha de ser:

```text
codi funcional
      ↓
identificar responsabilitats
      ↓
relacionar-les amb motius de canvi
      ↓
fer un moviment de refactoring
      ↓
executar els tests i comprovar que passen
      ↓
crear el commit del moviment
      ↓
repetir per als moviments següents
      ↓
avaluar si el resultat és més cohesionat
```

No volem que l'alumne comenci creant classes perquè sap que està treballant SRP.

Primer ha d'explicar **què canviaria i per què**.

---

# 5. Per què utilitzem factures?

La facturació és un domini proper al backend real i permet separar responsabilitats sense introduir infraestructura complexa.

`InvoiceService` pot:

```text
calcular totals
generar una representació TXT
guardar la factura en un fitxer
```

Si l'alumne necessita ajuda per entendre els motius de canvi, el formador pot plantejar exemples com:

- aplicar un descompte del 10 % al total;
- mostrar les etiquetes en català i els imports amb coma decimal i símbol d'euro.

Aquests exemples són pistes opcionals per a la discussió amb el formador. L'enunciat demana identificar i separar les responsabilitats del codi existent, sense afegir funcionalitats. L'activitat conserva el comportament inicial i l'operació `saveToFile(Invoice invoice, Path destination)`.

No s'ha de justificar la separació amb un canvi hipotètic a una base de dades: la ruta i el nom de l'operació formen part d'un contracte específic de fitxers.

El domini permet entendre SRP sense necessitar Spring, base de dades o APIs externes.

---

# 6. Què ha de contenir el starter?

## Model

Com a mínim:

```text
Invoice
InvoiceLine
```

En el codi inicial, els models representen les dades de la factura. El càlcul dels imports ha de romandre a `InvoiceService` perquè l'alumne el pugui identificar i decidir on situar-lo durant la refactorització.

## Servei inicial

`InvoiceService` només ha d'exposar aquesta operació:

```text
saveToFile(Invoice invoice, Path destination)
```

El mètode ha de barrejar deliberadament el càlcul dels imports, la construcció del TXT i l'escriptura del fitxer. Genera sempre el mateix format, independentment de l'extensió de la destinació. No ha de tenir mètodes auxiliars que anticipin la separació.

`InvoiceApp` ha de permetre executar l'exemple i generar `output/invoices/invoice.txt` per defecte, o desar el mateix TXT en una altra ruta rebuda com a argument. Els tests inicials han de comprovar el contingut del TXT a través de `saveToFile()`, incloent el total d'una factura buida.

La classe ha de funcionar correctament.

No volem errors funcionals.

El problema és de disseny.

---

# 7. Per què el starter ha de funcionar?

SRP es treballa com a **refactoring**, no com a reparació.

Si el codi inicial està trencat, l'alumne pot confondre:

```text
corregir errors
```

amb:

```text
millorar disseny
```

Els tests inicials han d'estar en verd i protegir:

- càlcul del total;
- format esperat del TXT;
- persistència al fitxer;
- comunicació d'un error d'escriptura amb `UncheckedIOException`, conservant la causa i el missatge amb el número de factura.

---

# 8. Pregunta detonant

La pregunta principal és:

> **Quins motius diferents podrien obligar-nos a modificar `InvoiceService`?**

Si l'alumne respon:

> “Només té un mètode, per tant només té una responsabilitat.”

cal aprofundir.

Busquem una resposta semblant a:

> El mateix mètode calcula els imports, construeix el document i l'escriu. Cal justificar quines decisions han de quedar juntes i quines tenen motius diferents per canviar. La visibilitat i el nombre de mètodes no determinen les responsabilitats.

---

# 9. Per què no donem la solució exacta?

No s'ha d'indicar que cal crear:

```text
InvoiceCalculator
InvoiceFormatter
InvoiceFileWriter
```

encara que siguin noms possibles.

Si donem les classes, l'alumne només reprodueix una estructura.

Volem avaluar si sap:

```text
detectar responsabilitat
      ↓
justificar separació
      ↓
assignar un lloc coherent al comportament
```

També és acceptable que alguns comportaments acabin al mateix model si tenen sentit semàntic, per exemple un subtotal dins d'`InvoiceLine`.

## Suport amb les eines de l'IDE

Si l'alumne ja ha justificat què vol separar i necessita ajuda per fer-ho amb IntelliJ IDEA, es pot explicar aquesta seqüència:

1. **Refactor → Extract → Method**: seleccionar un fragment i donar-li nom com a mètode.
2. **Refactor → Extract → Delegate**: seleccionar el mètode que es vol traslladar a una classe nova i revisar a la vista prèvia la delegació resultant.

Després de cada moviment, cal executar els tests i crear el commit corresponent abans de continuar. Extreure mètodes privats pot facilitar el procés, però no resol SRP si els motius de canvi continuen concentrats en la mateixa classe.

Si el destí és una classe existent, es pot utilitzar **Refactor → Move**. En mètodes d'instància, les destinacions disponibles depenen dels tipus dels paràmetres i dels camps de la classe.

Aquesta ajuda s'ofereix quan cal suport amb l'eina, sense anticipar el disseny ni convertir la seqüència en un requisit de l'exercici.

Referències: [Extract Delegate](https://www.jetbrains.com/help/idea/extract-into-class-refactorings.html) i [Move](https://www.jetbrains.com/help/idea/move-refactorings.html).

---

# 10. Per què no exigim interfícies?

SRP no implica necessàriament abstraccions.

Crear una interfície per cada classe extreta pot afegir soroll sense resoldre cap problema real.

La pregunta ha de ser:

> **Necessito substituir aquesta dependència o només separar responsabilitats?**

En aquest exercici, el focus és la cohesió i els motius de canvi.

---

# 11. Solució esperada

No hi ha una única estructura correcta.

Una solució adequada ha de:

- reduir responsabilitats independents dins d'`InvoiceService`;
- evitar duplicar càlculs;
- mantenir el comportament;
- deixar components cohesionats;
- no crear abstraccions arbitràries;
- ser fàcil d'explicar en termes de motius de canvi.

`InvoiceService` conserva l'operació pública utilitzada per l'exemple i els tests. Pot coordinar col·laboradors per preparar el document i mantenir-ne l'escriptura, sempre que l'alumne justifiqui les responsabilitats resultants. No s'ha d'exigir una classe específica per guardar el fitxer.

---

# 12. Solucions superficials

## Una classe per mètode

Si cada mètode passa a una classe diferent només perquè sí, cal demanar:

> Quin motiu de canvi justifica aquesta classe?

## Només extreure mètodes privats

Pot millorar llegibilitat, però no separa responsabilitats si totes continuen canviant dins de la mateixa classe.

## Crear interfícies per tot

No aporta SRP per si mateix.

## Duplicar subtotal

Si els components resultants tornen a calcular el mateix import de formes diferents, s'ha separat estructura però s'ha empitjorat cohesió.

---

# 13. Evidències d'aprenentatge

## Evidència 1 — Anàlisi

L'alumne identifica les responsabilitats inicials i les relaciona amb fragments de codi i motius de canvi.

## Evidència 2 — Refactoring

Les responsabilitats queden separades de forma coherent.

## Evidència 3 — Tests

Els tests inicials continuen passant, inclòs el cas d'error d'escriptura, i els components resultants amb comportament propi tenen tests directes. Es pot adaptar la preparació dels tests conservant les dades d'entrada i les comprovacions originals.

## Evidència 4 — Justificació

L'alumne pot explicar per què la nova distribució és millor sense utilitzar només frases com “és més SOLID”.

## Evidència 5 — Procés de refactoring

L'historial conté un commit per moviment, amb una transformació coherent i un missatge explicatiu. `EXPLICACIO.md` descriu les operacions de l'IDE utilitzades. Cada commit ha de deixar el projecte compilant i els tests passant.

---

# 14. Criteris d'avaluació

## Comprensió insuficient

- divideix per mida o nombre de mètodes;
- crea classes sense responsabilitat clara;
- duplica lògica;
- introdueix interfícies sense necessitat;
- no pot identificar motius de canvi.

## Comprensió adequada

- permet que el càlcul i la presentació evolucionin independentment;
- manté el comportament;
- evita duplicació;
- justifica les classes per responsabilitat;
- comprova directament els components resultants amb comportament propi;
- registra els moviments amb commits coherents i documenta les eines utilitzades.

## Comprensió avançada

- compara una alternativa de distribució de responsabilitats amb la solució escollida;
- justifica els avantatges i els costos de la seva elecció amb referències al codi i als motius de canvi;
- explica per què els comportaments de cada component han de quedar junts i quina complexitat ha decidit evitar.

No s'ha de premiar una estructura concreta ni l'ús de vocabulari tècnic per si mateix. Situar comportament al model o crear un col·laborador són alternatives que s'han de valorar per la justificació i les conseqüències en el disseny.

---

# 15. Feedback recomanat

Preferir preguntes com:

> Què faria canviar aquest mètode?

> Aquestes dues operacions canviarien sempre juntes?

> Aquesta classe representa una responsabilitat o només un mètode?

> Hi ha algun càlcul duplicat després del refactoring?

> Aquesta interfície resol algun problema real?

Evitar:

> “Fes més classes.”

---

# 16. Connexió amb el roadmap

Aquest exercici prepara:

- serveis Spring més cohesionats;
- separació entre lògica, mapping i persistència;
- arquitectura per capes;
- testabilitat;
- refactorings posteriors cap a ports/adapters.

L'alumne ha d'entendre SRP abans que un framework li imposi carpetes o anotacions.

---

# 17. Què NO forma part de l'exercici

No s'avalua:

- Spring;
- JPA;
- base de dades;
- patrons arquitectònics;
- nombre exacte de classes;
- nombre de línies per classe;
- ús obligatori d'interfícies.

---

# 18. Checklist del formador

- [ ] el starter funciona;
- [ ] `InvoiceService` barreja decisions de càlcul i presentació en un únic mètode públic;
- [ ] els tests protegeixen càlcul, format, persistència i comunicació dels errors d'escriptura;
- [ ] no es dona la solució exacta;
- [ ] es demana identificar i separar responsabilitats, sense afegir funcionalitats ni imposar un component de persistència;
- [ ] l'alumne ha de parlar de motius de canvi;
- [ ] la solució pot evolucionar sense exigir interfícies;
- [ ] els nous components amb comportament poden testejar-se;
- [ ] el refactoring es pot fer en passos petits amb IntelliJ;
- [ ] l'historial permet revisar un commit per moviment;
- [ ] els criteris valoren les decisions justificades sense privilegiar una estructura concreta.

---

# 19. Fonts i criteris

L'exercici segueix una lògica de *backward design*: resultat d'aprenentatge, evidència observable i activitat.

La idea central es manté alineada amb la formulació clàssica d'SRP: una unitat de software hauria de tenir un únic motiu coherent per canviar.

Referències útils:

- [Martin Fowler — Refactoring](https://refactoring.com/)
- [IntelliJ IDEA — Refactoring Source Code](https://www.jetbrains.com/help/idea/refactoring-source-code.html)
- [Cornell Center for Teaching Innovation — Setting Learning Outcomes](https://teaching.cornell.edu/setting-learning-outcomes)

---

# 20. Idea pedagògica que s'ha de conservar

Encara que canviï el domini, l'exercici ha de conservar:

```text
component funcional
      ↓
diversos motius de canvi
      ↓
anàlisi abans de refactoritzar
      ↓
separació per responsabilitat
      ↓
tests que protegeixen el comportament
```

El domini de facturació és substituïble.

La idea de **motiu de canvi i cohesió** no ho és.
