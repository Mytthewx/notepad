package eu.mytthew.notepad;

import eu.mytthew.notepad.auth.IAuthService;
import eu.mytthew.notepad.auth.RuntimeAuthService;
import eu.mytthew.notepad.entity.Note;
import eu.mytthew.notepad.entity.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        IAuthService authService = new RuntimeAuthService();
        List<MenuItem> loginMenuItems = new ArrayList<>();
        loginMenuItems.add(new MenuItem(0, "Exit", () -> false));
        loginMenuItems.add(new MenuItem(1, "Log in", () -> logIn(authService)));
        loginMenuItems.add(new MenuItem(2, "Add user", () -> addUser(authService)));
        boolean repeat = true;
        while (repeat) {
            loginMenuItems
                    .stream()
                    .map(menu -> menu.getId() + ". " + menu.getName())
                    .forEach(System.out::println);
            int selection = scanner.nextInt();
            scanner.nextLine();
            repeat = loginMenuItems.get(selection).getBody().get();
        }
    }

    public static void loginLoop(IAuthService authService, String login) {
        boolean isUserLogged = false;
        while (!isUserLogged) {
            System.out.println("Type password: ");
            String password = scanner.nextLine();
            if (authService.login(login, password)) {
                System.out.println("Logged in!");
                isUserLogged = true;
                display(authService);
            } else {
                System.out.println("Wrong password!");
            }
        }
    }

    public static void display(IAuthService authService) {
        List<MenuItem> menuItems = new ArrayList<>();
        menuItems.add(new MenuItem(0, "Exit", () -> {
            System.exit(0);
            return false;
        }));
        menuItems.add(new MenuItem(1, "Add note", () -> addNote(authService)));
        menuItems.add(new MenuItem(2, "Review notes", () -> displayAllNotes(authService)));
        menuItems.add(new MenuItem(3, "Display today notes", () -> displayTodayNotes(authService)));
        menuItems.add(new MenuItem(4, "Remove note", () -> removeNotes(authService)));
        menuItems.add(new MenuItem(5, "Edit note", () -> editNote(authService)));
        menuItems.add(new MenuItem(6, "Add reminder", () -> addReminder(authService)));
        menuItems.add(new MenuItem(7, "Change login", () -> changeLogin(authService)));
        menuItems.add(new MenuItem(8, "Change password", () -> changePassword(authService)));
        menuItems.add(new MenuItem(9, "Logout", () -> {
            System.out.println("Logged out.");
            return false;
        }));
        boolean repeat = true;
        while (repeat) {
            menuItems
                    .stream()
                    .map(menuItem -> menuItem.getId() + ". " + menuItem.getName())
                    .forEach(System.out::println);
            int selection = scanner.nextInt();
            scanner.nextLine();
            repeat = menuItems
                    .get(selection)
                    .getBody()
                    .get();
        }
    }

    public static String formatNote(Note note) {
        return "\nDate: " + note.getNoteDate() +
                "\nTitle: " + note.getTitle() +
                "\nContent: '" + note.getContent() + '\'' +
                note.getReminders()
                        .stream()
                        .filter(reminder -> reminder.getDate().equals(LocalDate.now()))
                        .map(reminder -> "\nReminder name: " + reminder.getName())
                        .collect(Collectors.joining()) + "\n";
    }

    public static void verifyEditNote(Note note, String newTitle, String newContent, String newDate) {
        if (!newTitle.equals("")) {
            note.setTitle(newTitle);
        }
        if (!newContent.equals("")) {
            note.setContent(newContent);
        }
        if (!newDate.equals("")) {
            note.setNoteDate(LocalDate.parse(newDate));
        }
    }

    public static void logIn(IAuthService authService) {
        System.out.println("Type nickname: ");
        String login = scanner.nextLine();
        if (authService.containsNickname(login)) {
            loginLoop(authService, login);
        } else {
            System.out.println("The user with the given name does not exist.");
        }
    }

    public static void addUser(IAuthService authService) {
        System.out.println("Type nickname: ");
        String nickname = scanner.nextLine();
        System.out.println("Type password: ");
        String password = scanner.nextLine();
        if (authService.addUser(nickname, password)) {
            System.out.println("User added successfully!");
        } else {
            System.out.println("This nickname is already taken.");
        }
    }

    public static void addNote(IAuthService authService) {
        System.out.println("Note title:");
        String title = scanner.nextLine();
        System.out.println("Your note:");
        String content = scanner.nextLine();
        System.out.println("Set date [yyyy-MM-dd]:");
        String date = scanner.nextLine();
        User loggedUser = authService.getLoggedUser();
        if (date.equals("")) {
            date = String.valueOf(LocalDate.now());
            loggedUser.addNote(new Note(title, content, LocalDate.parse(date)));
            System.out.println("Note added successfully!");
        } else if (date.matches("^\\d{4}\\-(0[1-9]|1[012])\\-(0[1-9]|[12][0-9]|3[01])$")) {
            loggedUser.addNote(new Note(title, content, LocalDate.parse(date)));
            System.out.println("Note added successfully!");
        } else {
            System.out.println("Wrong date format.");
        }
    }

    public static void displayAllNotes(IAuthService authService) {
        User user = authService.getLoggedUser();
        if (user.getNotes().isEmpty()) {
            System.out.println("No notes.");
        } else {
            user.getNotes()
                    .stream()
                    .map(Main::formatNote)
                    .forEach(System.out::println);
        }
    }

    public static void displayTodayNotes(IAuthService authService) {
        User user = authService.getLoggedUser();
        if (user.getNotes()
                .stream()
                .noneMatch(note -> note.getNoteDate().equals(LocalDate.now()))) {
            System.out.println("No notes for today.");
        } else {
            user.getNotes()
                    .stream()
                    .filter(note -> note.getNoteDate().equals(LocalDate.now()))
                    .map(Main::formatNote)
                    .forEach(System.out::println);
        }
    }

    public static void removeNotes(IAuthService authService) {
        if (authService.getLoggedUser().getNotes().isEmpty()) {
            System.out.println("No notes.");
        } else {
            System.out.println("Select note: ");
            String id = scanner.nextLine();
            User user = authService.getLoggedUser();
            if (user.getNotes().size() > Integer.parseInt(id)) {
                if (user.removeNote(user.getNotes().get(Integer.parseInt(id)).getUuid())) {
                    System.out.println("Note removed.");
                }
            } else {
                System.out.println("Note with this id doesn't exist.");
            }
        }
    }

    public static boolean editNote(IAuthService authService) {
        if (authService.getLoggedUser().getNotes().isEmpty()) {
            System.out.println("No notes.");
        } else {
            System.out.println("Select note to edit:");
            String selectedNote = scanner.nextLine();
            User user = authService.getLoggedUser();
            if (Integer.parseInt(selectedNote) < user.getNotes().size()) {
                System.out.println("New title:");
                String newTitle = scanner.nextLine();
                System.out.println("New content:");
                String newContent = scanner.nextLine();
                System.out.println("New date:");
                String newDate = scanner.nextLine();
                verifyEditNote(user.getNotes().get(Integer.parseInt(selectedNote)), newTitle, newContent, newDate);
                System.out.println("Note changed successfully.");
            } else {
                System.out.println("Note with this id doesn't exist.");
            }
        }
        return true;
    }

    public static boolean addReminder(IAuthService authService) {
        System.out.println("Select note:");
        String selectedNote = scanner.nextLine();
        User user = authService.getLoggedUser();
        if (Integer.parseInt(selectedNote) < user.getNotes().size()) {
            System.out.println("Reminder name:");
            String reminderName = scanner.nextLine();
            System.out.println("Reminder date[yyyy-MM-dd]:");
            String reminderDate = scanner.nextLine();
            user.getNotes().get(Integer.parseInt(selectedNote)).addReminder(new Reminder(reminderName, LocalDate.parse(reminderDate)));
            System.out.println("Reminder added successfully.");
        } else {
            System.out.println("Note with this id doesn't exist.");
        }
        return true;
    }

    public static void changeLogin(IAuthService authService) {
        System.out.println("Type new login: ");
        String newNickname = scanner.nextLine();
        if (authService.containsNickname(newNickname)) {
            System.out.println("This nickname is already taken.");
        } else {
            String oldNickname = authService.getLoggedUser().getNickname();
            authService.getLoggedUser().setNickname(newNickname);
            System.out.println("Nickname changed successfully!");
            System.out.println("Old nickname: " + oldNickname);
            System.out.println("New nickname: " + newNickname);
        }
    }

    public static void changePassword(IAuthService authService) {
        System.out.println("Type current password:");
        String currentPassword = scanner.nextLine();
        System.out.println("Type new password:");
        String newPassword = scanner.nextLine();
        if (authService.changePassword(currentPassword, newPassword)) {
            System.out.println("Password changed successfully.");
        } else {
            System.out.println("Wrong current password.");
        }
    }
}


