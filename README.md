# Synapsis

Synapsis è un middleware realizzato con lo scopo di collegare 'mente' e 'corpo'. La mente, generamente è intesa come entità in grado di effettuare ragionamenti mentre il corpo è lo strumento grazie al quale la mente ha una rappresentazione fisica nell'ambiente ed attraverso il quale può effettuare azione (movimento, ricerca, prelevamento....). Il corpo ha anche il compito di notificare alla mente le percezioni che riceve dall'amnbiente che lo circonda (ad esempio il fatto di essere stato toccato).

## Structure

Per realizzare questo Middleware è stato fatto uso del [PlayFramework](https://www.playframework.com/). I principali motivi che hanno portato alla scelta di questo framework sono scalabilità, reattività, distribuzione e modularità, resi possibili dal largo uso di [Akka](https://akka.io/).

Le principali tecnologie utilizzate sono:

* [Akka](https://akka.io/)
* [WebSocket](https://tools.ietf.org/html/rfc6455)
* [Json](https://www.json.org/)

Per capire bene come utilizzarlo leggere la [tesi](https://amslaurea.unibo.it/id/eprint/19116).

## Supported Game Engine

* [Unity3D](https://unity.com/) -> [Library](game-engine-libs/Synapsis.unitypackage)

## Supported Multi-Agent System

* [JaCaMo](http://jacamo.sourceforge.net/) -> [Library](mas-libs/SynapsisJaCaMo.jar)

## Setup

1. Installare [sbt](https://www.scala-sbt.org/index.html) sul proprio pc (nel mio caso ho utilizzato homebrew)
2. Clone/Fork del repository (consiglio il fork per rimanere aggiornati in caso di cambiamenti)
3. Entrare da terminale nella sottocartella ```synapsis-middleware```
4. Utilizzare il comando ```sbt compile``` per effettuare una prima compilare del progetto

## Usage

Per avviare il progetto è sufficiente:

1. Entrare da terminale nella sottocartella ```synapsis-middleware```
2. Utilizzare il comando ```sbt run``` per avviare il progetto

Il progetto sarà così avviato su <http://localhost:9000>
La pagina principale è ancora un template quindi non ha funzionalità ma serve solo a capire se il middleware è online

Online è presente la stessa versione del middleware disonibile all'indirizzo [Synapsis-middleware-heroku](https://synapsis-middleware.herokuapp.com/). Raccomando di non abusare dato che è un account free e quindi soggetto a limitazioni.

## Hints

Per velocizzare lo sviluppo è stata messa a disposizione degli sviluppatori la possibilità di realizzare MockActor, pienamente personalizzabili, che possono essere sostituite a una delle due entità (corpo/mente). La modalità di utilizzo è illustrata nel pdf.

Utilizzare il terminale per controllare lo stato di esecuzione del middleware. Verranno stampate informazioni utili come collegamenti, messaggi scambiati ...

## Contacts

Mail: luca.pascucci@studio.unibo.it

[LinkedIn](https://www.linkedin.com/in/luca-pascucci-526190138/)
