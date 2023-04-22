# GoodReads
## IT Talents - Final Project
This is a RESTful Spring Web API for the backend of the Goodreads site.The original site is a popular social networking site for book lovers.
Goodreads allows users to connect with other users, discover new books, and share their thoughts and opinions on books.
## Main functionalities of Goodreads:
- Making friends:
  Users can make friends with other users and see what their friends are reading, their reviews and their challenges.
- Reading books:
  Users can add books to their "Want to Read", "Currently Reading", and "Read" shelves, as well as create custom shelves for other categories, such as "Favorites" or     "Classics."
- Creating challenges:
  Goodreads allows users to set reading goals for themselves, such as a certain number of books to read in a year.
- Ratings and Reviews:
  Users can rate and review books they have read, which are used to generate recommendations for other users based on their reading preferences.

## For local developmnet
- Clone repository
- Open project in intellij
- Build project with maven
- Configure port for the application (Default 8080)
- Configure database by applying \src\test\resources\schema.sql file to local sql server
- Start application from GoodreadsApplication class

### Notes
In addition to these features, the is the ability to follow other users and rate books, as well as the functionality for CRUD operations for Books, Shelves, Comments,Reviews and Challenges.
To use our API, simply send HTTP requests to the appropriate endpoints for each feature. All the endpoints and their expected parameters are documented using Swagger.
It can be accessed by running the project and navigating to the following URL: http://{host}:{port}/swagger-ui.html.
We hope you enjoy exploring our Goodreads copy and look forward to hearing your feedback!

### Contributors:
@AntoniaRuseva @MariaChesh
