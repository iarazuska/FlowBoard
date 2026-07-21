<div align="center">

# FlowBoard

**A modern Kanban board desktop application built with JavaFX**

![Java](https://img.shields.io/badge/Java-17-orange?style=flat-square&logo=java)
![JavaFX](https://img.shields.io/badge/JavaFX-13-blue?style=flat-square)
![Maven](https://img.shields.io/badge/Maven-3.8-red?style=flat-square&logo=apache-maven)
![License](https://img.shields.io/badge/License-MIT-green?style=flat-square)

</div>

---

## Features

| Feature | Description |
|---|---|
| **Card Management** | Create, edit and delete cards with title, description, color, priority and due date |
| **Drag & Drop** | Move cards between columns with smooth drag and drop |
| **Real-time Search** | Filter cards instantly as you type |
| **Dark UI** | Modern dark-only theme with a black, purple and gray palette |
| **Sort Cards** | Sort by priority or due date with one click |
| **Export to JSON** | Save your board data to a JSON file |
| **Settings Panel** | Sliding settings panel with font size and sorting options |
| **Card Counter** | See how many cards are in each column |
| **Editable Title** | Click the board name to rename it |

---

## Getting Started

### Prerequisites

- Java 17+
- Maven 3.8+

### Run the app

```bash
# Clone the repository
git clone https://github.com/iarazuska/FlowBoard.git

# Navigate to project folder
cd FlowBoard

# Run with Maven
mvn clean javafx:run
```

---

## UI

FlowBoard uses a single dark theme defined in a dedicated stylesheet
(`flowboard.css`) instead of inline styles: black backgrounds, a purple
accent for actions and highlights, and gray for secondary text and
borders. Cards and columns have soft shadows, and interactive elements
(cards, buttons, the add-column button) react with hover states defined
directly in CSS.

---

## Tech Stack

<div align="center">

| Technology | Purpose |
|---|---|
| Java 17 | Core language |
| JavaFX 13 | UI framework |
| Maven | Build tool |
| Jackson | JSON serialization |

</div>

---
