# 🧱 S - Principi de Responsabilitat Única (SRP)

Aquest document conté una introducció a l'SRP. L'exercici de facturació i les instruccions per executar-lo són al [nou enunciat de la S](../../src/main/java/cat/itacademy/solid/s/README.md).

## 🧠 Què és?

El **Principi de Responsabilitat Única** estableix que:

> **Una classe ha de tenir una única raó per canviar.**

Dit d’una altra manera, una classe hauria de tenir **una sola responsabilitat**, o **un sol motiu per ser modificada**.

👩‍🏫 **Exemple:**
Si tens una classe `Informe` que:
- genera contingut,
- imprimeix l’informe,
- i el desa l'informe.

```java
public class Informe {
    private String contingut;

    public Informe(String contingut) {
        this.contingut = contingut;
    }
    
    public String obtenirContingut() {
        return contingut;
    }

    public void imprimir() {
        System.out.println("Imprimint informe:");
        System.out.println(contingut);
    }

    public void desar(String nomFitxer) {
        try (FileWriter writer = new FileWriter(nomFitxer)) {
            writer.write(contingut);
            System.out.println("Informe desat a " + nomFitxer);
        } catch (IOException e) {
            System.err.println("Error en desar l'informe: " + e.getMessage());
        }
    }
}
```
🔴 Problema: Cada una d’aquestes funcions **pertany a responsabilitats diferents**, i haurien d’estar separades en diferents classes.

⚠️ Estàs violant el principi! 

✅ Versió refactoritzada amb SRP aplicat: separem les responsabilitats en classes diferents:

- **1️⃣ Informe: només conté el contingut.**

```java
// Classe amb una única responsabilitat: mantenir el contingut
public class Informe {
    private String contingut;

    public Informe(String contingut) {
        this.contingut = contingut;
    }

    public String obtenirContingut() {
        return contingut;
    }
}
```

- **2️⃣ Impressora: s'encarrega d'imprimir.**

```java
// Classe amb una única responsabilitat: imprimir informes
public class Impressora {
    public void imprimirInforme(Informe informe) {
        System.out.println("Imprimint informe:");
        System.out.println(informe.obtenirContingut());
    }
}
```
- **3️⃣ Desament: s'encarrega de desar l'informe.**

```java
// Classe amb una única responsabilitat: desar informes
public class Desament {
    public void desarInforme(Informe informe, String nomFitxer) {
        try (FileWriter writer = new FileWriter(nomFitxer)) {
            writer.write(informe.obtenirContingut());
            System.out.println("Informe desat a " + nomFitxer);
        } catch (IOException e) {
            System.err.println("Error en desar l'informe: " + e.getMessage());
        }
    }
}
```
- **4️⃣ Exemple d'ús:**

```java
public class Main {
    public static void main(String[] args) {
        Informe informe = new Informe("Aquest és el contingut de l'informe.");

        ImpressoraInforme impressora = new ImpressoraInforme();
        impressora.imprimirInforme(informe);

        Desament desament = new Desament();
        desament.desarInforme(informe, "informe.txt");
    }
}
```
---

## 🎯 Objectiu de l’exercici

A l’arxiu Java adjunt trobaràs una classe que **no respecta aquest principi**: fa massa coses alhora.

🔧 El teu repte és:

1. Analitzar les responsabilitats múltiples que té la classe.
2. Separar-les en **classes diferents**, cadascuna amb una sola responsabilitat clara.
3. Mantenir el codi llegible, modular i fàcil de mantenir.

---

## 📌 Consells per aplicar SRP

✅ Pregunta’t: *"Quines raons tindria aquesta classe per canviar?" i "Quines són les responsabilitats d’aquesta classe?"*

✅ Si n’hi ha més d’una... és hora de separar responsabilitats!

✅ No tinguis por de crear **més classes petites i enfocades**.

---


## 💬 Reflexió

Quan una classe té només una responsabilitat:
- És més fàcil de llegir.
- És més fàcil de provar.
- És menys probable que generi errors quan canvies una funcionalitat.

🔁 **Menys acoblament, més cohesió.**

---

🚀 Endavant! Revisa el codi, aplica el principi SRP i gaudeix del procés de refactorització.

❓ **Quantes responsabilitats té la classe?**

