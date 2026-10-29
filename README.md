# 🏋️‍♀️ Exercicis SOLID

> En aquest repositori trobaràs cada lletra dels principis SOLID representada amb codi Java que hauràs de refactoritzar per tal que compleixi correctament el principi corresponent.

### Per a acomplir aquesta tasca, hauràs de:

- Crear un nou repositori a partir d'aquest **template repository**.
- Navegar per les carpetes corresponents a cada principi.
- Analitzar el codi problemàtic proporcionat.
- Refactoritzar-lo per ajustar-lo al principi SOLID.
- Cal escriure una explicació de les teves decisions.

## 📥 Com començar amb el template repository

> En comptes de clonar directament aquest repositori, hauràs de crear el teu propi repositori a partir d'aquest **template repository** per poder treballar còmodament i tenir el teu historial de canvis.

1. Fes clic a **Use this template**.
2. Crea un nou repositori amb el teu nom i la teva configuració.
3. Després, clona el nou repositori creat al teu equip local amb:

 ```bash
   git clone https://github.com/usuari/nom-del-teu-repositori.git
   cd nom-del-teu-repositori
```

## 🧪 Executar l'exercici de la S

Trobaràs l'enunciat i el codi de facturació a [l'exercici de la S](src/main/java/cat/itacademy/solid/s/README.md).

Necessites **JDK 17 o superior** i **Maven**. Des de l'arrel del projecte, executa:

```bash
mvn test
java -cp target/classes cat.itacademy.solid.s.InvoiceApp
```

L'exemple genera `output/invoices/invoice.txt`.

## 🧲 Executar l'exercici de la D

Trobaràs el codi de reserves i els dos reptes de refactoring a [l'exercici de la D](src/main/java/cat/itacademy/solid/d/README.md).

Importa el projecte com a projecte Maven a IntelliJ IDEA i executa el `main` de `cat.itacademy.solid.d.BookingApp`. La dependència H2 està inclosa al projecte.

Els repositoris H2 i en memòria ja estan implementats i provats. El primer repte és connectar qualsevol dels dos a l'aplicació; el segon, controlar el temps als tests. El test del límit exacte de 24 hores està deshabilitat expressament al punt de partida.

## 🛠️ Com fer la tasca

- 📖 Llegeix el fitxer README.md dins la carpeta del principi que vols estudiar.
- 👀 Analitza la classe Java problemàtica proporcionada.
- 🧠 Identifica com i per què aquesta classe incompleix el principi SOLID.
- ✏️ Refactoritza la classe per tal que respecti correctament el principi.
- 💬 Reflexiona sobre els canvis:
1. Què estava malament?
2. Per què incomplia el principi?
3. Quina solució has aplicat i per què?
- 📝 Escriu una breu explicació dins un fitxer EXPLICACIO.md a la mateixa carpeta de l’exercici.

## 📤 Entrega
> L'entrega es farà a través de Moodle.

### Has d’enviar:
- L'URL del teu repositori GitHub amb les tasques fetes.
- i el nom de l’alumne que ha corregit el teu exercici.

