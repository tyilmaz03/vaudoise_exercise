#  Vaudoise Application

Vaudoise est une application **Spring Boot** permettant de gérer des **clients** et leurs **contrats** (personnes physiques ou morales) avec une logique métier complète :
- création, modification, suppression de clients,
- création, modification et consultation de contrats,
- calcul des montants totaux des contrats actifs.

---

## 📚 Table des matières
- [Fonctionnalités principales](#fonctionnalités-principales)
  - [Gestion des clients](#gestion-des-clients)
  - [Gestion des contrats](#gestion-des-contrats)
- [Prérequis](#prérequis)
- [Structure du projet](#structure-du-projet)
- [Exécution avec Docker](#exécution-avec-docker)
  - [1. Cloner le projet](#1-cloner-le-projet)
  - [2. Lancer l’application et la base-postgresql](#2-lancer-lapplication-et-la-base-postgresql)
  - [3. Vérifier que tout fonctionne](#3-vérifier-que-tout-fonctionne)
  - [4. Accéder-à-lapi-pour-tester-les-fonctionnalités](#4-accéder-à-lapi-pour-tester-les-fonctionnalités)
  - [5. Arrêter l’application](#5-arrêter-lapplication)
- [Mise à jour de l’application](#mise-à-jour-de-lapplication)
- [CI/CD et publication automatique des images](#cicd-et-publication-automatique-des-images)
  - [Fonctionnement](#fonctionnement)
  - [Emplacement de l’image](#emplacement-de-limage)
- [Preuve de bon fonctionnement (Proof of Functionality)](#preuve-de-bon-fonctionnement-proof-of-functionality)
- [Architecture et conception](#architecture-et-conception)


---

##  Fonctionnalités principales

### Gestion des clients
- Créer un client (`PERSON` ou `COMPANY`)
- Modifier les informations de contact (nom, email, téléphone)
- Supprimer un client  
  ↳ Les contrats actifs sont automatiquement clôturés  
  ↳ Impossible de supprimer un client avec un contrat futur

###  Gestion des contrats
- Créer un contrat (dates et montant validés)
- Modifier un contrat (montant, dates)
- Lister :
  - tous les contrats d’un client
  - uniquement les contrats actifs
  - somme totale des montants des contrats actifs

---

##  Prérequis

- **Docker** et **Docker Compose** installés  

---

##  Structure du projet

├── src/
│ ├── main/java/com/example/vaudoise/
│ │ ├── app/ # Services, logique métier
│ │ ├── core/ # Entités JPA et exceptions
│ │ ├── data/ # Repositories Spring Data JPA
│ │ ├── web/ # Controllers et DTO
│ └── resources/
│ ├── application.yml
│ └── ...
├── Dockerfile
├── docker-compose.yml
└── README.md

##  Exécution avec Docker

### 1. Cloner le projet

```bash
git clone https://github.com/tyilmaz03/vaudoise_exercise.git
cd vaudoise_exercise

### 2. Lancer l’application et la base PostgreSQL

`docker compose up -d`

Cette commande va :

  -Télécharger l’image de l’application depuis GitHub Container Registry (GHCR)

  -Démarrer PostgreSQL avec les bonnes variables d’environnement

  -Exposer l’API sur le port 8080

  -Conserver les données PostgreSQL grâce à un volume Docker persistant

### 3. Vérifier que tout fonctionne

`docker ps`

### 4. Accéder à l'API pour tester les fonctionnalités

`http://localhost:8081/swagger-ui.html`

### 5. Arrêter l’application

`docker compose down`

##Mise à jour de l’application

À chaque mise à jour du code (push sur la branche main), une nouvelle image Docker est automatiquement construite et publiée sur le registre GitHub (GHCR).

Pour récupérer et exécuter la dernière version de l’application :

`docker compose pull`
`docker compose up -d`

Ces commandes :

  -Téléchargent la dernière image disponible sur GHCR

  -Redémarrent automatiquement les services

  -Conservent les données déjà présentes dans PostgreSQL


## CI/CD et publication automatique des images

Ce projet utilise GitHub Actions pour automatiser la construction et la publication de l’image Docker.

###Fonctionnement

À chaque push sur la branche main :

  -GitHub Actions exécute un workflow de build.

  -L’image Docker est construite à partir du Dockerfile.

  -L’image est publiée automatiquement sur GitHub Container Registry (GHCR) sous le tag :
  `ghcr.io/tyilmaz03/vaudoise_app:latest`

Il est ensuite possible de lancer via les commandes: 

  `docker compose pull`
  `docker compose up -d`

  pour récupérer la nouvelle version sans rien reconstruire localement.

### Emplacement de l’image

Les images générées automatiquement sont disponibles ici :
  `https://github.com/tyilmaz03?tab=packages`


## Preuve de bon fonctionnement (Proof of Functionality)

L’API a été **testée et vérifiée** à travers plusieurs moyens :

- **Swagger UI** (`/swagger-ui.html`) :  
  Tous les endpoints peuvent être exécutés directement pour vérifier leur comportement en temps réel.

- **Tests manuels réalisés via Swagger** :
  1. Création d’un client de type `PERSON` via `POST /clients`  
  2. Création d’un contrat lié à ce client via `POST /contracts`  
  3. Récupération des contrats actifs du client via `GET /contracts/client/{clientId}/active`  
  4. Suppression du client via `DELETE /clients/{id}` → les contrats actifs sont automatiquement clôturés à la date du jour  

Les comportements attendus ont été **confirmés par les réponses des endpoints** (codes HTTP, messages d’erreur cohérents et mise à jour correcte des données).


## Architecture et conception

L’application suit une architecture en couches claire et maintenable :
- **Web layer** (`controller`) : expose les endpoints REST (Clients et Contrats) et gère les réponses HTTP.  
- **Service layer** : contient la logique métier, les validations (règles de création, modification, suppression) et l’orchestration des accès aux données.  
- **Data layer** : utilise Spring Data JPA pour interagir avec PostgreSQL via des repositories typés.  
- **Core layer** : regroupe les entités JPA et les exceptions métier.  
Cette séparation permet une bonne testabilité et une évolution simple des règles métier.  
L’application respecte les principes **RESTful** et utilise **DTOs** pour isoler la couche API des entités internes.  
La configuration via **Docker Compose** facilite le déploiement local et la portabilité entre environnements.  
L’usage de **Lombok** réduit le code "boilerplate", et **Swagger** documente automatiquement l’API.


