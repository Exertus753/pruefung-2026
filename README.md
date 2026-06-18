# IPWA02-01 – Programmierung von industriellen Informationssystemen mit Java EE

In diesem Repository befindet sich meine Lösung zur Aufgabenstellung des Kurses **IPWA02-01**. Hierbei habe ich mich für die **"Aufgabenstellung 1: Like Hero To Zero"** entschieden.

Dieses Projekt wurde als eine **integrierte Java EE Webanwendung** realisiert, bei welcher Frontend und Backend in einer einzigen ausführbaren `.war`-Datei gebündelt sind. Dies erleichtert das Deployment und die lokale Ausführung erheblich, da kein separater Node.js Dev-Server oder eine getrennte REST-API verwaltet werden muss.

---

## 1. Technologiestack und Struktur

Die Webanwendung wurde streng nach den Vorgaben des IU-Skripts mit folgenden Java-Enterprise-Technologien entwickelt:

*   **Präsentationsschicht (Frontend)**: JSF (JavaServer Faces 2.2/2.3) mit standardkonformem CSS in einem schlichten, minimalistischen Light-Theme (klassische studentische Webästhetik).
*   **Steuerungsschicht (Controller/Beans)**: CDI Beans (`@Named` und `@Inject`) zur Abwicklung der Geschäftslogik und zur asynchronen Benutzeroberflächen-Aktualisierung mittels JSF AJAX.
*   **Datenhaltungsschicht (Persistence)**: JPA (Java Persistence API) unter Verwendung von **Hibernate Core** als Persistenz-Provider.
*   **Datenbank**: Relationale Datenbank (**MySQL / MariaDB**).
*   **Applikationsserver**: Apache TomEE 8.0.15 (WebProfile).

---

## 2. Voraussetzungen zur lokalen Ausführung

Um das Projekt lokal zu bauen und auszuführen, benötigen Sie:

*   **Git** auf Ihrem System installiert.
*   **Java JDK (Version 17)** installiert.
*   **Apache Maven** zum Builden des Projekts.
*   **MySQL oder MariaDB** auf Ihrem System laufend.
*   **Apache TomEE 8.0.15 (WebProfile)** als Servlet-Container.

---

## 3. Installation und Ausführung (Manuell)

### Schritt 1: Repository klonen
Klonen Sie das Repository auf Ihr lokales System:
```bash
git clone https://github.com/Exertus753/pruefung-2026.git
cd pruefung-2026
```

### Schritt 2: Datenbank vorbereiten
*   Stellen Sie sicher, dass Ihre MySQL/MariaDB-Instanz auf Port `3306` active ist.
*   Die DataSource-Konfiguration befindet sich im Projekt unter [resources.xml](file:///c:/Users/tief_/Desktop/Prüfung/src/main/webapp/WEB-INF/resources.xml). 
*   Standardmäßig verbindet sich das Projekt mit der Datenbank `herozero` über den Benutzer `root` ohne Passwort.
*   > [!IMPORTANT]
    > Sie müssen die Datenbank vorab **nicht** manuell anlegen oder Tabellen erzeugen. Hibernate erzeugt das Schema (`herozero`) und die Tabellen automatisch (`hibernate.hbm2ddl.auto = update`).

### Schritt 3: Maven Build durchführen
Bauen Sie das Projekt im Projektverzeichnis, um die `.war`-Datei zu generieren:
```bash
mvn clean package
```
Die fertige Datei `herozero.war` befindet sich nach dem erfolgreichen Build im Ordner `target/`.

### Schritt 4: Deployment in Apache TomEE
*   Kopieren Sie die generierte Datei `target/herozero.war` (oder entpacken Sie den Inhalt) in das Verzeichnis `/webapps/` Ihres Apache TomEE-Servers.
*   Starten Sie den TomEE-Server über die Konsole in dessen `/bin/`-Verzeichnis:
    *   **Windows**: `.\catalina.bat run`
    *   **Linux/macOS**: `./catalina.sh run`

### Schritt 5: Webanwendung aufrufen
Öffnen Sie Ihren Webbrowser und navigieren Sie zu:
`http://localhost:8080/herozero/`

---

## 4. Ausführung über die integrierte portable Umgebung (Schnellstart)

Falls Sie das Projekt direkt über die im Repository enthaltenen portablen Tools starten möchten, führen Sie folgende Schritte aus:

### Schritt 1: MariaDB-Datenbank starten
Öffnen Sie ein PowerShell-Terminal im Projektordner und führen Sie aus:
```powershell
& "C:\Users\(your_user)\Desktop\Prüfung\tools\mariadb\bin\mysqld.exe" --datadir="C:\Users\(your_user)\Desktop\Prüfung\tools\mariadb\data" --port=3306 --console
```
*(Lassen Sie dieses Terminal geöffnet)*

### Schritt 2: TomEE-Server starten
Öffnen Sie ein **zweites** PowerShell-Terminal im Projektordner und führen Sie aus:
```powershell
$env:JAVA_HOME="C:\Program Files\Eclipse Adoptium\jdk-17.0.19.10-hotspot"
$env:CATALINA_HOME="C:\Users\(your_user)\Desktop\Prüfung\tools\tomee"
& "C:\Users\(your_user)\Desktop\Prüfung\tools\tomee\bin\catalina.bat" run
```
*(Lassen Sie auch dieses Terminal geöffnet)*

### Schritt 3: Webanwendung aufrufen
Öffnen Sie Ihren Webbrowser und gehen Sie auf:
`http://localhost:8080/herozero/`

---

## 5. Testen der Rollen und Funktionalitäten

Das Projekt initialisiert beim ersten Start automatisch Testdaten und Standard-Benutzerkonten über die Klasse [DbInit.java](file:///c:/Users/tief_/Desktop/Prüfung/src/main/java/com/herozero/util/DbInit.java). Folgende Konten stehen zum Testen der Funktionalität bereit:

1.  **Bürger (Öffentliche Ansicht)**:
    *   Keine Anmeldung erforderlich. Auf der Startseite kann ein Land ausgewählt werden, um den aktuellsten CO2-Ausstoß (AJAX-unterstützt) sowie den historischen Überblick zu sehen.
2.  **Wissenschaftler (Scientist)**:
    *   **Benutzername**: `scientist` | **Passwort**: `password123`
    *   Ermöglicht das Eintragen neuer Klimaforschungsdaten und die Korrektur bestehender Werte im Dashboard. Neue oder geänderte Einträge verbleiben im Status `PENDING` (Ausstehend) und sind öffentlich nicht sichtbar.
3.  **Herausgeber (Publisher)**:
    *   **Benutzername**: `publisher` | **Passwort**: `admin123`
    *   Ermöglicht das Einsehen, Freigeben (`APPROVED`) oder Ablehnen von ausstehenden Datenänderungen im Dashboard. Erst nach der Freigabe sind die Werte in der Bürger-Ansicht sichtbar.
