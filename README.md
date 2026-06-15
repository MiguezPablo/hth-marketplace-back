# HTH Marketplace Back

Backend autonomo del marketplace. No depende de wallet ni comparte su base.

## Stack

- Spring Boot 3
- Java 21
- PostgreSQL
- Flyway
- JWT + refresh tokens

## Variables requeridas

- `SPRING_DATASOURCE_URL`
- `SPRING_DATASOURCE_USERNAME`
- `SPRING_DATASOURCE_PASSWORD`
- `JWT_SECRET`
- `CORS_ALLOWED_ORIGINS`

## Endpoints principales

- `POST /api/auth/register`
- `POST /api/auth/login`
- `POST /api/auth/refresh`
- `GET /api/marketplace/products`
- `GET /api/marketplace/products/{id}`
- `GET /api/marketplace/products/seller/{alias}`
- `GET /api/marketplace/products/my/listings`
- `POST /api/marketplace/products`
- `PUT /api/marketplace/products/{id}`
- `DELETE /api/marketplace/products/{id}`
- `POST /api/marketplace/orders`
- `GET /api/marketplace/orders/buyer`
- `GET /api/marketplace/orders/seller`
