#  Vaudoise Application

Vaudoise est une application **Spring Boot** permettant de gérer des **clients** et leurs **contrats** (personnes physiques ou morales) avec une logique métier complète :
- création, modification, suppression de clients,
- création, modification et consultation de contrats,
- calcul des montants totaux des contrats actifs.

---

## 📚 Table des matières
- [Fonctionnalités principales](#fonctionnalités-principales)
- [Prérequis](#prérequis)
- [Structure du projet](#structure-du-projet)
- [Exécution avec Docker](#exécution-avec-docker)
- [Preuve de bon fonctionnement](#preuve-de-bon-fonctionnement-proof-of-functionality)
- [Architecture et conception](#architecture-et-conception)

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
│   ├── main/java/com/example/vaudoise/
│   │   ├── app/           # Services, logique métier
│   │   ├── core/          # Entités JPA et exceptions
│   │   ├── data/          # Repositories Spring Data JPA
│   │   ├── web/           # Controllers et DTO
│   └── resources/
│       ├── application.yml
│       └── ...
├── Dockerfile
├── docker-compose.yml
└── README.md

##  Exécution avec Docker

###  1. Construire l’image

`docker compose build app-prod`

### 2. Lancer l’application et la base PostgreSQL

`docker compose up app-prod -d`

### 3. Vérifier que tout fonctionne

`docker ps`

### 4. Accéder à l'API pour tester les fonctionnalités

`http://localhost:8081/swagger-ui.html`

### 5. Arrêter l’application

`docker compose down`


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


