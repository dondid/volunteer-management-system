# Volunteer Management System - REST API

Sistem de management al voluntarilor implementat ca serviciu web RESTful conform cerințelor cursului **Distributed Services Oriented Architectures**.

## 📋 Descriere

Aplicația permite gestionarea:
- **Organizațiilor** care coordonează proiecte de voluntariat
- **Voluntarilor** care participă la proiecte
- **Proiectelor** de voluntariat
- **Evenimentelor** organizate în cadrul proiectelor
- **Asignărilor** voluntar-proiect
- **Prezențelor** și orelor lucrate
- **Certificatelor** emise pentru voluntari
- **Feedback-ului** între organizații și voluntari
- **Competențelor** necesare pentru proiecte

## 🏗️ Arhitectură

- **Backend**: Java 17, JAX-RS (Jersey), JPA/Hibernate
- **Baza de date**: MySQL 8.0
- **Containerizare**: Docker & Docker Compose
- **Server**: Apache Tomcat 10.1
- **Client**: JAX-RS Client (inclus în proiect)

## 📊 Structura Bazei de Date

Baza de date conține **12 tabele** interconectate:

1. `organizations` - Organizații
2. `volunteers` - Voluntari
3. `skills` - Competențe
4. `volunteer_skills` - Relație Many-to-Many voluntar-competință
5. `projects` - Proiecte
6. `project_skills` - Competențe necesare pentru proiecte
7. `assignments` - Asignări voluntar-proiect
8. `attendance` - Prezență/ore lucrate
9. `certificates` - Certificate emise
10. `events` - Evenimente
11. `event_participants` - Participanți la evenimente
12. `feedback` - Evaluări/feedback

## 🚀 Instalare și Rulare

### Cerințe
- Java 17 sau superior
- Maven 3.6+
- Docker & Docker Compose
- Apache Tomcat 10.1 (sau IntelliJ IDEA cu Smart Tomcat)

### Pași de instalare

1. **Clonează repository-ul**
```bash
git clone https://github.com/dondid/volunteer-management-system.git
cd volunteer-management-system/volunteer-management-system
```

2. **Pornește baza de date MySQL cu Docker**
```bash
docker-compose up -d
```

Aceasta va porni MySQL pe portul 3306 cu:
- Database: `volunteer_db`
- User: `volunteer_user`
- Password: `volunteer_pass`

3. **Construiește proiectul**
```bash
mvn clean package
```

4. **Deploy în Tomcat**

**Opțiunea 1: IntelliJ IDEA cu Smart Tomcat**
- Configurează Smart Tomcat plugin
- Setează context path: `/volunteer-management-system`
- Port: `8080`

**Opțiunea 2: Deploy manual**
- Copiază `target/volunteer-api.war` în `$TOMCAT_HOME/webapps/`
- Pornește Tomcat

5. **Verifică aplicația**
- Deschide browser: `http://localhost:8080/volunteer-management-system`
- Sau testează API: `http://localhost:8080/volunteer-management-system/api/organizations`

## 📡 API Endpoints

### Base URL
```
http://localhost:8080/volunteer-management-system/api
```

### Resurse disponibile

#### Organizations
- `GET /organizations` - Listă toate organizațiile
- `GET /organizations/{id}` - Obține organizație după ID
- `POST /organizations` - Creează organizație nouă
- `PUT /organizations/{id}` - Actualizează organizație
- `DELETE /organizations/{id}` - Șterge organizație

#### Volunteers
- `GET /volunteers` - Listă toți voluntarii
- `GET /volunteers/{id}` - Obține voluntar după ID
- `POST /volunteers` - Creează voluntar nou
- `PUT /volunteers/{id}` - Actualizează voluntar
- `DELETE /volunteers/{id}` - Șterge voluntar

#### Projects
- `GET /projects` - Listă toate proiectele
  - Query params: `status`, `organizationId`, `available`
- `GET /projects/{id}` - Obține proiect după ID
- `POST /projects` - Creează proiect nou
- `PUT /projects/{id}` - Actualizează proiect
- `DELETE /projects/{id}` - Șterge proiect
- `GET /projects/organization/{organizationId}` - Proiecte per organizație
- `GET /projects/status/{status}` - Proiecte după status

#### Skills
- `GET /skills` - Listă toate competențele
  - Query params: `category`, `name`
- `GET /skills/{id}` - Obține competență după ID
- `POST /skills` - Creează competență nouă
- `PUT /skills/{id}` - Actualizează competență
- `DELETE /skills/{id}` - Șterge competență
- `GET /skills/category/{category}` - Competențe după categorie

#### Events
- `GET /events` - Listă toate evenimentele
  - Query params: `projectId`, `status`, `upcoming`, `available`
- `GET /events/{id}` - Obține eveniment după ID
- `POST /events` - Creează eveniment nou
- `PUT /events/{id}` - Actualizează eveniment
- `DELETE /events/{id}` - Șterge eveniment
- `GET /events/project/{projectId}` - Evenimente per proiect

#### Assignments
- `GET /assignments` - Listă toate asignările
  - Query params: `volunteerId`, `projectId`, `status`
- `GET /assignments/{id}` - Obține asignare după ID
- `POST /assignments` - Creează asignare nouă
- `PUT /assignments/{id}` - Actualizează asignare
- `DELETE /assignments/{id}` - Șterge asignare

#### Attendance
- `GET /attendance` - Listă toate prezențele
  - Query params: `assignmentId`, `volunteerId`
- `GET /attendance/{id}` - Obține prezență după ID
- `POST /attendance` - Creează prezență nouă
- `PUT /attendance/{id}` - Actualizează prezență
- `DELETE /attendance/{id}` - Șterge prezență
- `GET /attendance/assignment/{assignmentId}` - Prezențe per asignare

#### Certificates
- `GET /certificates` - Listă toate certificatele
  - Query params: `volunteerId`, `projectId`
- `GET /certificates/{id}` - Obține certificat după ID
- `POST /certificates` - Creează certificat nou
- `PUT /certificates/{id}` - Actualizează certificat
- `DELETE /certificates/{id}` - Șterge certificat
- `GET /certificates/volunteer/{volunteerId}` - Certificate per voluntar

#### Feedback
- `GET /feedback` - Listă toate feedback-urile
  - Query params: `assignmentId`, `type`, `minRating`
- `GET /feedback/{id}` - Obține feedback după ID
- `POST /feedback` - Creează feedback nou
- `PUT /feedback/{id}` - Actualizează feedback
- `DELETE /feedback/{id}` - Șterge feedback
- `GET /feedback/assignment/{assignmentId}` - Feedback per asignare

### Statistici (Query-uri complexe)

#### Overview
- `GET /statistics/overview` - Statistici generale (COUNT pentru toate entitățile)

#### JOIN-uri și agregări
- `GET /statistics/volunteers-per-organization` - Număr voluntari per organizație (JOIN + COUNT)
- `GET /statistics/hours-per-volunteer?limit=10` - Total ore lucrate per voluntar (JOIN + SUM)
- `GET /statistics/average-rating-per-project` - Rating mediu per proiect (JOIN + AVG)
- `GET /statistics/projects-by-volunteer-count?limit=10` - Proiecte cu cei mai mulți voluntari (JOIN complex)
- `GET /statistics/most-requested-skills?limit=10` - Competențe cele mai cerute (JOIN + GROUP BY)

#### Paginare și sortare
- `GET /statistics/volunteers-paginated?page=0&size=10&sortBy=lastName&order=ASC` - Voluntari cu paginare

#### Filtrare complexă
- `GET /statistics/available-projects-filtered?organizationId=1&minSlots=5` - Proiecte disponibile cu filtrare

## 💻 Client Java

Proiectul include un client JAX-RS (`VolunteerManagementClient.java`) care demonstrează conectarea la serviciu.

### Rulare client

```bash
# Asigură-te că serverul rulează
# Apoi rulează clientul:
java -cp target/classes ro.ucv.inf.soa.client.VolunteerManagementClient
```

Sau din IntelliJ IDEA:
- Deschide `VolunteerManagementClient.java`
- Rulează metoda `main()`

### Exemple de utilizare client

```java
VolunteerManagementClient client = new VolunteerManagementClient();

// Test conexiune
client.testConnection();

// Obține organizații
client.getAllOrganizations();

// Obține voluntari
client.getAllVolunteers();

// Obține statistici
client.getStatistics();

// Obține proiecte disponibile
client.getAvailableProjects();

// Obține voluntari cu paginare
client.getVolunteersPaginated(0, 10);

// Obține ore per voluntar
client.getHoursPerVolunteer();

client.close();
```

## 🧪 Testare API

### Cu cURL

```bash
# Obține toate organizațiile
curl http://localhost:8080/volunteer-management-system/api/organizations

# Obține un voluntar
curl http://localhost:8080/volunteer-management-system/api/volunteers/1

# Creează organizație
curl -X POST http://localhost:8080/volunteer-management-system/api/organizations \
  -H "Content-Type: application/json" \
  -d '{"name":"Test Org","email":"test@example.com","phone":"123456789"}'

# Obține statistici
curl http://localhost:8080/volunteer-management-system/api/statistics/overview
```

### Cu Postman

Importă colecția de request-uri sau testează manual endpoint-urile de mai sus.

## 📝 Format Răspuns API

Toate răspunsurile folosesc formatul `ApiResponse`:

```json
{
  "success": true,
  "message": "Operation successful",
  "data": { ... }
}
```

Sau pentru erori:

```json
{
  "success": false,
  "message": "Error message",
  "data": null
}
```

## 🗄️ Inițializare Baza de Date

Baza de date se inițializează automat la prima rulare prin:
- `init-db/01-schema.sql` - Creează schema
- `init-db/02-sample-data.sql` - Inserează date de test (dacă există)

## 🔧 Configurare

### Persistence Unit
Configurarea JPA se află în `src/main/resources/META-INF/persistence.xml`

### Docker Compose
Configurarea MySQL în `docker-compose.yml`

## 📚 Tehnologii Utilizate

- **Java 17** - Limbaj de programare
- **JAX-RS (Jersey 3.1.3)** - Framework REST
- **JPA/Hibernate 6.2.7** - ORM
- **MySQL 8.0** - Baza de date
- **Docker** - Containerizare
- **Maven** - Build tool
- **Jackson** - Serializare JSON

## ✅ Cerințe îndeplinite

- ✅ Serviciu web REST implementat
- ✅ Client Java care se conectează la serviciu
- ✅ Baza de date cu minim 3 tabele (implementat 12 tabele)
- ✅ Relații între tabele (One-to-Many, Many-to-Many)
- ✅ Query-uri complexe (JOIN-uri, agregări, filtrare, paginare, sortare)
- ✅ Operații CRUD complete pentru toate entitățile
- ✅ Documentație completă

## 👤 Autor

Proiect realizat pentru cursul **Distributed Services Oriented Architectures**

## 📄 Licență

Acest proiect este realizat în scop educațional.
