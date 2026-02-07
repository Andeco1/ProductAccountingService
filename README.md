# Сервис учёта поставок
Этот проект реализует REST-сервис на Java с использованием Spring Boot, который позволяет учитывать поставки продуктов от разных поставщиков. Проект включает работу с БД PostgreSQL через JPA, миграции схемы через Liquibase, а также контейнеризацию с Docker.
## Используемые технологии:
Java 21, Spring Boot 3, Spring Data JPA (Hibernate), PostgreSQL, Liquibase, Docker, MapStruct, Maven, JUnit 5, Mockito
## Сборка и запуск
### Запуск через Docker
 - Убедитесь, что установлены Docker и Docker Compose
 - Запустите контейнеры командой:
   - ``` docker-compose up -d --build ```
 - Это соберёт образ приложения и запустит его вместе с контейнером PostgreSQL.
 Приложение будет доступно по адресу http://localhost:8080. Для настройки подключения к БД используются переменные окружения из docker-compose.yml (по умолчанию указаны product_accounting, admin, admin).

### Локальная установка
- Убедитесь, что установлены Java 17+ и Maven, а также запущена СУБД PostgreSQL.
- Создайте базу данных product_accounting и пользователя, соответствующего настройкам в application.properties (по умолчанию URL jdbc:postgresql://localhost:5432/product_accounting, пользователь admin, пароль admin).
- Проверьте и при необходимости скорректируйте настройки подключения к БД через переменные окружения SPRING_DATASOURCE_URL, SPRING_DATASOURCE_USERNAME, SPRING_DATASOURCE_PASSWORD.
- Соберите проект с помощью Maven:
``` mvn clean package ```
- Запустите приложение, выполнив JAR-файл (пример):
``` java -jar target/*.jar ```
- После старта Liquibase автоматически создаст таблицы и применит все миграции.
# API документация (v1.0)

> Все данные передаются в формате `JSON`.  
> API сгруппировано по контроллерам.

---

## ProductController (`/api-v1.0/product`)

### GET `/api-v1.0/product?name={name}`
Получить информацию о продукте по названию.

- **Параметр запроса**
  - `name` (`String`) — название продукта.

- **Ответ** (`ProductDTO`, `application/json`)
```json
{
  "id": 1,
  "name": "Apple",
  "info": "Доп. информация (необязательное)",
  "measurementUnit": "kg"
}
```

- **Поля `ProductDTO`**
  - `id` (`Long`) — идентификатор продукта.
  - `name` (`String`) — название продукта.
  - `info` (`String`, optional) — дополнительная информация.
  - `measurementUnit` (`String`) — единица измерения (например, `"kg"` или `"pcs"`).

---

### POST `/api-v1.0/product`
Создать или обновить продукт.

- **Тело запроса** (`ProductDTO`, `application/json`)
```json
{
  "name": "Apple",
  "info": "Доп. информация",
  "measurementUnit": "kg"
}
```

- **Ответ** (`ProductDTO`, `application/json`) — сохранённый объект с присвоенным `id`.

---

## SupplierController (`/api-v1.0/supplier`)

### GET `/api-v1.0/supplier?name={name}`
Получить поставщика по имени.

- **Параметр запроса**
  - `name` (`String`) — наименование поставщика.

- **Ответ** (`SupplierDTO`, `application/json`)
```json
{
  "uuid": "550e8400-e29b-41d4-a716-446655440000",
  "name": "ООО Поставщик",
  "address": "г. Москва, ул. Пример, 1"
}
```

- **Поля `SupplierDTO`**
  - `uuid` (`UUID`) — уникальный идентификатор поставщика.
  - `name` (`String`) — имя поставщика.
  - `address` (`String`) — адрес поставщика.

---

### POST `/api-v1.0/supplier`
Создать или обновить поставщика.

- **Тело запроса** (`SupplierDTO`, `application/json`)
```json
{
  "name": "ООО Поставщик",
  "address": "г. Москва, ул. Пример, 1"
}
```

- **Ответ** (`SupplierDTO`, `application/json`) — сохранённый объект с `uuid`.

---

## DeliveryRecordsController (`/api-v1.0/deliveryRecord`)

### POST `/api-v1.0/deliveryRecord`
Сохранить одну или несколько записей о поставках.

- **Тело запроса** (`DeliveryRecordsRequest`, `application/json`)
```json
{
  "deliveries": [
    {
      "supplierName": "Имя поставщика",
      "date": "2023-01-25T10:15:30Z",
      "info": "Дополнительная информация",
      "items": [
        {
          "productName": "Apple",
          "quantity": 10.5,
          "acceptance": true
        },
        {
          "productName": "Orange",
          "quantity": 5,
          "acceptance": false
        }
      ]
    }
  ]
}
```

- **Структура**
  - `DeliveryRecordsRequest`
    - `deliveries` (`array` of `DeliveryRecordInfo`)
  - `DeliveryRecordInfo`
    - `supplierName` (`String`) — имя поставщика.
    - `date` (`String`, ISO-8601) — дата и время поставки.
    - `info` (`String`) — информация о поставке.
    - `items` (`array` of `ItemInfo`) — список товаров.
  - `ItemInfo`
    - `productName` (`String`) — название продукта.
    - `quantity` (`Number`) — количество принятого товара.
    - `acceptance` (`Boolean`) — признак принятия товара.

- **Обработка**
  - Для каждого элемента списка вызывается `DeliveryRecordService.saveDeliveryRecord`.

- **Ответ**
  - HTTP `200 OK`

---

### GET `/api-v1.0/deliveryRecord`
Получить все записи о поставках.

- **Ответ** (`application/json`) — массив объектов `DeliveryRecord` (сущностей).

**Пример**
```json
[
  {
    "id": 1,
    "supplier": {
      "uuid": "550e8400-e29b-41d4-a716-446655440000",
      "name": "Имя",
      "address": "Адрес"
    },
    "date": "2023-01-25T10:15:30Z",
    "info": "Дополнительная информация"
  }
]
```

- **Поля записи**
  - `id` (`Long`)
  - `supplier` (объект `Supplier`)
  - `date` (`Instant`, ISO-8601)
  - `info` (`String`)

---

## ProductPricePeriodController (`/api-v1.0/ppp`)

### POST `/api-v1.0/ppp`
Создать ценовой период для продукта от поставщика.

- **Тело запроса** (`ProductPricePeriodDTO`, `application/json`)
```json
{
  "supplierName": "ООО Поставщик",
  "productName": "Apple",
  "startDate": "2023-01-01T00:00:00Z",
  "endDate": "2023-01-31T23:59:59Z",
  "pricePerMeasurementUnit": 12.34
}
```

- **Поля `ProductPricePeriodDTO`**
  - `supplierName` (`String`)
  - `productName` (`String`)
  - `startDate` (`String`, ISO-8601)
  - `endDate` (`String`, ISO-8601)
  - `pricePerMeasurementUnit` (`Number`, BigDecimal)

- **Ответ**
  - `ProductPricePeriodDTO` с сохранёнными данными.

---

### GET `/api-v1.0/ppp/all`
Получить все ценовые периоды.

- **Ответ** (`application/json`) — массив `ProductPricePeriodDTO`:
```json
[
  {
    "supplierName": "ООО Поставщик",
    "productName": "Apple",
    "startDate": "2023-01-01T00:00:00Z",
    "endDate": "2023-01-31T23:59:59Z",
    "pricePerMeasurementUnit": 12.34
  }
]
```

---

## StatisticsController (`/api-v1.0/statistics`)

### GET `/api-v1.0/statistics/report?dateFrom={dateFrom}&dateTo={dateTo}`
Получить статистику по поставкам за указанный период.

- **Параметры запроса**
  - `dateFrom` (`LocalDate`, формат `YYYY-MM-DD`) — начало периода (включительно).
  - `dateTo` (`LocalDate`, формат `YYYY-MM-DD`) — конец периода (включительно).

- **Ответ** (`ReportResponse`, `application/json`) — объект со статистическими данными.

**Структура `ReportResponse`**
- `quantityOfPeriod` (`Number`) — суммарное количество всех принятых товаров за период.
- `priceOfPeriod` (`Number`) — суммарная стоимость всех принятых товаров за период.
- `statistics` (`array` of `StatInfo`) — по одному объекту на каждого поставщика.

**`StatInfo`**
- `supplierName` (`String`)
- `items` (`array` of `AcceptedItemInfo`)

**`AcceptedItemInfo`**
- `productName` (`String`)
- `quantity` (`Number`)
- `price` (`Number`)

**Итоговые поля на поставщика**
- `totalItemsQuantity` (`Number`) — общее количество товаров у поставщика.
- `totalItemsPrice` (`Number`) — общая стоимость товаров у поставщика.

**Пример ответа**
```json
{
  "quantityOfPeriod": 15,
  "priceOfPeriod": 150,
  "statistics": [
    {
      "supplierName": "ООО Большой Сад",
      "items": [
        {
          "productName": "Apple",
          "quantity": 15,
          "price": 150
        }
      ],
      "totalItemsQuantity": 15,
      "totalItemsPrice": 150
    }
  ]
}
```

---

## Примечания
- Все временные поля в теле запросов и ответах приведены в **ISO-8601** (для `date`/`dateTime`/`Instant`).
- Числовые поля для цены рекомендуется передавать как `BigDecimal` (например, в JSON — число с плавающей точкой или строка, если требуется точность).

---

# Структура базы данных 
![Структура БД.png](%D0%A1%D1%82%D1%80%D1%83%D0%BA%D1%82%D1%83%D1%80%D0%B0%20%D0%91%D0%94.png)
# Архитектура проекта
![Архитектура.png](%D0%90%D1%80%D1%85%D0%B8%D1%82%D0%B5%D0%BA%D1%82%D1%83%D1%80%D0%B0.png)
# Пример отчёта
![Пример отчёта.png](%D0%9F%D1%80%D0%B8%D0%BC%D0%B5%D1%80%20%D0%BE%D1%82%D1%87%D1%91%D1%82%D0%B0.png)