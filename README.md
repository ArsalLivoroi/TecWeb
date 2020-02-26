# TecWeb
Questo è un generatore di codice per l'esercizio sulla persistenza.
Ho cercato di creare un output il più completo possibile, in modo da risparmiare tempo prezioso durante l'esame.

## Come usarlo

Nel metodo `inizializzaProblema()` della classe `Generatore.java` inserire i parametri seguendo il diagramma UML del esercizio proposto.
Dopo aver eseguito il programma troverete tutto nella cartella `out` (eseguire il Refresh se non viene visualizzato subito).
Il codice generato dovrebbe garantire la sufficienza.

Per completare l'esercizio bisogna implementare i metodi richiesti,
le alternative sono 2
- **Java-Like** 
    nella classe di Test creare un metodo dove utilizzate i vari `findYYYByXXX()` delle classi di interfacciamento al DB, per creare delle liste prefiltrate, per poi manipolare queste liste ottenendo il risultato richiesto
    (*Attenzione!* In alcuni casi è necessario modificare leggermente la stringa SQL, es. se viene richiesto di comparare delle quantità)
- **SQL-Like** (scelta migliore) nelle classi di interfacciamento al DB scrivere direttamente la Query richiesta ed utilizzare il metodo `findYYYByXXX()` come base per implementarla

Infine implementare la scrittura su file

## Per chi volesse modificare il progetto

Purtroppo, dato il poco tempo che avevo e dato l'utilizzo di una tecnologia per me nuova (FreeMarker Template) 
il progetto è molto incasinato.
Classi inutili o ridondanti, riferimenti circolari, codice ripetuto ed altro ancora.

Ma l'output è abbastanza completo, soprattutto per quanto riguarda Forza Bruta e DAO. Mentre per la parte relativa ad Hibernate è da migliorare, ma comunque è abbastanza per garantire la sufficienza abbondante.

Quindi per chi volesse modificarlo, piuttosto, consiglierei di dare un occhiata al mio codice, schifarlo, per poi riprogettare il generatore da zero. Magari partendo col capire come funziona 
<a href="https://freemarker.apache.org/" target="_blank">**FreeMarker Template**</a>
per poi progettare i vari template (.ftl) ed infine progettare il generatore (cioè il contrario di quello che ho fatto io).

In ogni caso, se volete contattarmi
- Telegram: <a href="https://t.me/arsallivoroi" target="_blank">**@arsallivoroi**</a>
- E-mail:    **arsalhanif.muhammad@studio.unibo.it**
