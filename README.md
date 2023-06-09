# Аттестационный проект
## Платформа для размещения объявлений
***

### Проект написан студентом-разработчиком курса "Профессия Java-разработчик ISA":
* Большаков Никита ([GitHub](https://github.com/bnikmik))
***

## Описание проекта и его функциональность
Платформа является аналогом сервиса "Авито". Пользователи могут регистрироваться, просматривать объявления и оставлять комментарии к ним, а также могут размещать объявления. 

**Задачей было написать backend-часть сайта на Java и реализовать следующий функционал:**
* Авторизация и аутентификация пользователей.
* Распределение ролей между пользователями: пользователь и администратор.
* CRUD для объявлений на сайте: администратор может удалять или редактировать все объявления, а пользователи — только свои.
* CRUD для комментариев к объявлениям на сайте: администратор может удалять или редактировать все комментарии, а пользователи — только свои.
* Загрузка и отображение изображений объявлений и аватаров пользователей.
* В заголовке сайта можно осуществлять поиск объявлений по названию.

В качестве шаблона был предоставлен файл [Openapi](openapi.yaml)  
ТЗ на разработку [Technical task](https://skyengpublic.notion.site/02df5c2390684e3da20c7a696f5d463d)

### Стек технологий
***
**В проекте используются**:

* Backend:
    - Java 11
    - Maven
    - Spring Boot
    - Spring Web
    - Spring Data JPA
    - Spring Security
    - GIT
    - REST
    - Swagger
    - Lombok
    - Stream API
* SQL:
    - PostgreSQL
    - Liquibase
* Test:
    - Mockito
* Frontend:
    - Docker образ 

### Запуск
***

**Для запуска нужно:**
- Клонировать проект в среду разработки
- Прописать путь к БД в файле **[application.properties](src/main/resources/application.properties)**
- Запустить **[Docker](https://www.docker.com)**
- Скачать Docker образ с помощью команды ```docker pull ghcr.io/bizinmitya/front-react-avito:latest```
- Запустить Docker образ с помощью команды ```docker run -p 3000:3000 ghcr.io/bizinmitya/front-react-avito:latest```
- Запустить метод **main** в файле **[HomeworkApplication.java](src/main/java/ru/skypro/homework/HomeworkApplication.java)**

После выполнения всех действий сайт будет доступен по ссылке http://localhost:3000 и Swagger по [ссылке](http://localhost:8080/swagger-ui/index.html#).
