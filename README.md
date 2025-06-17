# </> Tasca S3.0 - Principis SOLID 

## 📝 Introducció

Els **principis SOLID** són un conjunt de bones pràctiques del disseny orientat a objectes que ajuden a crear programari més:

✅ Mantenible  
✅ Escalable  
✅ Llegible  
✅ Robúst

Van ser proposats per **Robert C. Martin (Uncle Bob)** i s'utilitzen àmpliament en el desenvolupament de programari professional.

> 🧠 **SOLID** és un acrònim dels següents 5 principis fonamentals:

| Lletra | Principi | Nom en català | Descripció breu |
|--------|----------|----------------|------------------|
| **S** | *Single Responsibility* | Responsabilitat Única | Cada classe ha de tenir una única responsabilitat |
| **O** | *Open/Closed* | Obert/Tancat | El codi ha d’estar obert a l’extensió però tancat a la modificació |
| **L** | *Liskov Substitution* | Substitució de Liskov | Les subclasses han de poder substituir les superclasses |
| **I** | *Interface Segregation* | Segregació d’Interfícies | No obliguis a dependre d’interfícies que no es fan servir |
| **D** | *Dependency Inversion* | Inversió de Dependències | Depèn d’abstraccions, no de classes concretes |

---

## 🎯 Objectiu

Aquest [repositori](https://github.com/IT-Academy-Back/S3-SOLID-JAVA) t’ajudarà a **aprendre i aplicar els principis SOLID** mitjançant exercicis pràctics en llenguatge Java.

**Cada principi està ubicat en la seva pròpia carpeta:**

- 📁 S/ → Single Responsibility Principle
- 📁 O/ → Open/Closed Principle 
- 📁 L/ → Liskov Substitution Principle 
- 📁 I/ → Interface Segregation Principle 
- 📁 D/ → Dependency Inversion Principle 

**Dins de cada directori trobaràs:**

- 📄 `README.md` amb una explicació clara i exemples senzills.
- 🧪 Una classe Java que **no segueix el principi**.

## 🛠️ Com fer la tasca

1. 📖 Llegeix el `README.md` de la carpeta del principi que vols estudiar.
2. 👀 Analitza la classe Java problemàtica.
3. 🧠 Identifica en què infringeix el principi.
4. ✏️ Refactoritza la classe perquè **respecti el principi SOLID**.
5. 💬 Reflexiona i explica què estava malament, per què incomplia el principi i per què has aplicat la teva solució.  
   Escriu una breu explicació en un fitxer `EXPLICACIO.md` dins la carpeta de l’exercici.

---


# ⭐ Nivell 1

>En aquest nivell hauràs de fer els exercicis de les carpetes S/ i D/ del repositori. 
- Treballaràs amb classes que fan massa coses alhora o que estan fortament acoblades a altres components.
- Refactoritza-les per dividir responsabilitats i fer servir dependències basades en abstraccions.

---

# ⭐⭐ Nivell 2 
>En aquest nivell hauràs de fer els exercicis de les carpetes O/ i I/.

- Et trobaràs amb codi que has de modificar per afegir funcionalitats o interfícies que obliguen a implementar mètodes inútils.
- Refactoritza perquè el codi sigui fàcil d'estendre sense modificar-lo i crea interfícies més específiques.

---

# ⭐⭐⭐ Nivell 3 
>En aquest nivell hauràs de fer l’exercici de la carpeta L/.
- Treballaràs amb herències que trenquen el comportament esperat de la classe base.
- Refactoritza per assegurar que totes les subclasses es poden utilitzar de manera segura com si fossin la seva classe pare.

---

## 🚀 Recomanacions

- No tinguis por de **experimentar**.
- Practicar és la millor manera d'aprendre.
- Compara la teva solució amb la d'altres companys.
- Fes ús de patrons de disseny quan sigui útil.




