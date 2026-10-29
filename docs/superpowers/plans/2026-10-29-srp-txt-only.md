# SRP TXT-only Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:executing-plans to implement this plan. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Simplificar el starter de la S perquè només generi TXT.

**Architecture:** Un únic `saveToFile(Invoice invoice, Path destination)` prepara la capçalera, calcula i presenta les línies, afegeix el total i escriu el fitxer. Les responsabilitats es mantenen juntes com a material de l'exercici.

**Tech Stack:** Java 17, Maven, JUnit 5.11.0.

**Spec:** `docs/superpowers/specs/2026-10-29-srp-txt-only-design.md`.

## Global Constraints

- Es conserva el contracte TXT, `BigDecimal`, els salts de línia i `UncheckedIOException` amb causa i missatge.
- `InvoiceApp` conserva la destinació per defecte `output/invoices/invoice.txt` i la ruta opcional.
- No s'afegeixen mètodes auxiliars, classes ni dependències.
- Es treballa a `main`, amb els canvis pendents de commit; es respecta l'eliminació de `User.java`.

## Review Focus

- Destinacions amb extensió CSV o sense extensió: el test del format ha d'exigir sempre el mateix TXT.
- Factura buida: el test ha de comprovar la capçalera i `TOTAL: 0.00`.
- Destinació no escrivible: el test existent ha de conservar la causa i el número de factura.
- Execució real d'`InvoiceApp`: comprovar en un directori temporal la ruta per defecte i una ruta personalitzada amb subdirectoris.

## Task 1: Simplificar el starter i actualitzar el material

**Files:**
- Modify: `src/main/java/cat/itacademy/solid/s/InvoiceService.java`.
- Test: `src/test/java/cat/itacademy/solid/s/InvoiceServiceTest.java`.
- Modify: `README.md`.
- Modify: `src/main/java/cat/itacademy/solid/s/README.md`.
- Modify: `src/main/java/cat/itacademy/solid/s/S_OBJECTIU_PEDAGOGIC.md`.

**Interfaces:** Es consumeixen `Invoice` i `InvoiceLine`; es conserva `public void saveToFile(Invoice invoice, Path destination)`.

- [x] Parametritzar el test del TXT complet amb `invoice.txt`, `invoice.csv`, `invoice.CSV` i `invoice`, conservant les expectatives literals del TXT amb total `125.00`. Substituir els tests específics CSV per una factura buida en TXT amb total `0.00`.
- [x] Executar `mvn -B -ntp -Dtest=InvoiceServiceTest test`: s'esperen dos errors d'asserció perquè les destinacions CSV encara generen CSV.
- [x] Simplificar `saveToFile()` a un flux TXT, conservant el contingut i la comunicació d'errors.
- [x] Actualitzar el README general, l'enunciat i l'objectiu pedagògic perquè descriguin únicament TXT.
- [x] Executar `mvn -B -ntp clean test`: s'esperen 9 casos correctes.
- [x] Executar `InvoiceApp` en un directori temporal amb ruta per defecte i ruta personalitzada; comprovar el TXT i el total `125.00`.
- [x] Revisar el canvi, els enllaços locals i l'abast; preparar els fitxers modificats sense crear commits.

## Resultats de verificació

- RED: 9 casos executats, amb els dos errors d'asserció esperats per a `.csv` i `.CSV`.
- GREEN: `mvn -B -ntp clean test`, 9 casos correctes, sense errors ni omissions.
- `InvoiceApp`: TXT complet comprovat amb la ruta per defecte i una ruta personalitzada amb subdirectoris.
- Revisió independent: cap incidència crítica, important o menor.
