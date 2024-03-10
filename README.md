# TutorManagement

### Preview

https://github.com/FernandoLomazzi/TutorManagement/assets/78239004/e61abb1f-c7a4-4ff8-ae8f-50c68d227a22

### Build

To build TutorManagement, execute the following command:

    mvn package
    
**NOTE**: TutorManagement requires **Java 17** and above.

### Requeriments

#### Functional
- FR-1: The system must allow adding, modifying and deleting students.
- FR-2: The system must allow adding, modifying and deleting teachers.
- FR-3: The system must allow adding, modifying and deleting subjects.
- FR-4: The system must allow adding, modifying and deleting institutions.
- FR-5: The system must allow generating classes between students, teachers and subjects.
- FR-6: The system must allow generating weekly reports with detailed income statistics.
- FR-7: The system must allow students to be tracked in terms of which weeks they had classes and how many hours they were.
- FR-8: The system must allow generating a report of unpaid classes by students where each student and their unpaid classes are displayed, allowing each one to mark paid.
- FR-9: The system must allow generating a report of unpaid classes to teachers where each teacher and their unpaid classes are displayed, allowing each one to mark paid.

#### Non-Functional
- NFR-1: The system should be able to handle a minimum of 500 students and 100 teachers.
- NFR-2: The system should process reports within 3 seconds.
- NFR-3: The system must allow migrating existing data from an Excel spreadsheet.

### Entity-Relationship Diagram
![](/docs/EntityRelationshipDiagram.png)
