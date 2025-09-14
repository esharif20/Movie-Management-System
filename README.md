# WarwickPlus – Movie Management Application

WarwickPlus is a Java-based CLI application for managing movie data, ratings, and credits.  
It was developed as part of a coursework project and demonstrates the use of **Java, Gradle, JUnit 5 testing, and external libraries** such as JSON and Apache Commons.

---

## ✨ Features

- Load and process **movie, rating, and credit data** from CSV/JSON files.
- Perform queries on movies, ratings, and cast/crew information.
- CLI interface with configurable options (e.g. number of records to process).
- Custom Gradle tasks for running the app with different configurations.
- Unit tests written with **JUnit 5** for reliability.

---

## 🛠️ Tech Stack

- **Java 17+**
- **Gradle 8.7** (build automation)
- **JUnit 5 (Jupiter)** for testing
- **Apache Commons CSV** for CSV parsing
- **JSON.org** for JSON handling
- **Commons CLI** for command-line options

---

## Installation

Clone the repository and navigate into the project folder:

```bash
git clone https://github.com/yourusername/WarwickPlus.git
cd WarwickPlus
```

Ensure you have **Java 17+** and **Gradle 8.7+** installed.  
(If using the Gradle wrapper, no global Gradle install is required.)

---

## ▶️ Running the Application

### Default run

```bash
./gradlew run
```

## ▶️ Running the Tests

### Default run

```bash
./gradlew test
```
