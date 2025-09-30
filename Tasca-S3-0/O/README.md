# 🚪 O - Principi Obert/Tancat (OCP)

## 🧠 Què és?

El **Principi Obert/Tancat** estableix que:

> **El codi ha d’estar obert a l’extensió, però tancat a la modificació**

Això vol dir que hauríem de poder **afegir noves funcionalitats** al nostre sistema **sense haver de modificar el codi existent**.


### 👩‍🏫 **Exemple:**

Suposa que tens una classe `CalculadoraDescomptes` amb aquest mètode:

```java
public double calculaDescompte(Producte producte) {
    if (producte.getTipus().equals("Nadal")) {
        return producte.getPreu() * 0.2;
    } else if (producte.getTipus().equals("BlackFriday")) {
        return producte.getPreu() * 0.3;
    }
    return 0;
}
```
🔴 Problema: Cada vegada que vols afegir un nou tipus de descompte, **has de modificar aquesta classe**.
⚠️ Això **viola el principi OCP**, perquè el codi no està tancat a la modificació.


✅ La solució és usar **polimorfisme** o **patrons de disseny** (com a **Estratègia** o **Fàbrica**), que permetin afegir nous comportaments sense tocar el codi existent:

- **1️⃣ Definim una interfície comuna:**

```java
public interface EstrategiaDescompte {
    double aplicaDescompte(Producte producte);
}
```
- **2️⃣ Implementem estratègies concretes:**

```java
public class DescompteNadal implements EstrategiaDescompte {
    public double aplicaDescompte(Producte producte) {
        return producte.getPreu() * 0.2;
    }
}

public class DescompteBlackFriday implements EstrategiaDescompte {
    public double aplicaDescompte(Producte producte) {
        return producte.getPreu() * 0.3;
    }
}

public class DescompteAniversari implements EstrategiaDescompte {
    public double aplicaDescompte(Producte producte) {
        return producte.getPreu() * 0.1;
    }
}

```
- **3️⃣ Classe CalculadoraDescomptes flexible i oberta a l’extensió:**

```java

public class CalculadoraDescomptes {

    public double calculaDescompte(Producte producte, EstrategiaDescompte estrategia) {
        return estrategia.aplicaDescompte(producte);
    }
}
````
- **4️⃣ Exemple d'ús:**

```java
public class Main {
    public static void main(String[] args) {
        Producte producte = new Producte("Portàtil", 1000);

        CalculadoraDescomptes calculadora = new CalculadoraDescomptes();
 
        double descompte = calculadora.calculaDescompte(producte, new DescompteNadal());
        System.out.println("Descompte aplicat: " + descompte);
    }
}
```
---

## 🎯 Objectiu de l’exercici

En l’arxiu Java adjunt trobaràs una classe que **no respecta el principi OCP**: necessita ser modificada cada cop que hi ha un canvi o extensió de funcionalitat.

🔧 El teu repte és:

1. Identificar quina part del codi està **massa exposada a modificacions**.
2. Refactoritzar-lo perquè sigui **fàcilment extensible** sense alterar el comportament existent.
3. Aplicar **abstraccions i polimorfisme** per fer el codi més flexible i robust.

---

## 📌 Consells per aplicar OCP

✅ **Evita instruccions condicionals (if/else, switch)** per decidir comportaments que poden variar amb el temps.

✅ **Defineix interfícies o classes abstractes** que permetin afegir noves funcionalitats sense tocar el codi existent.

✅ **Fes servir patrons com a Estratègia, Fàbrica o Cadena de Responsabilitat** segons el context.

---


## 💬 Reflexió

Quan un sistema està ben dissenyat segons **OCP**:
- Pots afegir **noves funcionalitats** amb facilitat.
- El teu codi és **més estable** i menys vulnerable a regressions`*`.
- Millores la **reutilització** i **mantenibilitat**.

🔁 **Extensible**, però **segur**. Aquest és el poder de l’OCP. 

`*` **Regressió** significa que una funcionalitat que **abans funcionava correctament, ara ha deixat de funcionar** després d’haver fet canvis al codi.

---

🚀 Endavant! Revisa el codi, detecta com es pot millorar i aplica el principi OCP per fer-lo més modular i preparat pel futur.

❓ **Ets capaç d’afegir un nou producte sense canviar el codi?** 


