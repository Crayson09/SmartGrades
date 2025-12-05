# SmartGrades

## Konzept

* Noten eintragen
* Termine für Klausuren festlegen
* Automatische Berechnung: Welche Note brauchst du, um deine Gesamtwertung zu verbessern, zu halten oder zu verschlechtern

## Daten-Struktur (MongoDB)

### Schüler (Collection: `students`)

```json
{
  "id": "UUID",
  "name": "Max Mustermann",
  "class": "11A",
  "schule": "Gymnasium München"
}
```

### Fach (Collection: `subjects`)

```json
{
  "id": "UUID",
  "studentId": "UUID",        // Verweis auf Schüler
  "name": "Mathe",
  "fachTyp": "Hauptfach"      // Hauptfach, Nebenfach, etc.
}
```

### Note (Collection: `grades`)

```json
{
  "id": "UUID",
  "subjectId": "UUID",       // Verweis auf Fach
  "wert": 1,                 // z.B. 1–6 Punkte
  "typ": "Klausur",          // Klausur, Mündlich, Ex
  "datum": "2025-12-05"      // optional
}
```

