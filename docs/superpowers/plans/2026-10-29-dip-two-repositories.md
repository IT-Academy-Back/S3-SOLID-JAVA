# DIP Two Repositories Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:executing-plans to implement this plan. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Recuperar l'exercici D amb dues persistències funcionals i mantenir la inversió de dependències com a treball de l'alumnat.

**Architecture:** Es conserva el starter de reserves i la dependència concreta H2. Un repositori independent basat en `Map` proporciona les mateixes operacions de persistència amb còpies de les dades.

**Tech Stack:** Java 17, Maven, JUnit 5.11.0, H2 2.3.232.

**Spec:** `docs/superpowers/specs/2026-10-29-dip-two-repositories-design.md`.

## Global Constraints

- Treballar a `main` i preservar la S i la resta de principis.
- Conservar `BookingService`, `Booking` i les regles de cancel·lació.
- No resoldre encara DIP: conservar els tipus concrets H2 i la crida a `LocalDateTime.now()`.
- Proporcionar el repositori en memòria sense dependències d'H2 ni una interfície comuna.
- L'opció en memòria del menú continua sent un repte pendent de connectar; el missatge ha d'explicar correctament aquest estat.
- Deixar els canvis pendents de commit.

## Review Focus

- Desar, llegir o llistar no ha d'exposar les dades mutables guardades: provar explícitament que cal `save` per persistir canvis.
- Cada instància conserva només les seves reserves; els identificadors nous són diferents i les actualitzacions desconegudes es rebutgen.
- Les llistes s'ordenen per inici i, en cas d'empat, identificador.
- El menú continua funcionant amb H2 i el missatge de l'opció 2 explica que la implementació ja existeix.
- El test temporal deshabilitat es conserva i s'explica; no s'ha de presentar la suite com si no tingués omissions.

## Task 1: Recuperar i preparar el starter de la D

**Files:**
- Restore: `src/main/java/cat/itacademy/solid/d/` i `src/test/java/cat/itacademy/solid/d/` des de la branca guardada.
- Create: `src/main/java/cat/itacademy/solid/d/repository/InMemoryBookingRepository.java`.
- Test: `src/test/java/cat/itacademy/solid/d/repository/InMemoryBookingRepositoryTest.java`.
- Modify: `src/main/java/cat/itacademy/solid/d/BookingApp.java`, els README general i de la D, la guia docent i `pom.xml`.
- Remove: les tres classes de l'exercici antic a `Tasca-S3-0/D/src/`; enllaçar el README antic al nou enunciat.

**Interfaces:** `Booking save(Booking booking)`, `Optional<Booking> findById(Long id)` i `List<Booking> findAllOrderedByStart()`; cap tipus comú de producció.

- [x] Recuperar el starter i afegir H2 2.3.232 al POM existent.
- [x] Escriure els tests del repositori en memòria: identificadors, dades, actualitzacions, errors, ordenació, aïllament i còpies.
- [x] Executar `mvn -B -ntp -Dtest=InMemoryBookingRepositoryTest test`: s'espera un error de compilació perquè el repositori encara no existeix.
- [x] Implementar el repositori en memòria per satisfer els tests, mantenint el servei concret H2.
- [x] Actualitzar el missatge del menú, reduir l'enunciat a dos reptes i alinear la guia docent i els enllaços.
- [x] Executar `mvn -B -ntp clean test`: zero errors i només el test temporal intencionadament deshabilitat.
- [x] Comprovar l'arrencada real d'`BookingApp` amb les opcions 1 i 2, revisar els canvis i preparar-los sense commit.

## Resultats de verificació

- RED: el test nou no compila perquè encara falta `InMemoryBookingRepository`, tal com s'esperava.
- GREEN: `mvn -B -ntp clean test`, 44 casos totals; 43 correctes i el test temporal intencionadament deshabilitat. Els 12 casos del repositori en memòria passen.
- `BookingApp`: arrencada i sortida amb H2, avís de connexió pendent a l'opció en memòria, i creació i cancel·lació amb H2 correctes. La comprovació s'ha executat amb stdout i stderr UTF-8 perquè el terminal de verificació té `LC_ALL=C`.
- Revisió independent: cap incidència crítica o important. S'ha corregit la referència obsoleta als fitxers Java del directori antic.
