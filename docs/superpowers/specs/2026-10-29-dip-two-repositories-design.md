# Exercici D amb dues persistències implementades

Proposta aprovada durant la conversa del 29 d'octubre de 2026.

## Objectiu i alternatives

Es proporciona un starter de reserves amb H2 i memòria implementats i provats. L'alumnat se centra a invertir les dependències i a controlar el temps. Construir el repositori en memòria queda com a ampliació opcional, perquè gestionar identificadors i actualitzacions afegeix feina al primer contacte amb DIP.

## Contracte del starter

- Recuperar el codi i els tests de la D de `trabajo-solid-2026-10-29` a `main`, preservant la S.
- Proporcionar `H2BookingRepository` i `InMemoryBookingRepository` amb `save`, `findById` i `findAllOrderedByStart`.
- La memòria assigna identificadors a reserves noves, actualitza les existents, rebutja actualitzacions d'identificadors desconeguts i ordena per inici i identificador.
- Les instàncies en memòria són independents. Les dades retornades són còpies: cal tornar a desar una reserva per persistir-ne els canvis.
- `BookingService` i `BookingConsole` continuen depenent del tipus concret H2. No es proporciona cap abstracció comuna ni s'injecta encara `Clock`.
- `BookingApp` funciona amb H2. L'opció en memòria informa que falta connectar-la i continua explícitament amb H2; connectar-la és part del primer repte de l'alumne.
- Es conserven la regla de cancel·lació de 24 hores i el test temporal deshabilitat que forma part del segon repte.
- El nou enunciat explica els dos reptes i els criteris d'èxit. La guia docent reflecteix que tots dos repositoris ja es proporcionen.
- Els canvis queden preparats a `main`, pendents de commit i revisió de l'usuari.
