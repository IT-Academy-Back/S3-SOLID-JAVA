# 🧲 D - Principi d’Inversió de Dependències (DIP)

## 🧠 Què és?

El **Principi d’Inversió de Dependències (DIP)** estableix que:

> **Les classes han de dependre d’abstraccions, no de classes concretes.**

En altres paraules, el codi hauria de **basar-se en interfícies o classes abstractes, no en implementacions específiques**. Això permet que les diferents parts del sistema estiguin **desacoblades**, cosa que **facilita el canvi, la substitució i la reutilització** de components.

📌 Això es pot resumir en dues regles clau:
- 1 **Els mòduls de nivell alt** (lògica del negoci) **no han de dependre dels de nivell baix** (implementacions).
- 2 **Tots dos han de dependre d’abstraccions**.

## 🚨 Per què és important?
Sense DIP, els components d’alt nivell poden quedar **fortament acoblats a implementacions concretes**, cosa que fa que el sistema sigui **difícil de modificar, provar o estendre**.

Quan s’aplica DIP correctament:

- ✅ El codi és més flexible i fàcil de testejar.
- ✅ Es poden substituir implementacions sense afectar la lògica principal.
- ✅ Es fomenta la injecció de dependències i la programació orientada a abstractions.

### 👩‍🏫 **Exemple:**

Suposem que tens una aplicació que gestiona notificacions i que s’envien sempre per correu electrònic:

```java
public class EmailService {
    public void enviarEmail(String missatge) {
        System.out.println("Enviant email: " + missatge);
    }
}
```
```java
public class GestorNotificacions {
    private EmailService serveiEmail;

    public GestorNotificacions() {
        this.serveiEmail = new EmailService();
    }

    public void notificar(String missatge) {
        serveiEmail.enviarEmail(missatge);
    }
}
```

🔴 Problema:
`GestorNotificacions` depèn directament de `EmailService` (una implementació concreta). Si vols afegir altres canals com SMS, WhatsApp o Push, caldria modificar la classe.

⚠️Això viola el principi **OCP** i també el **DIP**.

✅ Solució amb DIP:

- **1️⃣ Crear una abstracció (interfície) per al servei de notificació:**

```java
public interface CanalNotificacio {
    void enviar(String missatge);
}

```
- **2️⃣ Fer que EmailService implementi la interfície:**

```java
public class EmailService implements CanalNotificacio {
    @Override
    public void enviar(String missatge) {
        System.out.println("Enviant email: " + missatge);
    }
}
```
- **3️⃣ Modificar GestorNotificacions per dependre de l’abstracció:**

```java
public class GestorNotificacions {
    private CanalNotificacio canal;

    public GestorNotificacions(CanalNotificacio canal) {
        this.canal = canal;
    }

    public void notificar(String missatge) {
        canal.enviar(missatge);
    }
}

```
- **4️⃣ Ara pots injectar diferents canals sense modificar GestorNotificacions:**

```java
public class Main {
    public static void main(String[] args) {
        CanalNotificacio canal = new EmailService(); // o new SmsService(), new PushService()...
        GestorNotificacions gestor = new GestorNotificacions(canal);
        gestor.notificar("Hola món!");
    }
}
```
---

## 🎯 Objectiu de l’exercici

A l’arxiu Java inclòs en aquest directori, trobaràs una classe que depèn **directament d’una altra classe concreta**.

🔧 El teu repte és:

1. Identificar aquesta dependència directa.
2. Crear una **interfície o abstracció** adequada.
3. Refactoritzar les classes perquè **depenquin de l’abstracció**, i no de la implementació concreta.
4. Aplicar **injecció de dependències** (via constructor, setter o mètode).
---

## 📌 Consells per aplicar DIP

✅ Les classes de nivell alt han de ser **independents dels detalls tècnics**.

✅ Utilitza **interfícies o classes abstractes** per desacoblar.

✅ Aplica patrons com **Injecció de Dependències (DI) o Fàbrica (Factory)**.

✅ Escriure proves unitàries és molt més fàcil quan s’aplica **DIP**.

---


## 💬 Reflexió

Quan segueixes **DIP**:
- El teu codi esdevé **modular i fàcil de mantenir**.
- Es poden afegir **noves funcionalitats sense trencar l’existent**.
- Afavoreixes un sistema més **net, testejable i escalable**.

🔁 **Canvia implementacions, no dissenys**.

---

🚀Endavant! Refactoritza amb el principi DIP en ment i millora l’estructura del teu codi.

❓ **Depens de classes concretes? Com podries invertir aquesta dependència?**
 


