# API Pagination Guide

This document describes how to use pagination in the Lock Service API.

## Overview

All list endpoints in the Lock Service API now support pagination using Spring Data's `Pageable` interface. This allows clients to retrieve large datasets in manageable chunks.

## Pagination Parameters

You can control pagination using query parameters:

| Parameter | Type    | Default | Description                                           |
|-----------|---------|---------|-------------------------------------------------------|
| `page`    | integer | 0       | Zero-based page index                                 |
| `size`    | integer | 20      | Number of items per page                              |
| `sort`    | string  | -       | Sorting criteria in format: `property[,asc\|desc]`    |

## Examples

### Basic Pagination

Get the first page with 10 items:
```
GET /api/v1/locks?page=0&size=10
```

Get the second page with 20 items:
```
GET /api/v1/locks?page=1&size=20
```

### Sorting

Sort by a single property (ascending by default):
```
GET /api/v1/locks?sort=id
```

Sort by a property in descending order:
```
GET /api/v1/locks?sort=id,desc
```

Sort by multiple properties:
```
GET /api/v1/locks?sort=macAddress,asc&sort=id,desc
```

### Combined Example

Get page 2 (items 21-30) sorted by ID in descending order:
```
GET /api/v1/locks?page=2&size=10&sort=id,desc
```

## Response Format

Paginated endpoints return a response in the following format:

```json
{
  "content": [
    {
      "id": 1,
      "macAddress": "00:11:22:33:44:55",
      ...
    },
    ...
  ],
  "pageable": {
    "sort": {
      "sorted": false,
      "unsorted": true,
      "empty": true
    },
    "pageNumber": 0,
    "pageSize": 20,
    "offset": 0,
    "paged": true,
    "unpaged": false
  },
  "totalPages": 5,
  "totalElements": 100,
  "last": false,
  "first": true,
  "size": 20,
  "number": 0,
  "sort": {
    "sorted": false,
    "unsorted": true,
    "empty": true
  },
  "numberOfElements": 20,
  "empty": false
}
```

### Response Fields

- `content`: Array of items for the current page
- `totalPages`: Total number of pages
- `totalElements`: Total number of items across all pages
- `size`: Number of items per page (requested size)
- `number`: Current page number (zero-based)
- `numberOfElements`: Number of items in the current page
- `first`: Whether this is the first page
- `last`: Whether this is the last page
- `empty`: Whether the page has no content

## Supported Endpoints

All list endpoints support pagination:

### Locks
- `GET /api/v1/locks`

### Lockers
- `GET /api/v1/lockers`
- `GET /api/v1/lockers/unassgined` (Note: endpoint has typo in original code)
- `GET /api/v1/lockers/{id}/locks`

### Lock Events
- `GET /api/v1/lock-events`

### Ski Resorts
- `GET /api/v1/ski-resorts`
- `GET /api/v1/ski-resorts/{id}/lockers`
- `GET /api/v1/ski-resorts/{id}/locks`
- `GET /api/v1/ski-resorts/{id}/lock-events`
- `GET /api/v1/ski-resorts/{id}/ski-tickets`

### Ski Tickets
- `GET /api/v1/ski-tickets`
- `GET /api/v1/ski-tickets/{id}/lock-events`

## Best Practices

1. **Always use pagination for large datasets**: Even if you think the dataset is small, it may grow over time.

2. **Choose appropriate page sizes**: 
   - Smaller pages (10-20) for UI lists
   - Larger pages (50-100) for batch processing
   - Very large pages can impact performance

3. **Use sorting for consistent results**: When iterating through pages, use sorting to ensure consistent ordering.

4. **Check `last` field**: Use the `last` field to know when you've reached the end of the data.

5. **Handle empty pages gracefully**: Check the `empty` field or `numberOfElements` to handle cases where a page has no content.

## Implementation Notes

- Pagination is implemented using Spring Data's `Pageable` and `Page` interfaces
- The default page size is 20 items if not specified
- Page numbers are zero-based (first page is 0)
- All repository methods support pagination through overloaded methods
- The old non-paginated methods are maintained for backward compatibility at the service layer

## Migration Guide

If you're currently using the non-paginated endpoints, you can:

1. **Continue using them without parameters**: The endpoints will work as before, returning all results
2. **Gradually adopt pagination**: Add pagination parameters when needed
3. **Update your code**: Eventually update client code to always use pagination for better performance

Note: While the controllers now return `Page<T>` objects, Spring will automatically handle clients that don't expect pagination by using default values.
