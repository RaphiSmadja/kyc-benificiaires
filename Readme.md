# KYC - Gestion des bénéficiaires effectifs

## 📝 Description
Ce projet est une API REST pour la gestion des bénéficiaires effectifs d'une entreprise dans le cadre des exigences KYC (Know Your Customer) chez Bpifrance.

### Objectifs :
Récupérer la liste des bénéficiaires effectifs d'une entreprise.
Créer des entreprises et ajouter des bénéficiaires (personnes physiques).
Utiliser une base en mémoire (H2) pour stocker temporairement les données.

### ⚙ Prérequis
Avant de lancer le projet, il faut avoir :

Java 21 installé.
Maven installé et configuré.
Git pour cloner le projet.

## 🚀 Installation et Exécution
1. Cloner le projet
   ```
   git clone https://github.com/ton-utilisateur/kyc-beneficiaires.git
   cd kyc-beneficiaires
   ```
2. Construire et lancer l'application
   ```
   mvn clean install
   mvn spring-boot:run
   ```
3. Accéder à l'API URL de base : http://localhost:8080
   Console H2 (base mémoire) :
   URL : http://localhost:8080/h2-console
   JDBC URL : jdbc:h2:mem:bpifrance_db
   Username : sa
   Password : (laisser vide)

## 📚 Endpoints disponibles
Créer une entreprise
   Méthode : POST
   URL : /entreprises
   Corps (JSON) :
   json
```
   {
   "nom": "Entreprise ABC"
   }
   Réponse : 201 Created avec un Location vers la ressource.
   ```
 Récupérer les bénéficiaires d'une entreprise
   Méthode : GET

URL : /entreprises/{id}/beneficiaires

Paramètre :

{id} : Identifiant de l'entreprise.
Exemple de réponse (200 OK) :
```
[{
    "id": 1, 
    "nom": "Jean Louis",
    "pourcentageCapital": 30.5
}]
```
Réponses possibles :

200 OK : Liste des bénéficiaires.
404 Not Found : Entreprise non trouvée.

## 💻 Exemples de requêtes cURL
Créer une entreprise
```
curl -X POST -H "Content-Type: application/json" -d '{
"nom": "Entreprise ABC"
}' http://localhost:8080/entreprises
```
Récupérer les bénéficiaires
```
curl -X GET http://localhost:8080/entreprises/1/beneficiaires
```

## 🛠 Architecture du projet

```
src/
├── main/
│   ├── java/
│   │   └── fr/bpifrance/kyc/
│   │       ├── controller/    # Contrôleurs REST
│   │       ├── model/         # Entités JPA
│   │       ├── repository/    # Repositories Spring Data JPA
│   │       └── service/       # Logique métier
│   ├── resources/
│   │   ├── application.properties  # Configuration H2 et Spring Boot
└── test/
└── java/                  # Tests unitaires et d'intégration
```

## 📈 Améliorations futures
Ajouter des validations pour les champs d'entrée avec spring-boot-starter-validation.
Implémenter des tests unitaires avec JUnit 5.
Remplacer H2 par une base de données persistante (ex. PostgreSQL).
Ajouter des DTOs pour séparer les entités des données exposées via l'API.
## 👤 Auteur
Projet développé par Raphael SMADJA dans le cadre d’un exercice technique.

## 📜 Licence
Ce projet est sous licence MIT. Tu peux le réutiliser librement.

## 📧 Contact
Si vous avez des questions, n'hésitez pas à me contacter :

Email : raphael_smadja@hotmail.com
GitHub : RaphiSmadja
