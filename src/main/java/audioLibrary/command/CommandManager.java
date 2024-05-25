package audioLibrary.command;

import audioLibrary.Pair;
import audioLibrary.Utils;
import audioLibrary.exceptions.*;
import audioLibrary.music.Library;
import audioLibrary.music.PlaylistManager;
import audioLibrary.user.User;

public class CommandManager {

    public static Pair<Command, Boolean> execute(String input, String[] parts, User user, Library library, PlaylistManager playlistManager) {
        user.addAudit(input);
        Boolean success = true;
        Command com = null;

        if (parts[0].equals("login")) {
            try {
                com = new LoginCommand(user);
            } catch (InvalidUserTypeException e) {
                success = false;
                System.out.println(e.getMessage());
            }
        }
        else if (parts[0].equals("register")) {
            try {
                com = new RegisterCommand(user);
            } catch (InvalidUserTypeException e) {
                success = false;
                System.out.println(e.getMessage());
            }
        }
        else if (parts[0].equals("logout")) {
            try {
                com = new LogoutCommand(user);
                //user.addAuditSuccess(success);
            } catch (InvalidUserTypeException e) {
                success = false;
                System.out.println(e.getMessage());
            }
        }
        else if (parts[0].equals("promote")) {
            try {
                com = new PromoteCommand(user);
            } catch (InvalidUserTypeException e) {
                success = false;
                System.out.println(e.getMessage());
            }
        }
        else if (parts[0].equals("create") && parts[1].equals("song")) {
            try {
                library.createSong(input, user);
            } catch (InvalidUserTypeException | InvalidCommandException e) {
                success = false;
                System.out.println(e.getMessage());
            }
        }
        else if (parts[0].equals("create") && parts[1].equals("playlist")) {
            try {
                playlistManager.createPlaylist(parts);
            } catch (InvalidUserTypeException | InvalidPlaylistNameException | InvalidCommandException e) {
                success = false;
                System.out.println(e.getMessage());
            }
        }
        else if (parts[0].equals("add")) {
            try {
                playlistManager.addSong(parts);
            } catch (InvalidCommandException | InvalidUserTypeException | InvalidPlaylistNameException |
                     InvalidSongIdException | SongAlreadyInPlaylistException e) {
                success = false;
                System.out.println(e.getMessage());
            }
        }
        else if (parts[0].equals("list") && parts[1].equals("playlists")) {
            try {
                playlistManager.listPlaylists(parts);
            } catch (InvalidCommandException | InvalidUserTypeException e) {
                success = false;
                System.out.println(e.getMessage());
            }
        }
        else if (parts[0].equals("search")) {
            try {
                library.searchSong(parts, input, user);
            } catch (InvalidCommandException | InvalidUserTypeException e) {
                success = false;
                System.out.println(e.getMessage());
            }
        }
        else if (parts[0].equals("export") && parts[1].equals("playlist")) {
            try {
                playlistManager.exportPlaylist(parts);
            } catch (InvalidUserTypeException | InvalidCommandException | InvalidPlaylistNameException e) {
                success = false;
                System.out.println(e.getMessage());
            }
        }
        else if (parts[0].equals("audit")) {
            try {
                Utils.audit(user, parts);
            } catch (InvalidUserTypeException | InvalidCommandException e) {
                success = false;
                System.out.println(e.getMessage());
            }
        }
        else if (parts[0].equals("help") && parts.length == 1){
            new HelpCommand(user).execute();
        }
        else {
            success = false;
            System.out.println("Invalid command!");
        }

        return new Pair(com, success);
    }
}
