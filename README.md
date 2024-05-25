# Audio Library

Welcome to the Audio Library project! This application allows users to manage their music collections by creating playlists, adding songs, and exporting playlists in various formats. 

# Features

The Audio Library project offers several key features to help you manage your music collection effectively:

1. **User Authentication:** Users can register new accounts or log in to existing ones to access the application's features securely.

2. **Playlist Management:** Create and modify playlists to organize your music collection according to your preferences.

3. **Song Management:** Add new songs to the library, search for existing songs, and add them to playlists.

4. **Export Playlists:** Export playlists in various formats, including JSON and CSV.

5. **Administrator Privileges:** Administrators have additional privileges, such as creating new songs in the library.

6. **Command-Line Interface (CLI):** The application offers a user-friendly CLI interface, allowing for easy interaction with its features using simple commands.

7. **Role-Based Access Control:** Users have access to different commands based on their roles (Anonymous, Authenticated, Administrator), ensuring proper access control.

8. **Error Handling:** The application includes robust error handling mechanisms to provide informative error messages and handle exceptional scenarios.

9. **Pagination:** Lists of items, such as playlists or search results, are paginated to improve readability.

10. **Documentation:** Comprehensive documentation, including JavaDoc comments, helps users understand how to use the application and its features effectively.

# User Types and Available Commands

## Anonymous User

An anonymous user is an unauthenticated user in the system. They have limited access and need to authenticate to use the application's advanced functionalities. The commands available for anonymous users are:

- `register`: Allows registering a new account in the system.
- `login`: Allows logging into an existing account.
- `help`: Displays the list of available commands and their descriptions.
- `exit`: Closes the application.

## Authenticated User

An authenticated user is a user who has successfully logged into the system. They have extended access and can use the application's main functionalities. The commands available for authenticated users are:

- `search`: Searches for music tracks in the music library.
- `create playlist`: Creates a new playlist for organizing tracks.
- `add`: Adds music tracks to existing playlists.
- `export playlist`: Exports playlists in various formats for sharing or backup.
- `logout`: Logs out of the current user session.

## Administrators

Administrators are users with extended privileges in the system, having full control over functionalities and data. In addition to the commands available to authenticated users, administrators have access to additional commands for system administration. The additional commands for administrators are:

- `create song`: Allows creating new music tracks in the library.
- `audit`: Accesses audit logs for monitoring system activity and user actions.
- `promote`: Promote users to higher user types.

# Data Storage
This application utilizes various data storage methods for different functionalities:

### User Database:
User information is managed within a SQL database, operated through Docker. The database schema includes the Account table, storing user credentials and related data. 
The application interacts with this database for user management operations like registration, authentication, and user promotion.

### Song Data:
Song information is stored in a CSV file. The application accesses this file to handle functionalities like song creation, searching, and listing. 
Each row in the CSV likely represents a song, with columns representing attributes such as title, artist and release year.

### Playlist Data:
Playlists are stored in JSON format. Each playlist is represented as a JSON file containing its name and the the songs it contains. 

### Audit Data:
The audit data, kept in a proprietary format, meticulously tracks user interactions within the audio library system.
Each entry includes detailed information such as the specific command executed and whether the operation was successful or not.

# Command Syntax
Here's the syntax for each command available in the Audio Library application:

### register
```sh
register <username> <password>
```
### login
```sh
login <username> <password>
```
### logout
```sh
logout 
```
### promote
```sh
promote <username> 
```
### create song
```sh
create song "<song_name>" "<artist>" <year>
```
### create playlist
```sh
create playlist <playlist_name>
```
### list playlists
```sh
list playlists
```
### add
```sh
add byName "<playlistName>" uniqueSongIdentifier1 uniqueSongIdentifier2 ...
or
add byId "<playlistId>" uniqueSongIdentifier1 uniqueSongIdentifier2 ...
```
### search
```sh
search <criteriaType> "<searchCriteria>"
```
### export playlist
```sh
export playlist <playlistName> <format>
or
export playlist <uniquePlaylistIdentifier> <format>
```
### audit
```sh
audit <username>
```
### help
```sh
help
```
### exit
```sh
exit
```




