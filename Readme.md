Employee Service Assignment Documentation
========================================

## Project Overview
This project is a Spring Boot REST API for managing employees and their education details. It demonstrates best practices for entity relationships, DTO mapping, validation, and robust update logic using JPA and ModelMapper.

## Features Implemented
- **CRUD operations for Employee** (Create, Read, Update, Delete)
- **Education Details**: Each employee can have multiple education records (e.g., Bachelors, Masters)
- **Validation**: Prevents duplicate education types for a single employee
- **Robust Update Logic**: Safely updates employee and education details, handling add, update, and delete scenarios
- **Error Handling**: Handles common JPA/Hibernate pitfalls (identifier alteration, orphan removal, etc.)

## Key Technical Decisions & Pitfalls
- **JPA Entity Identity**: Never change the `id` of a managed entity after loading from the database
- **ModelMapper**: Configured to skip mapping the `id` field during updates to avoid identifier errors
- **Collection Handling**: Always clear and add to the existing collection for `@OneToMany` with orphan removal, never replace the collection instance
- **Validation**: Checks for duplicate education types before processing requests
- **Transactional Service Methods**: Ensures atomicity for create and update operations

## Example API Requests

### Create Employee with Education Details
POST `/api/v1/employee`
```json
{
  "name": "Alice Smith",
  "age": 28,
  "gender": "Female",
  "dob": "1996-03-15",
  "birthPlace": "London",
  "educationDetails": [
    {
      "type": "BACHELORS",
      "institutionName": "UCL",
      "board": "UK Board",
      "passingYear": "2017",
      "result": "First Class",
      "scale": 4
    },
    {
      "type": "MASTERS",
      "institutionName": "Oxford",
      "board": "UK Board",
      "passingYear": "2023",
      "result": "Distinction",
      "scale": 4
    }
  ]
}
```

### Update Employee and Education Details
PUT `/api/v1/employee`
```json
{
  "id": 35,
  "name": "Alice Smith",
  "age": 29,
  "gender": "Female",
  "dob": "1996-03-15",
  "birthPlace": "London",
  "educationDetails": [
    {
      "id": 5,
      "type": "BACHELORS",
      "institutionName": "UCL",
      "board": "UK Board",
      "passingYear": "2017",
      "result": "First Class",
      "scale": 4
    },
    {
      "type": "PHD",
      "institutionName": "Oxford",
      "board": "UK Board",
      "passingYear": "2025",
      "result": "Distinction",
      "scale": 4
    }
  ]
}
```

## Common Errors & Solutions
- **Identifier alteration**: Never map or set the `id` field during update
- **Orphan removal error**: Always clear/add to the existing collection, never replace
- **Duplicate education type**: Validation prevents adding the same type twice
- **Enum mismatch**: Ensure `type` values match the Java enum exactly (case-sensitive)
- **500 Internal Server Error**: Check logs for the real cause; usually a mapping or validation issue

## Best Practices for Future Students
- Always fetch and update managed entities from the database
- Never change the `id` of a loaded entity
- Use DTOs for API requests/responses
- Validate input before processing
- Handle collections with care in JPA (clear/add, not replace)
- Read error messages and stack traces carefully—they point to the root cause

## Credits
Assignment and solution by [Your Name].
For questions or improvements, contact your instructor or project maintainer.
